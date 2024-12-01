package ru.gav.creditbank.calculator.utils.comparators;

import ru.gav.calculator.model.LoanOfferDto;

import java.util.Comparator;

public class LoanOfferRateComparator implements Comparator<LoanOfferDto> {
    @Override
    public int compare(LoanOfferDto o1, LoanOfferDto o2) {
        return o2.getRate().compareTo(o1.getRate());
    }
}
