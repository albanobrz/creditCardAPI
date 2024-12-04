package com.Project.CreditApp.core.dto;

import com.Project.CreditApp.core.enums.ECardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private long id;
    private String cpf;
    private String email;
    private String phone;
    private AddressDTO address;
    private AccountDTO account;
}