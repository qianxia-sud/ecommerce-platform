package com.ecommerce.product.feign;

import com.ecommerce.common.result.Result;
import org.springframework.stereotype.Component;

/**
 * 库存服务Feign客户端降级处理
 */
@Component
public class InventoryFeignClientFallback implements InventoryFeignClient {
    
    @Override
    public Result<Integer> getAvailableStock(Long productId) {
        // 降级返回0库存
        return Result.success(0);
    }
}

