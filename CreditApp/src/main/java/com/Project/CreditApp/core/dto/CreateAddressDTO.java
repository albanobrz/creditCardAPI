package com.Project.CreditApp.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAddressDTO {

    private String street;
    private String city;
    private String state;
    private String zipCode;
}
