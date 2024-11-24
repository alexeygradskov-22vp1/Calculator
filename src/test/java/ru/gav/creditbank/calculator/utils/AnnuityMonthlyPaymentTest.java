package ru.gav.creditbank.calculator.utils;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnnuityMonthlyPaymentTest {
    @Autowired
    private final CalcMonthlyPayment calcMonthlyPayment;

    private BigDecimal rate;
    private BigDecimal amount;
    private int term;

    private BigDecimal result;

    @BeforeEach
    public void init(){
        rate = new BigDecimal(15);
        amount = new BigDecimal(100000);
        term = 24;
        result = new BigDecimal(0);
    }

    @Test
    @DisplayName("Тестирование расчета месячного платежа с разными сроками погашения")
    public void testCalcMonthlyPaymentDifferentTerms() {
        result = calcMonthlyPayment.calc(rate, term, amount);
        assertEquals(new BigDecimal("4848.66"), result.setScale(2, RoundingMode.HALF_EVEN));
        term = 18;
        result = calcMonthlyPayment.calc(rate, term, amount);
        assertEquals(new BigDecimal("6238.48"), result.setScale(2, RoundingMode.HALF_EVEN));
        term = 12;
        result = calcMonthlyPayment.calc(rate, term, amount);
        assertEquals(new BigDecimal("9025.83"), result.setScale(2, RoundingMode.HALF_EVEN));
        term = 36;
        result = calcMonthlyPayment.calc(rate, term, amount);
        assertEquals(new BigDecimal("3466.53"), result.setScale(2, RoundingMode.HALF_EVEN));
        term = 6;
        result = calcMonthlyPayment.calc(rate, term, amount);
        assertEquals(new BigDecimal("17403.38"), result.setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    @DisplayName("Тестирование расчета месячного платежа с разными суммами долга")
    public void testCalcMonthlyPaymentDifferentAmounts() {
        result = calcMonthlyPayment.calc(rate, term, amount);
        assertEquals(new BigDecimal("4848.66"), result.setScale(2, RoundingMode.HALF_EVEN));
        amount = new BigDecimal("20000");
        result = calcMonthlyPayment.calc(rate, term, amount);
        assertEquals(new BigDecimal("969.73"), result.setScale(2, RoundingMode.HALF_EVEN));
        amount = new BigDecimal("200000");
        result = calcMonthlyPayment.calc(rate, term, amount);
        assertEquals(new BigDecimal("9697.33"), result.setScale(2, RoundingMode.HALF_EVEN));
        amount = new BigDecimal("300000");
        result = calcMonthlyPayment.calc(rate, term, amount);
        assertEquals(new BigDecimal("14545.99"), result.setScale(2, RoundingMode.HALF_EVEN));
        amount = new BigDecimal("65000");
        result = calcMonthlyPayment.calc(rate, term, amount);
        assertEquals(new BigDecimal("3151.63"), result.setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    @DisplayName("Тестирование расчета месячного платежа с разными процентными ставками")
    public void testCalcMonthlyPaymentDifferentRates() {
        result = calcMonthlyPayment.calc(rate, term, amount);
        assertEquals(new BigDecimal("4848.66"), result.setScale(2, RoundingMode.HALF_EVEN));
        rate = new BigDecimal(13);
        result = calcMonthlyPayment.calc(rate, term, amount);
        assertEquals(new BigDecimal("4754.18"), result.setScale(2, RoundingMode.HALF_EVEN));
        rate = new BigDecimal(12);
        result = calcMonthlyPayment.calc(rate, term, amount);
        assertEquals(new BigDecimal("4707.35"), result.setScale(2, RoundingMode.HALF_EVEN));
        rate = new BigDecimal(11);
        result = calcMonthlyPayment.calc(rate, term, amount);
        assertEquals(new BigDecimal("4660.78"), result.setScale(2, RoundingMode.HALF_EVEN));
        rate = new BigDecimal(18);
        result = calcMonthlyPayment.calc(rate, term, amount);
        assertEquals(new BigDecimal("4992.41"), result.setScale(2, RoundingMode.HALF_EVEN));
    }
}
