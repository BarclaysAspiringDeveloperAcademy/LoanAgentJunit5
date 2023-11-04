package org.example;

public class OriginalLoanAgent {
    ICreditAgency agency;
    public boolean processLoanApplication(LoanApplication application) throws InvalidCreditScoreException {
        boolean response = false;
        String ssn = application.getSSN();
        int creditScore = agency.getCreditScore(ssn);
        if(creditScore < 200 || creditScore > 850) {
            throw new InvalidCreditScoreException(creditScore + " is not a valid credit score");
        }
        if(creditScore >= 720) {
            response = true;
        }
        return response;
    }
    public void setAgency(ICreditAgency agency) {
        this.agency = agency;
    }
}
