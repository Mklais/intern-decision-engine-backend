package ee.taltech.inbankbackend.exceptions;

/**
 * Thrown when the applicant's age does not meet the loan criteria.
 */
public class InvalidAgeException extends RuntimeException {

    public InvalidAgeException(String message) {
        super(message);
    }

    public InvalidAgeException(String message, Throwable cause) {
        super(message, cause);
    }
}