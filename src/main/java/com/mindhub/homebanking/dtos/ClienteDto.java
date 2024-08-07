package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Cliente;
import com.mindhub.homebanking.models.Cuenta;

import java.util.Set;
import java.util.stream.Collectors;

public class ClienteDto {

    private Long id;
    private String name;
    private String lastName;
    private String email;
    private Set<CuentaDto> cuentas;

    public ClienteDto(Cliente cliente){
        this.id = cliente.getId();
        this.name = cliente.getName();
        this.lastName = cliente.getLastName();
        this.email = cliente.getEmail();
//        this.cuentas = cliente.getCuentas().stream()
//                .map(CuentaDto::new)
//                .collect(Collectors.toSet());
        this.cuentas = convertCuentasToDto(cliente.getCuentas());

    }

    private Set<CuentaDto> convertCuentasToDto(Set<Cuenta> cuentas) {
        return cuentas.stream()
                .map(CuentaDto::new)
                .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<CuentaDto> getCuentas() {
        return cuentas;
    }
}
