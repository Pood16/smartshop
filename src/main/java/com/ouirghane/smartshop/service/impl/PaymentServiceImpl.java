package com.ouirghane.smartshop.service.impl;


import com.ouirghane.smartshop.dto.request.PaymentCreateRequestDto;
import com.ouirghane.smartshop.dto.response.PaymentResponseDto;
import com.ouirghane.smartshop.entity.Order;
import com.ouirghane.smartshop.entity.OrderItem;
import com.ouirghane.smartshop.entity.Payment;
import com.ouirghane.smartshop.entity.Product;
import com.ouirghane.smartshop.enums.OrderStatus;
import com.ouirghane.smartshop.enums.PaymentStatus;
import com.ouirghane.smartshop.enums.PaymentType;
import com.ouirghane.smartshop.exception.ResourceNotFoundException;
import com.ouirghane.smartshop.exception.ValidationException;
import com.ouirghane.smartshop.mapper.PaymentMapper;
import com.ouirghane.smartshop.repository.OrderRepository;
import com.ouirghane.smartshop.repository.PaymentRepository;
import com.ouirghane.smartshop.repository.ProductRepository;
import com.ouirghane.smartshop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentMapper paymentMapper;





    @Override
    @Transactional
    public PaymentResponseDto createPayment(Long orderId, PaymentCreateRequestDto requestDto) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ValidationException("Cannot add payment to non pending order");
        }

        if (requestDto.getAmount().compareTo(order.getRemainingAmount()) > 0) {
            throw new ValidationException("Payment amount exceeds remaining amount");
        }

        Payment payment = paymentMapper.toEntity(requestDto);
        payment.setPaymentDate(LocalDateTime.now());

        long nextNumber =  paymentRepository.countByOrderId(order.getId()) + 1;
        payment.setPaymentNumber((int) nextNumber);

        validatePaymentTypeRules(payment);

        if (payment.getPaymentType() == PaymentType.CASH) {
            payment.setStatus(PaymentStatus.COLLECTED);
            payment.setCollectionDate(LocalDateTime.now());
            BigDecimal newRemaining = order.getRemainingAmount().subtract(requestDto.getAmount());
            order.setRemainingAmount(newRemaining);
            orderRepository.save(order);
        }else {
            payment.setStatus(PaymentStatus.PENDING);
        }

        payment.setOrder(order);
        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toResponseDto(savedPayment);
    }


    @Override
    @Transactional
    public PaymentResponseDto collectPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        Order order = payment.getOrder();

        if (payment.getOrder().getStatus() == OrderStatus.CONFIRMED) {
            throw new ValidationException("Cannot modify payment for confirmed order");
        }

        if (payment.getStatus() == PaymentStatus.COLLECTED) {
            throw new ValidationException("Payment is already collected");
        }

        payment.setStatus(PaymentStatus.COLLECTED);
        payment.setCollectionDate(LocalDateTime.now());

        BigDecimal newRemaining = order.getRemainingAmount().subtract(payment.getAmount());
        order.setRemainingAmount(newRemaining);
        orderRepository.save(order);

        Payment updatedPayment = paymentRepository.save(payment);
        return paymentMapper.toResponseDto(updatedPayment);
    }

    @Override
    @Transactional
    public PaymentResponseDto rejectPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (payment.getOrder().getStatus() == OrderStatus.CONFIRMED) {
            throw new ValidationException("Cannot modify payment for confirmed order");
        }

        if (payment.getPaymentType() == PaymentType.CASH) {
            throw new ValidationException("Cash payments cannot be rejected");
        }

        payment.setStatus(PaymentStatus.REJECTED);

        Payment updatedPayment = paymentRepository.save(payment);
        return paymentMapper.toResponseDto(updatedPayment);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponseDto> getAllPaymentsByOrderId(Long orderId, Pageable pageable){
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order not found with id: " + orderId);
        }
        
        return paymentRepository.findPaymentsByOrderId(orderId, pageable)
                .map(paymentMapper::toResponseDto);
    }

    private void validatePaymentTypeRules(Payment payment) {
        switch (payment.getPaymentType()) {
            case CASH:
                if (!payment.isCashAmountValid()) {
                    throw new ValidationException("Cash payment cannot exceed 20000 DH");
                }
                break;
            case CHECK:
                if (!payment.isCheckPaymentValid()) {
                    throw new ValidationException("Check payment requires bank name");
                }
                break;
            case TRANSFER:
                if (!payment.isTransferPaymentValid()) {
                    throw new ValidationException("Transfer payment requires bank name and reference number");
                }
                break;
        }
    }
}
