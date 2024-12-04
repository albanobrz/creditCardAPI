package com.Project.CreditApp.core.service;

import com.Project.CreditApp.core.dto.AccountDTO;
import com.Project.CreditApp.core.dto.CustomerDTO;
import com.Project.CreditApp.core.model.Account;
import com.Project.CreditApp.core.model.Customer;
import com.Project.CreditApp.core.repository.AccountRepository;
import com.Project.CreditApp.core.repository.CustomerRepository;
import com.Project.CreditApp.core.utils.AccountUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountDTO createAccount(CustomerDTO customerDTO) {
        Customer customer = mapToCustomerEntity(customerDTO);

        String accountNumber = AccountUtils.generateAccountNumber();

        var account = Account.builder()
                .accountNumber(accountNumber)
                .customer(customer)
                .active(true)
                .build();

        customer.setAccount(account);
        customerRepository.save(customer);

        return mapToAccountDTO(account);
    }


    // TODO: refatorar esses maps, ver se tem alguma lib que faz isso
    private Customer mapToCustomerEntity(CustomerDTO customerDTO) {
        return Customer.builder()
                .cpf(customerDTO.getCpf())
                .email(customerDTO.getEmail())
                .phone(customerDTO.getPhone())
                .address(null)
                .build();
    }

    private AccountDTO mapToAccountDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .customerId(account.getCustomer().getId())
                .active(account.getActive())
                .build();
    }
}
