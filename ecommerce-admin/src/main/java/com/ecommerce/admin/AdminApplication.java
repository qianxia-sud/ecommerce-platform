package com.ecommerce.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 管理后台启动类
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class AdminApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
        System.out.println("=====================================");
        System.out.println("  Admin Service 启动成功!");
        System.out.println("  端口: 8090");
        System.out.println("  访问地址: http://localhost:8090");
        System.out.println("=====================================");
    }
}

