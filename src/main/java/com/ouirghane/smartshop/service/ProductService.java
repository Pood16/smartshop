package com.ouirghane.smartshop.service;

import com.ouirghane.smartshop.dto.request.ProductCreateRequestDto;
import com.ouirghane.smartshop.dto.request.ProductFilter;
import com.ouirghane.smartshop.dto.request.ProductUpdateRequestDto;
import com.ouirghane.smartshop.dto.response.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductResponseDto createProduct(ProductCreateRequestDto request);
    
    ProductResponseDto getProductById(Long id);
    
    Page<ProductResponseDto> getAllProducts(ProductFilter filter, Pageable page);
    
    ProductResponseDto updateProduct(Long id, ProductUpdateRequestDto request);
    
    void deleteProduct(Long id);
}
