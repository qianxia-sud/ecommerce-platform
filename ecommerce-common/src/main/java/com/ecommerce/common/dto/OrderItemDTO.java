package com.ecommerce.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 订单商品项DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    
    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量必须大于0")
    private Integer quantity;
}


