package model;

public class CurrentAccount extends Account {
    private double overdraftLimit;

    public CurrentAccount(int accountId, int customerId, Bank bank, String accountType, double balance, double overdraftLimit) {
        super(accountId, customerId, bank, accountType, balance);
        this.overdraftLimit = overdraftLimit;
    }

    // Implement abstract method from Account
    @Override
    public String getAccountDetails() {
        return "Current Account with overdraft limit: " + overdraftLimit;
    }

    // Getter and Setter
    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
    
    @Override
    public String toString() {
        return "CurrentAccount{" +
                "accountId=" + getAccountId() +
                ", customerId=" + getCustomerId() +
                ", bank=" + getBank() +
                ", accountType='" + getAccountType() + '\'' +
                ", balance=" + getBalance() +
                ", overdraftLimit=" + overdraftLimit +
                '}';
    }
}
