package com.ouirghane.smartshop.controller;


import com.ouirghane.smartshop.dto.request.PaymentCreateRequestDto;
import com.ouirghane.smartshop.dto.response.PaymentResponseDto;
import com.ouirghane.smartshop.service.PaymentService;
import com.ouirghane.smartshop.service.SessionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final SessionService sessionService;
    private final PaymentService paymentService;


    @PostMapping("/order/{id}")
    public ResponseEntity<PaymentResponseDto> createPayment(
            @Valid
            @RequestBody
            PaymentCreateRequestDto requestDto,
            @PathVariable Long id,
            HttpSession session
            ){
        PaymentResponseDto response = paymentService.createPayment(id, requestDto);
        return ResponseEntity.ok(response);
    }


}
