package com.mindhub.homebanking.repositories;


import com.mindhub.homebanking.models.ClientePrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientLoanRepository extends JpaRepository<ClientePrestamo, Long> {
}
