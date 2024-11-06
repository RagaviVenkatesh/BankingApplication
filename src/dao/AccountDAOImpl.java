package dao;

import model.Account;
import model.Bank;
import model.SavingsAccount;
import model.CurrentAccount;
import utility.DBConnection;
import exceptions.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAOImpl implements AccountDAO {
    
    @Override
    public void createAccount(Account account) throws SQLException, InvalidAccountTypeException {
        String sql = "INSERT INTO Account (customer_id, bank_id, account_type, balance) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, account.getCustomerId());
            stmt.setInt(2, account.getBank().getBankId());
            stmt.setString(3, account.getAccountType());
            stmt.setDouble(4, account.getBalance());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new InvalidAccountTypeException("Account type not recognized: " + account.getAccountType());
            }
        }
    }

    @Override
    public Account getAccountById(int accountId) throws SQLException, AccountNotFoundException {
        String query = "SELECT a.account_id, a.customer_id, a.account_type, a.balance, a.bank_id, b.bank_name, b.bank_branch " +
                       "FROM Account a " +
                       "JOIN Bank b ON a.bank_id = b.bank_id " +
                       "WHERE a.account_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();

            // Check if a result is found
            if (rs.next()) {
                Bank bank = new Bank(rs.getInt("bank_id"), rs.getString("bank_name"), rs.getString("bank_branch"));
                String accountType = rs.getString("account_type");
                double balance = rs.getDouble("balance");

                if ("Savings".equalsIgnoreCase(accountType)) {
                    return new SavingsAccount(accountId, rs.getInt("customer_id"), bank, accountType, balance, 0.0); // Add interest rate if applicable
                } else if ("Current".equalsIgnoreCase(accountType)) {
                    return new CurrentAccount(accountId, rs.getInt("customer_id"), bank, accountType, balance, 0.0); // Add overdraft limit if applicable
                } else {
                    throw new AccountNotFoundException("Unknown account type for Account ID: " + accountId);
                }
            } else {
                // If no account is found, throw an exception
                throw new AccountNotFoundException("Account not found with ID: " + accountId);
            }
        }
    }


    @Override
    public List<Account> getAccountsByBankId(int bankId) throws SQLException, BankingException {
        String query = "SELECT * FROM AccountBankView WHERE bank_id = ?";
        List<Account> accounts = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bankId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Bank bank = new Bank(rs.getInt("bank_id"), rs.getString("bank_name"), rs.getString("bank_branch"));
                String accountType = rs.getString("account_type");
                double balance = rs.getDouble("balance");
                
                if ("Savings".equalsIgnoreCase(accountType)) {
                    double interestRate = rs.getDouble("interest_rate");
                    accounts.add(new SavingsAccount(rs.getInt("account_id"), rs.getInt("customer_id"), bank, accountType, balance, interestRate));
                } else if ("Current".equalsIgnoreCase(accountType)) {
                    double overdraftLimit = rs.getDouble("overdraft_limit");
                    accounts.add(new CurrentAccount(rs.getInt("account_id"), rs.getInt("customer_id"), bank, accountType, balance, overdraftLimit));
                }
            }
        } catch (SQLException e) {
            throw new BankingException("Error retrieving accounts for bank ID: " + bankId);
        }
        return accounts;
    }
    
    @Override
    public void updateAccount(Account account) throws SQLException, AccountNotFoundException {
        String sql = "UPDATE Account SET customer_id = ?, bank_id = ?, account_type = ?, balance = ? WHERE account_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, account.getCustomerId());
            stmt.setInt(2, account.getBank().getBankId());
            stmt.setString(3, account.getAccountType());
            stmt.setDouble(4, account.getBalance());
            stmt.setInt(5, account.getAccountId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new AccountNotFoundException("No account found with ID: " + account.getAccountId());
            }
        }
    }

    @Override
    public void deleteAccount(int accountId) throws SQLException, AccountNotFoundException {
        String sql = "DELETE FROM Account WHERE account_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new AccountNotFoundException("No account found with ID: " + accountId);
            }
        }
    }

}
