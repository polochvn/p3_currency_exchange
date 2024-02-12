package ru.skillbox.currency.exchange.valute;

import lombok.Data;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ValCurs")
public class ValuteCurs {

    @XmlAttribute(name = "Date")
    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date date;

    @XmlAttribute(name = "name")
    public String nameValuteCurs;

    @XmlElement(name = "Valute")
    Valute[] valutes;
}