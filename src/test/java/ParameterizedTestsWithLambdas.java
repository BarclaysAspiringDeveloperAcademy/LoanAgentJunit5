import org.example.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ParameterizedTestsWithLambdas {
    LoanAgent uut;
    ILoanApplication loanApplication;
    ICreditAgency agency;
    IErrorLog mockErrorLog;
    int creditScore;

    @BeforeEach
    public void setUp() {
        uut = new LoanAgent();
        uut.setMinimumCreditScore(720);

        mockErrorLog = mock(IErrorLog.class);
        uut.setErrorLog(mockErrorLog);

        loanApplication=() -> {return "123-456-789";};
    }

    @AfterEach
    public void teardown() {
        uut = null;
        mockErrorLog = null;
    }

    @ParameterizedTest
    @CsvSource({"200, false", "719, false", "720, true", "721, true", "850, true"})
    public void testWithValidCreditScores(int creditScore, boolean expectedResult) throws InvalidCreditScoreException {
        boolean actualResult;
        agency=(ssn) -> {return creditScore;};
        uut.setAgency(agency);

        try {
            actualResult = uut.processLoanApplication(loanApplication);
            assertEquals(expectedResult, actualResult);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }

    @ParameterizedTest
    @CsvSource({"199", "851"})
    public void testWithInvalidCreditScores(int creditScore) {
        agency=(ssn) -> {return creditScore;};
        uut.setAgency(agency);

        assertAll("Exception assertions",
                () -> {
                    InvalidCreditScoreException invalid = assertThrows(InvalidCreditScoreException.class,
                            () -> {uut.processLoanApplication(loanApplication);
                            });
                    assertEquals(creditScore + " is not a valid credit score", invalid.getMessage());
                });
        verify(mockErrorLog,times(1)).log(String.valueOf(creditScore) + " is not a valid credit score");
    }
}
