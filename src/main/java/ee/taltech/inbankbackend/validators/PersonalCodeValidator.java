package ee.taltech.inbankbackend.validators;

import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeValidator;
import ee.taltech.inbankbackend.common.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.InvalidPersonalCodeException;
import ee.taltech.inbankbackend.common.DecisionErrorMessages;
import ee.taltech.inbankbackend.exceptions.NoValidLoanException;

import java.time.LocalDate;
import java.time.Period;

public class PersonalCodeValidator implements Validator<String> {

    // Used to check for the validity of the presented ID code.
    private final EstonianPersonalCodeValidator validator = new EstonianPersonalCodeValidator();

    @Override
    public void validate(String personalCode) throws InvalidPersonalCodeException, NoValidLoanException {
        if (!validator.isValid(personalCode)) {
            throw new InvalidPersonalCodeException(DecisionErrorMessages.INVALID_ID_CODE);
        }

        int customerAge = getAgeFromPersonalCode(personalCode);
        if (isCustomerWithinAgeLimits(customerAge)) {
            throw new NoValidLoanException(DecisionErrorMessages.CUSTOMER_AGE_RESTRICTION);
        }
    }

    /**
     *
     * @param customerAge
     * @return true if the customer is n
     */
    private boolean isCustomerWithinAgeLimits(int customerAge) {
        return (customerAge < DecisionEngineConstants.MINIMUM_CUSTOMER_AGE || customerAge > DecisionEngineConstants.MAXIMUM_CUSTOMER_AGE);
    }

    private int getAgeFromPersonalCode(String personalCode) {
        int century = getCenturyDigit(personalCode.charAt(0));
        int yearIndex = Integer.parseInt(personalCode.substring(1, 3));
        int year = century + yearIndex;
        int month = Integer.parseInt(personalCode.substring(3, 5));
        int day = Integer.parseInt(personalCode.substring(5, 7));

        LocalDate birthDate = LocalDate.of(year, month, day);
        LocalDate currentDate = LocalDate.now();

        return Period.between(birthDate, currentDate).getYears();
    }

    private int getCenturyDigit(char centuryDigit) {
        return switch (centuryDigit) {
            case '5', '6' -> 2000;
            default -> 1900;
        };
    }
}
