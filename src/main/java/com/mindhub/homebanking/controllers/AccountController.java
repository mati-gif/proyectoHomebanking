package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.CuentaDto;
import com.mindhub.homebanking.models.Cuenta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.mindhub.homebanking.repositories.CountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController

@RequestMapping("/api/accounts")

public class AccountController {



    @Autowired
    private CountRepository countRepository;



    @GetMapping("/hello")
    public String hello() {
        return "Hello accounts!";
    }


    @GetMapping("/all")
    public ResponseEntity<List<CuentaDto>> getAllAccounts() {

        List<Cuenta>cuentas = countRepository.findAll();

        List<CuentaDto> cuentasDto = cuentas.stream()
                .map(cuenta -> new CuentaDto(cuenta))
                .collect(Collectors.toList());

        return new ResponseEntity<>(cuentasDto, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        Cuenta cuenta = countRepository.findById(id).orElse(null);

        if(cuenta == null){
            return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }

        CuentaDto cuentaDto = new CuentaDto(cuenta);
        return new ResponseEntity<>(cuentaDto, HttpStatus.OK);
    }


    @PatchMapping("/nuevaCuenta")
    public ResponseEntity<?> updateAccount(@RequestParam double balance,@RequestParam Long id) {
        Cuenta cuenta = countRepository.findById(id).orElse(null);
        if(cuenta == null){
            return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }
        cuenta.setBalance(balance);
        countRepository.save(cuenta);

        CuentaDto cuentaDto = new CuentaDto(cuenta);
        return new ResponseEntity<>(cuentaDto, HttpStatus.OK);

    }


    @PostMapping("/new")
    public ResponseEntity<?> createCount(@RequestParam double balance, @RequestParam String number, @RequestParam LocalDate creationDate) {

        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(balance);
        cuenta.setNumber(number);
        cuenta.setCreationDate(creationDate);

        countRepository.save(cuenta);

        CuentaDto cuentaDto = new CuentaDto(cuenta);

        return new ResponseEntity<>(cuentaDto, HttpStatus.CREATED);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCount(@PathVariable Long id) {
        if (countRepository.existsById(id)) {
            countRepository.deleteById(id);
            return new ResponseEntity<>("Cuenta eliminada", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }
    }



    @PutMapping("/modificar/{id}")
    public ResponseEntity<?> updateCount(@PathVariable Long id, @RequestParam double balance, @RequestParam String number, @RequestParam LocalDate creationDate) {
        Cuenta cuentaAModificar = countRepository.findById(id).orElse(null);
        if(countRepository.existsById(id)){
            cuentaAModificar.setBalance(balance);
            cuentaAModificar.setNumber(number);
            cuentaAModificar.setCreationDate(creationDate);
            countRepository.save(cuentaAModificar);
            CuentaDto cuentaDto = new CuentaDto(cuentaAModificar);
            return new ResponseEntity<>(cuentaDto, HttpStatus.OK);
        }
            return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }




}
