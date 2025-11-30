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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private PromoCodeRepository promoCodeRepository;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Client client;
    private Product product;
    private Order order;
    private OrderCreateRequestDto createRequest;
    private OrderResponseDto responseDto;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .clientLoyaltyLevel(ClientLoyaltyLevel.BASIC)
                .totalOrders(0)
                .totalSpent(BigDecimal.ZERO)
                .build();

        product = Product.builder()
                .name("Test Product")
                .unitPrice(new BigDecimal("100.00"))
                .availableStock(10)
                .deleted(false)
                .build();

        order = Order.builder()
                .client(client)
                .status(OrderStatus.PENDING)
                .totalAmount(new BigDecimal("120.00"))
                .remainingAmount(BigDecimal.ZERO)
                .orderItems(new ArrayList<>())
                .payments(new ArrayList<>())
                .build();

        OrderItemRequestDto itemRequest = new OrderItemRequestDto();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(2);

        createRequest = new OrderCreateRequestDto();
        createRequest.setClientId(1L);
        createRequest.setOrderItems(List.of(itemRequest));

        responseDto = new OrderResponseDto();
    }

    @Test
    void createOrder_Success() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        OrderResponseDto result = orderService.createOrder(createRequest);

        assertNotNull(result);
        verify(productRepository).save(any(Product.class));
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_WithPromoCode() {
        PromoCode promoCode = PromoCode.builder()
                .code("PROMO10")
                .discountPercentage(new BigDecimal("0.10"))
                .active(true)
                .build();

        createRequest.setPromoCode("promo10");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(promoCodeRepository.findByCode("PROMO10")).thenReturn(Optional.of(promoCode));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        OrderResponseDto result = orderService.createOrder(createRequest);

        assertNotNull(result);
        assertFalse(promoCode.isActive());
        verify(promoCodeRepository).save(promoCode);
    }

    @Test
    void createOrder_WithSilverLoyaltyDiscount() {
        client.setClientLoyaltyLevel(ClientLoyaltyLevel.SILVER);
        product.setUnitPrice(new BigDecimal("300.00"));

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        OrderResponseDto result = orderService.createOrder(createRequest);

        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_WithGoldLoyaltyDiscount() {
        client.setClientLoyaltyLevel(ClientLoyaltyLevel.GOLD);
        product.setUnitPrice(new BigDecimal("500.00"));

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        OrderResponseDto result = orderService.createOrder(createRequest);

        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_WithPlatinumLoyaltyDiscount() {
        client.setClientLoyaltyLevel(ClientLoyaltyLevel.PLATINUM);
        product.setUnitPrice(new BigDecimal("700.00"));

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        OrderResponseDto result = orderService.createOrder(createRequest);

        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_WithCustomTVA() {
        createRequest.setTvaPercentage(10.0);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        OrderResponseDto result = orderService.createOrder(createRequest);

        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_ReservesStockImmediately() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        orderService.createOrder(createRequest);

        assertEquals(8, product.getAvailableStock());
        verify(productRepository).save(product);
    }

    @Test
    void createOrder_ClientNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(createRequest));
    }

    @Test
    void createOrder_ProductNotFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(createRequest));
    }

    @Test
    void createOrder_ProductDeleted() {
        product.setDeleted(true);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(ValidationException.class, () -> orderService.createOrder(createRequest));
    }

    @Test
    void createOrder_ProductOutOfStock() {
        product.setAvailableStock(0);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(ValidationException.class, () -> orderService.createOrder(createRequest));
    }

    @Test
    void createOrder_InsufficientStock() {
        product.setAvailableStock(1);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(ValidationException.class, () -> orderService.createOrder(createRequest));
    }

    @Test
    void createOrder_PromoCodeNotFound() {
        createRequest.setPromoCode("INVALID");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(promoCodeRepository.findByCode("INVALID")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(createRequest));
    }

    @Test
    void createOrder_PromoCodeNotUsable() {
        PromoCode promoCode = PromoCode.builder()
                .code("EXPIRED")
                .active(false)
                .build();

        createRequest.setPromoCode("expired");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(promoCodeRepository.findByCode("EXPIRED")).thenReturn(Optional.of(promoCode));

        assertThrows(ValidationException.class, () -> orderService.createOrder(createRequest));
    }

    @Test
    void getOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toResponseDto(order)).thenReturn(responseDto);

        OrderResponseDto result = orderService.getOrderById(1L);

        assertNotNull(result);
    }

    @Test
    void getOrderById_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    void getAllOrders_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> orderPage = new PageImpl<>(List.of(order));

        when(orderRepository.findAll(pageable)).thenReturn(orderPage);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        Page<OrderResponseDto> result = orderService.getAllOrders(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void deleteOrder_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertDoesNotThrow(() -> orderService.deleteOrder(1L));
        verify(orderRepository).deleteById(1L);
    }

    @Test
    void deleteOrder_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrder(1L));
    }

    @Test
    void deleteOrder_ConfirmedOrder() {
        order.setStatus(OrderStatus.CONFIRMED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(ValidationException.class, () -> orderService.deleteOrder(1L));
    }

    @Test
    void deleteOrder_WithPendingPayments() {
        order.setRemainingAmount(new BigDecimal("100.00"));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(ValidationException.class, () -> orderService.deleteOrder(1L));
    }

    @Test
    void orderHistorique_Success() {
        User user = User.builder().build();
        client.setUser(user);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> orderPage = new PageImpl<>(List.of(order));

        when(clientRepository.findByUserId(1L)).thenReturn(Optional.of(client));
        when(orderRepository.findByClientId(1L, pageable)).thenReturn(orderPage);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        Page<OrderResponseDto> result = orderService.orderHistorique(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void orderHistorique_ClientNotFound() {
        Pageable pageable = PageRequest.of(0, 10);

        when(clientRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.orderHistorique(1L, pageable));
    }

    @Test
    void confirmOrder_Success() {
        order.setRemainingAmount(BigDecimal.ZERO);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        OrderResponseDto result = orderService.confirmOrder(1L);

        assertNotNull(result);
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        assertEquals(1, client.getTotalOrders());
        verify(clientRepository).save(client);
    }

    @Test
    void confirmOrder_UpdatesClientStatistics() {
        order.setRemainingAmount(BigDecimal.ZERO);
        order.setTotalAmount(new BigDecimal("1500.00"));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        orderService.confirmOrder(1L);

        assertEquals(1, client.getTotalOrders());
        assertEquals(new BigDecimal("1500.00"), client.getTotalSpent());
        assertNotNull(client.getFirstOrderDate());
        assertNotNull(client.getLastOrderDate());
    }

    @Test
    void confirmOrder_UpgradesToSilver() {
        order.setRemainingAmount(BigDecimal.ZERO);
        order.setTotalAmount(new BigDecimal("1000.00"));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        orderService.confirmOrder(1L);

        assertEquals(ClientLoyaltyLevel.SILVER, client.getClientLoyaltyLevel());
    }

    @Test
    void confirmOrder_UpgradesToGold() {
        order.setRemainingAmount(BigDecimal.ZERO);
        order.setTotalAmount(new BigDecimal("5000.00"));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        orderService.confirmOrder(1L);

        assertEquals(ClientLoyaltyLevel.GOLD, client.getClientLoyaltyLevel());
    }

    @Test
    void confirmOrder_UpgradesToPlatinum() {
        order.setRemainingAmount(BigDecimal.ZERO);
        order.setTotalAmount(new BigDecimal("15000.00"));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        orderService.confirmOrder(1L);

        assertEquals(ClientLoyaltyLevel.PLATINUM, client.getClientLoyaltyLevel());
    }

    @Test
    void confirmOrder_SetsFirstOrderDate() {
        order.setRemainingAmount(BigDecimal.ZERO);
        client.setFirstOrderDate(null);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        orderService.confirmOrder(1L);

        assertNotNull(client.getFirstOrderDate());
    }

    @Test
    void confirmOrder_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.confirmOrder(1L));
    }

    @Test
    void confirmOrder_CannotBeConfirmed() {
        order.setRemainingAmount(new BigDecimal("100.00"));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(ValidationException.class, () -> orderService.confirmOrder(1L));
    }

    @Test
    void cancelOrder_Success() {
        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(2)
                .build();
        order.setOrderItems(List.of(orderItem));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        OrderResponseDto result = orderService.cancelOrder(1L);

        assertNotNull(result);
        assertEquals(OrderStatus.CANCELED, order.getStatus());
    }

    @Test
    void cancelOrder_RestoresStock() {
        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(2)
                .build();
        order.setOrderItems(List.of(orderItem));
        product.setAvailableStock(8);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(responseDto);

        orderService.cancelOrder(1L);

        assertEquals(10, product.getAvailableStock());
        verify(productRepository).save(product);
    }

    @Test
    void cancelOrder_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.cancelOrder(1L));
    }

    @Test
    void cancelOrder_NotPending() {
        order.setStatus(OrderStatus.CONFIRMED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(ValidationException.class, () -> orderService.cancelOrder(1L));
    }

    @Test
    void cancelOrder_WithCollectedPayments() {
        Payment payment = Payment.builder()
                .status(PaymentStatus.COLLECTED)
                .build();
        order.setPayments(List.of(payment));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(ValidationException.class, () -> orderService.cancelOrder(1L));
    }
}
