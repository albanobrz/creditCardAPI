package com.Project.CreditApp.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {

    private long id;
    private String cpf;
    private String email;
    private String phone;
    private AddressDTO address;
    private long accountId;
}