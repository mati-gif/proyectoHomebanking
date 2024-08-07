package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CountRepository extends JpaRepository<Cuenta, Long> {




}
