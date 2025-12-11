package com.ecommerce.order.feign;

import com.ecommerce.common.dto.InventoryDTO;
import com.ecommerce.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 库存服务Feign客户端
 */
@FeignClient(name = "inventory-service")
public interface InventoryFeignClient {
    
    /**
     * 锁定库存
     */
    @PostMapping("/inventory/lock")
    Result<Boolean> lockStock(@RequestBody InventoryDTO dto);
    
    /**
     * 扣减库存
     */
    @PostMapping("/inventory/deduct")
    Result<Boolean> deductStock(@RequestBody InventoryDTO dto);
    
    /**
     * 释放库存
     */
    @PostMapping("/inventory/release")
    Result<Boolean> releaseStock(@RequestBody InventoryDTO dto);
}

