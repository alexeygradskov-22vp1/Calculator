package ru.gav.creditbank.calculator.utils.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gav.calculator.model.CreditDto;
import ru.gav.calculator.model.PaymentScheduleElementDto;
import ru.gav.calculator.model.ScoringDataDto;
import ru.gav.creditbank.calculator.config.GlobalProps;
import ru.gav.creditbank.calculator.mappers.CreditScoringDataMapper;
import ru.gav.creditbank.calculator.service.ScoringService;
import ru.gav.creditbank.calculator.utils.CalcMonthlyPayment;
import ru.gav.creditbank.calculator.utils.PaymentScheme;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Бин для расчета кредита по аннуитетной схеме платежей
 */

@Service(value = "AnnuityPaymentScheme")
@RequiredArgsConstructor
public class AnnuityPaymentScheme implements PaymentScheme {
    private final GlobalProps globalProps;
    private final ScoringService scoringService;
    private final CreditScoringDataMapper creditScoringDataMapper;
    private final CalcMonthlyPayment calcMonthlyPaymentService;

    @Override
    public CreditDto calculatePaymentScheme(ScoringDataDto scoringDataDto) {
        CreditDto calculated = creditScoringDataMapper.scoringDataToCredit(scoringDataDto);
        return calculated.
                rate(scoringService.scoreRateOfCredit(scoringDataDto)).
                monthlyPayment(calcMonthlyPayment(calculated)).
                paymentSchedule(calculatePaymentSchedule(calculated)).
                psk(calculatePsk(calculated));
    }

    /**
     * Метод для расчета ежемесячного платежа по аннуитету
     * @param creditDto Данные кредита
     * @return ежемесячный платеж
     */


    public BigDecimal calcMonthlyPayment(CreditDto creditDto) {
        return calcMonthlyPaymentService.calc(creditDto.getRate(),creditDto.getTerm(),creditDto.getAmount());
    }

    /**
     * Метод для расчета графика платежей
     * @param creditDto Данные кредита
     * @return график платежей
     */
    public List<PaymentScheduleElementDto> calculatePaymentSchedule(CreditDto creditDto) {

        List<PaymentScheduleElementDto> payments = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        BigDecimal balanceOfDebt = creditDto.getAmount();
        BigDecimal monthlyPayment = new BigDecimal(0);
        BigDecimal rate = convertRatePersentsToNumber(creditDto);
        BigDecimal interestPayment = new BigDecimal(0), debtPayment = new BigDecimal(0);
        int term = 0;
        while (term <= creditDto.getTerm()) {
            payments.add(
                    new PaymentScheduleElementDto().
                            date(localDate).
                            remainingDebt(balanceOfDebt).
                            interestPayment(interestPayment).
                            debtPayment(debtPayment).
                            totalPayment(monthlyPayment).
                            number(term));
            monthlyPayment = creditDto.getMonthlyPayment();
            interestPayment = calculateInterestPayment(balanceOfDebt, rate);
            debtPayment = monthlyPayment.subtract(interestPayment);
            balanceOfDebt = balanceOfDebt.subtract(debtPayment);
            localDate = localDate.plusMonths(1);
            term++;
        }
        return payments;
    }

    /**
     * Метод конвертирует годовую ставку в месячную
     * @param creditDto данные кредита
     * @return месячная ставка
     */

    private BigDecimal convertRatePersentsToNumber(CreditDto creditDto) {
        return creditDto.getRate().
                divide(globalProps.getQuantityMonthsInYear(), globalProps.getPrecisionOfCalculations()).
                divide(new BigDecimal(100), globalProps.getPrecisionOfCalculations());
    }

    /**
     * Рассчитывает платеж по процентам от остатка долга
     * @param balanceOfDebt остаток долга
     * @param rate месячная ставка
     * @return месячная ставка
     */

    private BigDecimal calculateInterestPayment(BigDecimal balanceOfDebt,
                                                BigDecimal rate) {
        BigDecimal interestPayment;
        interestPayment = balanceOfDebt.
                multiply(rate);
        return interestPayment;
    }

    /**
     * Метод рассчитывает полную стоимость кредита
     * @param creditDto данные кредита
     * @return полная стоимость кредита
     */

    private BigDecimal calculatePsk(CreditDto creditDto) {
        return creditDto.getMonthlyPayment().multiply(new BigDecimal(creditDto.getTerm()));
    }

}
