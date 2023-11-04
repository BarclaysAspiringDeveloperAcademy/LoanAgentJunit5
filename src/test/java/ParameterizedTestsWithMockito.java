import org.example.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ParameterizedTestsWithMockito {
    LoanAgent uut;
    ILoanApplication mockLoanApplication;
    ICreditAgency mockAgency;
    IErrorLog mockErrorLog;

    @BeforeEach
    public void setUp() {
        uut = new LoanAgent();
        mockLoanApplication = mock(ILoanApplication.class);
        when(mockLoanApplication.getSSN()).thenReturn("123-45-6789");
        mockAgency = mock(ICreditAgency.class);
        uut.setMinimumCreditScore(720);
        mockErrorLog = mock(IErrorLog.class);
        uut.setErrorLog(mockErrorLog);
    }

    @AfterEach
    public void teardown() {
        uut = null;
        mockAgency = null;
    }

    @ParameterizedTest
    @CsvSource({"200, false", "719, false", "720, true", "721, true", "850, true"})
    public void testWithValidCreditScores(int creditScore, boolean expectedResult) {
        boolean actualResult;
        when(mockAgency.getCreditScore(anyString())).thenReturn(creditScore);
        uut.setAgency(mockAgency);

        try {
            actualResult = uut.processLoanApplication(mockLoanApplication);
            assertEquals(expectedResult, actualResult);
        } catch (Exception e) {
            fail();
        }
    }

    @ParameterizedTest
    @CsvSource({"199", "851"})
    public void testWithInvalidCreditScores(int creditScore) {

        uut.setAgency(mockAgency);
        when(mockAgency.getCreditScore(anyString())).thenReturn(creditScore);

        assertAll("Exception assertions",
                () -> {
                    InvalidCreditScoreException invalid = assertThrows(InvalidCreditScoreException.class,
                            () -> {uut.processLoanApplication(mockLoanApplication);
                            });
                    assertEquals(creditScore + " is not a valid credit score", invalid.getMessage());
                });
        verify(mockErrorLog,times(1)).log(String.valueOf(creditScore) + " is not a valid credit score");
    }
}
