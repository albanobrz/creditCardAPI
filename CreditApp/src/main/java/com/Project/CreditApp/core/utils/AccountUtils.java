package com.Project.CreditApp.core.utils;

import java.security.SecureRandom;

public class AccountUtils {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateAccountNumber() {
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            accountNumber.append(RANDOM.nextInt(10));
        }
        return accountNumber.toString();
    }
}