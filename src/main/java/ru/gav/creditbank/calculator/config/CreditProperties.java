package ru.gav.creditbank.calculator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "credit")
@Getter
@Setter
public class CreditProperties {

    private BigDecimal rate;
    private BigDecimal insuranceCost;
    private BigDecimal reductionInsuranceRates;
    private BigDecimal reductionSalaryRates;
    private BigDecimal insuranceRatio;

}
