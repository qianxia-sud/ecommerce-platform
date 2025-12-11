package com.ecommerce.order.feign;

import com.ecommerce.common.entity.Product;
import com.ecommerce.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商品服务Feign客户端
 */
@FeignClient(name = "product-service")
public interface ProductFeignClient {
    
    /**
     * 获取商品实体
     */
    @GetMapping("/product/{id}/entity")
    Result<Product> getProductEntityById(@PathVariable("id") Long id);
    
    /**
     * 增加销量
     */
    @PutMapping("/product/{id}/sales")
    Result<Void> increaseSales(@PathVariable("id") Long id, @RequestParam("quantity") Integer quantity);
}

