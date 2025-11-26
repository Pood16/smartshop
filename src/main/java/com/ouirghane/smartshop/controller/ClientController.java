package com.ouirghane.smartshop.controller;


import com.ouirghane.smartshop.dto.request.ClientCreateRequestDto;
import com.ouirghane.smartshop.dto.response.ClientResponseDto;
import com.ouirghane.smartshop.service.ClientService;
import com.ouirghane.smartshop.service.SessionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {


    private final SessionService sessionService;
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponseDto> createClient(
            @Valid
            @RequestBody
            ClientCreateRequestDto request,
            HttpSession session) {
        sessionService.validateAdminRole(session);
        ClientResponseDto response = clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
