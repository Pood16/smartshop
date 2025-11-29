package com.ouirghane.smartshop.mapper;

import com.ouirghane.smartshop.dto.response.*;
import com.ouirghane.smartshop.entity.*;
import org.mapstruct.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponseDto toResponseDto(Order order);

    @Mapping(target = "itemTotalAmount", source = "itemTotal")
    OrderItemResponseDto toOrderItemResponseDto(OrderItem orderItem);


}
