package model;

import java.util.Date;

public class DepositTransaction extends Transaction {
    private String depositMethod;

    public DepositTransaction(int transactionId, int accountId, String transactionType, double amount, Date transactionDate, String depositMethod) {
        super(transactionId, accountId, transactionType, amount, transactionDate);
        this.depositMethod = depositMethod;
    }

    // Implement abstract method
    @Override
    public String getTransactionDetails() {
        return "Deposit Transaction via " + depositMethod;
    }

    // Getter and Setter
    public String getDepositMethod() {
        return depositMethod;
    }

    public void setDepositMethod(String depositMethod) {
        this.depositMethod = depositMethod;
    }
}
