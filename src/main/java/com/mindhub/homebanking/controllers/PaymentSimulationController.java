package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CreateTransactionDto;
import com.mindhub.homebanking.dtos.SimulateTransactionDto;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class PaymentSimulationController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/simulate-payment")
    public ResponseEntity<?> simulatePayment(@RequestBody SimulateTransactionDto simulateTransactionDto,
                                             Authentication authentication) {
        try {
            // Buscar la tarjeta por número
            Card card = cardService.getCardByNumber(simulateTransactionDto.getCardNumber());

            // Validar que la tarjeta exista y que el CVV coincida
            if (card == null || !String.valueOf(card.getCvv()).equals(simulateTransactionDto.getCvv())) {
                return new ResponseEntity<>("Tarjeta o CVV inválido", HttpStatus.FORBIDDEN);
            }

            // Verificar si la tarjeta pertenece al cliente autenticado
            Client client = clientService.findClientByEmail(authentication.getName());
            if (!client.getCards().contains(card)) {
                return new ResponseEntity<>("Esta tarjeta no pertenece al cliente autenticado", HttpStatus.FORBIDDEN);
            }

            // Obtener una de las cuentas asociadas al cliente
            Account sourceAccount = client.getAccounts().stream().findFirst().orElse(null);

            if (sourceAccount == null) {
                return new ResponseEntity<>("El cliente no tiene cuentas asociadas", HttpStatus.FORBIDDEN);
            }

            // Verificar si hay suficientes fondos en la cuenta para realizar el pago
            if (sourceAccount.getBalance() < simulateTransactionDto.getAmount()) {
                return new ResponseEntity<>("Fondos insuficientes", HttpStatus.FORBIDDEN);
            }

            // Crear la transacción simulada (de cuenta a cuenta)
            CreateTransactionDto createTransactionDto = new CreateTransactionDto(
                    simulateTransactionDto.getAmount(),
                    simulateTransactionDto.getDescription(),
                    sourceAccount.getNumber(),
                    simulateTransactionDto.getDestinationAccountNumber()
            );

            // Realizar la transacción
            transactionService.createTransaction(createTransactionDto, authentication);

            return new ResponseEntity<>("Pago simulado con éxito", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
