package ru.gav.creditbank.calculator.builders.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gav.calculator.model.LoanOfferDto;
import ru.gav.creditbank.calculator.builders.LoanOfferBuilder;
import ru.gav.creditbank.calculator.config.CreditProperties;
import ru.gav.creditbank.calculator.utils.CalcMonthlyPayment;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class LoanOfferBuilderImpl implements LoanOfferBuilder {

    private final CreditProperties creditProperties;
    private final CalcMonthlyPayment calcMonthlyPaymentService;

    @Override
    public LoanOfferDto build(LoanOfferDto loanOfferDto) {
        return loanOfferDto.
                statementId(generateUUID()).
                rate(calculateRate(loanOfferDto)).
                totalAmount(calculateTotalAmount(loanOfferDto)).
                monthlyPayment(calculateMonthlyPayment(loanOfferDto));
    }


    private UUID generateUUID() {
        return UUID.randomUUID();
    }

    private BigDecimal calculateRate(LoanOfferDto loanOfferDto) {
        loanOfferDto.setRate(creditProperties.getRate());
        if (loanOfferDto.getIsInsuranceEnabled()) {
            loanOfferDto.setRate(loanOfferDto.getRate().subtract(creditProperties.getReductionInsuranceRates()));
        }
        if (loanOfferDto.getIsSalaryClient()) {
            loanOfferDto.setRate(loanOfferDto.getRate().subtract(creditProperties.getReductionSalaryRates()));
        }
        return loanOfferDto.getRate();
    }

    private BigDecimal calculateTotalAmount(LoanOfferDto loanOfferDto) {
        loanOfferDto.setTotalAmount(loanOfferDto.getRequestedAmount());
        if (loanOfferDto.getIsInsuranceEnabled()) {
            loanOfferDto.setTotalAmount(loanOfferDto.getRequestedAmount().add(calculateInsuranceCost(loanOfferDto.getRequestedAmount())));
        }
        return loanOfferDto.getTotalAmount();
    }


    private BigDecimal calculateInsuranceCost(BigDecimal requestedAmount){
        return requestedAmount.
                divide(creditProperties.getInsuranceRatio(), new MathContext(10, RoundingMode.CEILING)).
                multiply(creditProperties.getInsuranceCost());
    }

    private BigDecimal calculateMonthlyPayment(LoanOfferDto loanOfferDto) {
        loanOfferDto.monthlyPayment(
                calcMonthlyPaymentService.calc(
                        loanOfferDto.getRate(),
                        loanOfferDto.getTerm(),
                        loanOfferDto.getTotalAmount()));
        return loanOfferDto.getMonthlyPayment();
    }
}
