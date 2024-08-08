package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LoanRepository extends JpaRepository<Prestamo, Long> {


}
