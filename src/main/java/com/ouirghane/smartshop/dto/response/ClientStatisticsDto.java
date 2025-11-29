package com.ouirghane.smartshop.dto.response;

import com.ouirghane.smartshop.enums.ClientLoyaltyLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientStatisticsDto {

    private Integer totalOrders;
    private BigDecimal totalSpent;
    private ClientLoyaltyLevel currentLevel;
    private ClientLoyaltyLevel nextLevel;
    private Integer ordersToNextLevel;
    private BigDecimal amountToNextLevel;
}