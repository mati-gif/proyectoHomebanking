package com.mindhub.homebanking.repositories;


import com.mindhub.homebanking.models.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaccion, Long> {
}
