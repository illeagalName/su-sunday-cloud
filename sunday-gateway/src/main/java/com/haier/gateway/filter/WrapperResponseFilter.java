package com.haier.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/25 15:06
 */
@Component
@Slf4j
public class WrapperResponseFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取response的 返回数据
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (Objects.equals(getStatusCode(), HttpStatus.OK) && body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                    return super.writeWith(fluxBody.map(dataBuffer -> {
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        //释放掉内存
                        DataBufferUtils.release(dataBuffer);
                        //responseData就是下游系统返回的内容,可以查看修改
                        String responseData = new String(content, StandardCharsets.UTF_8);
                        log.info("***********************************响应信息**********************************");
                        log.info("响应内容:{}", responseData);
                        log.info("****************************************************************************\n");
                        byte[] uppedContent = responseData.getBytes(StandardCharsets.UTF_8);
                        return bufferFactory.wrap(uppedContent);
                    }));
                } else {
                    log.error("响应code异常:{}", getStatusCode());
                }
                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    /**
     * implement GlobalFilter & Ordered
     * important: the order has to be <-1 or else the standard NettyWriteResponseFilter will send the response before your filter gets a chance to be called
     * in the overriden filter method, create a ServerHttpResponseDecorator over exchange.getResponse(), override the writeWith method of this decorator and do your body modifications there (I had to cast the body from a Publisher<? extends DataBuffer> to a Flux to make it easier to modify), return super.writeWith(yourModifiedBodyFlux). Mutate the exchange and set your decorator as the new response.
     *
     * @return
     */
    @Override
    public int getOrder() {
        return -2;
    }
}
