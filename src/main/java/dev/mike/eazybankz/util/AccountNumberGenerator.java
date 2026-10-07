package dev.mike.eazybankz.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AccountNumberGenerator {

    private final Random random = new Random();

    public String generateAccountNumber() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 16; i++)
            sb.append(random.nextInt(10));

        return sb.toString();
    }
}
