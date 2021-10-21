package com.haier.ratelimiter.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.ratelimiter.annotation
 * @ClassName: RateLimiter
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/21 14:53
 * @Version: 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    int DEFAULT_REQUEST_LIMIT = 8;

    /**
     * max - 最大请求数
     */
    @AliasFor("max") int value() default DEFAULT_REQUEST_LIMIT;

    /**
     * @see RateLimiter#value()
     */
    @AliasFor("value") int max() default DEFAULT_REQUEST_LIMIT;

    /**
     * 限流的唯一标识
     */
    String key() default "";

    /**
     * 超时时长，默认半分钟
     */
    long timeout() default 30;

    /**
     * 超时时间单位，默认 分钟
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
