package service;

import dao.BankDAO;
import dao.BankDAOImpl;
import dao.TransactionDAO;
import dao.TransactionDAOImpl;
import dao.AccountDAO;
import dao.AccountDAOImpl;
import exceptions.BankingException;
import exceptions.InvalidAccountTypeException;
import model.Bank;
import model.Account;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BankService {
    private final BankDAO bankDAO;
    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public BankService() {
        this.bankDAO = new BankDAOImpl();
        this.accountDAO = new AccountDAOImpl();
        this.transactionDAO = new TransactionDAOImpl();
    }

    public void createBank(Bank bank) throws SQLException, BankingException {
        bankDAO.createBank(bank);
    }

    public Bank getBankById(int bankId) throws SQLException, BankingException {
        return bankDAO.getBankById(bankId);
    }

    public List<Bank> getAllBanks() throws SQLException, BankingException {
        return bankDAO.getAllBanks();
    }

    public void updateBankInfo(Bank bank) throws SQLException, BankingException {
        bankDAO.updateBank(bank);
    }

    public void deleteBank(int bankId) throws SQLException, BankingException {
        bankDAO.deleteBank(bankId);
    }

    // Report generation using `total_balance_for_bank` function
    public double getTotalBalanceForBank(int bankId) throws SQLException, BankingException {
        return bankDAO.getTotalBalanceForBank(bankId);
    }

    // Fetch account details from AccountBankView for reporting
    public List<Account> getAccountsByBank(int bankId) throws SQLException, BankingException, InvalidAccountTypeException {
        return accountDAO.getAccountsByBankId(bankId);
    }
    
    //Retrieves and displays a summary of all transactions for a given day.
    public List<String> displayDailyTransactionSummary(Date date) throws SQLException, BankingException {
        List<String> summary = transactionDAO.getDailyTransactionSummary(date);
        return summary != null ? summary : new ArrayList<>();
    }
}
