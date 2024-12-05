package com.Project.CreditApp.core.service;

import com.Project.CreditApp.config.exception.BusinessException;
import com.Project.CreditApp.config.response.Messages;
import com.Project.CreditApp.core.dto.CardDTO;
import com.Project.CreditApp.core.enums.ECardType;
import com.Project.CreditApp.core.model.Account;
import com.Project.CreditApp.core.model.Card;
import com.Project.CreditApp.core.repository.AccountRepository;
import com.Project.CreditApp.core.repository.CardRepository;
import com.Project.CreditApp.core.utils.MapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CardServiceTest {

    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MapperUtils mapperUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void activateCard_success() {
        // given
        Card card = new Card();
        card.setCardNumber("12345");
        card.setActive(false);

        when(cardRepository.findByCardNumber("12345")).thenReturn(Optional.of(card));

        // when
        cardService.activateCard("12345");

        // then
        assertTrue(card.getActive());
        verify(cardRepository).save(card);
    }

    @Test
    void activateCard_alreadyActivated_throwsException() {
        // given
        Card card = new Card();
        card.setCardNumber("12345");
        card.setActive(true);

        when(cardRepository.findByCardNumber("12345")).thenReturn(Optional.of(card));

        // when and then
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> cardService.activateCard("12345")
        );
        assertEquals(Messages.CARD_ALREADY_ACTIVATED, exception.getMessage());
    }

    @Test
    void blockCard_success() {
        // given
        Card card = new Card();
        card.setCardNumber("12345");
        card.setBlocked(false);

        when(cardRepository.findByCardNumber("12345")).thenReturn(Optional.of(card));

        // when
        cardService.blockCard("12345");

        // then
        assertTrue(card.getBlocked());
        verify(cardRepository).save(card);
    }

    @Test
    void blockCard_alreadyBlocked_throwsException() {
        // given
        Card card = new Card();
        card.setCardNumber("12345");
        card.setBlocked(true);

        when(cardRepository.findByCardNumber("12345")).thenReturn(Optional.of(card));

        // when and then
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> cardService.blockCard("12345")
        );
        assertEquals(Messages.CARD_ALREADY_BLOCKED, exception.getMessage());
    }

    @Test
    void getCardsFromAccount_success() {
        // given
        Account account = new Account();
        account.setAccountNumber("11111");

        Card card1 = new Card();
        card1.setCardNumber("12345");
        card1.setCardType(ECardType.PHYSICAL);

        Card card2 = new Card();
        card2.setCardNumber("67890");
        card2.setCardType(ECardType.VIRTUAL);

        CardDTO cardDTO1 = CardDTO.builder()
                .cardNumber("12345")
                .cardType(ECardType.PHYSICAL)
                .build();

        CardDTO cardDTO2 = CardDTO.builder()
                .cardNumber("67890")
                .cardType(ECardType.VIRTUAL)
                .build();

        when(accountRepository.findByAccountNumber("11111")).thenReturn(Optional.of(account));
        when(cardRepository.findByAccount(account)).thenReturn(List.of(card1, card2));
        when(mapperUtils.mapToCardDTO(card1)).thenReturn(cardDTO1);
        when(mapperUtils.mapToCardDTO(card2)).thenReturn(cardDTO2);

        // when
        List<CardDTO> cards = cardService.getCardsFromAccount("11111");

        // then
        assertEquals(2, cards.size());
        assertEquals("12345", cards.get(0).getCardNumber());
        assertEquals("67890", cards.get(1).getCardNumber());
    }

    @Test
    void getCardsFromAccount_accountNotFound_throwsException() {
        // given
        when(accountRepository.findByAccountNumber("11111")).thenReturn(Optional.empty());

        // when and then
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> cardService.getCardsFromAccount("11111")
        );
        assertEquals(Messages.ACCOUNT_NOT_FOUND, exception.getMessage());
    }
}