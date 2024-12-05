package com.Project.CreditApp.core.dto;

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
    private String sortCode;
    private long customerId;
    private boolean active;
    private String customerName;
}
