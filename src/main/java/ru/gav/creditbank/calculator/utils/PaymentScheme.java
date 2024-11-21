package ru.gav.creditbank.calculator.utils;

import ru.gav.calculator.model.CreditDto;
import ru.gav.calculator.model.ScoringDataDto;

import java.math.BigDecimal;

public interface PaymentScheme {

    CreditDto calculatePaymentScheme(ScoringDataDto scoringDataDto);
}
