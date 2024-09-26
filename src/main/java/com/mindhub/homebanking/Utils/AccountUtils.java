package com.mindhub.homebanking.Utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AccountUtils {

    public static String generateAccountNumber() {
        Random random = new Random();
        int min = 100; // Valor mínimo para 8 dígitos
        int max = 999; // Valor máximo para 8 dígitos
        int randomNumber = random.nextInt(max - min + 1) + min;

        // Rellenar con ceros a la izquierda si el número tiene menos de 8 dígitos
        String formattedNumber = String.format("%08d", randomNumber); // asegura 8 dígitos
        return "VIN-" + formattedNumber;
    }
}
