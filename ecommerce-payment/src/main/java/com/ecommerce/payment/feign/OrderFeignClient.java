package com.ecommerce.payment.feign;

import com.ecommerce.common.entity.Order;
import com.ecommerce.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 订单服务Feign客户端
 */
@FeignClient(name = "order-service")
public interface OrderFeignClient {
    
    /**
     * 获取订单实体
     */
    @GetMapping("/order/{id}/entity")
    Result<Order> getOrderEntityById(@PathVariable("id") Long id);
    
    /**
     * 更新订单状态
     */
    @PutMapping("/order/{id}/status")
    Result<Void> updateOrderStatus(@PathVariable("id") Long id, @RequestParam("status") String status);
    
    /**
     * 支付订单
     */
    @PutMapping("/order/{id}/pay")
    Result<Void> payOrder(@PathVariable("id") Long id);
}

