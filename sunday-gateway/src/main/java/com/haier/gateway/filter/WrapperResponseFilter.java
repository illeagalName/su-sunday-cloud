package com.haier.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.web.WebFluxSleuthOperators;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
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

    @Autowired
    Tracer tracer;

    @Autowired
    CurrentTraceContext currentTraceContext;

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
                    return super.writeWith(fluxBody.buffer().map(dataBuffer -> {
                        // 响应数据过大时会分批打印，需要用join
                        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                        DataBuffer joinBuffer = dataBufferFactory.join(dataBuffer);
                        byte[] content = new byte[joinBuffer.readableByteCount()];
                        joinBuffer.read(content);
                        //释放掉内存
                        DataBufferUtils.release(joinBuffer);
                        //responseData就是下游系统返回的内容,可以查看修改
                        String responseData = new String(content, StandardCharsets.UTF_8);
                        WebFluxSleuthOperators.withSpanInScope(tracer, currentTraceContext, exchange, () -> {
                            log.info("***********************************响应信息**********************************");
                            log.info("响应内容:{}", responseData);
                            log.info("****************************************************************************\n");
                        });
                        return bufferFactory.wrap(content);
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
