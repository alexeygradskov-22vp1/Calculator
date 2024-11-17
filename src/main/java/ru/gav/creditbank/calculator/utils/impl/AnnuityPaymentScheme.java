package ru.gav.creditbank.calculator.utils.impl;

import org.springframework.stereotype.Service;
import ru.gav.calculator.model.CreditDto;
import ru.gav.calculator.model.ScoringDataDto;
import ru.gav.creditbank.calculator.utils.PaymentScheme;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service(value = "AnnuityPaymentScheme")
public class AnnuityPaymentScheme implements PaymentScheme {
    private final BigDecimal QUANTITY_OF_MONTHS_PER_YEAR=new BigDecimal(12);

    @Override
    public CreditDto calculatePaymentScheme(ScoringDataDto scoringDataDto, BigDecimal rate) {
        CreditDto result = new CreditDto();

        return null;
    }

    public BigDecimal calcMonthlyPayment(BigDecimal rate, Integer term){
        BigDecimal monthlyRate = rate.divide(QUANTITY_OF_MONTHS_PER_YEAR, RoundingMode.HALF_UP);
        BigDecimal numerator = new BigDecimal(1).add(monthlyRate).pow(term).multiply(monthlyRate);
        BigDecimal denominator = new BigDecimal(1).add(monthlyRate).pow(term).subtract(new BigDecimal(1));
        return numerator.divide(denominator,RoundingMode.HALF_UP);
    }
}
