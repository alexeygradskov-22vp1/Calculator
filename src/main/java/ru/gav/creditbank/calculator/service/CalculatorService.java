package ru.gav.creditbank.calculator.service;

import ru.gav.calculator.model.CreditDto;
import ru.gav.calculator.model.LoanOfferDto;
import ru.gav.calculator.model.LoanStatementRequestDto;
import ru.gav.calculator.model.ScoringDataDto;

import java.util.List;

public interface CalculatorService {

    List<LoanOfferDto> createOffers(LoanStatementRequestDto loanStatementRequestDto);

    CreditDto scoreData(ScoringDataDto scoringDataDto);

}
