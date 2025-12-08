package com.ouirghane.smartshop.controller;


import com.ouirghane.smartshop.dto.request.ClientCreateRequestDto;
import com.ouirghane.smartshop.dto.request.ClientUpdateRequestDto;
import com.ouirghane.smartshop.dto.response.ClientMinimalInformationsDto;
import com.ouirghane.smartshop.dto.response.ClientResponseDto;
import com.ouirghane.smartshop.dto.response.OrderResponseDto;
import com.ouirghane.smartshop.entity.Client;
import com.ouirghane.smartshop.entity.User;
import com.ouirghane.smartshop.service.ClientService;
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
import org.springframework.web.bind.annotation.*;;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {


    private final SessionService sessionService;
    private final ClientService clientService;
    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<ClientMinimalInformationsDto> createClient(
            @Valid
            @RequestBody
            ClientCreateRequestDto request,
            HttpSession session) {
        sessionService.validateAdminRole(session);
        ClientMinimalInformationsDto response = clientService.createClient(request);
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

    @GetMapping
    public ResponseEntity<Page<ClientResponseDto>> listAllClients(HttpSession session, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable){
        sessionService.validateAdminRole(session);
        Page<ClientResponseDto> response = clientService.getAllClients(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders-history")
    public ResponseEntity<Page<OrderResponseDto>> getClientOrdersHistory(
            HttpSession session,
            @PageableDefault(size = 1, sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable){
        Client client = clientService.getClientByUserId(sessionService.getAuthenticatedUserId(session));
        Page<OrderResponseDto> response = orderService.getOrdersByClient(client.getId() ,pageable);
        return ResponseEntity.ok(response);
    }


}
