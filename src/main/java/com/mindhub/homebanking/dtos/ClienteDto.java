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
    private Set<CuentaDto> cuentas;   // Declara una variable privada de tipo Set<CuentaDto> llamada cuentas.


    public ClienteDto(Cliente cliente){
        this.id = cliente.getId(); //Asigna el valor del id del objeto Cliente al id del ClienteDto.
        this.name = cliente.getName();
        this.lastName = cliente.getLastName();   // Asigna el valor del lastName del objeto Cliente al lastName del ClienteDto.
        this.email = cliente.getEmail();
//        this.cuentas = cliente.getCuentas().stream()
//                .map(CuentaDto::new)
//                .collect(Collectors.toSet());
        this.cuentas = convertCuentasToDto(cliente.getCuentas()); //Convierte el conjunto de Cuenta del objeto Cliente a un conjunto de CuentaDto y lo asigna a la variable cuentas del ClienteDto.

    }

    private Set<CuentaDto> convertCuentasToDto(Set<Cuenta> cuentas) {   //Declara un método privado que toma un conjunto de Cuenta y devuelve un conjunto de CuentaDto.
        return cuentas.stream()             //Convierte el conjunto de Cuenta en un Stream de CuentaDto.
                .map(CuentaDto::new)          //Mapea cada objeto Cuenta a un nuevo objeto CuentaDto.
                .collect(Collectors.toSet());     // Recoge los objetos CuentaDto en un conjunto y lo devuelve.
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
