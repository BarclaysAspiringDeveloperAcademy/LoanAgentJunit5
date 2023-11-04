package org.example;

public class LoanAgent {
    ICreditAgency agency;
    int minimumCreditScore;

    IErrorLog errorLog;



    public boolean processLoanApplication(ILoanApplication application) throws InvalidCreditScoreException {
        boolean response = false;
        String ssn = application.getSSN();
        int creditScore = agency.getCreditScore(ssn);
        if(creditScore < 200 || creditScore > 850) {
            //errorLog.log(creditScore + " is not a valid credit score");
            throw new InvalidCreditScoreException(creditScore + " is not a valid credit score");
        }
        if(creditScore >= minimumCreditScore) {
            response = true;
        }
        return response;
    }

    public ICreditAgency getAgency() {
        return agency;
    }

    public void setAgency(ICreditAgency agency) {
        this.agency = agency;
    }

    public int getMinimumCreditScore() {
        return minimumCreditScore;
    }

    public void setMinimumCreditScore(int minimumCreditScore) {
        this.minimumCreditScore = minimumCreditScore;
    }

    public void setErrorLog(IErrorLog errorLog) {
        this.errorLog = errorLog;
    }
}
