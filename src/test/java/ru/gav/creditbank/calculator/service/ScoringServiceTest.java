package ru.gav.creditbank.calculator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.gav.calculator.model.EmploymentStatusDto;
import ru.gav.calculator.model.MaritalStatusDto;
import ru.gav.calculator.model.PositionDto;
import ru.gav.calculator.model.ScoringDataDto;
import ru.gav.creditbank.calculator.exceptions.InvalidScoringException;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ScoringServiceTest {

    @Autowired
    private final ScoringService scoringService;

    private ScoringDataDto scoringDataDto;

    private BigDecimal resultedRate;

    @BeforeEach
    @SneakyThrows
    public void init() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        InputStream is = ScoringDataDto.class.getResourceAsStream("/scoring-test-data.json");
        scoringDataDto = mapper.readValue(is, ScoringDataDto.class);
        resultedRate = new BigDecimal(0);
    }


    @Test
    public void testScore() {
        //Employee(0), Top_manager(-3), 24salaries(+), Single(+1), genderAndAge(0), experience(+)
        resultedRate = scoringService.scoreRateOfCredit(scoringDataDto);
        assertEquals(new BigDecimal(13), resultedRate);

        //Business_owner(+1), Top_manager(-3), 24salaries(+), Single(+1), genderAndAge(0), experience(+)
        scoringDataDto.employment(scoringDataDto.getEmployment().employmentStatus(EmploymentStatusDto.BUSINESS_OWNER));
        resultedRate = scoringService.scoreRateOfCredit(scoringDataDto);
        assertEquals(new BigDecimal(14), resultedRate);

        //Business_owner(+1), Contractor(0), 24salaries(+), Single(+1), genderAndAge(0), experience(+)
        scoringDataDto.employment(scoringDataDto.getEmployment().position(PositionDto.CONTRACTOR));
        resultedRate = scoringService.scoreRateOfCredit(scoringDataDto);
        assertEquals(new BigDecimal(17), resultedRate);

        //Business_owner(+1), Contractor(0), 24salaries(+), Single(+1), genderAndAge(-3), experience(+)
        scoringDataDto.birthdate(LocalDate.parse("1990-11-11"));
        resultedRate = scoringService.scoreRateOfCredit(scoringDataDto);
        assertEquals(new BigDecimal(14), resultedRate);

        //Business_owner(+1), Contractor(0), 24salaries(+), Married(-3), genderAndAge(-3), experience(+)
        scoringDataDto.maritalStatus(MaritalStatusDto.MARRIED);
        resultedRate = scoringService.scoreRateOfCredit(scoringDataDto);
        assertEquals(new BigDecimal(10), resultedRate);

        //Business_owner(+1), Top_manager(-3), 24salaries(+), Single(+1), genderAndAge(0), experience(+)
        scoringDataDto.employment(scoringDataDto.getEmployment().position(PositionDto.TOP_MANAGER));
        resultedRate = scoringService.scoreRateOfCredit(scoringDataDto);
        assertEquals(new BigDecimal(7), resultedRate);
    }

    @Test
    public void testValidatedAge() {
        //Age less than 20
        scoringDataDto.birthdate(LocalDate.parse("2006-11-11"));
        assertThrows(InvalidScoringException.class, () -> scoringService.scoreRateOfCredit(scoringDataDto));

        //Age greater than 65
        scoringDataDto.birthdate(LocalDate.parse("1950-11-11"));
        assertThrows(InvalidScoringException.class, () -> scoringService.scoreRateOfCredit(scoringDataDto));
    }

    @Test
    public void testValidatedTwentyFourSalaries() {
        //24 salaries (120000) less than 200000
        scoringDataDto.employment(scoringDataDto.getEmployment().salary(new BigDecimal(5000)));
        assertThrows(InvalidScoringException.class, () -> scoringService.scoreRateOfCredit(scoringDataDto));
    }

    @Test
    public void testValidatedCurrentWorkExperience(){
        //Current exp less than 3 months
        scoringDataDto.employment(scoringDataDto.getEmployment().workExperienceCurrent(2));
        assertThrows(InvalidScoringException.class, () -> scoringService.scoreRateOfCredit(scoringDataDto));
    }

    @Test
    public void testValidatedTotalWorkExperience(){
        //Total exp less than 18 months
        scoringDataDto.employment(scoringDataDto.getEmployment().workExperienceTotal(15));
        assertThrows(InvalidScoringException.class, () -> scoringService.scoreRateOfCredit(scoringDataDto));
    }
}
