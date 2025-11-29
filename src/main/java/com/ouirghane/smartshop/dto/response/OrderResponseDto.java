package com.ouirghane.smartshop.dto.response;

import com.ouirghane.smartshop.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {

    private Long id;
    private ClientMinimalInformationsDto client;
    private LocalDateTime orderDate;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal tvaAmount;
    private Double tvaPercentage;
    private BigDecimal totalAmount;
    private String promoCode;
    private OrderStatus status;
    private BigDecimal remainingAmount;
    private List<OrderItemResponseDto> orderItems;
    @Builder.Default
    private List<PaymentSummaryDto> payments = new ArrayList<>();
}