package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Tarjeta, Long> {

}
