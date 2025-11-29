package com.ouirghane.smartshop.controller;


import com.ouirghane.smartshop.dto.request.ClientCreateRequestDto;
import com.ouirghane.smartshop.dto.request.ClientUpdateRequestDto;
import com.ouirghane.smartshop.dto.response.ClientResponseDto;
import com.ouirghane.smartshop.entity.Client;
import com.ouirghane.smartshop.entity.User;
import com.ouirghane.smartshop.service.ClientService;
import com.ouirghane.smartshop.service.SessionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

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

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDto> updateClient(
            @PathVariable
            Long id,
            @Valid
            @RequestBody
            ClientUpdateRequestDto requestDto,
            HttpSession session
    ){
        sessionService.validateAdminRole(session);
        ClientResponseDto updatedClient = clientService.updateClient(id, requestDto);
        return ResponseEntity.ok(updatedClient);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDto> getClientById(
            @PathVariable Long id,
            HttpSession session
    ){
        sessionService.validateAdminRole(session);
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @GetMapping("/profile")
    public ResponseEntity<ClientResponseDto> getClientProfile(HttpSession session){
        User authenticatedUser = sessionService.getAuthenticatedUser(session);
        ClientResponseDto client = clientService.getClientProfile(authenticatedUser.getClient().getId());
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id, HttpSession session){
        sessionService.validateAdminRole(session);
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }


}
