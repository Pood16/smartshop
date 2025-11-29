package com.ouirghane.smartshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientMinimalInformationsDto {

    private Long id;
    private String name;
    private String email;
}