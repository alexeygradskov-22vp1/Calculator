package ru.gav.creditbank.calculator.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.gav.calculator.CalculatorApi;
import ru.gav.calculator.model.CreditDto;
import ru.gav.calculator.model.LoanOfferDto;
import ru.gav.calculator.model.LoanStatementRequestDto;
import ru.gav.calculator.model.ScoringDataDto;
import ru.gav.creditbank.calculator.service.CalculatorService;

import java.util.List;
@RestController
@RequiredArgsConstructor
public class CalculatorController implements CalculatorApi {
    private final CalculatorService calculatorService;
    @Override
    public ResponseEntity<CreditDto> calcCredit(ScoringDataDto scoringDataDto) {
        return ResponseEntity.ok(calculatorService.scoreData(scoringDataDto));
    }

    @Override
    public ResponseEntity<List<LoanOfferDto>> calculateOffers(LoanStatementRequestDto loanStatementRequestDto) {
        return ResponseEntity.ok(calculatorService.createOffers(loanStatementRequestDto));
    }
}
