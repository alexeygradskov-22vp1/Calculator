package ru.gav.creditbank.calculator.builders;

import ru.gav.calculator.model.LoanOfferDto;
import ru.gav.calculator.model.LoanStatementRequestDto;

import java.util.List;

public interface LoanOfferBuilder {
    LoanOfferDto build(LoanOfferDto loanOfferDto);
}
