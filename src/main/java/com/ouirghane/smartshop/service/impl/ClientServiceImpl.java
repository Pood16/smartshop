package com.ouirghane.smartshop.service.impl;


import com.ouirghane.smartshop.dto.request.ClientCreateRequestDto;
import com.ouirghane.smartshop.dto.response.ClientResponseDto;
import com.ouirghane.smartshop.entity.Client;
import com.ouirghane.smartshop.entity.User;
import com.ouirghane.smartshop.enums.UserRole;
import com.ouirghane.smartshop.exception.ValidationException;
import com.ouirghane.smartshop.mapper.ClientMapper;
import com.ouirghane.smartshop.repository.ClientRepository;
import com.ouirghane.smartshop.repository.UserRepository;
import com.ouirghane.smartshop.service.ClientService;
import com.ouirghane.smartshop.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.BadBinaryOpValueExpException;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;
    private final PasswordUtil passwordUtil;

    @Override
    public ClientResponseDto createClient(ClientCreateRequestDto requestDto){

        if (userRepository.existsByUsername(requestDto.getUsername())){
            throw new ValidationException("Username already exists");
        }

        if (clientRepository.existsByEmail(requestDto.getEmail())){
            throw new ValidationException("Username already exists");
        }

        User user = User
                .builder()
                .role(UserRole.CLIENT)
                .username(requestDto.getUsername())
                .password(passwordUtil.hashPassword(requestDto.getPassword()))
                .build();
        User savedUser = userRepository.save(user);

        Client client = clientMapper.toEntity(requestDto, savedUser);
        Client savedClient = clientRepository.save(client);

        return clientMapper.toResponseDto(savedClient);
    }
}
