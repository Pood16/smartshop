package com.ouirghane.smartshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductFilter {
    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minStock;
    private Integer maxStock;
    private Boolean deletedStatus;
    private Boolean inStock;
}
