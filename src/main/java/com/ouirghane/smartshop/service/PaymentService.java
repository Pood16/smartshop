package com.ouirghane.smartshop.service;

import com.ouirghane.smartshop.dto.request.PaymentCreateRequestDto;
import com.ouirghane.smartshop.dto.response.PaymentResponseDto;

public interface PaymentService {
    PaymentResponseDto createPayment(Long orderId, PaymentCreateRequestDto requestDto);
}
