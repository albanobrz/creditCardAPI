package com.Project.CreditApp.core.utils;

import com.Project.CreditApp.core.dto.AccountDTO;
import com.Project.CreditApp.core.dto.CardDTO;
import com.Project.CreditApp.core.dto.CreateAccountDTO;
import com.Project.CreditApp.core.model.Account;
import com.Project.CreditApp.core.model.Address;
import com.Project.CreditApp.core.model.Card;
import com.Project.CreditApp.core.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class MapperUtils {

    public Customer mapToCustomerEntity(CreateAccountDTO customerDTO) {
        var address = Address.builder()
                .street(customerDTO.getAddress().getStreet())
                .city(customerDTO.getAddress().getCity())
                .state(customerDTO.getAddress().getState())
                .zipCode(customerDTO.getAddress().getZipCode())
                .build();

        Customer customer = Customer.builder()
                .name(customerDTO.getName())
                .cpf(customerDTO.getCpf())
                .email(customerDTO.getEmail())
                .phone(customerDTO.getPhone())
                .build();

        address.setCustomer(customer);
        customer.setAddress(address);

        return customer;
    }

    public AccountDTO mapToAccountDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .sortCode(account.getSortCode())
                .customerId(account.getCustomer().getId())
                .active(account.getActive())
                .customerName(account.getCustomer().getName())
                .build();
    }

    public CardDTO mapToCardDTO(Card card) {
        return CardDTO.builder()
                .id(card.getId())
                .cardType(card.getCardType())
                .active(card.getActive())
                .cardNumber(card.getCardNumber())
                .holderName(card.getHolderName())
                .expirationDate(card.getExpirationDate())
                .cvv(card.getCvv())
                .isDelivered(card.getIsDelivered())
                .accountId(card.getAccount().getId())
                .blocked(card.getBlocked())
                .build();
    }
}
