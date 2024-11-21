package ru.gav.creditbank.calculator.utils.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gav.creditbank.calculator.config.GlobalProps;
import ru.gav.creditbank.calculator.utils.CalcMonthlyPayment;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class AnnuityCalcMonthlyPayment implements CalcMonthlyPayment {
    private final GlobalProps globalProps;



    @Override
    public BigDecimal calc(BigDecimal rate, Integer term, BigDecimal amount) {
        BigDecimal monthlyRate =rate.
                        divide(globalProps.getQuantityMonthsInYear(), globalProps.getPrecisionOfCalculations()).
                        divide(globalProps.getPercents(), globalProps.getPrecisionOfCalculations());
        BigDecimal pow = BigDecimal.ONE.
                add(monthlyRate).
                pow(term);
        BigDecimal denominator = pow.
                subtract(BigDecimal.ONE);
        BigDecimal annuityRate = monthlyRate.
                divide(denominator, globalProps.getPrecisionOfCalculations()).
                add(monthlyRate);
        return amount.multiply(annuityRate);
    }
}
