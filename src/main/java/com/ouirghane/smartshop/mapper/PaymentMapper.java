package com.ouirghane.smartshop.mapper;
import com.ouirghane.smartshop.dto.request.PaymentCreateRequestDto;
import com.ouirghane.smartshop.dto.response.PaymentResponseDto;
import com.ouirghane.smartshop.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order.id")
    PaymentResponseDto toResponseDto(Payment payment);


    @Mapping(target = "payment.id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "paymentNumber", ignore = true)
    @Mapping(target = "paymentDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "collectionDate", ignore = true)
    Payment toEntity(PaymentCreateRequestDto dto);

}
