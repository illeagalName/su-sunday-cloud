package com.haier.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/24 21:23
 */
@Component
@Slf4j
public class CacheRequestFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("===================CacheRequestFilter=================");
        // GET DELETE 不过滤
        HttpMethod method = exchange.getRequest().getMethod();
        if (method == null || method.matches("GET") || method.matches("DELETE")) {
            return chain.filter(exchange);
        }
        return DataBufferUtils.join(exchange.getRequest().getBody()).map(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);
            return bytes;
        }).defaultIfEmpty(new byte[0]).flatMap(bytes -> {
            DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
            ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                public Flux<DataBuffer> getBody() {
                    if (bytes.length > 0) {
                        return Flux.just(dataBufferFactory.wrap(bytes));
                    }
                    return Flux.empty();
                }
            };
            return chain.filter(exchange.mutate().request(decorator).build());
        });
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
