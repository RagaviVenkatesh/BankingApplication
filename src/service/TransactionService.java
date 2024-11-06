package service;

import dao.TransactionDAO;
import dao.TransactionDAOImpl;
import exceptions.*;
import utility.TransactionHistoryUtil;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TransactionService {
    private final TransactionDAO transactionDAO;
    private final ExecutorService executorService;

    public TransactionService() {
        this.transactionDAO = new TransactionDAOImpl();
        this.executorService = Executors.newFixedThreadPool(5); // Adjust pool size as needed
    }

    public Future<?> deposit(int accountId, double amount) throws InvalidTransactionAmountException {
        return executorService.submit(() -> {
            try {
                transactionDAO.deposit(accountId, amount);
                TransactionHistoryUtil.saveTransaction("Deposit", accountId, amount); // Log to file
            } catch (SQLException | TransactionFailedException | InvalidTransactionAmountException e) {
                System.err.println("Error during deposit: " + e.getMessage());
            }
        });
    }

    public Future<?> withdraw(int accountId, double amount) throws InvalidTransactionAmountException, InsufficientFundsException {
        return executorService.submit(() -> {
            try {
                transactionDAO.withdraw(accountId, amount);
                TransactionHistoryUtil.saveTransaction("Withdraw", accountId, amount); // Log to file
            } catch (SQLException | InsufficientFundsException | TransactionFailedException | InvalidTransactionAmountException e) {
                System.err.println("Error during withdrawal: " + e.getMessage());
            }
        });
    }

    public Future<?> transfer(int fromAccountId, int toAccountId, double amount) throws InvalidTransactionAmountException, InsufficientFundsException {
        return executorService.submit(() -> {
            try {
                transactionDAO.transferFunds(fromAccountId, toAccountId, amount);
                TransactionHistoryUtil.saveTransaction("Transfer Out", fromAccountId, amount); // Log fromAccount transaction
                TransactionHistoryUtil.saveTransaction("Transfer In", toAccountId, amount); // Log toAccount transaction
            } catch (SQLException | InsufficientFundsException | TransactionFailedException | InvalidTransactionAmountException e) {
                System.err.println("Error during transfer: " + e.getMessage());
            }
        });
    }

    public void shutdownExecutorService() {
        executorService.shutdown();
    }

    // Method to retrieve all transaction history from file
    public List<String> getTransactionHistoryFromFile() {
        return TransactionHistoryUtil.retrieveTransactionHistory();
    }

    // Method to retrieve daily transaction summary for a specific date
    public List<String> getDailyTransactionSummary(Date date) throws SQLException, BankingException {
        // Call the instance method on transactionDAO instead of TransactionDAO class
        return transactionDAO.getDailyTransactionSummary(date);
    }
}
