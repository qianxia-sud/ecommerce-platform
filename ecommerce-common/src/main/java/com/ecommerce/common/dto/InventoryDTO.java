package com.ecommerce.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 库存操作DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "商品ID不能为空")
    private Long productId;
    
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量必须大于0")
    private Integer quantity;
    
    /** 订单ID（用于锁定/释放库存） */
    private Long orderId;
}


