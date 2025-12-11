package com.ecommerce.common.structure;

import com.ecommerce.common.entity.Category;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 商品分类缓存
 * 使用HashMap数据结构实现分类的快速查询和树形结构构建
 */
public class CategoryCache {
    
    /** 分类ID -> 分类对象映射 */
    private final Map<Long, Category> categoryMap;
    
    /** 父分类ID -> 子分类列表映射 */
    private final Map<Long, List<Category>> childrenMap;
    
    public CategoryCache() {
        this.categoryMap = new ConcurrentHashMap<>();
        this.childrenMap = new ConcurrentHashMap<>();
    }
    
    /**
     * 加载所有分类到缓存
     * @param categories 分类列表
     */
    public void loadAll(List<Category> categories) {
        clear();
        for (Category category : categories) {
            put(category);
        }
    }
    
    /**
     * 添加单个分类到缓存
     * @param category 分类对象
     */
    public void put(Category category) {
        if (category == null || category.getId() == null) {
            return;
        }
        categoryMap.put(category.getId(), category);
        
        // 更新父子关系
        Long parentId = category.getParentId() != null ? category.getParentId() : 0L;
        childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(category);
    }
    
    /**
     * 根据ID获取分类
     * @param id 分类ID
     * @return 分类对象
     */
    public Category get(Long id) {
        return categoryMap.get(id);
    }
    
    /**
     * 获取分类名称
     * @param id 分类ID
     * @return 分类名称
     */
    public String getName(Long id) {
        Category category = categoryMap.get(id);
        return category != null ? category.getName() : null;
    }
    
    /**
     * 获取子分类列表
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    public List<Category> getChildren(Long parentId) {
        return childrenMap.getOrDefault(parentId, Collections.emptyList());
    }
    
    /**
     * 获取顶级分类（一级分类）
     * @return 顶级分类列表
     */
    public List<Category> getRootCategories() {
        return getChildren(0L);
    }
    
    /**
     * 获取所有分类
     * @return 所有分类列表
     */
    public List<Category> getAll() {
        return new ArrayList<>(categoryMap.values());
    }
    
    /**
     * 移除分类
     * @param id 分类ID
     */
    public void remove(Long id) {
        Category removed = categoryMap.remove(id);
        if (removed != null) {
            Long parentId = removed.getParentId() != null ? removed.getParentId() : 0L;
            List<Category> siblings = childrenMap.get(parentId);
            if (siblings != null) {
                siblings.removeIf(c -> c.getId().equals(id));
            }
        }
    }
    
    /**
     * 清空缓存
     */
    public void clear() {
        categoryMap.clear();
        childrenMap.clear();
    }
    
    /**
     * 获取缓存大小
     * @return 缓存中的分类数量
     */
    public int size() {
        return categoryMap.size();
    }
    
    /**
     * 检查分类是否存在
     * @param id 分类ID
     * @return 是否存在
     */
    public boolean contains(Long id) {
        return categoryMap.containsKey(id);
    }
    
    /**
     * 获取分类的完整路径名称
     * @param id 分类ID
     * @return 路径名称，如"电子产品/手机/智能手机"
     */
    public String getFullPath(Long id) {
        List<String> path = new ArrayList<>();
        Category current = categoryMap.get(id);
        
        while (current != null) {
            path.add(0, current.getName());
            if (current.getParentId() == null || current.getParentId() == 0) {
                break;
            }
            current = categoryMap.get(current.getParentId());
        }
        
        return String.join("/", path);
    }
    
    /**
     * 构建分类树形结构
     * @return 树形分类列表
     */
    public List<CategoryTreeNode> buildTree() {
        return buildTreeRecursive(0L);
    }
    
    private List<CategoryTreeNode> buildTreeRecursive(Long parentId) {
        List<Category> children = getChildren(parentId);
        if (children.isEmpty()) {
            return Collections.emptyList();
        }
        
        return children.stream()
            .sorted(Comparator.comparingInt(Category::getSort))
            .map(category -> {
                CategoryTreeNode node = new CategoryTreeNode();
                node.setId(category.getId());
                node.setName(category.getName());
                node.setParentId(category.getParentId());
                node.setLevel(category.getLevel());
                node.setIcon(category.getIcon());
                node.setChildren(buildTreeRecursive(category.getId()));
                return node;
            })
            .collect(Collectors.toList());
    }
    
    /**
     * 分类树节点
     */
    @lombok.Data
    public static class CategoryTreeNode {
        private Long id;
        private String name;
        private Long parentId;
        private Integer level;
        private String icon;
        private List<CategoryTreeNode> children;
    }
}


