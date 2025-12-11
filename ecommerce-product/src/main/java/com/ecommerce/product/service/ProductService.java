package com.ecommerce.product.service;

import com.ecommerce.common.dto.ProductDTO;
import com.ecommerce.common.entity.Product;
import com.ecommerce.common.result.PageResult;
import com.ecommerce.common.vo.ProductVO;

import java.util.List;

/**
 * 商品服务接口
 */
public interface ProductService {
    
    /**
     * 创建商品
     */
    ProductVO createProduct(ProductDTO productDTO);
    
    /**
     * 更新商品
     */
    ProductVO updateProduct(Long id, ProductDTO productDTO);
    
    /**
     * 根据ID获取商品详情
     */
    ProductVO getProductById(Long id);
    
    /**
     * 根据ID获取商品实体
     */
    Product getProductEntityById(Long id);
    
    /**
     * 分页查询商品
     */
    PageResult<ProductVO> getProductList(Integer pageNum, Integer pageSize, String keyword);
    
    /**
     * 根据分类查询商品
     */
    List<ProductVO> getProductsByCategory(Long categoryId);
    
    /**
     * 上架商品
     */
    void onShelf(Long id);
    
    /**
     * 下架商品
     */
    void offShelf(Long id);
    
    /**
     * 删除商品
     */
    void deleteProduct(Long id);
    
    /**
     * 增加销量
     */
    void increaseSales(Long id, Integer quantity);
    
    /**
     * 获取商品总数
     */
    Long getProductCount();
}

