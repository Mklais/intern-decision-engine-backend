package ee.taltech.inbankbackend.common;

/**
 * Holds all necessary constants for decision errors.
 */
public class DecisionErrorMessages {

    private DecisionErrorMessages() {
    }

    public static final String INVALID_ID_CODE = "Invalid personal ID code!";

    public static final String INVALID_LOAN_AMOUNT = "Invalid loan amount!";

    public static final String INVALID_LOAN_PERIOD = "Invalid loan period!";

    public static final String CUSTOMER_AGE_RESTRICTION = "Customer age is outside the approved range!";

    public static final String CUSTOMER_DEBT_RESTRICTION = "No valid loan found due to debt!";
}
