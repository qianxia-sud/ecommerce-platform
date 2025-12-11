package com.ecommerce.product.service;

import com.ecommerce.common.entity.Category;
import com.ecommerce.common.structure.CategoryCache;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService {
    
    /**
     * 创建分类
     */
    Category createCategory(Category category);
    
    /**
     * 更新分类
     */
    Category updateCategory(Long id, Category category);
    
    /**
     * 根据ID获取分类
     */
    Category getCategoryById(Long id);
    
    /**
     * 获取所有分类
     */
    List<Category> getAllCategories();
    
    /**
     * 获取子分类
     */
    List<Category> getChildCategories(Long parentId);
    
    /**
     * 获取分类树
     */
    List<CategoryCache.CategoryTreeNode> getCategoryTree();
    
    /**
     * 删除分类
     */
    void deleteCategory(Long id);
    
    /**
     * 获取分类名称
     */
    String getCategoryName(Long id);
}

