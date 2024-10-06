package com.mindhub.homebanking.dtos;

import java.time.LocalDateTime;

public class SimulateTransactionDto {

    private Long id;
    private String cardHolder;          // Nombre del titular de la tarjeta
    private String cardNumber;          // Número de tarjeta ingresado
    private LocalDateTime expirationDate;      // Fecha de vencimiento de la tarjeta
    private int cvv;                 // CVV ingresado
    private double amount;              // Monto a pagar
    private String description;         // Descripción del pago
    private String destinationAccountNumber;  // La cuenta de destino (pago) en el homebanking
    private String sourceAccountNumber; // Número de cuenta de origen

    public SimulateTransactionDto(String cardHolder, String cardNumber, LocalDateTime expirationDate, int cvv, double amount, String description, String destinationAccountNumber) {
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
        this.destinationAccountNumber = destinationAccountNumber;
    }


    public SimulateTransactionDto(String sourceAccountNumber, String description, double amount, int cvv, LocalDateTime expirationDate, String cardHolder, String cardNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
        this.description = description;
        this.amount = amount;
        this.cvv = cvv;
        this.expirationDate = expirationDate;
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
    }

    public SimulateTransactionDto(String cardNumber, String destinationAccountNumber, String description, double amount, int cvv) {
        this.cardNumber = cardNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.description = description;
        this.amount = amount;
        this.cvv = cvv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }
}
