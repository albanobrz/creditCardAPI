package com.Project.CreditApp.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private long id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private long customerId;
}
