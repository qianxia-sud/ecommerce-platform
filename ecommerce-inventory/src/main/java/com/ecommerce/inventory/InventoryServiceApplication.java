package com.ecommerce.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 库存服务启动类
 */
@SpringBootApplication
@EnableEurekaClient
@EntityScan(basePackages = {"com.ecommerce.common.entity", "com.ecommerce.inventory"})
public class InventoryServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
        System.out.println("=====================================");
        System.out.println("  Inventory Service 启动成功!");
        System.out.println("  端口: 8083");
        System.out.println("=====================================");
    }
}

