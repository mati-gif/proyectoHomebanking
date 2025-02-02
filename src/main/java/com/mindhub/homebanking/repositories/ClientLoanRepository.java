package com.mindhub.homebanking.repositories;


import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {
//    boolean existsByClientAndLoan(Client client, Loan loan);
    boolean existsByClientAndLoanId(Client client, Long loanId);


}
