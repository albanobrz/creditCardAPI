package com.Project.CreditApp.core.controller;

import com.Project.CreditApp.config.response.ApiResponse;
import com.Project.CreditApp.config.response.Messages;
import com.Project.CreditApp.core.dto.CardDTO;
import com.Project.CreditApp.core.dto.CreateCardDTO;
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
    public ResponseEntity<CardDTO> createCard(@PathVariable String accountNumber, @RequestBody CreateCardDTO cardDTO) {
        CardDTO card = cardService.createCard(accountNumber, cardDTO);
        return new ResponseEntity<>(card, HttpStatus.CREATED);
    }

    @PatchMapping("/{cardNumber}/activate")
    public ResponseEntity<ApiResponse> activateCard(@PathVariable String cardNumber) {
        cardService.activateCard(cardNumber);
        return ResponseEntity.ok(new ApiResponse(Messages.CARD_ACTIVATED_SUCCESSFULLY));
    }

    @PatchMapping("/{cardNumber}/block")
    public ResponseEntity<ApiResponse> blockCard(@PathVariable String cardNumber) {
        cardService.blockCard(cardNumber);
        return ResponseEntity.ok(new ApiResponse(Messages.CARD_BLOCKED_SUCCESSFULLY));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<CardDTO>> getCardsFromAccount(@PathVariable String accountNumber) {
        List<CardDTO> cards = cardService.getCardsFromAccount(accountNumber);
        return ResponseEntity.ok(cards);
    }
}
