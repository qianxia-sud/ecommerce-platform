package com.ecommerce.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 订单创建DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotEmpty(message = "订单商品不能为空")
    private List<OrderItemDTO> items;
    
    @NotNull(message = "地址ID不能为空")
    private Long addressId;
    
    private String remark;
}


