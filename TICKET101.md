# Review and Conclusion for TICKET-101

## Overview

TICKET-101 involved implementing a new feature to enhance user authentication within our application.

## Validation Process

- **Code Review**: Ensured adherence to coding standards.
- **Functionality Testing**: Unit tests were conducted to verify the new features' functionality.

---
## Key Observations

#### If you wish to see more of the ideas on how to make it better, check out the mind-map in the /images.

### Strengths

- **Well-Structured Code**: The code is well-organized and thoroughly documented, making maintenance and future updates more manageable.
- **Robust Exception Handling**: The implementation uses custom exceptions effectively, enhancing error management and application reliability.
Easy to follow and manage.
- **Effective Use of Constants**: Constants improve code readability and maintainability, providing clear references for critical values.
- **Initial inputs & verification**: Before processing data, a basic verification has been set up to go over the inputs.
They are compared to constants to check if they meet the application requirements.
- **Use of controller**: Using a controller makes the code more readable, and is part of object-oriented programming.


### Areas for Improvement

- **Project structure**: Integrating validators into a structured validator approach provides us consistent and clarity of the code.
  This approach encapsulates validation logic separately from the business logic of calculating loan approvals.
- **Refactor Validation Logic**: Extracting validation logic into dedicated classes or methods would enhance readability and maintainability, adhering to the Single Responsibility Principle.
- **Decision Engine**: Inside the engine, there is a method ``highestValidLoanAmount`` which calculates the highest valid loan amount correctly with creditModifier and loanPeriod as inputs.
  But there is a potential for an infinite loop in adjusting the loan period.
<br>
`while (highestValidLoanAmount(loanPeriod) < DecisionEngineConstants.MINIMUM_LOAN_AMOUNT) { loanPeriod++; }`
<br>
The loop does not have a clear fixed upper limit and will keep running because it has no condition to exit if the `loanPeriod` exceeds the maximum allowable period or
if no valid loan configuration is possible.
If the loan period required to meet the minimum loan amount exceeds the maximum loan period, the loop doesn't stop,
and potentially causes this method to run indefinitely.
- **Custom exceptions**: There are improvements that can be made to align with best practices in Java programming, particularly concerning the exception handling.
  Major issue in the current implementation is that the custom exceptions are extending ``Throwable`` directly.
  In Java its more conventional and appropriate for custom exceptions to extend to ``Exception`` or ``RunTimeException``.
  To align with good practices, the custom exception classes should extend to ``RunTimeException`` rather than ``Throwable``.
- **Data Types**: Switch from `Integer` to `int` where constants are involved, as they do not require object reference features and are not nullable.
- **Code simplification examples**: 
  - Simplify complex boolean conditions for better readability and less error-proneness.
  - As we are already using constant values, it would improve the code to use constants for error messages also.
  <br>
```java
// Before
  if (!(DecisionEngineConstants.MINIMUM_LOAN_AMOUNT <= loanAmount) || !(loanAmount <= DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT)) {...}
// After
if (loanAmount < DecisionEngineConstants.MINIMUM_LOAN_AMOUNT || loanAmount > DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT) {...}
```
````java
❌ throw new InvalidPersonalCodeException("Invalid personal ID code!");
✅ throw new InvalidPersonalCodeException(ResponseErrorMessages.INVALID_ID_CODE);
````
````java
❌ throw new InvalidPersonalCodeException("Invalid personal ID code!");
✅ throw new InvalidPersonalCodeException(ResponseErrorMessages.INVALID_ID_CODE);
````
- **Shared instance**:
  - ❌ It is not good idea to declare an instance of ``DecisionResponse`` as a field in controller class.
    This is because multiple requests handled by different threads might modify the same DecisionResponse object at the same time - leading to unpredictable outcomes.
  - ✅ Instead, we should create a new instance of ``DecisionResponse`` within the method that we process the request in.
    This ensures that each request is handled independently.
---

## Most Important Shortcoming
### No credit score calculation algorithm
The most significant shortcoming in TICKET-101 is the lack of scoring algorithm implementation. 
Not having this implementation changes the results and the ``Decision`` model drastically and output wrong data.
This algorithm is the core function of the decision engine, as it directly affects the decision outcome on loan approvals.
![local Image](C:\Users\ferre\OneDrive\Desktop\Code\Inbank-Decision-Engine\intern-decision-engine-backend\images\MindMap.png)