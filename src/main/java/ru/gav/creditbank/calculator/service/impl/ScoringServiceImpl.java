package ru.gav.creditbank.calculator.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gav.calculator.model.*;
import ru.gav.creditbank.calculator.config.CreditProperties;
import ru.gav.creditbank.calculator.exceptions.supplier.ExceptionSupplier;
import ru.gav.creditbank.calculator.service.ScoringService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ScoringServiceImpl implements ScoringService {
    private final ExceptionSupplier exceptionSupplier;
    private final CreditProperties creditProperties;


    @Override
    public BigDecimal scoreRateOfCredit(ScoringDataDto scoringDataDto) {
        validAge(scoringDataDto);
        validWorkExperience(scoringDataDto);
        validLoanAmountGreaterThenTwentyFourSalaries(scoringDataDto);
        BigDecimal currentRate = creditProperties.getRate();
        currentRate = calculateRateByEmployeeStatus(currentRate, scoringDataDto.getEmployment().getEmploymentStatus());
        currentRate = calculateRateByEmployeePosition(currentRate, scoringDataDto.getEmployment().getPosition());
        currentRate = calculateRateByMaritalStatus(currentRate, scoringDataDto.getMaritalStatus());
        currentRate = calculateRateByGenderAndAge(currentRate, scoringDataDto);
        return currentRate;
    }

    private BigDecimal calculateRateByEmployeeStatus(BigDecimal currentRate, EmploymentStatusDto employmentStatusDto) {
        Optional<BigDecimal> rateReduction = switch (employmentStatusDto) {
            case EMPLOYEE -> Optional.of(new BigDecimal(0));
            case SELF_EMPLOYED ->  Optional.of(new BigDecimal(2));
            case UNEMPLOYED -> Optional.empty();
            case BUSINESS_OWNER ->  Optional.of(new BigDecimal(1));
        };
        return currentRate.add(
                rateReduction.orElseThrow(exceptionSupplier.scoringExceptionSupplier("Working status"))
        );
    }

    private BigDecimal calculateRateByEmployeePosition(BigDecimal currentRate, PositionDto positionDto) {
        BigDecimal rateReduction = switch (positionDto) {
            case TOP_MANAGER -> new BigDecimal(3);
            case MIDDLE_MANAGER -> new BigDecimal(2);
            case LOWER_LEVEL_MANAGER -> new BigDecimal(1);
            case CONTRACTOR -> new BigDecimal(0);
        };
        return currentRate.subtract(rateReduction);
    }

    private BigDecimal calculateRateByMaritalStatus(BigDecimal currentRate, MaritalStatusDto maritalStatusDto) {
        BigDecimal rateReduction = switch (maritalStatusDto) {
            case SINGLE -> new BigDecimal(-1);
            case MARRIED -> new BigDecimal(3);
        };
        return currentRate.subtract(rateReduction);
    }

    private long calcAge(LocalDate birthdate) {
        LocalDate now = LocalDate.now();
        return ChronoUnit.YEARS.between(birthdate, now);
    }

    private BigDecimal calculateRateByGenderAndAge(BigDecimal currentRate, ScoringDataDto scoringDataDto) {
        BigDecimal rateReduction = new BigDecimal(0);
        long age = calcAge(scoringDataDto.getBirthdate());
        if (scoringDataDto.getGender().equals(GenderDto.MALE) && age >= 30 && age < 55){
            rateReduction = new BigDecimal(3);
        }else if (scoringDataDto.getGender().equals(GenderDto.FEMALE)&&age>=32&&age<60){
            rateReduction = new BigDecimal(3);
        }
        else if (!scoringDataDto.getGender().equals(GenderDto.MALE)&&!scoringDataDto.getGender().equals(GenderDto.FEMALE)){
            rateReduction = new BigDecimal(-7);
        }
        return currentRate.subtract(rateReduction);
    }

    //Validation Methods

    private void validWorkExperience(ScoringDataDto scoringDataDto){
        if (scoringDataDto.getEmployment().getWorkExperienceTotal()<18||
                scoringDataDto.getEmployment().getWorkExperienceCurrent()<3)
            throw exceptionSupplier.scoringExceptionSupplier("Unacceptable length of work experience").get();
    }

    private void validLoanAmountGreaterThenTwentyFourSalaries(ScoringDataDto scoringDataDto) {
        BigDecimal twentyFourSalaries = scoringDataDto.getEmployment().getSalary().multiply(new BigDecimal(24));
        if (twentyFourSalaries.compareTo(scoringDataDto.getAmount()) < 0) {
            throw exceptionSupplier.scoringExceptionSupplier("Twenty four salaries less than requested amount").get();
        }
    }

    private void validAge(ScoringDataDto scoringDataDto) {
        long age = calcAge(scoringDataDto.getBirthdate());
        if (age < 20 || age >= 65)
            throw exceptionSupplier.scoringExceptionSupplier("Age goes beyond the limits of permissible").get();
    }
}
