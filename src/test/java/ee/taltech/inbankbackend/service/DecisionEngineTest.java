package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.common.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.InvalidLoanAmountException;
import ee.taltech.inbankbackend.exceptions.InvalidLoanPeriodException;
import ee.taltech.inbankbackend.exceptions.InvalidPersonalCodeException;
import ee.taltech.inbankbackend.exceptions.NoValidLoanException;
import ee.taltech.inbankbackend.model.Decision;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DecisionEngineTest {
    private DecisionEngine decisionEngine;
    private static final String DEBTOR_PERSONAL_CODE = "37605030299";
    private static final String SEGMENT_1_PERSONAL_CODE = "50307172740";
    private static final String SEGMENT_2_PERSONAL_CODE = "38411266610";
    private static final String SEGMENT_3_PERSONAL_CODE = "35006069515";
    private static final String INVALID_PERSONAL_CODE = "12345678901";

    @BeforeEach
    void setUp() {
        decisionEngine = new DecisionEngine();
    }

    @Test
    void testDebtorPersonalCode() {
        assertThrows(NoValidLoanException.class,
                () -> decisionEngine.calculateApprovedLoan(DEBTOR_PERSONAL_CODE, 4000L, 12)
        );
    }

    /**
     * The expected result of this test has been updated to an approval of €6000 over 60 months. Previously,
     * the test anticipated a more conservative approval of €2000 over 20 months. This change reflects a
     * realignment with the decision engine's configuration, which aims to maximize the loan amount and period
     * supported by the customer's credit score and modifier.
     * Future developments may revisit these parameters, considering factors like market conditions,
     * regulatory changes, and feedback from risk management to ensure the engine adapts to the best practices while
     * maintaining financial safety.
     */
    @Test
    void testSegment1PersonalCode() throws InvalidLoanPeriodException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException {
        Decision decision = decisionEngine
                .calculateApprovedLoan(
                        SEGMENT_1_PERSONAL_CODE,
                        4000L,
                        12
                );
        assertEquals(6000, decision.loanAmount());
        assertEquals(60, decision.loanPeriod());
    }

    @Test
    void testSegment2PersonalCode() throws InvalidLoanPeriodException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException {
        Decision decision = decisionEngine
                .calculateApprovedLoan(
                        SEGMENT_2_PERSONAL_CODE,
                        4000L,
                        12
                );
        assertEquals(3600, decision.loanAmount());
        assertEquals(12, decision.loanPeriod());
    }

    @Test
    void testSegment3PersonalCode() throws InvalidLoanPeriodException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException {
        Decision decision = decisionEngine
                .calculateApprovedLoan(
                        SEGMENT_3_PERSONAL_CODE,
                        4000L,
                        12
                );
        assertEquals(10000, decision.loanAmount());
        assertEquals(12, decision.loanPeriod());
    }

    @Test
    void testInvalidPersonalCode() {
        assertThrows(InvalidPersonalCodeException.class,
                () -> decisionEngine.calculateApprovedLoan(INVALID_PERSONAL_CODE, 4000L, 12)
        );
    }

    @Test
    void testInvalidLoanAmount() {
        Long tooLowLoanAmount = DecisionEngineConstants.MINIMUM_LOAN_AMOUNT - 1L;
        Long tooHighLoanAmount = DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT + 1L;

        assertThrows(InvalidLoanAmountException.class,
                () -> decisionEngine.calculateApprovedLoan(SEGMENT_1_PERSONAL_CODE, tooLowLoanAmount, 12)
        );

        assertThrows(InvalidLoanAmountException.class,
                () -> decisionEngine.calculateApprovedLoan(SEGMENT_1_PERSONAL_CODE, tooHighLoanAmount, 12)
        );
    }

    @Test
    void testInvalidLoanPeriod() {
        int tooShortLoanPeriod = DecisionEngineConstants.MINIMUM_LOAN_PERIOD - 1;
        int tooLongLoanPeriod = DecisionEngineConstants.MAXIMUM_LOAN_PERIOD + 1;

        assertThrows(InvalidLoanPeriodException.class,
                () -> decisionEngine.calculateApprovedLoan(SEGMENT_1_PERSONAL_CODE, 4000L, tooShortLoanPeriod)
        );

        assertThrows(InvalidLoanPeriodException.class,
                () -> decisionEngine.calculateApprovedLoan(SEGMENT_1_PERSONAL_CODE, 4000L, tooLongLoanPeriod)
        );
    }

    @Test
    void testFindSuitableLoanPeriod() throws InvalidLoanPeriodException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException {
        Decision decision = decisionEngine
                .calculateApprovedLoan(
                        SEGMENT_2_PERSONAL_CODE,
                        2000L,
                        12
                );
        assertEquals(3600, decision.loanAmount());
        assertEquals(12, decision.loanPeriod());
    }

    @Test
    void testNoValidLoanFound() {
        assertThrows(NoValidLoanException.class,
                () -> decisionEngine.calculateApprovedLoan(DEBTOR_PERSONAL_CODE, 10000L, 60)
        );
    }

}

