package com.ouirghane.smartshop.service.impl;


import com.ouirghane.smartshop.dto.request.OrderCreateRequestDto;
import com.ouirghane.smartshop.dto.request.OrderItemRequestDto;
import com.ouirghane.smartshop.dto.response.OrderResponseDto;
import com.ouirghane.smartshop.entity.*;
import com.ouirghane.smartshop.enums.ClientLoyaltyLevel;
import com.ouirghane.smartshop.enums.OrderStatus;
import com.ouirghane.smartshop.enums.PaymentStatus;
import com.ouirghane.smartshop.exception.ResourceNotFoundException;
import com.ouirghane.smartshop.exception.ValidationException;
import com.ouirghane.smartshop.mapper.OrderMapper;
import com.ouirghane.smartshop.repository.*;
import com.ouirghane.smartshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderCreateRequestDto requestDto) {

        Client client = clientRepository
                .findById(requestDto.getClientId())
                .orElseThrow(()->new ResourceNotFoundException("Client not Found"));

        List<OrderItem> items = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;
        String promoCode = "";

        for (OrderItemRequestDto requestItem: requestDto.getOrderItems()){
            Product product = productRepository
                    .findById(requestItem.getProductId())
                    .orElseThrow(()-> new ResourceNotFoundException("Product not Found"));

            if (product.isDeleted()){
                throw new ValidationException("Selected product is no longer available [ product name:  " + product.getName() + " ]");
            }

            if (product.getAvailableStock() <= 0){
                throw new ValidationException("Selected product is out of stock, [ product name: " + product.getName() + " ]");
            }

            if (product.getAvailableStock() < requestItem.getQuantity()){
                throw new ValidationException("Insufficient Stock for product " + product.getName());
            }

            BigDecimal itemTotalAmount = product
                    .getUnitPrice()
                    .multiply(BigDecimal.valueOf(requestItem.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);
            subtotal = subtotal.add(itemTotalAmount);

            OrderItem orderItem = OrderItem
                    .builder()
                    .itemTotal(itemTotalAmount)
                    .product(product)
                    .quantity(requestItem.getQuantity())
                    .build();
            items.add(orderItem);
        }

        if (requestDto.getPromoCode() != null){
            promoCode = requestDto.getPromoCode().trim().toUpperCase();
            PromoCode promocode = promoCodeRepository.findByCode(promoCode)
                    .orElseThrow(()-> new ResourceNotFoundException("Promo code not found"));

            if (!promocode.isUsable()){
                throw new ValidationException("Promo code is not available for usage");
            }
            discountAmount = subtotal
                    .multiply(promocode.getDiscountPercentage())
                    .setScale(2, RoundingMode.HALF_UP);

            promocode.setActive(false);
            promoCodeRepository.save(promocode);
        }

        BigDecimal levelLoyaltyDiscount = calculateLoyaltyDiscount(client.getClientLoyaltyLevel(), subtotal);
        discountAmount = discountAmount
                .add(levelLoyaltyDiscount)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalAfterDiscount = subtotal
                .subtract(discountAmount)
                .setScale(2, RoundingMode.HALF_UP);

        double tvaPercentage = requestDto.getTvaPercentage() != null ? requestDto.getTvaPercentage() : 20.00;
        BigDecimal tvaAmount = totalAfterDiscount
                .multiply(BigDecimal.valueOf(tvaPercentage / 100))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = totalAfterDiscount.add(tvaAmount).setScale(2, RoundingMode.HALF_UP);

        Order order = Order
                .builder()
                .client(client)
                .orderDate(LocalDateTime.now())
                .subtotal(subtotal)
                .tvaAmount(tvaAmount)
                .discountAmount(discountAmount)
                .tvaPercentage(tvaPercentage)
                .totalAmount(totalAmount)
                .promoCode(promoCode)
                .status(OrderStatus.PENDING)
                .remainingAmount(totalAmount)
                .orderItems(items)
                .build();
        
        for (OrderItem item : items) {
            item.setOrder(order);
        }
        
        // Reserve stock immediately when order is created
        for (OrderItem item : items) {
            Product product = item.getProduct();
            product.setAvailableStock(product.getAvailableStock() - item.getQuantity());
            productRepository.save(product);
        }
        
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toResponseDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));
        return orderMapper.toResponseDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        return orderRepository
                .findAll(pageable)
                .map(orderMapper::toResponseDto);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Order not Found"));
        if (order.getStatus() == OrderStatus.CONFIRMED){
            throw new ValidationException("You can't delete confirmed orders");
        }
        if (order.getRemainingAmount().compareTo(BigDecimal.ZERO) > 0){
            throw new ValidationException("You can't Delete Orders with pending payments");
        }
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDto> orderHistorique(Long userId, Pageable pageable) {
        Client client = clientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not Found"));
        return orderRepository.findByClientId(client.getId(), pageable)
                .map(orderMapper::toResponseDto);
    }


    @Override
    @Transactional
    public OrderResponseDto confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.canBeConfirmed()) {
            throw new ValidationException("Order cannot be confirmed. Must be PENDING and fully paid.");
        }


        

        Client client = order.getClient();
        client.setTotalOrders(client.getTotalOrders() + 1);
        client.setTotalSpent(client.getTotalSpent().add(order.getTotalAmount()));

        if (client.getFirstOrderDate() == null) {
            client.setFirstOrderDate(LocalDateTime.now());
        }
        client.setLastOrderDate(LocalDateTime.now());


        updateClientLoyaltyLevel(client);
        clientRepository.save(client);


        order.setStatus(OrderStatus.CONFIRMED);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toResponseDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponseDto cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ValidationException("Only PENDING orders can be canceled");
        }

        if (!order.getPayments().isEmpty()){
            for (Payment orderPayment: order.getPayments()){
                if (orderPayment.getStatus() == PaymentStatus.COLLECTED){
                    throw new ValidationException("You can't delete Orders with collected payments");
                }
            }
        }


        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setAvailableStock(product.getAvailableStock() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELED);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toResponseDto(savedOrder);
    }

    private void updateClientLoyaltyLevel(Client client) {
        if (client.getTotalOrders() >= 20 || client.getTotalSpent().compareTo(new BigDecimal("15000")) >= 0) {
            client.setClientLoyaltyLevel(ClientLoyaltyLevel.PLATINUM);
        } else if (client.getTotalOrders() >= 10 || client.getTotalSpent().compareTo(new BigDecimal("5000")) >= 0) {
            client.setClientLoyaltyLevel(ClientLoyaltyLevel.GOLD);
        } else if (client.getTotalOrders() >= 3 || client.getTotalSpent().compareTo(new BigDecimal("1000")) >= 0) {
            client.setClientLoyaltyLevel(ClientLoyaltyLevel.SILVER);
        }
    }

    private BigDecimal calculateLoyaltyDiscount(ClientLoyaltyLevel level, BigDecimal subtotal) {
         BigDecimal discount = BigDecimal.ZERO;
         BigDecimal SILVER_AMOUNT = new BigDecimal("500");
         BigDecimal GOLD_AMOUNT = new BigDecimal("800");
         BigDecimal PLATINUM_AMOUNT = new BigDecimal("1200");
         BigDecimal SILVER_DISCOUNT_RATE = new BigDecimal("0.05");
         BigDecimal GOLD_DISCOUNT_RATE = new BigDecimal("0.10");
         BigDecimal PLATINUM_DISCOUNT_RATE = new BigDecimal("0.15");

        switch (level) {
            case SILVER -> {
                if (subtotal.compareTo(SILVER_AMOUNT) >= 0) {
                    discount = subtotal.multiply(SILVER_DISCOUNT_RATE);
                }
            }
            case GOLD -> {
                if (subtotal.compareTo(GOLD_AMOUNT) >= 0) {
                    discount = subtotal.multiply(GOLD_DISCOUNT_RATE);
                }
            }
            case PLATINUM -> {
                if (subtotal.compareTo(PLATINUM_AMOUNT) >= 0) {
                    discount = subtotal.multiply(PLATINUM_DISCOUNT_RATE);
                }
            }
        }
        return discount.setScale(2, RoundingMode.HALF_UP);
    }
}
