package com.ecommerce.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka服务注册中心启动类
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
        System.out.println("=====================================");
        System.out.println("  Eureka Server 启动成功!");
        System.out.println("  访问地址: http://localhost:8761");
        System.out.println("=====================================");
    }
}


