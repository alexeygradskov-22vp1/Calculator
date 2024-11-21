package ru.gav.creditbank.calculator.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.gav.calculator.model.CreditDto;
import ru.gav.calculator.model.LoanOfferDto;
import ru.gav.calculator.model.LoanStatementRequestDto;
import ru.gav.calculator.model.ScoringDataDto;
import ru.gav.creditbank.calculator.builders.LoanOfferBuilder;
import ru.gav.creditbank.calculator.mappers.LoanStatementLoanOfferMapper;
import ru.gav.creditbank.calculator.service.CalculatorService;
import ru.gav.creditbank.calculator.utils.PaymentScheme;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculatorServiceImpl implements CalculatorService {

    private final LoanStatementLoanOfferMapper mapper;
    private final LoanOfferBuilder loanOfferBuilder;

    @Qualifier(value = "AnnuityPaymentScheme")
    private final PaymentScheme annuityPaymentScheme;


    @Override
    public List<LoanOfferDto> createOffers(LoanStatementRequestDto loanStatementRequestDto) {
        LoanOfferDto loanOfferDto = mapper.loanStatementToLoanOffer(loanStatementRequestDto);
        return combine(loanOfferDto);
    }

    @Override
    public CreditDto scoreData(ScoringDataDto scoringDataDto) {
        return annuityPaymentScheme.calculatePaymentScheme(scoringDataDto);
    }

    private List<LoanOfferDto> combine(LoanOfferDto loanOfferDto) {
        List<LoanOfferDto> offers = new ArrayList<>();
        for (boolean isInsurance : new boolean[]{false, true}) {
            for (boolean isSalaryClient : new boolean[]{false, true}) {
                LoanOfferDto changed = mapper.copyLoanOffer(loanOfferDto).
                        isInsuranceEnabled(isInsurance).
                        isSalaryClient(isSalaryClient);
                offers.add(loanOfferBuilder.build(changed));
            }
        }
        return offers;
    }

    private void calcAnnuityPaymentScheme(BigDecimal rate){

    }


}
