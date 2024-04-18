package ee.taltech.inbankbackend.validators;

import ee.taltech.inbankbackend.common.DecisionErrorMessages;
import ee.taltech.inbankbackend.exceptions.NoValidLoanException;
import org.springframework.stereotype.Component;

@Component
public class CreditModifierValidator implements Validator<Integer> {
    @Override
    public void validate(Integer creditModifier) throws NoValidLoanException {
        if (creditModifier == 0) {
            throw new NoValidLoanException(DecisionErrorMessages.CUSTOMER_DEBT_RESTRICTION);
        }
    }
}
