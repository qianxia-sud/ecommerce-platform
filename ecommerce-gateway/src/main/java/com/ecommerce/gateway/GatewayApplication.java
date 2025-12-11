package com.ecommerce.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 网关服务启动类
 */
@SpringBootApplication
@EnableEurekaClient
public class GatewayApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        System.out.println("=====================================");
        System.out.println("  Gateway Service 启动成功!");
        System.out.println("  端口: 8080");
        System.out.println("=====================================");
    }
}

