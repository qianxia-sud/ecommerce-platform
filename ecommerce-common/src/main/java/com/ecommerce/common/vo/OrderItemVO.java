package com.ecommerce.common.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单商品项VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    private Long productId;
    
    private String productName;
    
    private String productImage;
    
    private BigDecimal price;
    
    private Integer quantity;
    
    private BigDecimal subtotal;
}


