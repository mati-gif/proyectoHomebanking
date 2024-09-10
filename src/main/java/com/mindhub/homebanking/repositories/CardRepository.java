package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {


    Boolean existsCardsByClientAndColorAndType(Client client, ColorType color, CardType type);
    Boolean existsCardNumberByNumber(String number);//Metodo que busca en la base de datos un CardNumber en especifico y devuelve true o false.
    Boolean existsByCvv(int cvv);//Metodo que busca en la base de datos un cvv en especifico y devuelve true o false.



}
