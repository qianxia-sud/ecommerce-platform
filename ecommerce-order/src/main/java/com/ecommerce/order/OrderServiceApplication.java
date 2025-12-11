package com.ecommerce.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 订单服务启动类
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EntityScan(basePackages = {"com.ecommerce.common.entity", "com.ecommerce.order"})
public class OrderServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
        System.out.println("=====================================");
        System.out.println("  Order Service 启动成功!");
        System.out.println("  端口: 8084");
        System.out.println("=====================================");
    }
}

