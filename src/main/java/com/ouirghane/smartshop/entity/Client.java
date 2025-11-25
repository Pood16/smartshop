package com.ouirghane.smartshop.entity;


import com.ouirghane.smartshop.enums.ClientLoyaltyLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "clients")
public class Client extends BaseEntity{

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private ClientLoyaltyLevel clientLoyaltyLevel;

    /*
    * What is the relationship between user and client?
    * How the users are created
    * How to handle authentication if no relation between user and client
    **/

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;
}
