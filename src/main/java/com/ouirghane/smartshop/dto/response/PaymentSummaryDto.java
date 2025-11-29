package com.ouirghane.smartshop.dto.response;

import com.ouirghane.smartshop.enums.PaymentStatus;
import com.ouirghane.smartshop.enums.PaymentType;
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
public class PaymentSummaryDto {

    private Long id;
    private BigDecimal amount;
    private PaymentType paymentType;
    private LocalDateTime paymentDate;
    private PaymentStatus status;
}