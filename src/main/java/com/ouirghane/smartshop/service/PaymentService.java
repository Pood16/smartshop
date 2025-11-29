package com.ouirghane.smartshop.service;

import com.ouirghane.smartshop.dto.request.PaymentCreateRequestDto;
import com.ouirghane.smartshop.dto.response.PaymentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    PaymentResponseDto createPayment(Long orderId, PaymentCreateRequestDto requestDto);
    PaymentResponseDto collectPayment(Long paymentId);
    PaymentResponseDto rejectPayment(Long paymentId);
    Page<PaymentResponseDto> getAllPaymentsByOrderId(Long orderId, Pageable pageable);
}
