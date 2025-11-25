package com.ouirghane.smartshop.entity;


import com.ouirghane.smartshop.enums.ClientLoyaltyLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "clients")
public class Client extends BaseEntity{

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ClientLoyaltyLevel clientLoyaltyLevel = ClientLoyaltyLevel.BASIC;

    @Column(name = "total_orders")
    @Builder.Default
    private Integer totalOrders = 0;

    @Column(name = "total_spent", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @Column(name = "first_order_date")
    private LocalDateTime firstOrderDate;

    @Column(name = "last_order_date")
    private LocalDateTime lastOrderDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;
}
