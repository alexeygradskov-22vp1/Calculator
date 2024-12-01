package ru.gav.creditbank.calculator.mappers;

import org.mapstruct.Mapper;
import ru.gav.calculator.model.CreditDto;
import ru.gav.calculator.model.ScoringDataDto;

@Mapper(componentModel = "SPRING")
public interface CreditScoringDataMapper {

    CreditDto scoringDataToCredit(ScoringDataDto scoringDataDto);
}
