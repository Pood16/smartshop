package com.ouirghane.smartshop.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateRequestDto {

    @NotNull(message = "Client is required")
    private Long clientId;

    @NotNull(message = "Order items are required")
    @Size(min = 1, message = "Order must contain at least one item")
    @Valid
    private List<OrderItemRequestDto> orderItems;

    private Double tvaPercentage;

    private String promoCode;
}