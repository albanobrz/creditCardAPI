package com.Project.CreditApp.core.service;

import com.Project.CreditApp.config.exception.BusinessException;
import com.Project.CreditApp.core.enums.ECardType;
import com.Project.CreditApp.core.dto.CardDTO;
import com.Project.CreditApp.core.model.Account;
import com.Project.CreditApp.core.model.Card;
import com.Project.CreditApp.core.repository.AccountRepository;
import com.Project.CreditApp.core.repository.CardRepository;
import com.Project.CreditApp.core.utils.CardUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    public CardDTO createCard(String accountNumber, CardDTO cardDTO) {
        ECardType cardType = cardDTO.getCardType();

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new BusinessException("Conta não encontrada.", HttpStatus.NOT_FOUND));

        if (!account.getActive()) {
            throw new BusinessException("A conta está desativada.", HttpStatus.BAD_REQUEST);
        }

        // caso virtual:
        // se não existir cartão físico, voltar erro
        // se nenhum cartão físico estiver ativo, voltar erro
        // se existir cartão físico sem ativar, mas pelo menos um estar ativo, criar
        // se existir um cartão físico ativado, criar cartão (sempre já ativo)

        // caso físico:
        // sempre criar (criar desativado)

        List<Card> physicalCards = cardRepository.findByAccountAndCardType(account, ECardType.PHYSICAL);

        if (cardType == ECardType.VIRTUAL) {
            // verifica se tem cartão físico
            if (physicalCards.isEmpty()) {
                throw new BusinessException("Não é possível criar um cartão virtual sem um cartão físico associado.", HttpStatus.BAD_REQUEST);
            }

            // verifica se o cartão físico existente está ativo
            boolean hasActivePhysicalCard = physicalCards.stream().anyMatch(Card::getActive);

            if (!hasActivePhysicalCard) {
                throw new BusinessException("Não é possível criar um cartão virtual sem um cartão físico ativo.", HttpStatus.BAD_REQUEST);
            }

            // caso exista um cartão físico ativo, cria-se o virtual
            Card virtualCard = new Card();
            virtualCard.setAccount(account);
            virtualCard.setCardType(ECardType.VIRTUAL);
            virtualCard.setActive(true); // no caso considero que o cartão virtual já começa ativo
            virtualCard.setHolderName(account.getCustomer().getName());
            virtualCard.setCardNumber(CardUtils.generateCardNumber());
            virtualCard.setCvv(CardUtils.generateCvv());
            virtualCard.setExpirationDate(String.valueOf(LocalDate.now().plusYears(6)));
            cardRepository.save(virtualCard);

            return mapToCardDTO(virtualCard);
        } else if (cardType == ECardType.PHYSICAL) {
            Card physicalCardToCreate = new Card();
            physicalCardToCreate.setAccount(account);
            physicalCardToCreate.setCardType(ECardType.PHYSICAL);
            physicalCardToCreate.setActive(false); // Físico começa inativo
            physicalCardToCreate.setHolderName(account.getCustomer().getName());
            physicalCardToCreate.setCardNumber(CardUtils.generateCardNumber());
            physicalCardToCreate.setCvv(CardUtils.generateCvv());
            physicalCardToCreate.setExpirationDate(String.valueOf(LocalDate.now().plusYears(6)));
            cardRepository.save(physicalCardToCreate);

            return mapToCardDTO(physicalCardToCreate);
        }

        throw new BusinessException("Tipo de cartão inválido.", HttpStatus.BAD_REQUEST);
    }

    public void activateCard(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new BusinessException("Cartão não encontrado.", HttpStatus.NOT_FOUND));

        if (card.getActive()) {
            throw new BusinessException("Cartão já ativado.", HttpStatus.BAD_REQUEST);
        }

        card.setActive(true);

        cardRepository.save(card);
    }

    public void blockCard(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new BusinessException("Cartão não encontrado.", HttpStatus.NOT_FOUND));

        if (card.getBlocked()) {
            throw new BusinessException("Cartão já bloqueado.", HttpStatus.BAD_REQUEST);
        }

        card.setBlocked(true);

        cardRepository.save(card);
    }

    public List<CardDTO> getCardsFromAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new BusinessException("Conta não encontrada.", HttpStatus.NOT_FOUND));

        List<Card> cards = cardRepository.findByAccount(account);

        return cards.stream()
                .map(this::mapToCardDTO)
                .collect(Collectors.toList());
    }

    private CardDTO mapToCardDTO(Card card) {
        return CardDTO.builder()
                .id(card.getId())
                .cardType(card.getCardType())
                .active(card.getActive())
                .cardNumber(card.getCardNumber())
                .holderName(card.getHolderName())
                .expirationDate(card.getExpirationDate())
                .accountId(card.getAccount().getId())
                .blocked(card.getBlocked())
                .build();
    }
}
