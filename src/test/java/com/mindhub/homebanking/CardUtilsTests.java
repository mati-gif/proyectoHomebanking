/*
package com.mindhub.homebanking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static com.mindhub.homebanking.Utils.CardsUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardUtilsTests {


//-----------------------  Test GenerateCardNumber ---------------------------//
    @Test
    public void cardNumberIsCreated(){

        String cardNumber = GenerateCardNumber();

        assertThat(cardNumber,is(not(emptyOrNullString())));

    }

    @Test
    public void cardNumberHasCorrectFormat() {
        String cardNumber = GenerateCardNumber();

        // Verifica que la longitud del número de tarjeta sea 19 caracteres (16 dígitos + 3 guiones)
        assertThat(cardNumber.length(), is(19));

        // Verifica que el formato siga el patrón de 4 grupos de 4 dígitos separados por guiones
        assertThat(cardNumber, matchesPattern("\\d{4}-\\d{4}-\\d{4}-\\d{4}"));
    }

    @Test
    public void cardNumberGroupsAreNumeric() {
        String cardNumber = GenerateCardNumber();

        // Divide el número de tarjeta en los 4 grupos de dígitos
        String[] groups = cardNumber.split("-");

        // Verifica que haya exactamente 4 grupos
        assertThat(groups.length, is(4));

        // Verifica que cada grupo esté compuesto por 4 caracteres numéricos
        for (String group : groups) {
            assertThat(group.length(), is(4));
            assertThat(group, matchesPattern("\\d{4}"));
        }
    }


    @Test
    public void cardNumberGroupsAreNonNegative() {
        String cardNumber = GenerateCardNumber();

        // Divide el número en los 4 grupos de dígitos
        String[] groups = cardNumber.split("-");

        // Verifica que cada grupo sea un número positivo (o cero)
        for (String group : groups) {
            int number = Integer.parseInt(group);
            assertThat(number, greaterThanOrEqualTo(0));
        }
    }

//-----------------------  Test GenerateCodigoSeguridad (CVV) ---------------------------//

    @Test
    public void codigoSeguridadEstaDentroDelRango() {
        int codigo = generarCodigoSeguridad();

        // Verifica que el código esté entre 100 y 999
        assertThat(codigo, allOf(greaterThanOrEqualTo(100), lessThanOrEqualTo(999)));
    }

//    @Test
//    public void codigoSeguridadNoEsNuloNiNegativo() {
//        int codigo = generarCodigoSeguridad();
//
//        // Verifica que el código esté dentro del rango permitido
//        assertThat(codigo, is(greaterThanOrEqualTo(100)));
//    }

//-----------------------  Test GenerateExpirationDate ---------------------------//

    @Test
    public void expirationDateIsFiveYearsInTheFuture() {
        // Obtiene la fecha actual
        LocalDate currentDate = LocalDate.now();

        // Llama al método que genera la fecha de vencimiento
        LocalDate expirationDate = generateExpirationDate();

        // Verifica que la fecha de vencimiento sea 5 años después de la fecha actual
        assertThat(expirationDate, equalTo(currentDate.plusYears(5)));
    }

    @Test
    public void expirationDateIsInTheFuture() {
        // Obtiene la fecha actual
        LocalDate currentDate = LocalDate.now();

        // Llama al método que genera la fecha de vencimiento
        LocalDate expirationDate = generateExpirationDate();

        // Verifica que la fecha de vencimiento esté en el futuro
        assertThat(expirationDate, greaterThan(currentDate));
    }

//-----------------------  Test GenerateFromDate ---------------------------//

    @Test
    public void fromDateIsCurrentDate() {
        // Obtiene la fecha actual
        LocalDate currentDate = LocalDate.now();

        // Llama al método que genera la fecha de inicio
        LocalDate fromDate = generateFromDate();

        // Verifica que la fecha de inicio sea igual a la fecha actual
        assertThat(fromDate, equalTo(currentDate));
    }

    @Test
    public void fromDateIsInThePresent() {
        // Obtiene la fecha actual
        LocalDate currentDate = LocalDate.now();

        // Llama al método que genera la fecha de inicio
        LocalDate fromDate = generateFromDate();

        // Verifica que la fecha de inicio sea igual o antes de la fecha actual
        assertThat(fromDate, lessThanOrEqualTo(currentDate));
        // También asegura que no sea una fecha en el futuro
        assertThat(fromDate, greaterThanOrEqualTo(LocalDate.of(1900, 1, 1))); // Ajusta según el rango válido
    }

}
*/
