package com.ouirghane.smartshop.service;

import com.ouirghane.smartshop.dto.request.ProductCreateRequestDto;
import com.ouirghane.smartshop.dto.request.ProductUpdateRequestDto;
import com.ouirghane.smartshop.dto.response.ProductResponseDto;

import java.util.List;

public interface ProductService {

    ProductResponseDto createProduct(ProductCreateRequestDto request);
    
    ProductResponseDto getProductById(Long id);
    
    List<ProductResponseDto> getAllProducts();
    
    ProductResponseDto updateProduct(Long id, ProductUpdateRequestDto request);
    
    void deleteProduct(Long id);
}
