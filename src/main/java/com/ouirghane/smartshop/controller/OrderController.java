package com.ouirghane.smartshop.controller;


import com.ouirghane.smartshop.dto.request.OrderCreateRequestDto;
import com.ouirghane.smartshop.dto.response.OrderResponseDto;
import com.ouirghane.smartshop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {



    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid
                                                        @RequestBody
                                                        OrderCreateRequestDto requestDto){
        OrderResponseDto response = orderService.createOrder(requestDto);
        return ResponseEntity.ok(response);
    }
}
