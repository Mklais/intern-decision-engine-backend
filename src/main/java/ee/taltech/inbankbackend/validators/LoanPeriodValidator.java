package ee.taltech.inbankbackend.validators;

import ee.taltech.inbankbackend.common.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.InvalidLoanPeriodException;
import ee.taltech.inbankbackend.common.DecisionErrorMessages;
import org.springframework.stereotype.Component;

@Component
public class LoanPeriodValidator implements Validator<Integer> {
    @Override
    public void validate(Integer loanPeriod) throws InvalidLoanPeriodException {
        if (loanPeriod < DecisionEngineConstants.MINIMUM_LOAN_PERIOD
                || loanPeriod > DecisionEngineConstants.MAXIMUM_LOAN_PERIOD) {
            throw new InvalidLoanPeriodException(DecisionErrorMessages.INVALID_LOAN_PERIOD);
        }
    }
}
