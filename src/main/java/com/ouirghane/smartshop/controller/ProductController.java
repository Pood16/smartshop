package com.ouirghane.smartshop.controller;

import com.ouirghane.smartshop.dto.request.ProductCreateRequestDto;
import com.ouirghane.smartshop.dto.request.ProductFilter;
import com.ouirghane.smartshop.dto.request.ProductUpdateRequestDto;
import com.ouirghane.smartshop.dto.response.ProductResponseDto;
import com.ouirghane.smartshop.service.ProductService;
import com.ouirghane.smartshop.service.SessionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final SessionService sessionService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @Valid
            @RequestBody
            ProductCreateRequestDto request,
            HttpSession session) {
        sessionService.validateAdminRole(session);
        ProductResponseDto response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(
            @PathVariable Long id,
            HttpSession session) {
        sessionService.validateAuthentication(session);
        ProductResponseDto response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(

            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false) Integer maxStock,
            @RequestParam(required = false) Boolean deletedStatus,
            @RequestParam(required = false) Boolean inStock,
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            HttpSession session) {
        sessionService.validateAuthentication(session);
        ProductFilter filter = ProductFilter
                .builder()
                .name(name)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .minStock(minStock)
                .maxStock(maxStock)
                .deletedStatus(deletedStatus)
                .inStock(inStock)
                .build();
        Page<ProductResponseDto> response = productService.getAllProducts(filter, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @Valid
            @RequestBody
            ProductUpdateRequestDto request,
            HttpSession session) {
        sessionService.validateAdminRole(session);
        ProductResponseDto response = productService.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            HttpSession session) {
        sessionService.validateAdminRole(session);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
