package com.ouirghane.smartshop.service.impl;


import com.ouirghane.smartshop.dto.request.ClientCreateRequestDto;
import com.ouirghane.smartshop.dto.request.ClientUpdateRequestDto;
import com.ouirghane.smartshop.dto.response.ClientMinimalInformationsDto;
import com.ouirghane.smartshop.dto.response.ClientResponseDto;
import com.ouirghane.smartshop.entity.Client;
import com.ouirghane.smartshop.entity.User;
import com.ouirghane.smartshop.enums.OrderStatus;
import com.ouirghane.smartshop.enums.UserRole;
import com.ouirghane.smartshop.exception.BusinessException;
import com.ouirghane.smartshop.exception.ResourceNotFoundException;
import com.ouirghane.smartshop.exception.ValidationException;
import com.ouirghane.smartshop.mapper.ClientMapper;
import com.ouirghane.smartshop.repository.ClientRepository;
import com.ouirghane.smartshop.repository.OrderRepository;
import com.ouirghane.smartshop.repository.UserRepository;
import com.ouirghane.smartshop.service.ClientService;
import com.ouirghane.smartshop.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ClientMapper clientMapper;
    private final PasswordUtil passwordUtil;

    @Override
    public ClientMinimalInformationsDto createClient(ClientCreateRequestDto requestDto){
        if (userRepository.existsByUsername(requestDto.getUsername())){
            throw new ValidationException("Username already exists");
        }

        if (clientRepository.existsByEmail(requestDto.getEmail())){
            throw new ValidationException("Email already exists");
        }

        User user = User
                .builder()
                .role(UserRole.CLIENT)
                .username(requestDto.getUsername())
                .password(passwordUtil.hashPassword(requestDto.getPassword()))
                .build();


        Client client = clientMapper.toEntity(requestDto, user);
        Client savedClient = clientRepository.save(client);

        return clientMapper.toClientMinimalistInformations(savedClient);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDto getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        return clientMapper.toResponseDto(client);
    }

    @Override
    public Client getClientByUserId(Long id) {
        return clientRepository.findByUserId(id).orElseThrow(()->new ResourceNotFoundException("Client not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDto getClientProfile(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        return clientMapper.toResponseDto(client);
    }

    @Override
    @Transactional
    public ClientResponseDto updateClient(Long id, ClientUpdateRequestDto requestDto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        if (clientRepository.existsByEmail(requestDto.getEmail())) {
            throw new ValidationException("Email already exists");
        }

        clientMapper.updateClientFromDto(requestDto, client);
        if (requestDto.getUsername() != null && !requestDto.getUsername().isEmpty()){
            client.getUser().setUsername(requestDto.getUsername());
        }
        if (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()){
            client.getUser().setPassword(passwordUtil.hashPassword(requestDto.getPassword()));
        }

        Client updatedClient = clientRepository.save(client);

        return clientMapper.toResponseDto(updatedClient);
    }

    @Override
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client not found");
        }
        if (orderRepository.existsByClientIdAndStatus(id, OrderStatus.PENDING)){
            throw new BusinessException("You can not delete client with active orders");
        }
        clientRepository.deleteById(id);
    }

    @Override
    public Page<ClientResponseDto> getAllClients(Pageable pageable){
        return clientRepository.findAll(pageable).map(clientMapper::toResponseDto);
    }
}
