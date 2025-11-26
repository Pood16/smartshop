package com.ouirghane.smartshop.mapper;


import com.ouirghane.smartshop.dto.request.ClientCreateRequestDto;
import com.ouirghane.smartshop.dto.request.ClientUpdateRequestDto;
import com.ouirghane.smartshop.dto.response.ClientResponseDto;
import com.ouirghane.smartshop.dto.response.ClientSummaryDto;
import com.ouirghane.smartshop.dto.response.ClientStatisticsDto;
import com.ouirghane.smartshop.dto.response.ClientOrderHistoryDto;
import com.ouirghane.smartshop.entity.Client;
import com.ouirghane.smartshop.entity.Order;
import com.ouirghane.smartshop.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mappings({
            @Mapping(target = "client.id", ignore = true),
            @Mapping(target = "clientLoyaltyLevel", ignore = true),
            @Mapping(target = "totalOrders", ignore = true),
            @Mapping(target = "totalSpent", ignore = true),
            @Mapping(target = "firstOrderDate", ignore = true),
            @Mapping(target = "lastOrderDate", ignore = true),
            @Mapping(target = "orders", ignore = true),
            @Mapping(target = "user", source = "user")
    })
    Client toEntity(ClientCreateRequestDto requestDTO, User user);

    ClientResponseDto toResponseDto(Client client);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "clientLoyaltyLevel", ignore = true),
            @Mapping(target = "totalOrders", ignore = true),
            @Mapping(target = "totalSpent", ignore = true),
            @Mapping(target = "firstOrderDate", ignore = true),
            @Mapping(target = "lastOrderDate", ignore = true),
            @Mapping(target = "orders", ignore = true),
            @Mapping(target = "user", ignore = true)
    })
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateClientFromDto(ClientUpdateRequestDto requestDto, @MappingTarget Client client);

}
