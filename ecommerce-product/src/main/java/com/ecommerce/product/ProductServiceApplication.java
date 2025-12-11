package com.ecommerce.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 商品服务启动类
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EntityScan(basePackages = {"com.ecommerce.common.entity", "com.ecommerce.product"})
public class ProductServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
        System.out.println("=====================================");
        System.out.println("  Product Service 启动成功!");
        System.out.println("  端口: 8082");
        System.out.println("=====================================");
    }
}

