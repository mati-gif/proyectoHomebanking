package com.mindhub.homebanking;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.mindhub.homebanking.Utils.AccountUtils.generateAccountNumber;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

@SpringBootTest
public class AccountUtilsTest {


    @Test
    public void accountNumberIsNotNullOrEmpty() {
        // Llama al método que genera el número de cuenta
        String accountNumber = generateAccountNumber();

        // Verifica que el número de cuenta no sea nulo ni vacío
        assertThat(accountNumber, is(not(emptyOrNullString())));
    }


    @Test
    public void accountNumberStartsWithVIN() {
        // Llama al método que genera el número de cuenta
        String accountNumber = generateAccountNumber();

        // Verifica que el número de cuenta comience con "VIN-"
        assertThat(accountNumber, startsWith("VIN-"));
    }
}
