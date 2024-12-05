package com.Project.CreditApp.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountDTO {

    private String name;
    private String cpf;
    private String email;
    private String phone;
    private CreateAddressDTO address;
}