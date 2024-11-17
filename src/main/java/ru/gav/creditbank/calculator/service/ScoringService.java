package ru.gav.creditbank.calculator.service;

import ru.gav.calculator.model.ScoringDataDto;

import java.math.BigDecimal;

public interface ScoringService {

    BigDecimal scoreRateOfCredit(ScoringDataDto scoringDataDto);
}
