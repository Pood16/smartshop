package com.ouirghane.smartshop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientUpdateRequestDto {

    private String name;

    private String username;

    private String email;

    private String password;

}