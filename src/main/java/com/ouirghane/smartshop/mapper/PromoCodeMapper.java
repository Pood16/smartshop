package com.ouirghane.smartshop.mapper;


import com.ouirghane.smartshop.dto.response.PromoCodeResponseDto;
import com.ouirghane.smartshop.entity.PromoCode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromoCodeMapper {
    PromoCodeResponseDto toPromoCodeResponseDto(PromoCode promoCode);
}
