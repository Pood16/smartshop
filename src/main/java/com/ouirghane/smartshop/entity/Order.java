package com.ouirghane.smartshop.entity;


import com.ouirghane.smartshop.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "order_date", nullable = false)
    @Builder.Default
    private LocalDateTime orderDate = LocalDateTime.now();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "tva_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal tvaAmount;

    @Builder.Default
    @Column(name = "tva_percentage", nullable = false)
    private Double tvaPercentage = 20.00;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;


    @JoinColumn(name = "promo_code")
    private String promoCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "remaining_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal remainingAmount;

    @OneToMany(mappedBy = "order", fetch =  FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;


    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Payment> payments;

    public boolean isFullyPaid(){
        return remainingAmount.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean canBeConfirmed(){
        return status == OrderStatus.PENDING && isFullyPaid();
    }




}
