package com.ecommerce.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局日志过滤器
 */
@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        String path = request.getPath().value();
        String method = request.getMethod().name();
        String remoteAddress = request.getRemoteAddress() != null 
                ? request.getRemoteAddress().getAddress().getHostAddress() 
                : "unknown";
        
        long startTime = System.currentTimeMillis();
        
        log.info("请求开始 - {} {} from {}", method, path, remoteAddress);
        
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long duration = System.currentTimeMillis() - startTime;
            int statusCode = exchange.getResponse().getStatusCode() != null 
                    ? exchange.getResponse().getStatusCode().value() 
                    : 0;
            log.info("请求结束 - {} {} - 状态码: {} - 耗时: {}ms", method, path, statusCode, duration);
        }));
    }
    
    @Override
    public int getOrder() {
        return -100;
    }
}

