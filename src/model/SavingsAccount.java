package model;

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(int accountId, int customerId, Bank bank, String accountType, double balance, double interestRate) {
        super(accountId, customerId, bank, accountType, balance);
        this.interestRate = interestRate;
    }

    // Implement abstract method from Account
    @Override
    public String getAccountDetails() {
        return "Savings Account with interest rate: " + interestRate;
    }

    // Calculate interest based on balance
    public double calculateInterest() {
        return getBalance() * interestRate / 100;
    }

    // Getter and Setter
    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
    
    @Override
    public String toString() {
        return "SavingsAccount{" +
                "accountId=" + getAccountId() +
                ", customerId=" + getCustomerId() +
                ", bank=" + getBank() +
                ", accountType='" + getAccountType() + '\'' +
                ", balance=" + getBalance() +
                ", interestRate=" + interestRate +
                '}';
    }
}
