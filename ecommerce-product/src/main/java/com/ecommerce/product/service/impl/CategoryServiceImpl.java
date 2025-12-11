package com.ecommerce.product.service.impl;

import com.ecommerce.common.entity.Category;
import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.common.result.ResultCode;
import com.ecommerce.common.structure.CategoryCache;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 分类服务实现类
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryRepository categoryRepository;
    private final CategoryCache categoryCache = new CategoryCache();
    
    /**
     * 初始化时加载分类到缓存
     */
    @PostConstruct
    public void init() {
        refreshCache();
    }
    
    /**
     * 刷新缓存
     */
    private void refreshCache() {
        List<Category> categories = categoryRepository.findByStatusOrderBySort(1);
        categoryCache.loadAll(categories);
    }
    
    @Override
    @Transactional
    public Category createCategory(Category category) {
        // 检查同级分类名称是否存在
        if (categoryRepository.existsByNameAndParentId(category.getName(), category.getParentId())) {
            throw new BusinessException("同级分类名称已存在");
        }
        
        // 设置层级
        if (category.getParentId() == null || category.getParentId() == 0) {
            category.setParentId(0L);
            category.setLevel(1);
        } else {
            Category parent = categoryRepository.findById(category.getParentId())
                    .orElseThrow(() -> new BusinessException("父分类不存在"));
            category.setLevel(parent.getLevel() + 1);
        }
        
        Category saved = categoryRepository.save(category);
        refreshCache();
        return saved;
    }
    
    @Override
    @Transactional
    public Category updateCategory(Long id, Category category) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.CATEGORY_NOT_FOUND));
        
        if (category.getName() != null) {
            existing.setName(category.getName());
        }
        if (category.getSort() != null) {
            existing.setSort(category.getSort());
        }
        if (category.getIcon() != null) {
            existing.setIcon(category.getIcon());
        }
        if (category.getStatus() != null) {
            existing.setStatus(category.getStatus());
        }
        
        Category saved = categoryRepository.save(existing);
        refreshCache();
        return saved;
    }
    
    @Override
    public Category getCategoryById(Long id) {
        // 先从缓存获取
        Category category = categoryCache.get(id);
        if (category != null) {
            return category;
        }
        
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.CATEGORY_NOT_FOUND));
    }
    
    @Override
    public List<Category> getAllCategories() {
        return categoryCache.getAll();
    }
    
    @Override
    public List<Category> getChildCategories(Long parentId) {
        return categoryCache.getChildren(parentId);
    }
    
    @Override
    public List<CategoryCache.CategoryTreeNode> getCategoryTree() {
        return categoryCache.buildTree();
    }
    
    @Override
    @Transactional
    public void deleteCategory(Long id) {
        // 检查是否有子分类
        List<Category> children = categoryCache.getChildren(id);
        if (!children.isEmpty()) {
            throw new BusinessException("存在子分类，无法删除");
        }
        
        categoryRepository.deleteById(id);
        refreshCache();
    }
    
    @Override
    public String getCategoryName(Long id) {
        return categoryCache.getName(id);
    }
}

