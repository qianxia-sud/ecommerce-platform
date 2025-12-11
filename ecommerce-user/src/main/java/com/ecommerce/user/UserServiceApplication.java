package com.ecommerce.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 用户服务启动类
 */
@SpringBootApplication
@EnableEurekaClient
@EntityScan(basePackages = {"com.ecommerce.common.entity", "com.ecommerce.user"})
public class UserServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        System.out.println("=====================================");
        System.out.println("  User Service 启动成功!");
        System.out.println("  端口: 8081");
        System.out.println("=====================================");
    }
}

