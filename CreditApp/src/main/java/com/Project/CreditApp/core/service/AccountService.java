package com.Project.CreditApp.core.service;

import com.Project.CreditApp.config.exception.BusinessException;
import com.Project.CreditApp.core.dto.AccountDTO;
import com.Project.CreditApp.core.dto.CustomerDTO;
import com.Project.CreditApp.core.model.Account;
import com.Project.CreditApp.core.model.Address;
import com.Project.CreditApp.core.model.Customer;
import com.Project.CreditApp.core.repository.AccountRepository;
import com.Project.CreditApp.core.repository.CustomerRepository;
import com.Project.CreditApp.core.utils.AccountUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountDTO createAccount(CustomerDTO customerDTO) {
        if (customerRepository.existsByCpf(customerDTO.getCpf())) {
            throw new BusinessException("CPF já cadastrado.", HttpStatus.CONFLICT);
        }
        if (customerRepository.existsByEmail(customerDTO.getEmail())) {
            throw new BusinessException("Email já cadastrado.", HttpStatus.CONFLICT);
        }

        Customer customer = mapToCustomerEntity(customerDTO);
        customer = customerRepository.save(customer);

        String accountNumber = AccountUtils.generateAccountNumber();
        var account = Account.builder()
                .accountNumber(accountNumber)
                .customer(customer)
                .active(true)
                .build();

        account = accountRepository.save(account);

        return mapToAccountDTO(account);
    }

    public AccountDTO getAccountByCpf(String cpf) {
        var customer = customerRepository.findByCpf(cpf)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado para o CPF: " + cpf, HttpStatus.NOT_FOUND));

        var account = accountRepository.findByCustomer(customer)
                .orElseThrow(() -> new BusinessException("Conta não encontrada para o cliente.", HttpStatus.NOT_FOUND));

        return mapToAccountDTO(account);
    }

    public void deactivateAccount(String cpf) {
        var customer = customerRepository.findByCpf(cpf)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado para o CPF: " + cpf, HttpStatus.NOT_FOUND));

        var account = accountRepository.findByCustomer(customer)
                .orElseThrow(() -> new BusinessException("Conta não encontrada para o cliente.", HttpStatus.NOT_FOUND));

        if(!account.getActive()) {
            throw new BusinessException("A conta já está desativada.", HttpStatus.BAD_REQUEST);
        }

        account.setActive(false);
        accountRepository.save(account);
    }


    // TODO: refatorar esses maps, ver se tem alguma lib que faz isso
    private Customer mapToCustomerEntity(CustomerDTO customerDTO) {
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

    private AccountDTO mapToAccountDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .customerId(account.getCustomer().getId())
                .active(account.getActive())
                .customerName(account.getCustomer().getName())
                .build();
    }
}
