package com.ouirghane.smartshop.entity;


import com.ouirghane.smartshop.enums.PaymentStatus;
import com.ouirghane.smartshop.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payments")
public class Payment{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "payment_number", nullable = false)
    private Integer paymentNumber;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "collection_date")
    private LocalDateTime collectionDate;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "bank_name")
    private String bankName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    public boolean isCashAmountValid() {
        if (paymentType == PaymentType.CASH) {
            return amount.compareTo(new BigDecimal("20000")) <= 0;
        }
        return true;
    }

    public boolean isCheckPaymentValid() {
        if (paymentType == PaymentType.CHECK) {
            return bankName != null && !bankName.trim().isEmpty();
        }
        return true;
    }

    public boolean isTransferPaymentValid() {
        if (paymentType == PaymentType.TRANSFER) {
            return bankName != null && !bankName.trim().isEmpty();
        }
        return true;
    }

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}