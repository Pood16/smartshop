package com.ouirghane.smartshop.service.impl;

import com.ouirghane.smartshop.dto.request.ProductCreateRequestDto;
import com.ouirghane.smartshop.dto.request.ProductFilter;
import com.ouirghane.smartshop.dto.request.ProductUpdateRequestDto;
import com.ouirghane.smartshop.dto.response.ProductResponseDto;
import com.ouirghane.smartshop.entity.Product;
import com.ouirghane.smartshop.exception.ResourceNotFoundException;
import com.ouirghane.smartshop.exception.ValidationException;
import com.ouirghane.smartshop.mapper.ProductMapper;
import com.ouirghane.smartshop.repository.OrderItemRepository;
import com.ouirghane.smartshop.repository.ProductRepository;
import com.ouirghane.smartshop.service.ProductService;
import com.ouirghane.smartshop.specifications.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDto createProduct(ProductCreateRequestDto request){
        if (productRepository.existsByName(request.getName())){
            throw new ValidationException("Product name already exists");
        }
        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProducts(ProductFilter filter, Pageable pageable) {
        Specification<Product> spec = ProductSpecifications.buildSpecification(filter);
        return productRepository.findAll(spec, pageable).map(productMapper::toResponse);
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductUpdateRequestDto request) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        productMapper.updateProductFromDto(request, product);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsByIdAndDeletedFalse(id)) {
            throw new ResourceNotFoundException("Product not found");
        }

        boolean hasOrders = orderItemRepository.existsByProductId(id);
        
        if (hasOrders) {
            productRepository.softDeleteById(id);
        } else {
            productRepository.deleteById(id);
        }
    }
}
