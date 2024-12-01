package ru.gav.creditbank.calculator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Configuration
@Getter
@Setter
public class GlobalProps {
    private final BigDecimal quantityMonthsInYear=new BigDecimal(12);
    private final BigDecimal percents = new BigDecimal(100);
    private final MathContext precisionOfCalculations = new MathContext(10, RoundingMode.CEILING);
}
