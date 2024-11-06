package dao;

import model.Account;
import exceptions.AccountNotFoundException;
import exceptions.BankingException;
import exceptions.InvalidAccountTypeException;

import java.sql.SQLException;
import java.util.List;

public interface AccountDAO {
    void createAccount(Account account) throws SQLException, BankingException, InvalidAccountTypeException;
    Account getAccountById(int accountId) throws SQLException, BankingException, AccountNotFoundException, InvalidAccountTypeException;
    List<Account> getAccountsByBankId(int bankId) throws SQLException, BankingException, InvalidAccountTypeException;
	void deleteAccount(int accountId) throws SQLException, AccountNotFoundException;
	void updateAccount(Account account) throws SQLException, AccountNotFoundException;
}
