package ru.gav.creditbank.calculator.builders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.gav.calculator.model.LoanOfferDto;
import ru.gav.creditbank.calculator.config.CreditProperties;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoanOfferBuilderTest {
    @Autowired
    private final LoanOfferBuilder loanOfferBuilder;
    @Autowired
    private final CreditProperties creditProperties;

    //Тестовые данные

    private LoanOfferDto falseFalseLoanOfferDto;
    private LoanOfferDto falseTrueLoanOfferDto;
    private LoanOfferDto trueFalseLoanOfferDto;
    private LoanOfferDto trueTrueLoanOfferDto;

    private LoanOfferDto ffResult;
    private LoanOfferDto ftResult;
    private LoanOfferDto tfResult;
    private LoanOfferDto ttResult;


    @BeforeEach
    public void init() {
        try (InputStream ffIS = LoanOfferDto.class.getResourceAsStream("/offers/false-false-loan-offer.json");
             InputStream ftIS = LoanOfferDto.class.getResourceAsStream("/offers/false-true-loan-offer.json");
             InputStream tfIS = LoanOfferDto.class.getResourceAsStream("/offers/true-false-loan-offer.json");
             InputStream ttIS = LoanOfferDto.class.getResourceAsStream("/offers/true-true-loan-offer.json")) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            falseFalseLoanOfferDto = mapper.readValue(ffIS, LoanOfferDto.class);
            falseTrueLoanOfferDto = mapper.readValue(ftIS, LoanOfferDto.class);
            trueFalseLoanOfferDto = mapper.readValue(tfIS, LoanOfferDto.class);
            trueTrueLoanOfferDto = mapper.readValue(ttIS, LoanOfferDto.class);
        } catch (IOException e) {
            System.err.printf(e.toString());
        }
    }

    @Test
    public void testBuildLoanOffer() {
        ffResult = loanOfferBuilder.build(falseFalseLoanOfferDto);
        ftResult = loanOfferBuilder.build(falseTrueLoanOfferDto);
        tfResult = loanOfferBuilder.build(trueFalseLoanOfferDto);
        ttResult = loanOfferBuilder.build(trueTrueLoanOfferDto);

        assertEquals(ffResult.getRate(), creditProperties.getRate());
        assertEquals(ftResult.getRate(), creditProperties.getRate().subtract(new BigDecimal(1)));
        assertEquals(tfResult.getRate(), creditProperties.getRate().subtract(new BigDecimal(3)));
        assertEquals(ttResult.getRate(), creditProperties.getRate().subtract(new BigDecimal(4)));

        assertEquals(ffResult.getTotalAmount(), new BigDecimal(100000));
        assertEquals(ftResult.getTotalAmount(), new BigDecimal(100000));
        assertEquals(tfResult.getTotalAmount(), new BigDecimal(101000));
        assertEquals(ttResult.getTotalAmount(), new BigDecimal(101000));

        assertEquals(ffResult.getMonthlyPayment().setScale(2, RoundingMode.HALF_EVEN), new BigDecimal("4848.66"));
        assertEquals(ftResult.getMonthlyPayment().setScale(2, RoundingMode.HALF_EVEN), new BigDecimal("4801.29"));
        assertEquals(tfResult.getMonthlyPayment().setScale(2, RoundingMode.HALF_EVEN), new BigDecimal("4754.42"));
        assertEquals(ttResult.getMonthlyPayment().setScale(2, RoundingMode.HALF_EVEN), new BigDecimal("4707.39"));
    }


    @Test
    public void testDifferentRequestedAmounts() {
        BigDecimal testAmount = new BigDecimal(20000);
        falseFalseLoanOfferDto.requestedAmount(testAmount);
        falseTrueLoanOfferDto.requestedAmount(testAmount);
        trueFalseLoanOfferDto.requestedAmount(testAmount);
        trueTrueLoanOfferDto.requestedAmount(testAmount);

        ffResult = loanOfferBuilder.build(falseFalseLoanOfferDto);
        ftResult = loanOfferBuilder.build(falseTrueLoanOfferDto);
        tfResult = loanOfferBuilder.build(trueFalseLoanOfferDto);
        ttResult = loanOfferBuilder.build(trueTrueLoanOfferDto);

        assertEquals(ffResult.getTotalAmount(), new BigDecimal(20000));
        assertEquals(ftResult.getTotalAmount(), new BigDecimal(20000));
        assertEquals(tfResult.getTotalAmount(), new BigDecimal("20200.0"));
        assertEquals(ttResult.getTotalAmount(), new BigDecimal("20200.0"));

        assertEquals(ffResult.getMonthlyPayment().setScale(2, RoundingMode.HALF_EVEN), new BigDecimal("969.73"));
        assertEquals(ftResult.getMonthlyPayment().setScale(2, RoundingMode.HALF_EVEN), new BigDecimal("960.26"));
        assertEquals(tfResult.getMonthlyPayment().setScale(2, RoundingMode.HALF_EVEN), new BigDecimal("950.88"));
        assertEquals(ttResult.getMonthlyPayment().setScale(2, RoundingMode.HALF_EVEN), new BigDecimal("941.48"));


        testAmount = new BigDecimal(300000);
        falseFalseLoanOfferDto.requestedAmount(testAmount);
        falseTrueLoanOfferDto.requestedAmount(testAmount);
        trueFalseLoanOfferDto.requestedAmount(testAmount);
        trueTrueLoanOfferDto.requestedAmount(testAmount);

        ffResult = loanOfferBuilder.build(falseFalseLoanOfferDto);
        ftResult = loanOfferBuilder.build(falseTrueLoanOfferDto);
        tfResult = loanOfferBuilder.build(trueFalseLoanOfferDto);
        ttResult = loanOfferBuilder.build(trueTrueLoanOfferDto);

        assertEquals(ffResult.getTotalAmount(), new BigDecimal(300000));
        assertEquals(ftResult.getTotalAmount(), new BigDecimal(300000));
        assertEquals(tfResult.getTotalAmount(), new BigDecimal(303000));
        assertEquals(ttResult.getTotalAmount(), new BigDecimal(303000));

        assertEquals(ffResult.getMonthlyPayment().setScale(2, RoundingMode.HALF_EVEN), new BigDecimal("14545.99"));
        assertEquals(ftResult.getMonthlyPayment().setScale(2, RoundingMode.HALF_EVEN), new BigDecimal("14403.86"));
        assertEquals(tfResult.getMonthlyPayment().setScale(2, RoundingMode.HALF_EVEN), new BigDecimal("14263.26"));
        assertEquals(ttResult.getMonthlyPayment().setScale(2, RoundingMode.HALF_EVEN), new BigDecimal("14122.17"));
    }
}
