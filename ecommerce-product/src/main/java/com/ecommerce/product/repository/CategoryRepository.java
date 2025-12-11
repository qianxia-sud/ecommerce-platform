package com.ecommerce.product.repository;

import com.ecommerce.common.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 分类数据访问接口
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * 根据父ID查找子分类
     */
    List<Category> findByParentIdAndStatusOrderBySort(Long parentId, Integer status);
    
    /**
     * 查找所有启用的分类
     */
    List<Category> findByStatusOrderBySort(Integer status);
    
    /**
     * 检查分类名称是否存在
     */
    boolean existsByNameAndParentId(String name, Long parentId);
}

