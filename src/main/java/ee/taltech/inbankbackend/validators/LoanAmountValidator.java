package ee.taltech.inbankbackend.validators;

import ee.taltech.inbankbackend.common.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.InvalidLoanAmountException;
import ee.taltech.inbankbackend.common.DecisionErrorMessages;
import org.springframework.stereotype.Component;

@Component
public class LoanAmountValidator implements Validator<Long> {
    @Override
    public void validate(Long loanAmount) throws InvalidLoanAmountException {
        if (!isValid(loanAmount)) {
            throw new InvalidLoanAmountException(DecisionErrorMessages.INVALID_LOAN_AMOUNT);
        }
    }

    @Override
    public boolean isValid(Long loanAmount) {
        return (loanAmount >= DecisionEngineConstants.MINIMUM_LOAN_AMOUNT &&
                loanAmount <= DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT
        );
    }
}
