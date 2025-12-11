package com.ecommerce.common.vo;

import com.ecommerce.common.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单详情VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    private String orderNo;
    
    private Long userId;
    
    private BigDecimal totalAmount;
    
    private BigDecimal payAmount;
    
    private BigDecimal freight;
    
    private OrderStatus status;
    
    private String statusDesc;
    
    private String receiverName;
    
    private String receiverPhone;
    
    private String receiverAddress;
    
    private String remark;
    
    private LocalDateTime payTime;
    
    private LocalDateTime shipTime;
    
    private LocalDateTime receiveTime;
    
    private LocalDateTime completeTime;
    
    private LocalDateTime cancelTime;
    
    private LocalDateTime createTime;
    
    /** 订单商品列表 */
    private List<OrderItemVO> items;
}


