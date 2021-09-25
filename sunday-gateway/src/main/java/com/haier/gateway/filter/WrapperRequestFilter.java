package com.haier.gateway.filter;

import com.haier.core.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;


/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/24 21:23
 */
@Component
@Slf4j
public class WrapperRequestFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI URIPath = request.getURI();
        String path = request.getPath().value();
        String method = request.getMethodValue();
        HttpHeaders header = request.getHeaders();
        log.info("");
        log.info("***********************************请求信息**********************************");
        log.info("请求request信息：URI = {}, path = {}，method = {}，header = {}。", URIPath, path, method, header);
        if ("POST".equals(method) || "PUT".equals(method)) {
            Flux<DataBuffer> body = exchange.getRequest().getBody();
            return DataBufferUtils.join(body).map(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                log.info("请求参数:{}", new String(bytes, StandardCharsets.UTF_8));
                log.info("****************************************************************************\n");
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
        } else if ("GET".equals(method)) {
            MultiValueMap<String, String> queryParams = request.getQueryParams();
            log.info("请求参数:{}", JsonUtils.toString(queryParams));
            log.info("****************************************************************************\n");
            return chain.filter(exchange);
        }
        return chain.filter(exchange);
    }

    /**
     * 优先级top
     *
     * @return
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
