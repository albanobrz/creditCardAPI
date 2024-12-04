package com.Project.CreditApp.core.controller;

import com.Project.CreditApp.config.exception.BusinessException;
import com.Project.CreditApp.core.dto.AccountDTO;
import com.Project.CreditApp.core.dto.CustomerDTO;
import com.Project.CreditApp.core.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{cpf}")
    public ResponseEntity<AccountDTO> getAccountByCpf(@PathVariable String cpf) {
        AccountDTO accountDTO = accountService.getAccountByCpf(cpf);
        return ResponseEntity.ok(accountDTO);
    }

    @PatchMapping("/{cpf}/deactivate")
    public ResponseEntity<String> deactivateAccount(@PathVariable String cpf) {
        accountService.deactivateAccount(cpf);
        return ResponseEntity.ok("Account successfully deactivated");
    }
}
