package com.ouirghane.smartshop.service;

import com.ouirghane.smartshop.dto.request.OrderCreateRequestDto;
import com.ouirghane.smartshop.dto.response.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponseDto createOrder(OrderCreateRequestDto requestDto);

    OrderResponseDto getOrderById(Long id);

    Page<OrderResponseDto> getAllOrders(Pageable pageable);

    void deleteOrder(Long id);

    Page<OrderResponseDto> orderHistorique(Long clientId, Pageable pageable);
    
    OrderResponseDto confirmOrder(Long orderId);
    
    OrderResponseDto cancelOrder(Long orderId);
}
