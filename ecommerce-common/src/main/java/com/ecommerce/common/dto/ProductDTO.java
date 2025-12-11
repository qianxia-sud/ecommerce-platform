package com.ecommerce.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotBlank(message = "商品名称不能为空")
    private String name;
    
    private String description;
    
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
    
    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.01", message = "商品价格必须大于0")
    private BigDecimal price;
    
    private BigDecimal marketPrice;
    
    private String mainImage;
    
    private String images;
    
    /** 初始库存数量 */
    private Integer stock;
}


