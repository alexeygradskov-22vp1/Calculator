package ru.gav.creditbank.calculator.utils;

import java.math.BigDecimal;

public interface CalcMonthlyPayment {
    BigDecimal calc(BigDecimal rate, Integer term, BigDecimal amount);
}
