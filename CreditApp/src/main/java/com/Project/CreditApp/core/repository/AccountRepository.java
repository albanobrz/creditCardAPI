package com.Project.CreditApp.core.repository;

import com.Project.CreditApp.core.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByCustomerCpf(String cpf);

    Optional<Account> findByAccountNumber(String accountNumber);
}
