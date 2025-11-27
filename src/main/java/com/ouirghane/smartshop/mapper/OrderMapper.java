package com.ouirghane.smartshop.mapper;

import com.ouirghane.smartshop.dto.response.*;
import com.ouirghane.smartshop.entity.*;
import org.mapstruct.*;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {


    OrderResponseDto toResponseDto(Order order);

    @Mapping(target = "unitPrice", source = "product.unitPrice")
    @Mapping(target = "itemTotalAmount", source = "itemTotal")
    OrderItemResponseDto toOrderItemResponseDto(OrderItem orderItem);
    
    ProductResponseDto toProductResponseDto(Product product);

    ClientMinimalInformationsDto toClientSummaryDto(Client client);

    PromoCodeResponseDto toPromoCodeResponseDto(PromoCode promoCode);

    PaymentSummaryDto toPaymentSummaryDto(Payment payment);

    List<OrderResponseDto> toResponseDtoList(List<Order> orders);

}
