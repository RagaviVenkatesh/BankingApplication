package model;

import java.util.Date;

public class WithdrawalTransaction extends Transaction {
    private String withdrawalMethod;

    public WithdrawalTransaction(int transactionId, int accountId, String transactionType, double amount, Date transactionDate, String withdrawalMethod) {
        super(transactionId, accountId, transactionType, amount, transactionDate);
        this.withdrawalMethod = withdrawalMethod;
    }

    // Implement abstract method
    @Override
    public String getTransactionDetails() {
        return "Withdrawal Transaction via " + withdrawalMethod;
    }

    // Getter and Setter
    public String getWithdrawalMethod() {
        return withdrawalMethod;
    }

    public void setWithdrawalMethod(String withdrawalMethod) {
        this.withdrawalMethod = withdrawalMethod;
    }
}
