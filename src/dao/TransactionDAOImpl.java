package dao;

import utility.DBConnection;
import exceptions.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public synchronized void deposit(int accountId, double amount) throws SQLException, InvalidTransactionAmountException, TransactionFailedException {
        if (amount <= 0) {
            throw new InvalidTransactionAmountException("Deposit amount must be positive.");
        }

        try (Connection conn = DBConnection.getConnection()) {
            CallableStatement stmt = conn.prepareCall("{CALL deposit_procedure(?, ?)}");
            stmt.setInt(1, accountId);
            stmt.setDouble(2, amount);
            stmt.execute();
        } catch (SQLException e) {
            throw new TransactionFailedException("Deposit failed: " + e.getMessage());
        }
    }

    @Override
    public synchronized void withdraw(int accountId, double amount) throws SQLException, InsufficientFundsException, InvalidTransactionAmountException, TransactionFailedException {
        if (amount <= 0) {
            throw new InvalidTransactionAmountException("Withdrawal amount must be positive.");
        }

        try (Connection conn = DBConnection.getConnection()) {
            CallableStatement stmt = conn.prepareCall("{CALL withdraw_procedure(?, ?)}");
            stmt.setInt(1, accountId);
            stmt.setDouble(2, amount);
            stmt.execute();
        } catch (SQLException e) {
            if (e.getSQLState().equals("45000")) { // Custom error code for insufficient funds
                throw new InsufficientFundsException("Insufficient funds for withdrawal.");
            }
            throw new TransactionFailedException("Withdrawal failed: " + e.getMessage());
        }
    }

    @Override
    public synchronized void transferFunds(int fromAccount, int toAccount, double amount) throws SQLException, InsufficientFundsException, InvalidTransactionAmountException, TransactionFailedException {
        if (amount <= 0) {
            throw new InvalidTransactionAmountException("Transfer amount must be positive.");
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try {
                withdraw(fromAccount, amount); // Withdraw from source account
                deposit(toAccount, amount);    // Deposit into target account
                conn.commit(); // Commit transaction
            } catch (SQLException | InsufficientFundsException | InvalidTransactionAmountException e) {
                conn.rollback(); // Rollback on failure
                throw new TransactionFailedException("Transfer failed: " + e.getMessage());
            }
        }
    }
    
    public List<String> getDailyTransactionSummary(Date date) throws BankingException {
        List<String> summary = new ArrayList<>();
        String query = "SELECT transaction_type, COUNT(*) AS count, SUM(amount) AS total_amount " +
                       "FROM Transaction WHERE DATE(transaction_date) = ? " +
                       "GROUP BY transaction_type";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String type = rs.getString("transaction_type");
                int count = rs.getInt("count");
                double totalAmount = rs.getDouble("total_amount");
                summary.add(String.format("%s: Count = %d, Total Amount = %.2f", type, count, totalAmount));
            }
        } catch (SQLException e) {
            throw new BankingException("Error retrieving daily transaction summary: " + e.getMessage());
        }

        // Ensure that an empty list is returned if no transactions are found
        return summary;
    }

}
