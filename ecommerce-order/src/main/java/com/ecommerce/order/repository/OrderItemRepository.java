package com.ecommerce.order.repository;

import com.ecommerce.common.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单商品明细数据访问接口
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    /**
     * 根据订单ID查找订单商品
     */
    List<OrderItem> findByOrderId(Long orderId);
    
    /**
     * 根据订单号查找订单商品
     */
    List<OrderItem> findByOrderNo(String orderNo);
}

