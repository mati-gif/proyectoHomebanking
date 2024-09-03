package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.ColorType;

public record CreateCardDto(CardType type, ColorType color) {
}
