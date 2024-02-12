package ru.skillbox.currency.exchange.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "connection")
public class Connection {

    private String url;
    private String keyAgent;
    private String valueAgent;
}
