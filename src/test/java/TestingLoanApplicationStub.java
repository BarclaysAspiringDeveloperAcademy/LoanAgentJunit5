import org.example.ILoanApplication;

public class TestingLoanApplicationStub implements ILoanApplication {
    @Override
    public String getSSN() {
        return "dummy ssn";
    }
}
