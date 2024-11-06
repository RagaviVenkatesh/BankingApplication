package service;

import dao.AccountDAO;
import dao.AccountDAOImpl;
import exceptions.*;
import model.Account;
import model.Bank;

import java.sql.SQLException;
import java.util.List;

public class AccountService {
    private final AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAOImpl();
    }

    public void createAccount(Account account) throws SQLException, InvalidAccountTypeException, BankingException {
        accountDAO.createAccount(account);
    }

    public Account getAccountById(int accountId) throws SQLException, AccountNotFoundException, InvalidAccountTypeException, BankingException {
        return accountDAO.getAccountById(accountId);
    }

    public List<Account> getAccountsByBankId(int bankId) throws SQLException, InvalidAccountTypeException, BankingException {
        return accountDAO.getAccountsByBankId(bankId);
    }

    public void updateAccountInfo(Account account) throws SQLException, AccountNotFoundException {
        accountDAO.updateAccount(account);
    }

    public void deleteAccount(int accountId) throws SQLException, AccountNotFoundException {
        accountDAO.deleteAccount(accountId);
    }
}
