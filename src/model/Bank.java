package model;

public class Bank {
    private int bankId;
    private String bankName;
    private String bankBranch;

    public Bank(int bankId, String bankName, String bankBranch) {
        this.bankId = bankId;
        this.bankName = bankName;
        this.bankBranch = bankBranch;
    }

    // Getters and Setters
    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }
}
