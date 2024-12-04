package com.Project.CreditApp.core.controller;

import com.Project.CreditApp.core.dto.AccountDTO;
import com.Project.CreditApp.core.dto.CustomerDTO;
import com.Project.CreditApp.core.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody CustomerDTO customerDTO) {
        AccountDTO accountDTO = accountService.createAccount(customerDTO);
        return new ResponseEntity<>(accountDTO, HttpStatus.CREATED);
    }
}
