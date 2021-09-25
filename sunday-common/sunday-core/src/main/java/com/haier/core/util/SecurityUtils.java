package com.haier.core.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.UUID;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.haier.core.constant.CacheConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/23 21:46
 */
@Slf4j
public class SecurityUtils {

    /**
     * 获取用户
     */
    public static String getUsername() {
        return Optional.ofNullable(ServletUtils.getHeader(CacheConstants.DETAILS_USERNAME)).map(ServletUtils::urlDecode).orElse(null);
    }

    /**
     * 获取用户ID
     */
    public static Long getUserId() {
        return Optional.ofNullable(ServletUtils.getHeader(CacheConstants.DETAILS_USER_ID)).map(Convert::toLong).orElse(null);
    }

    /**
     * 获取客户端标识
     */
    public static String getClientId() {
        return ServletUtils.getHeader(CacheConstants.DETAILS_CLIENT_ID);
    }

    /************************密码对比************************/

    static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


    /************************token操作************************/

    //设置头部信息 header
    private static final Map<String, Object> HEADER = new HashMap<>() {{
        put("typ", "JWT");
        put("alg", "HS256");
    }};

    /**
     * 创建token
     *
     * @param uniqueId 唯一标识
     * @param clientId 分组id
     * @param secret   加密串
     * @param date     过期时间
     * @return String
     */
    public static String createToken(Long uniqueId, String clientId, String secret, Date date) {
        try {
            //秘钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(secret);

            //携带userId，userName信息，生成签名
            return JWT.create()
                    .withHeader(HEADER)
                    .withClaim("clientId", clientId)
                    .withClaim("uniqueId", uniqueId)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            log.error("生成token异常", e);
            return null;
        }
    }

    /**
     * @param token  验证token，通过返回true
     * @param secret 盐
     * @return
     */
    public static boolean verifyToken(String token, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            log.info(" 解析：{} ", jwt.getClaims());
            return true;
        } catch (Exception e) {
            log.error("校验token异常", e);
            return false;
        }
    }

    /**
     * 从header中获取jwt token
     *
     * @param request
     * @return
     */
    public static String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(CacheConstants.HEADER);
        if (StringUtils.isNotBlank(token) && token.startsWith(CacheConstants.TOKEN_PREFIX)) {
            token = token.replace(CacheConstants.TOKEN_PREFIX, "");
        }
        return token;
    }


    /************************ID操作************************/

    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 随机UUID
     */
    public static String fastUUID() {
        return UUID.fastUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String fastSimpleUUID() {
        return UUID.fastUUID().toString(true);
    }


    /**
     * 盐 生成器（三位随机数 + 时间戳）
     * 前七位使用精确到毫秒的时间戳，转成 62 进制方式。7 位的 62 进制可以使用差不多 2080 年，基本足够用了。
     * 后三位使用随机数填充，碰撞概率为：1/238327。
     */
    public static String generateSecurityCode() {
        try {
            int i = SecureRandom.getInstance("SHA1PRNG").nextInt(62 * 62 * 62 - 1);
            // 前 7 位是时间戳，精确到毫秒。
            String time = StringUtils.leftPad(to62String(System.currentTimeMillis()), 7, '0');
            // 后三位是随机数
            String random = StringUtils.leftPad(to62String(i), 3, '0');
            return time + random;
        } catch (Exception e) {
            log.error("生成安全码异常", e);
            return fastSimpleUUID();
        }
    }

    /**
     * Base64 字符
     */
    private static final char[] DIGITS = {
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x',
            'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z',
            '*', '-'
    };

    /**
     * 10 进制转成 62 进制
     */
    private static String to62String(long timeMillis) {
        int mask = 62;
        int bufLength = 11;
        int charPos = bufLength;
        char[] buf = new char[bufLength];
        do {
            buf[--charPos] = DIGITS[(int) (timeMillis % mask)];
            timeMillis = timeMillis / mask;
        } while (timeMillis > 0);
        return new String(buf, charPos, (bufLength - charPos));
    }
}
