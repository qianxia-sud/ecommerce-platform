package com.ecommerce.product.feign;

import com.ecommerce.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 库存服务Feign客户端
 */
@FeignClient(name = "inventory-service", fallback = InventoryFeignClientFallback.class)
public interface InventoryFeignClient {
    
    /**
     * 获取商品可用库存
     */
    @GetMapping("/inventory/product/{productId}/stock")
    Result<Integer> getAvailableStock(@PathVariable("productId") Long productId);
}

