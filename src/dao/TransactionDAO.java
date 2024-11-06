package dao;

import exceptions.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface TransactionDAO {
    void deposit(int accountId, double amount) throws SQLException, InvalidTransactionAmountException, TransactionFailedException;
    void withdraw(int accountId, double amount) throws SQLException, InsufficientFundsException, InvalidTransactionAmountException, TransactionFailedException;
    void transferFunds(int fromAccount, int toAccount, double amount) throws SQLException, InsufficientFundsException, InvalidTransactionAmountException, TransactionFailedException;
	List<String> getDailyTransactionSummary(Date date) throws BankingException;
	
}
