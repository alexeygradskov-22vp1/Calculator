package ru.gav.creditbank.calculator.scheme;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.gav.calculator.model.CreditDto;
import ru.gav.calculator.model.ScoringDataDto;
import ru.gav.creditbank.calculator.utils.impl.AnnuityPaymentScheme;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Verified with https://calculator-credit.ru/
@SpringBootTest

public class AnnuityPaymentSchemeTest {
    private CreditDto creditDto;
    private ScoringDataDto scoringDataDto;
    @Autowired
    private AnnuityPaymentScheme annuityPaymentScheme;


    @BeforeEach
    @SneakyThrows
    public void init() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        InputStream is = ScoringDataDto.class.getResourceAsStream("/scoring-test-data.json");
        scoringDataDto = mapper.readValue(is, ScoringDataDto.class);
    }


    @Test
    public void testPsk() {
        //rate: 13 amount: 200000 term: 24
        creditDto = annuityPaymentScheme.calculatePaymentScheme(scoringDataDto);
        assertEquals(new BigDecimal("228200.75"), creditDto.getPsk().setScale(2, RoundingMode.HALF_EVEN));

        //rate: 13 amount: 300000 term: 24
        scoringDataDto.amount(new BigDecimal(300000));
        creditDto = annuityPaymentScheme.calculatePaymentScheme(scoringDataDto);
        assertEquals(new BigDecimal("342301.12"), creditDto.getPsk().setScale(2, RoundingMode.HALF_EVEN));

        //rate: 13 amount: 300000 term: 12
        scoringDataDto.term(12);
        creditDto = annuityPaymentScheme.calculatePaymentScheme(scoringDataDto);
        assertEquals(new BigDecimal("321542.19"), creditDto.getPsk().setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    public void testPaymentScheduleWithAmountTwoHundredThousand() {
        //rate: 13 amount: 200000 term: 6
        scoringDataDto.term(6);
        creditDto = annuityPaymentScheme.calculatePaymentScheme(scoringDataDto);
        assertEquals(new BigDecimal("2166.67"),
                creditDto.getPaymentSchedule().get(1).getInterestPayment().setScale(2, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("1815.21"),
                creditDto.getPaymentSchedule().get(2).getInterestPayment().setScale(2, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("1459.95"),
                creditDto.getPaymentSchedule().get(3).getInterestPayment().setScale(2, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("1100.84"),
                creditDto.getPaymentSchedule().get(4).getInterestPayment().setScale(2, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("737.84"),
                creditDto.getPaymentSchedule().get(5).getInterestPayment().setScale(2, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("370.91"),
                creditDto.getPaymentSchedule().get(6).getInterestPayment().setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    public void testPaymentScheduleWithAmountThreeHundredThousand() {
        //rate: 13 amount: 200000 term: 6
        scoringDataDto.term(6);
        scoringDataDto.amount(new BigDecimal(300000));
        creditDto = annuityPaymentScheme.calculatePaymentScheme(scoringDataDto);
        assertEquals(new BigDecimal("3250.00"),
                creditDto.getPaymentSchedule().get(1).getInterestPayment().setScale(2, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("2722.82"),
                creditDto.getPaymentSchedule().get(2).getInterestPayment().setScale(2, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("2189.93"),
                creditDto.getPaymentSchedule().get(3).getInterestPayment().setScale(2, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("1651.26"),
                creditDto.getPaymentSchedule().get(4).getInterestPayment().setScale(2, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("1106.76"),
                creditDto.getPaymentSchedule().get(5).getInterestPayment().setScale(2, RoundingMode.HALF_EVEN));
        assertEquals(new BigDecimal("556.36"),
                creditDto.getPaymentSchedule().get(6).getInterestPayment().setScale(2, RoundingMode.HALF_EVEN));
    }

}
