package com.ecommerce.order.service;

import com.ecommerce.common.dto.OrderCreateDTO;
import com.ecommerce.common.entity.Order;
import com.ecommerce.common.result.PageResult;
import com.ecommerce.common.vo.OrderVO;

import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService {
    
    /**
     * 创建订单
     */
    OrderVO createOrder(OrderCreateDTO orderDTO);
    
    /**
     * 根据ID获取订单
     */
    OrderVO getOrderById(Long id);
    
    /**
     * 根据订单号获取订单
     */
    OrderVO getOrderByOrderNo(String orderNo);
    
    /**
     * 获取订单实体
     */
    Order getOrderEntityById(Long id);
    
    /**
     * 获取用户订单列表
     */
    List<OrderVO> getUserOrders(Long userId);
    
    /**
     * 分页获取所有订单
     */
    PageResult<OrderVO> getAllOrders(Integer pageNum, Integer pageSize);
    
    /**
     * 支付订单
     */
    void payOrder(Long orderId);
    
    /**
     * 发货
     */
    void shipOrder(Long orderId);
    
    /**
     * 确认收货
     */
    void receiveOrder(Long orderId);
    
    /**
     * 取消订单
     */
    void cancelOrder(Long orderId);
    
    /**
     * 完成订单
     */
    void completeOrder(Long orderId);
    
    /**
     * 更新订单状态（供支付服务回调）
     */
    void updateOrderStatus(Long orderId, String status);
    
    /**
     * 获取订单总数
     */
    Long getOrderCount();
    
    /**
     * 获取总收入
     */
    java.math.BigDecimal getTotalRevenue();
}

