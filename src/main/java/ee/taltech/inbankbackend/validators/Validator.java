package ee.taltech.inbankbackend.validators;

import ee.taltech.inbankbackend.exceptions.InvalidLoanAmountException;
import ee.taltech.inbankbackend.exceptions.InvalidLoanPeriodException;
import ee.taltech.inbankbackend.exceptions.InvalidPersonalCodeException;
import ee.taltech.inbankbackend.exceptions.NoValidLoanException;

public interface Validator<T> {
    void validate(T input) throws
            InvalidPersonalCodeException,
            InvalidLoanAmountException,
            InvalidLoanPeriodException,
            NoValidLoanException;

    default boolean isValid(T input) {
        try {
            validate(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
