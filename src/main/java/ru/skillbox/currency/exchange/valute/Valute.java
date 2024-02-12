package ru.skillbox.currency.exchange.valute;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Valute")
public class Valute {

    @XmlElement(name = "NumCode")
    public Long numCode;

    @XmlElement(name = "CharCode")
    public String charCode;

    @XmlElement(name = "Nominal")
    public Long nominal;

    @XmlElement(name = "Name")
    public String nameValute;

    @XmlElement(name = "Value")
    public String valueValute;

    @XmlElement(name = "VunitRate")
    public String vunitRate;
}
