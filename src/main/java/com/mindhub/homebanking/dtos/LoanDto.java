package com.mindhub.homebanking.dtos;

import java.util.List;

public record LoanDto(Long id, String name, double maxAmount, List<Integer> payments) {
}
