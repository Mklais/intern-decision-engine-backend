package ee.taltech.inbankbackend.common;

/**
 * Holds all necessary constants for the decision engine.
 */
public class DecisionEngineConstants {

    private DecisionEngineConstants() {
    }
    public static final long MINIMUM_LOAN_AMOUNT = 2000;
    public static final long MAXIMUM_LOAN_AMOUNT = 10000;
    public static final int MAXIMUM_LOAN_PERIOD = 60;
    public static final int MINIMUM_LOAN_PERIOD = 12;
    public static final int SEGMENT_1_CREDIT_MODIFIER = 100;
    public static final int SEGMENT_2_CREDIT_MODIFIER = 300;
    public static final int SEGMENT_3_CREDIT_MODIFIER = 1000;
    public static final int MINIMUM_CUSTOMER_AGE = 18;
    public static final int MAXIMUM_CUSTOMER_AGE = 75;
    public static final int LOAN_PERIOD_ADJUSTMENT_STEP_UP = 1;
}
