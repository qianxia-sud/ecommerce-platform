package com.ecommerce.payment.repository;

import com.ecommerce.common.entity.Payment;
import com.ecommerce.common.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 支付记录数据访问接口
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    /**
     * 根据支付流水号查找
     */
    Optional<Payment> findByPaymentNo(String paymentNo);
    
    /**
     * 根据订单ID查找
     */
    Optional<Payment> findByOrderId(Long orderId);
    
    /**
     * 根据订单号查找
     */
    Optional<Payment> findByOrderNo(String orderNo);
    
    /**
     * 根据状态查找
     */
    List<Payment> findByStatus(PaymentStatus status);
    
    /**
     * 根据用户ID查找
     */
    List<Payment> findByUserIdOrderByCreateTimeDesc(Long userId);
}

