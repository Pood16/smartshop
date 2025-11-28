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

        validatePaymentTypeRules(payment, requestDto);

        if (payment.getPaymentType() == PaymentType.CASH) {
            payment.setStatus(PaymentStatus.COLLECTED);
            payment.setCollectionDate(LocalDateTime.now());
        } else {
            payment.setStatus(PaymentStatus.PENDING);
        }


        if (paymentRepository.countByOrderId(orderId) == 0){
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem item: orderItems){
                Product product = item.getProduct();
                product.setAvailableStock(product.getAvailableStock() - item.getQuantity());
                productRepository.save(product);
            }
        }
        payment.setOrder(order);
        Payment savedPayment = paymentRepository.save(payment);

        BigDecimal newRemaining = order.getRemainingAmount().subtract(requestDto.getAmount());
        order.setRemainingAmount(newRemaining);
        orderRepository.save(order);

        return paymentMapper.toResponseDto(savedPayment);
    }

    private void validatePaymentTypeRules(Payment payment, PaymentCreateRequestDto dto) {
        switch (payment.getPaymentType()) {
            case CASH:
                if (!payment.isCashAmountValid()) {
                    throw new ValidationException("Cash payment cannot exceed 20,000 DH");
                }
                break;
            case CHECK:
                if (dto.getBankName() == null || dto.getDueDate() == null) {
                    throw new ValidationException("Check payment requires bank name and due date");
                }
                break;
            case TRANSFER:
                if (dto.getBankName() == null || dto.getReferenceNumber() == null) {
                    throw new ValidationException("Transfer payment requires bank name and reference number");
                }
                break;
        }
    }
}
