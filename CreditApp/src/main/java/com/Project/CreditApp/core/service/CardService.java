package com.Project.CreditApp.core.service;

import com.Project.CreditApp.config.exception.BusinessException;
import com.Project.CreditApp.config.response.Messages;
import com.Project.CreditApp.core.dto.CreateCardDTO;
import com.Project.CreditApp.core.enums.ECardType;
import com.Project.CreditApp.core.dto.CardDTO;
import com.Project.CreditApp.core.model.Account;
import com.Project.CreditApp.core.model.Card;
import com.Project.CreditApp.core.repository.AccountRepository;
import com.Project.CreditApp.core.repository.CardRepository;
import com.Project.CreditApp.core.utils.CardUtils;
import com.Project.CreditApp.core.utils.MapperUtils;
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

    private final MapperUtils mapperUtils;

    public CardDTO createCard(String accountNumber, CreateCardDTO cardDTO) {
        ECardType cardType = cardDTO.getCardType();

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new BusinessException(Messages.ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (!account.getActive()) {
            throw new BusinessException(Messages.ACCOUNT_IS_DEACTIVATED, HttpStatus.BAD_REQUEST);
        }

        // caso virtual:
        // se não existir cartão físico, voltar erro
        // se nenhum cartão físico estiver ativo, voltar erro
        // se existir cartão físico sem ativar, mas pelo menos um estar ativo, criar
        // se existir um cartão físico ativado, criar cartão (sempre já ativo para virtuais)

        // caso físico:
        // sempre criar (criar desativado)

        List<Card> physicalCards = cardRepository.findByAccountAndCardType(account, ECardType.PHYSICAL);

        if (cardType == ECardType.VIRTUAL) {
            // verifica se tem cartão físico
            if (physicalCards.isEmpty()) {
                throw new BusinessException(Messages.NOT_POSSIBLE_CREATE_CARD_WITHOUT_PHYSIC_CARD, HttpStatus.BAD_REQUEST);
            }

            // verifica se o cartão físico existente está ativo
            boolean hasActivePhysicalCard = physicalCards.stream().anyMatch(Card::getActive);

            if (!hasActivePhysicalCard) {
                throw new BusinessException(Messages.NOT_POSSIBLE_CREATE_CARD_WITHOUT_ACTIVATED_PHYSIC_CARD, HttpStatus.BAD_REQUEST);
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

            return mapperUtils.mapToCardDTO(virtualCard);
        }

        if (cardType == ECardType.PHYSICAL) {
            Card physicalCardToCreate = new Card();
            physicalCardToCreate.setAccount(account);
            physicalCardToCreate.setCardType(ECardType.PHYSICAL);
            physicalCardToCreate.setHolderName(account.getCustomer().getName());
            physicalCardToCreate.setCardNumber(CardUtils.generateCardNumber());
            physicalCardToCreate.setCvv(CardUtils.generateCvv());
            physicalCardToCreate.setExpirationDate(String.valueOf(LocalDate.now().plusYears(6)));
            cardRepository.save(physicalCardToCreate);

            return mapperUtils.mapToCardDTO(physicalCardToCreate);
        }

        throw new BusinessException(Messages.INVALID_CARD_TYPE, HttpStatus.BAD_REQUEST);
    }

    public void activateCard(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new BusinessException(Messages.CARD_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (card.getActive()) {
            throw new BusinessException(Messages.CARD_ALREADY_ACTIVATED, HttpStatus.BAD_REQUEST);
        }

        if (!card.getIsDelivered()) {
            throw new BusinessException(Messages.CARD_NOT_YET_DELIVERED, HttpStatus.BAD_REQUEST);
        }

        card.setActive(true);

        cardRepository.save(card);
    }

    public void blockCard(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new BusinessException(Messages.CARD_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (card.getBlocked()) {
            throw new BusinessException(Messages.CARD_ALREADY_BLOCKED, HttpStatus.BAD_REQUEST);
        }

        card.setBlocked(true);

        cardRepository.save(card);
    }

    public List<CardDTO> getCardsFromAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new BusinessException(Messages.ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND));

        List<Card> cards = cardRepository.findByAccount(account);

        return cards.stream()
                .map(mapperUtils::mapToCardDTO)
                .collect(Collectors.toList());
    }

    public void updateCVV(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new BusinessException(Messages.CARD_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (card.getCardType() != ECardType.VIRTUAL) {
            throw new BusinessException(Messages.CVV_UPDATE_ONLY_FOR_VIRTUAL_CARDS, HttpStatus.BAD_REQUEST);
        }

        String newCVV = CardUtils.generateCvv();

        card.setCvv(newCVV);
        cardRepository.save(card);
    }

    public void registerDelivery(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new BusinessException(Messages.CARD_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (card.getCardType() == ECardType.VIRTUAL) {
            throw new BusinessException(Messages.DELIVERY_ONLY_FOR_PHYSICAL_CARDS, HttpStatus.BAD_REQUEST);
        }

        card.setIsDelivered(true);
        cardRepository.save(card);
    }
}
