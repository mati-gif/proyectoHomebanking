package com.mindhub.homebanking.dtos;

public class SimulateTransactionDto {

    private Long id;
    private String cardHolder;          // Nombre del titular de la tarjeta
    private String cardNumber;          // Número de tarjeta ingresado
    private String expirationDate;      // Fecha de vencimiento de la tarjeta
    private String cvv;                 // CVV ingresado
    private double amount;              // Monto a pagar
    private String description;         // Descripción del pago
    private String destinationAccountNumber;  // La cuenta de destino (pago) en el homebanking

    public SimulateTransactionDto(String cardHolder, String cardNumber, String expirationDate, String cvv, double amount, String description, String destinationAccountNumber) {
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
        this.destinationAccountNumber = destinationAccountNumber;
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

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
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
}
