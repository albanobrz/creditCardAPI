package com.Project.CreditApp.core.dto;

import com.Project.CreditApp.core.model.Card;
import com.Project.CreditApp.core.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private long id;
    private String accountNumber;
    private CustomerDTO customer;
    private List<CardDTO> cards;
    private boolean active;
}