package com.Project.CreditApp.core.webhook;

import com.Project.CreditApp.config.response.ApiResponse;
import com.Project.CreditApp.config.response.Messages;
import com.Project.CreditApp.core.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final CardService cardService;

    @PostMapping("/update-cvv")
    public ResponseEntity<ApiResponse> updateCVV(@RequestBody UpdateCVVRequest request) {
        cardService.updateCVV(request.getCardNumber());
        return ResponseEntity.ok(new ApiResponse(Messages.RECEIVED));
    }

    @PostMapping("/confirm-delivery")
    public ResponseEntity<ApiResponse> registerDelivery(@RequestBody CardDeliveryRequest request) {
        cardService.registerDelivery(request.getCardNumber());
        return ResponseEntity.ok(new ApiResponse(Messages.RECEIVED));
    }
}
