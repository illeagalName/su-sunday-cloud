package com.haier.gateway.filter;

import com.haier.api.user.domain.TokenVO;
import com.haier.api.user.domain.UserVO;
import com.haier.core.constant.CacheConstants;
import com.haier.core.domain.R;
import com.haier.core.util.JsonUtils;
import com.haier.core.util.SecurityUtils;
import com.haier.core.util.ServletUtils;
import com.haier.core.util.StringUtils;
import com.haier.gateway.properties.WhiteListProperties;
import com.haier.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/24 21:30
 */
@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    WhiteListProperties whiteListProperties;

    @Autowired
    RedisService redisService;

    Base64.Decoder decoder = Base64.getDecoder();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {


        log.info("===================AuthFilter=================");
        log.info("白名单 {}", JsonUtils.toStringPretty(whiteListProperties.getLists()));

        String url = exchange.getRequest().getURI().getPath();
        // 跳过不需要验证的路径
        if (StringUtils.matches(url, whiteListProperties.getLists())) {
            return chain.filter(exchange);
        }
        String token = SecurityUtils.getToken(exchange.getRequest());
        if (StringUtils.isBlank(token)) {
            return setUnauthorizedResponse(exchange, "令牌不能为空");
        }
        // 获取到token，解析token
        String[] split = token.split("\\.");
        if (split.length != 3) {
            return setUnauthorizedResponse(exchange, "令牌格式不正确");
        }
        TokenVO tokenVO = JsonUtils.toObject(decode(split[1]), TokenVO.class);
        if (Objects.isNull(tokenVO)) {
            return setUnauthorizedResponse(exchange, "令牌格式不正确");
        }
        // 查询缓存数据
        String clientId = tokenVO.getClientId();
        UserVO user = redisService.getObject(CacheConstants.AUTHORIZATION_USER_TOKEN + clientId + ":" + tokenVO.getUniqueId());
        if (Objects.isNull(user)) {
            return setUnauthorizedResponse(exchange, "登录状态已过期");
        }
        // 校验令牌完整性
        boolean verify = SecurityUtils.verifyToken(token, user.getSecret());
        if (!verify) {
            return setUnauthorizedResponse(exchange, "令牌校验未通过");
        }
        Long userid = user.getUserId();
        String username = user.getUserName();

        // 设置用户信息到请求
        ServerHttpRequest mutableReq = exchange.getRequest().mutate().header(CacheConstants.DETAILS_USER_ID, String.valueOf(userid))
                .header(CacheConstants.DETAILS_USERNAME, ServletUtils.urlEncode(username)).header(CacheConstants.DETAILS_CLIENT_ID, clientId).build();
        ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();

        return chain.filter(mutableExchange);
    }

    private String decode(String str) {
        return new String(decoder.decode(str), StandardCharsets.UTF_8);
    }

    private Mono<Void> setUnauthorizedResponse(ServerWebExchange exchange, String msg) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.OK);

        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());

        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            return bufferFactory.wrap(JsonUtils.toByte(R.error(HttpStatus.UNAUTHORIZED.value(), msg)));

        }));
    }


    @Override
    public int getOrder() {
        return 1;
    }
}
