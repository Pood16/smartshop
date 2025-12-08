package com.ouirghane.smartshop.controller;


import com.ouirghane.smartshop.dto.request.PaymentCreateRequestDto;
import com.ouirghane.smartshop.dto.response.PaymentResponseDto;
import com.ouirghane.smartshop.service.PaymentService;
import com.ouirghane.smartshop.service.SessionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
            HttpSession session){
        sessionService.validateAdminRole(session);
        PaymentResponseDto response = paymentService.createPayment(id, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/collect")
    public ResponseEntity<PaymentResponseDto> collectPayment(
            @PathVariable Long id,
            HttpSession session){
        sessionService.validateAdminRole(session);
        PaymentResponseDto response = paymentService.collectPayment(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<PaymentResponseDto> rejectPayment(
            @PathVariable Long id,
            HttpSession session){
        sessionService.validateAdminRole(session);
        PaymentResponseDto response = paymentService.rejectPayment(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Page<PaymentResponseDto>> getAllPaymentsByOrderId(
            @PathVariable Long orderId,
            @PageableDefault(sort = "paymentDate") Pageable pageable,
            HttpSession session){
        sessionService.validateAdminRole(session);
        Page<PaymentResponseDto> response = paymentService.getAllPaymentsByOrderId(orderId, pageable);
        return ResponseEntity.ok(response);
    }
}
