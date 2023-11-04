import org.example.ICreditAgency;

public class CreditAgencyTestingStub implements ICreditAgency {
    int creditScore;
    @Override
    public int getCreditScore(String ssn) {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }
}
