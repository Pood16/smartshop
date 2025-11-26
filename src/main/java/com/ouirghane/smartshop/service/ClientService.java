package com.ouirghane.smartshop.service;

import com.ouirghane.smartshop.dto.request.ClientCreateRequestDto;
import com.ouirghane.smartshop.dto.request.ClientUpdateRequestDto;
import com.ouirghane.smartshop.dto.response.ClientResponseDto;
import com.ouirghane.smartshop.dto.response.ClientSummaryDto;
import com.ouirghane.smartshop.dto.response.ClientStatisticsDto;
import com.ouirghane.smartshop.dto.response.ClientOrderHistoryDto;
import com.ouirghane.smartshop.enums.ClientLoyaltyLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {

    ClientResponseDto createClient(ClientCreateRequestDto requestDTO);
    
    ClientResponseDto getClientById(Long id);
    
    ClientResponseDto getClientProfile(Long clientId);

    ClientResponseDto updateClient(Long id, ClientUpdateRequestDto requestDto);

    void deleteClient(Long id);

}
