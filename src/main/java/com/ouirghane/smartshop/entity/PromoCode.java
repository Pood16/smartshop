package com.ouirghane.smartshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "promo_codes")
public class PromoCode extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal discountPercentage = new BigDecimal("0.05");

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    public boolean isUsable() {
        return active;
    }
}