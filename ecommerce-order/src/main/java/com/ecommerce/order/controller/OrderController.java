package com.ecommerce.order.controller;

import com.ecommerce.common.dto.OrderCreateDTO;
import com.ecommerce.common.entity.Order;
import com.ecommerce.common.result.PageResult;
import com.ecommerce.common.result.Result;
import com.ecommerce.common.vo.OrderVO;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    /**
     * 创建订单
     */
    @PostMapping
    public Result<OrderVO> createOrder(@RequestBody @Validated OrderCreateDTO orderDTO) {
        return Result.success(orderService.createOrder(orderDTO));
    }
    
    /**
     * 获取订单详情
     */
    @GetMapping("/{id}")
    public Result<OrderVO> getOrderById(@PathVariable Long id) {
        return Result.success(orderService.getOrderById(id));
    }
    
    /**
     * 根据订单号获取订单
     */
    @GetMapping("/no/{orderNo}")
    public Result<OrderVO> getOrderByOrderNo(@PathVariable String orderNo) {
        return Result.success(orderService.getOrderByOrderNo(orderNo));
    }
    
    /**
     * 获取订单实体（内部调用）
     */
    @GetMapping("/{id}/entity")
    public Result<Order> getOrderEntityById(@PathVariable Long id) {
        return Result.success(orderService.getOrderEntityById(id));
    }
    
    /**
     * 获取用户订单列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<OrderVO>> getUserOrders(@PathVariable Long userId) {
        return Result.success(orderService.getUserOrders(userId));
    }
    
    /**
     * 分页获取所有订单
     */
    @GetMapping("/list")
    public Result<PageResult<OrderVO>> getAllOrders(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(orderService.getAllOrders(pageNum, pageSize));
    }
    
    /**
     * 支付订单（模拟，实际应由支付服务回调）
     */
    @PutMapping("/{id}/pay")
    public Result<Void> payOrder(@PathVariable Long id) {
        orderService.payOrder(id);
        return Result.success();
    }
    
    /**
     * 发货
     */
    @PutMapping("/{id}/ship")
    public Result<Void> shipOrder(@PathVariable Long id) {
        orderService.shipOrder(id);
        return Result.success();
    }
    
    /**
     * 确认收货
     */
    @PutMapping("/{id}/receive")
    public Result<Void> receiveOrder(@PathVariable Long id) {
        orderService.receiveOrder(id);
        return Result.success();
    }
    
    /**
     * 取消订单
     */
    @PutMapping("/{id}/cancel")
    public Result<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return Result.success();
    }
    
    /**
     * 完成订单
     */
    @PutMapping("/{id}/complete")
    public Result<Void> completeOrder(@PathVariable Long id) {
        orderService.completeOrder(id);
        return Result.success();
    }
    
    /**
     * 更新订单状态（供支付服务回调）
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        orderService.updateOrderStatus(id, status);
        return Result.success();
    }
    
    /**
     * 获取订单总数
     */
    @GetMapping("/count")
    public Result<Long> getOrderCount() {
        return Result.success(orderService.getOrderCount());
    }
    
    /**
     * 获取总收入
     */
    @GetMapping("/revenue")
    public Result<java.math.BigDecimal> getTotalRevenue() {
        return Result.success(orderService.getTotalRevenue());
    }
}

