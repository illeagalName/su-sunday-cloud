package com.haier.ratelimiter.aspect;

import com.haier.core.util.IpUtils;
import com.haier.core.util.ServletUtils;
import com.haier.ratelimiter.annotation.RateLimiter;
import com.haier.ratelimiter.exception.RateLimiterException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.ratelimiter.aspect
 * @ClassName: RateLimiterAspect
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/21 14:58
 * @Version: 1.0
 */
@Slf4j
@Aspect
@Component
public class RateLimiterAspect implements InitializingBean {

    @Autowired
    RedissonClient redissonClient;

    private String rateLimitLuaSha;

    private final static String REDIS_LIMIT_KEY_PREFIX = "sunday:limit:";

    private final Codec STRING_CODEC = new StringCodec(StandardCharsets.UTF_8);

    @Around("@annotation(com.haier.ratelimiter.annotation.RateLimiter)")
    public Object pointcut(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        // 通过 AnnotationUtils.findAnnotation 获取 RateLimiter 注解(此方法在method上找不到时还会找超类方法)
        RateLimiter rateLimiter = AnnotationUtils.findAnnotation(method, RateLimiter.class);
        if (Objects.nonNull(rateLimiter)) {
            // key-  (自定义key 或 类名.方法名):ip地址
            String key = Optional.ofNullable(rateLimiter.key()).filter(s -> !Objects.equals(s, "")).orElseGet(() -> method.getDeclaringClass().getName().concat(".").concat(method.getName())).concat(":").concat(IpUtils.getIpAddr(ServletUtils.getRequest()));
            // 限流最大值
            long max = rateLimiter.max();
            // 时效
            long timeout = rateLimiter.timeout();
            TimeUnit timeUnit = rateLimiter.timeUnit();
            boolean limited = shouldLimited(key, max, timeout, timeUnit);
            if (limited) {
                throw new RateLimiterException("手速太快了，慢点儿吧~");
            }
        }

        return point.proceed();
    }

    private boolean shouldLimited(String key, long max, long timeout, TimeUnit timeUnit) {
        // 最终的 key 格式为-  sunday:limit:(自定义key 或 类名.方法名):IP
        String finalKey = REDIS_LIMIT_KEY_PREFIX + key;
        // 统一使用单位毫秒
        long ttl = timeUnit.toMillis(timeout);
        // 当前时间毫秒数
        long now = Instant.now().toEpochMilli();
        // expired之前的数据需要在脚本中清空
        long expired = now - ttl;

        List<Object> keys = new ArrayList<>();
        keys.add(finalKey);
        // command arguments must be strings or integers

        Object[] values = new Object[]{now, ttl, expired, max};
        Long executeTimes;
        if (Objects.nonNull(rateLimitLuaSha)) {
            executeTimes = redissonClient.getScript(STRING_CODEC)
                    .evalSha(RScript.Mode.READ_WRITE, rateLimitLuaSha, RScript.ReturnType.INTEGER, keys, values);
        } else {
            executeTimes = redissonClient.getScript(STRING_CODEC)
                    .eval(RScript.Mode.READ_WRITE, RedisLua.RATE_LIMITER_SCRIPT, RScript.ReturnType.INTEGER, keys, values);
        }
        if (Objects.nonNull(executeTimes)) {
            if (executeTimes == 0) {
                log.error("【{}】在时间 {} 毫秒内已达到访问上限，当前接口上限 {}", key, ttl, max);
                return true;
            } else {
                log.info("【{}】在时间 {} 毫秒内访问 {} 次", key, ttl, executeTimes);
                return false;
            }
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rateLimitLuaSha = redissonClient.getScript().scriptLoad(RedisLua.RATE_LIMITER_SCRIPT);
        log.info("加载lua script【RedisLua.RATE_LIMITER_SCRIPT】成功");
    }

    static class RedisLua {
        public static final String RATE_LIMITER_SCRIPT;

        static {
            RATE_LIMITER_SCRIPT = "local key = KEYS[1];" +
                    "local now = tonumber(ARGV[1]);" +
                    "local ttl = tonumber(ARGV[2]);" +
                    "local expired = tonumber(ARGV[3]);" +
                    "local max = tonumber(ARGV[4]);" +
                    "redis.call('zremrangebyscore', key, 0, expired);" +
                    "local current = tonumber(redis.call('zcard', key));" +
                    "local next = current + 1;" +
                    "if next > max then" +
                    "  return 0;" +
                    "else" +
                    "  redis.call('zadd', key, now, now);" +
                    "  redis.call('pexpire', key, ttl);" +
                    "  return next;" +
                    "end;";
        }
    }
}
