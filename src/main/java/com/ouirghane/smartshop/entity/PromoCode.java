package com.ouirghane.smartshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "promo_codes")
public class PromoCode extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal discountPercentage = new BigDecimal("5.00");

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean singleUse = true;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Column(name = "used_by_order_id")
    private Long usedByOrderId;

    public boolean isUsable() {
        return active && (!singleUse || usedAt == null);
    }
}