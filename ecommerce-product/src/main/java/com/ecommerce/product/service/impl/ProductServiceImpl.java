package com.ecommerce.product.service.impl;

import com.ecommerce.common.dto.ProductDTO;
import com.ecommerce.common.entity.Product;
import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.common.result.PageResult;
import com.ecommerce.common.result.Result;
import com.ecommerce.common.result.ResultCode;
import com.ecommerce.common.vo.ProductVO;
import com.ecommerce.product.feign.InventoryFeignClient;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.CategoryService;
import com.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品服务实现类
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final InventoryFeignClient inventoryFeignClient;
    
    @Override
    @Transactional
    public ProductVO createProduct(ProductDTO productDTO) {
        // 验证分类存在
        categoryService.getCategoryById(productDTO.getCategoryId());
        
        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .categoryId(productDTO.getCategoryId())
                .price(productDTO.getPrice())
                .marketPrice(productDTO.getMarketPrice())
                .mainImage(productDTO.getMainImage())
                .images(productDTO.getImages())
                .status(1)
                .sales(0)
                .build();
        
        product = productRepository.save(product);
        return convertToVO(product);
    }
    
    @Override
    @Transactional
    public ProductVO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.PRODUCT_NOT_FOUND));
        
        if (productDTO.getName() != null) {
            product.setName(productDTO.getName());
        }
        if (productDTO.getDescription() != null) {
            product.setDescription(productDTO.getDescription());
        }
        if (productDTO.getCategoryId() != null) {
            categoryService.getCategoryById(productDTO.getCategoryId());
            product.setCategoryId(productDTO.getCategoryId());
        }
        if (productDTO.getPrice() != null) {
            product.setPrice(productDTO.getPrice());
        }
        if (productDTO.getMarketPrice() != null) {
            product.setMarketPrice(productDTO.getMarketPrice());
        }
        if (productDTO.getMainImage() != null) {
            product.setMainImage(productDTO.getMainImage());
        }
        if (productDTO.getImages() != null) {
            product.setImages(productDTO.getImages());
        }
        
        product = productRepository.save(product);
        return convertToVO(product);
    }
    
    @Override
    public ProductVO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.PRODUCT_NOT_FOUND));
        return convertToVO(product);
    }
    
    @Override
    public Product getProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.PRODUCT_NOT_FOUND));
    }
    
    @Override
    public PageResult<ProductVO> getProductList(Integer pageNum, Integer pageSize, String keyword) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));
        
        Page<Product> page;
        if (StringUtils.hasText(keyword)) {
            page = productRepository.findByNameContainingAndStatus(keyword, 1, pageRequest);
        } else {
            page = productRepository.findByStatus(1, pageRequest);
        }
        
        List<ProductVO> list = page.getContent().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        return PageResult.of(list, page.getTotalElements(), pageNum, pageSize);
    }
    
    @Override
    public List<ProductVO> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryIdAndStatus(categoryId, 1);
        return products.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void onShelf(Long id) {
        if (!productRepository.existsById(id)) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_FOUND);
        }
        productRepository.updateStatus(id, 1);
    }
    
    @Override
    @Transactional
    public void offShelf(Long id) {
        if (!productRepository.existsById(id)) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_FOUND);
        }
        productRepository.updateStatus(id, 0);
    }
    
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new BusinessException(ResultCode.PRODUCT_NOT_FOUND);
        }
        productRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public void increaseSales(Long id, Integer quantity) {
        productRepository.increaseSales(id, quantity);
    }
    
    @Override
    public Long getProductCount() {
        return productRepository.count();
    }
    
    /**
     * 转换为VO对象
     */
    private ProductVO convertToVO(Product product) {
        // 获取库存信息
        Integer stock = 0;
        try {
            Result<Integer> stockResult = inventoryFeignClient.getAvailableStock(product.getId());
            if (stockResult.isSuccess() && stockResult.getData() != null) {
                stock = stockResult.getData();
            }
        } catch (Exception e) {
            // 获取库存失败，使用默认值
        }
        
        return ProductVO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .categoryId(product.getCategoryId())
                .categoryName(categoryService.getCategoryName(product.getCategoryId()))
                .price(product.getPrice())
                .marketPrice(product.getMarketPrice())
                .mainImage(product.getMainImage())
                .images(product.getImages())
                .status(product.getStatus())
                .sales(product.getSales())
                .stock(stock)
                .createTime(product.getCreateTime())
                .build();
    }
}

