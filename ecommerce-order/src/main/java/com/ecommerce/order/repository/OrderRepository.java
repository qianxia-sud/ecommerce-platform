package com.ecommerce.order.repository;

import com.ecommerce.common.entity.Order;
import com.ecommerce.common.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单数据访问接口
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * 根据订单号查找订单
     */
    Optional<Order> findByOrderNo(String orderNo);
    
    /**
     * 根据用户ID查找订单
     */
    List<Order> findByUserIdOrderByCreateTimeDesc(Long userId);
    
    /**
     * 分页查询用户订单
     */
    Page<Order> findByUserId(Long userId, Pageable pageable);
    
    /**
     * 根据状态查询订单
     */
    List<Order> findByStatus(OrderStatus status);
    
    /**
     * 分页查询所有订单
     */
    Page<Order> findAllByOrderByCreateTimeDesc(Pageable pageable);
    
    /**
     * 更新订单状态
     */
    @Modifying
    @Query("UPDATE Order o SET o.status = ?2, o.updateTime = ?3 WHERE o.id = ?1")
    void updateStatus(Long id, OrderStatus status, LocalDateTime updateTime);
    
    /**
     * 查找超时未支付的订单
     */
    @Query("SELECT o FROM Order o WHERE o.status = ?1 AND o.createTime < ?2")
    List<Order> findTimeoutOrders(OrderStatus status, LocalDateTime timeout);
    
    /**
     * 获取总收入（已支付/已完成订单的总金额）
     */
    @Query("SELECT COALESCE(SUM(o.payAmount), 0) FROM Order o WHERE o.status IN (com.ecommerce.common.enums.OrderStatus.PAID, com.ecommerce.common.enums.OrderStatus.SHIPPED, com.ecommerce.common.enums.OrderStatus.RECEIVED, com.ecommerce.common.enums.OrderStatus.COMPLETED)")
    java.math.BigDecimal getTotalRevenue();
}

