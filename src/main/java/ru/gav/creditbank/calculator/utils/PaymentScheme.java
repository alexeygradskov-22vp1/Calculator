package ru.gav.creditbank.calculator.utils;

import ru.gav.calculator.model.CreditDto;
import ru.gav.calculator.model.ScoringDataDto;

public interface PaymentScheme {

    CreditDto calculatePaymentScheme(ScoringDataDto scoringDataDto);
}
