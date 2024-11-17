package ru.gav.creditbank.calculator.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.gav.calculator.model.LoanOfferDto;
import ru.gav.calculator.model.LoanStatementRequestDto;

@Mapper(componentModel = "SPRING")
public interface LoanStatementLoanOfferMapper {

    @Mapping(source = "amount", target = "requestedAmount")
    @Mapping(source = "term", target = "term")
    LoanOfferDto loanStatementToLoanOffer(LoanStatementRequestDto loanStatementRequestDto);

    LoanOfferDto copyLoanOffer(LoanOfferDto loanOfferDto);
}
