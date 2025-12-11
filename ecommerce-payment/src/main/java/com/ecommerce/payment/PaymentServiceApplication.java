package com.ecommerce.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 支付服务启动类
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EntityScan(basePackages = {"com.ecommerce.common.entity", "com.ecommerce.payment"})
public class PaymentServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
        System.out.println("=====================================");
        System.out.println("  Payment Service 启动成功!");
        System.out.println("  端口: 8085");
        System.out.println("=====================================");
    }
}

