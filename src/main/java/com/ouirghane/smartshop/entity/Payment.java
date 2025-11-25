package com.ouirghane.smartshop.entity;


import com.ouirghane.smartshop.enums.PaymentStatus;
import com.ouirghane.smartshop.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payments")
public class Payment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Integer paymentNumber;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentType paymentType;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "collection_date")
    private LocalDateTime collectionDate;


    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "due_date")
    private LocalDate dueDate;

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
            return bankName != null && !bankName.trim().isEmpty() && dueDate != null;
        }
        return true;
    }

    public boolean isTransferPaymentValid() {
        if (paymentType == PaymentType.TRANSFER) {
            return bankName != null && !bankName.trim().isEmpty();
        }
        return true;
    }
}