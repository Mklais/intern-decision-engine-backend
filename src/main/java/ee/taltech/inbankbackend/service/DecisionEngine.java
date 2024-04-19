package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.common.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.*;
import ee.taltech.inbankbackend.model.Decision;
import ee.taltech.inbankbackend.validators.*;
import org.springframework.stereotype.Service;

/**
 * A service class that provides a method for calculating an approved loan amount and period for a customer.
 * The loan amount is calculated based on the customer's credit modifier,
 * which is determined by the last four digits of their ID code.
 */
@Service
public class DecisionEngine {
    private int creditModifier = 0;
    private final Validator<Integer> creditModifierValidator = new CreditModifierValidator();
    private final Validator<String> personalCodeValidator = new PersonalCodeValidator();
    private final Validator<Long> loanAmountValidator = new LoanAmountValidator();
    private final Validator<Integer> loanPeriodValidator = new LoanPeriodValidator();

    /**
     * Calculates the maximum loan amount and period for the customer based on their ID code,
     * the requested loan amount and the loan period.
     * The loan period must be between 12 and 60 months (inclusive).
     * The loan amount must be between 2000 and 10000€ months (inclusive).
     *
     * @param personalCode ID code of the customer that made the request.
     * @param loanAmount   Requested loan amount
     * @param loanPeriod   Requested loan period
     * @return A Decision object containing the approved loan amount and period, and an error message (if any)
     * @throws InvalidPersonalCodeException If the provided personal ID code is invalid
     * @throws InvalidLoanAmountException   If the requested loan amount is invalid
     * @throws InvalidLoanPeriodException   If the requested loan period is invalid
     * @throws NoValidLoanException         If there is no valid loan found for the given ID code, loan amount and loan period
     */
    public Decision calculateApprovedLoan(String personalCode, Long loanAmount, int loanPeriod)
            throws InvalidPersonalCodeException, InvalidLoanAmountException, InvalidLoanPeriodException,
            NoValidLoanException {
        verifyInputs(personalCode, loanAmount, loanPeriod);

        creditModifier = getCreditModifier(personalCode);

        creditModifierValidator.validate(creditModifier);

        long maximumAmountWithinRequestedPeriod = calculateLoanAmount(creditModifier, loanPeriod);

        if (calculateCreditScore(creditModifier, maximumAmountWithinRequestedPeriod, loanPeriod) &&
                loanAmountValidator.isValid(maximumAmountWithinRequestedPeriod)) {
            return new Decision((int) maximumAmountWithinRequestedPeriod, loanPeriod, null);
        }
        return findNewPeriodWithMaxLoanAmount();
    }

    private int getCreditModifier(String personalCode) {
        int segment = Integer.parseInt(personalCode.substring(personalCode.length() - 4));
        if (segment < 2500) return 0;
        else if (segment < 5000) return DecisionEngineConstants.SEGMENT_1_CREDIT_MODIFIER;
        else if (segment < 7500) return DecisionEngineConstants.SEGMENT_2_CREDIT_MODIFIER;
        return DecisionEngineConstants.SEGMENT_3_CREDIT_MODIFIER;
    }

    /**
     * Verify that all inputs are valid according to business rules.
     * If inputs are invalid, then throws corresponding exceptions.
     *
     * @param personalCode Provided personal ID code
     * @param loanAmount   Requested loan amount
     * @param loanPeriod   Requested loan period
     * @throws InvalidPersonalCodeException If the provided personal ID code is invalid
     * @throws InvalidLoanAmountException   If the requested loan amount is invalid
     * @throws InvalidLoanPeriodException   If the requested loan period is invalid
     * @throws NoValidLoanException   If the loan is not valid
     */
    private void verifyInputs(String personalCode, Long loanAmount, int loanPeriod)
            throws InvalidPersonalCodeException, InvalidLoanAmountException, InvalidLoanPeriodException, NoValidLoanException {
        personalCodeValidator.validate(personalCode);
        loanAmountValidator.validate(loanAmount);
        loanPeriodValidator.validate(loanPeriod);
    }

    private boolean calculateCreditScore(int creditModifier, long loanAmount, int loanPeriod) {
        return (((double) creditModifier / loanAmount) * loanPeriod) >= 1;
    }

    private long calculateLoanAmount(int creditModifier, int loanPeriod) {
        return Math.min(((long) creditModifier * loanPeriod), DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT);
    }

    private Decision findNewPeriodWithMaxLoanAmount() {
        long maxApprovedAmount = 0;
        int maxApprovedPeriod = DecisionEngineConstants.MINIMUM_LOAN_PERIOD;
        int proposedLoanPeriod = DecisionEngineConstants.MINIMUM_LOAN_PERIOD;

        // Loop through all periods up to the maximum allowed
        while (proposedLoanPeriod <= DecisionEngineConstants.MAXIMUM_LOAN_PERIOD) {
            long proposedLoanAmount = calculateLoanAmount(creditModifier, proposedLoanPeriod);
            if (calculateCreditScore(creditModifier, proposedLoanAmount, proposedLoanPeriod) &&
                    proposedLoanAmount > maxApprovedAmount) {
                maxApprovedAmount = proposedLoanAmount;
                maxApprovedPeriod = proposedLoanPeriod;
            }
            proposedLoanPeriod += DecisionEngineConstants.LOAN_PERIOD_ADJUSTMENT_STEP_UP;
        }

        if (maxApprovedAmount > 0) {
            return new Decision((int) maxApprovedAmount, maxApprovedPeriod, null);
        } else {
            return new Decision(0, DecisionEngineConstants.MINIMUM_LOAN_PERIOD,
                    "No suitable loan amount found within any period"
            );
        }
    }
}
