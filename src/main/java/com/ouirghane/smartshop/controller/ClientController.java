package com.ouirghane.smartshop.controller;


import com.ouirghane.smartshop.dto.request.ClientCreateRequestDto;
import com.ouirghane.smartshop.dto.request.ClientUpdateRequestDto;
import com.ouirghane.smartshop.dto.response.ClientMinimalInformationsDto;
import com.ouirghane.smartshop.dto.response.ClientResponseDto;
import com.ouirghane.smartshop.dto.response.OrderResponseDto;
import com.ouirghane.smartshop.service.ClientService;
import com.ouirghane.smartshop.service.OrderService;
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
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<ClientMinimalInformationsDto> createClient(
            @Valid
            @RequestBody
            ClientCreateRequestDto request) {
        ClientMinimalInformationsDto response = clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDto> updateClient(
            @PathVariable
            Long id,
            @Valid
            @RequestBody
            ClientUpdateRequestDto requestDto
    ){
        ClientResponseDto updatedClient = clientService.updateClient(id, requestDto);
        return ResponseEntity.ok(updatedClient);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDto> getClientById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<ClientResponseDto> getClientProfile(@PathVariable Long id){
        ClientResponseDto client = clientService.getClientProfile(id);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id){
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ClientResponseDto>> listAllClients(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable){
        Page<ClientResponseDto> response = clientService.getAllClients(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{clientId}/orders-history")
    public ResponseEntity<Page<OrderResponseDto>> getClientOrdersHistory(
            @PathVariable Long clientId,
            @PageableDefault(size = 1, sort = "orderDate", direction = Sort.Direction.DESC) Pageable pageable){
        Page<OrderResponseDto> response = orderService.getOrdersByClient(clientId, pageable);
        return ResponseEntity.ok(response);
    }


}
