package com.ouirghane.smartshop.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCodeCreateRequestDto {

    @NotNull(message = "Promo code is required")
    @Pattern(regexp = "PROMO-[A-Z0-9]{4}", message = "Promo code must follow format PROMO-XXXX")
    private String code;

    @NotNull(message = "Discount percentage is required")
    @DecimalMin(value = "0.01", message = "Discount percentage must be greater than 0")
    @DecimalMax(value = "100.00", message = "Discount percentage cannot exceed 100")
    private BigDecimal discountPercentage;

    @Builder.Default
    private boolean active = true;

}