package ee.taltech.inbankbackend.model;

/**
 * Holds the response data of the REST endpoint.
 */
public record Decision(Integer loanAmount, Integer loanPeriod, String errorMessage) {}