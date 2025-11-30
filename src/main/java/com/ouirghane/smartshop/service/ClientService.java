package com.ouirghane.smartshop.service;

import com.ouirghane.smartshop.dto.request.ClientCreateRequestDto;
import com.ouirghane.smartshop.dto.request.ClientUpdateRequestDto;
import com.ouirghane.smartshop.dto.response.ClientMinimalInformationsDto;
import com.ouirghane.smartshop.dto.response.ClientResponseDto;

public interface ClientService {

    ClientMinimalInformationsDto createClient(ClientCreateRequestDto requestDTO);
    
    ClientResponseDto getClientById(Long id);
    
    ClientResponseDto getClientProfile(Long clientId);

    ClientResponseDto updateClient(Long id, ClientUpdateRequestDto requestDto);

    void deleteClient(Long id);

}
