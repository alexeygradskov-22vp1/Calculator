package ru.gav.creditbank.calculator.builders.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gav.calculator.model.LoanOfferDto;
import ru.gav.creditbank.calculator.builders.LoanOfferBuilder;
import ru.gav.creditbank.calculator.config.CreditProperties;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class LoanOfferBuilderImpl implements LoanOfferBuilder {

    private final CreditProperties creditProperties;

    @Override
    public LoanOfferDto build(LoanOfferDto loanOfferDto) {
        LoanOfferDto result = loanOfferDto;
        result = generateUUID(result);
        result = calculateRate(result);
        result = calculateTotalAmount(result);
        result = calculateMonthlyPayment(result);
        return result;
    }


    private LoanOfferDto generateUUID(LoanOfferDto loanOfferDto) {
        return loanOfferDto.statementId(UUID.randomUUID());
    }

    private LoanOfferDto calculateRate(LoanOfferDto loanOfferDto) {
        loanOfferDto.setRate(creditProperties.getRate());
        if (loanOfferDto.getIsInsuranceEnabled()) {
            loanOfferDto.setRate(loanOfferDto.getRate().subtract(creditProperties.getReductionInsuranceRates()));
        }
        if (loanOfferDto.getIsSalaryClient()) {
            loanOfferDto.setRate(loanOfferDto.getRate().subtract(creditProperties.getReductionSalaryRates()));
        }
        return loanOfferDto;
    }

    private LoanOfferDto calculateTotalAmount(LoanOfferDto loanOfferDto) {
        loanOfferDto.setTotalAmount(loanOfferDto.getRequestedAmount());
        if (loanOfferDto.getIsInsuranceEnabled()) {
            loanOfferDto.setTotalAmount(loanOfferDto.getRequestedAmount().add(calculateInsuranceCost(loanOfferDto.getRequestedAmount())));
        }
        return loanOfferDto;
    }


    private BigDecimal calculateInsuranceCost(BigDecimal requestedAmount){
        return requestedAmount.divide(creditProperties.getInsuranceRatio()).multiply(creditProperties.getInsuranceCost());
    }

    private LoanOfferDto calculateMonthlyPayment(LoanOfferDto loanOfferDto) {
        BigDecimal monthlyPayment = loanOfferDto.getTotalAmount().divide(new BigDecimal(loanOfferDto.getTerm()), RoundingMode.HALF_UP);
        BigDecimal interestPayment = monthlyPayment.multiply(loanOfferDto.getRate()).divide(new BigDecimal(100));
        loanOfferDto.monthlyPayment(monthlyPayment.add(interestPayment));
        return loanOfferDto;
    }
}
