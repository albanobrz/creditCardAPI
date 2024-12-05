package com.Project.CreditApp.core.repository;

import com.Project.CreditApp.core.enums.ECardType;
import com.Project.CreditApp.core.model.Account;
import com.Project.CreditApp.core.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByAccountAndCardType(Account account, ECardType cardType);

    Optional<Card> findByCardNumber(String cardNumber);

    List<Card> findByAccount(Account account);
}
