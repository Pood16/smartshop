package com.ouirghane.smartshop.service;

import com.ouirghane.smartshop.dto.request.ClientCreateRequestDto;
import com.ouirghane.smartshop.dto.request.ClientUpdateRequestDto;
import com.ouirghane.smartshop.dto.response.ClientMinimalInformationsDto;
import com.ouirghane.smartshop.dto.response.ClientResponseDto;
import com.ouirghane.smartshop.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {

    ClientMinimalInformationsDto createClient(ClientCreateRequestDto requestDTO);
    
    ClientResponseDto getClientById(Long id);

    Client getClientByUserId(Long id);
    
    ClientResponseDto getClientProfile(Long clientId);

    ClientResponseDto updateClient(Long id, ClientUpdateRequestDto requestDto);

    void deleteClient(Long id);

    Page<ClientResponseDto> getAllClients(Pageable pageable);

}
