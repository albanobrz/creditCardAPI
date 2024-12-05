package com.Project.CreditApp.core.dto;

import com.Project.CreditApp.core.enums.ECardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {

    private long id;
    private String cardNumber;
    private String holderName;
    private String expirationDate;
    private ECardType cardType;
    private long accountId;
    private boolean active;
    private boolean blocked;
}
