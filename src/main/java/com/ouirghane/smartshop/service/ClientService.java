package com.ouirghane.smartshop.service;

import com.ouirghane.smartshop.dto.request.ClientCreateRequestDto;
import com.ouirghane.smartshop.dto.response.ClientResponseDto;

public interface ClientService {

    ClientResponseDto createClient(ClientCreateRequestDto requestDTO);
}
