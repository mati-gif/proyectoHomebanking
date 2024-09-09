package com.mindhub.homebanking.Utils;


import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

@Component
public class CardsUtils {

    public static String GenerateCardNumber() {
        Random random = new Random();
        StringBuilder formattedNumber = new StringBuilder(); //Crea una instancia de StringBuilder para construir la cadena de números de la tarjeta de manera eficiente.

        for (int i = 0; i < 4; i++) { //Inicia un bucle for que se ejecuta 4 veces, con el índice i que va de 0 a 3.
            int fourDigitNumber = 0 + random.nextInt(9000); //Genera un número aleatorio de 4 dígitos. random.nextInt(9000) genera un número entre 0 y 8999, y al sumarle 0, el rango sigue siendo el mismo.

            formattedNumber.append(String.format("%04d", fourDigitNumber)); //Formatea el número de 4 dígitos para asegurarse de que siempre tenga 4 dígitos (rellenando con ceros a la izquierda si es necesario) y lo añade a formattedNumber.
            if (i < 3) {
                formattedNumber.append("-");//Agrega un guion entre los dígitos de la tarjeta.
            }
        }

        return formattedNumber.toString();//Convierte el StringBuilder a un String y lo devuelve.
    }


    public static int generarCodigoSeguridad() {
        Random random = new Random();
        return 100 + random.nextInt(900); // Genera un número entre 0 y 900
    }

    public static LocalDate generateExpirationDate() {
        LocalDate thruDate = LocalDate.now().plusYears(5);
        return thruDate;

    }

    public static LocalDate generateFromDate() {
        LocalDate fromDate = LocalDate.now();
        return fromDate;
    }
}
