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
import ru.gav.creditbank.calculator.utils.comparators.LoanOfferRateComparator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculatorServiceImpl implements CalculatorService {

    private final LoanStatementLoanOfferMapper mapper;
    private final LoanOfferBuilder loanOfferBuilder;

    @Qualifier(value = "AnnuityPaymentScheme")
    private final PaymentScheme annuityPaymentScheme;

    /**
     * Создает предложения о кредите
     * @param loanStatementRequestDto данные для генерации предложений
     * @return список предложений
     */

    @Override
    public List<LoanOfferDto> createOffers(LoanStatementRequestDto loanStatementRequestDto) {
        LoanOfferDto loanOfferDto = mapper.loanStatementToLoanOffer(loanStatementRequestDto);
        return combine(loanOfferDto);
    }

    /**
     * Расчитывает кредит
     * @param scoringDataDto данные для скоринга
     * @return кредит
     */
    @Override
    public CreditDto scoreData(ScoringDataDto scoringDataDto) {
        return annuityPaymentScheme.calculatePaymentScheme(scoringDataDto);
    }

    /**
     * Генерирует различные предложения по принципу ((false false)(false true)(true false)(true true))
     * @param loanOfferDto предложение как основа
     * @return список предложений
     */
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
        offers.sort(new LoanOfferRateComparator());
        return offers;
    }
}
