package com.Project.CreditApp.core.dto;

import com.Project.CreditApp.core.enums.ECardType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCardDTO {

    private ECardType cardType;
}
