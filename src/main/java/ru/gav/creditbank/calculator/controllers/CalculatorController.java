package ru.gav.creditbank.calculator.controllers;

import org.springframework.http.ResponseEntity;
import ru.gav.calculator.CalculatorApi;
import ru.gav.calculator.model.CreditDto;
import ru.gav.calculator.model.LoanOfferDto;
import ru.gav.calculator.model.LoanStatementRequestDto;
import ru.gav.calculator.model.ScoringDataDto;

import java.util.List;

public class CalculatorController implements CalculatorApi {
    @Override
    public ResponseEntity<CreditDto> calcCredit(ScoringDataDto scoringDataDto) {
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<List<LoanOfferDto>> calculateOffers(LoanStatementRequestDto loanStatementRequestDto) {
        return ResponseEntity.ok(null);
    }
}
