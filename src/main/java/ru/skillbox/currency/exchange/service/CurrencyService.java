package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.skillbox.currency.exchange.config.Connection;
import ru.skillbox.currency.exchange.dto.Currencies;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.dto.ValuteValueDto;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.mapper.CurrencyMapper;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;
import ru.skillbox.currency.exchange.valute.Valute;
import ru.skillbox.currency.exchange.valute.ValuteCurs;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyMapper mapper;
    private final CurrencyRepository repository;
    private final Connection centralBankApi;

    public Currencies getAllCurrencies() {
        log.info("CurrencyService method getAllCurrencies executed");
        List<Currency> currencyList = repository.findAll();
        return new Currencies(currencyList
                .stream()
                .map(c -> new ValuteValueDto(c.getName(), c.getValue()))
                .collect(Collectors.toList()));
    }

    public CurrencyDto getById(Long id) {
        log.info("CurrencyService method getById executed");
        Currency currency = repository.findById(id).orElseThrow(() -> new RuntimeException("Currency not found with id: " + id));
        return mapper.convertToDto(currency);
    }

    public Double convertValue(Long value, Long numCode) {
        log.info("CurrencyService method convertValue executed");
        Currency currency = repository.findByIsoNumCode(numCode);
        return value * currency.getValue();
    }

    public CurrencyDto create(CurrencyDto dto) {
        log.info("CurrencyService method create executed");
        return  mapper.convertToDto(repository.save(mapper.convertToEntity(dto)));
    }

    @Scheduled(fixedRate = 3_600_000)
    public void updateСurrencies() throws Exception {
        log.info("CurrencyService method updateСurrencies executed");
        Arrays.stream(getValutes()).forEach(v -> {
            log.info(v.toString());
            Currency currency = repository.findByIsoCharCode(v.getCharCode());
            if (currency == null) {
                repository.save(mapper.convertToCurrency(v));
            } else {
                currency.setNominal(v.getNominal());
                currency.setValue(Double.valueOf(v.getValueValute().replace(",", ".")));
                repository.save(currency);
            }
        });
        closeConnection();
    }

    public HttpURLConnection connectionToCentralBankApi() throws Exception {
        URL url = new URL(centralBankApi.getUrl());
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.addRequestProperty(centralBankApi.getKeyAgent(), centralBankApi.getValueAgent());
        return http;
    }
    public void closeConnection () throws Exception {
        if(connectionToCentralBankApi() != null) {
            connectionToCentralBankApi().disconnect();
        }
    }

    public Valute[] getValutes() throws Exception {
        InputStream is = connectionToCentralBankApi().getInputStream();
        JAXBContext jc = JAXBContext.newInstance(ValuteCurs.class, Valute.class);
        Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        ValuteCurs valuteCurs = (ValuteCurs) jaxbUnmarshaller.unmarshal(is);
        is.close();
        return valuteCurs.getValutes();
    }
}