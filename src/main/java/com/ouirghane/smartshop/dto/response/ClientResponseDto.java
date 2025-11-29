package com.ouirghane.smartshop.dto.response;

import com.ouirghane.smartshop.enums.ClientLoyaltyLevel;
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
public class ClientResponseDto {

    private Long id;
    private String name;
    private String email;
    private ClientLoyaltyLevel clientLoyaltyLevel;
    private Integer totalOrders;
    private BigDecimal totalSpent;
    private LocalDateTime firstOrderDate;
    private LocalDateTime lastOrderDate;
}