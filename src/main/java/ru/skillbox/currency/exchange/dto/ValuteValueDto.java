package ru.skillbox.currency.exchange.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValuteValueDto {
    private String name;
    private Double value;
}
