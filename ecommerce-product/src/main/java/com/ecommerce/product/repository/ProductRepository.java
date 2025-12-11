package com.ecommerce.product.repository;

import com.ecommerce.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品数据访问接口
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * 根据分类ID查找商品
     */
    List<Product> findByCategoryIdAndStatus(Long categoryId, Integer status);
    
    /**
     * 分页查询上架商品
     */
    Page<Product> findByStatus(Integer status, Pageable pageable);
    
    /**
     * 根据名称模糊查询
     */
    Page<Product> findByNameContainingAndStatus(String name, Integer status, Pageable pageable);
    
    /**
     * 更新商品状态
     */
    @Modifying
    @Query("UPDATE Product p SET p.status = ?2 WHERE p.id = ?1")
    void updateStatus(Long id, Integer status);
    
    /**
     * 增加销量
     */
    @Modifying
    @Query("UPDATE Product p SET p.sales = p.sales + ?2 WHERE p.id = ?1")
    void increaseSales(Long id, Integer quantity);
}

