package com.ouirghane.smartshop.service;

import com.ouirghane.smartshop.dto.request.OrderCreateRequestDto;
import com.ouirghane.smartshop.dto.response.OrderResponseDto;

public interface OrderService {

    OrderResponseDto createOrder(OrderCreateRequestDto requestDto);

    OrderResponseDto getOrderById(Long id);

}
