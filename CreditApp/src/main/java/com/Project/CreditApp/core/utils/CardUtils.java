package com.Project.CreditApp.core.utils;

import java.security.SecureRandom;
import java.util.Random;

public class CardUtils {

    private static final Random RANDOM = new SecureRandom();

    public static String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            cardNumber.append(RANDOM.nextInt(10));
        }
        return cardNumber.toString();
    }

    public static String generateCvv() {
        int cvv = RANDOM.nextInt(900) + 100;
        return String.valueOf(cvv);
    }
}
