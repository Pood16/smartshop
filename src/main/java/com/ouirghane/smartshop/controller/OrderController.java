package com.ouirghane.smartshop.controller;


import com.ouirghane.smartshop.dto.request.OrderCreateRequestDto;
import com.ouirghane.smartshop.dto.response.OrderResponseDto;
import com.ouirghane.smartshop.service.OrderService;
import com.ouirghane.smartshop.service.SessionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {



    private final OrderService orderService;
    private final SessionService sessionService;


    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid
                                                        @RequestBody
                                                        OrderCreateRequestDto requestDto,
                                                        HttpSession session){
        sessionService.validateAdminRole(session);
        OrderResponseDto response = orderService.createOrder(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> getAllOrders(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "orderDate") String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String direction,
            @PageableDefault(size = 10, sort = "orderDate", direction = Sort.Direction.DESC, page = 0) Pageable pageable,
            HttpSession session
    ){
        sessionService.validateAdminRole(session);
        Page<OrderResponseDto> response = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long id, HttpSession session){
        sessionService.validateAdminRole(session);
        OrderResponseDto response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id, HttpSession session){
        sessionService.validateAdminRole(session);
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<Page<OrderResponseDto>> myOrders(
            HttpSession session,
            @PathVariable Long id,
            @PageableDefault(size = 1, sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable){
        sessionService.validateAdminRole(session);
        Page<OrderResponseDto> response = orderService.getOrdersByClient(id ,pageable);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<OrderResponseDto> confirmOrder(
            @PathVariable Long id,
            HttpSession session){
        sessionService.validateAdminRole(session);
        OrderResponseDto response = orderService.confirmOrder(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(
            @PathVariable Long id,
            HttpSession session){
        sessionService.validateAdminRole(session);
        OrderResponseDto response = orderService.cancelOrder(id);
        return ResponseEntity.ok(response);
    }
}
