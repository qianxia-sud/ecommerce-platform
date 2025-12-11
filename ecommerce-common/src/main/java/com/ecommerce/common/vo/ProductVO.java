package com.ecommerce.common.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品详情VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    private String name;
    
    private String description;
    
    private Long categoryId;
    
    private String categoryName;
    
    private BigDecimal price;
    
    private BigDecimal marketPrice;
    
    private String mainImage;
    
    private String images;
    
    private Integer status;
    
    private Integer sales;
    
    /** 可用库存 */
    private Integer stock;
    
    private LocalDateTime createTime;
}


