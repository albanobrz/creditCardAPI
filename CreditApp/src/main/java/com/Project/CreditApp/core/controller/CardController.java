package com.Project.CreditApp.core.controller;

import com.Project.CreditApp.core.dto.CardDTO;
import com.Project.CreditApp.core.enums.ECardType;
import com.Project.CreditApp.core.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping("/{accountNumber}")
    public ResponseEntity<CardDTO> createCard(@PathVariable String accountNumber, @RequestBody CardDTO cardDTO) {
        CardDTO card = cardService.createCard(accountNumber, cardDTO);
        return new ResponseEntity<>(card, HttpStatus.CREATED);
    }

    @PatchMapping("/{cardNumber}/activate")
    public ResponseEntity<String> activateCard(@PathVariable String cardNumber) {
        cardService.activateCard(cardNumber);
        return ResponseEntity.ok("Cartão ativado com sucesso.");
    }

    @PatchMapping("/{cardNumber}/block")
    public ResponseEntity<String> blockCard(@PathVariable String cardNumber) {
        cardService.blockCard(cardNumber);
        return ResponseEntity.ok("Cartão bloqueado com sucesso.");
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<CardDTO>> getCardsFromAccount(@PathVariable String accountNumber) {
        List<CardDTO> cards = cardService.getCardsFromAccount(accountNumber);
        return ResponseEntity.ok(cards);
    }
}
