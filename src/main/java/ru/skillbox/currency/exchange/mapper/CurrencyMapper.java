package ru.skillbox.currency.exchange.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.valute.Valute;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    CurrencyDto convertToDto(Currency currency);

    Currency convertToEntity(CurrencyDto currencyDto);

    @Mapping(source = "nameValute", target = "name")
    @Mapping(source = "nominal", target = "nominal")
    @Mapping(target = "value", expression = "java(Double.valueOf(valute.getValueValute().replace(\",\", \".\")))")
    @Mapping(source = "numCode", target = "isoNumCode")
    @Mapping(source = "charCode", target = "isoCharCode")
    Currency convertToCurrency(Valute valute);
}
