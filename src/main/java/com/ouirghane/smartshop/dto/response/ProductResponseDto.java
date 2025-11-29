package com.ouirghane.smartshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {

    private Long id;
    private String name;
    private BigDecimal unitPrice;
    private Integer availableStock;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}