package dao;

import model.Bank;
import utility.DBConnection;
import exceptions.BankingException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankDAOImpl implements BankDAO {

    @Override
    public void createBank(Bank bank) throws SQLException, BankingException {
        String sql = "INSERT INTO Bank (bank_name, bank_branch) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bank.getBankName());
            stmt.setString(2, bank.getBankBranch());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BankingException("Error creating bank: " + e.getMessage());
        }
    }

    @Override
    public Bank getBankById(int bankId) throws SQLException, BankingException {
        String query = "SELECT * FROM Bank WHERE bank_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bankId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Bank(bankId, rs.getString("bank_name"), rs.getString("bank_branch"));
            }
            throw new BankingException("Bank not found for ID: " + bankId);
        }
    }

    @Override
    public List<Bank> getAllBanks() throws SQLException, BankingException {
        List<Bank> banks = new ArrayList<>();
        String query = "SELECT * FROM Bank";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                banks.add(new Bank(rs.getInt("bank_id"), rs.getString("bank_name"), rs.getString("bank_branch")));
            }
        } catch (SQLException e) {
            throw new BankingException("Error retrieving banks: " + e.getMessage());
        }
        return banks;
    }

    @Override
    public void updateBank(Bank bank) throws SQLException, BankingException {
        String sql = "UPDATE Bank SET bank_name = ?, bank_branch = ? WHERE bank_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bank.getBankName());
            stmt.setString(2, bank.getBankBranch());
            stmt.setInt(3, bank.getBankId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new BankingException("No bank found with ID: " + bank.getBankId());
            }
        }
    }

    @Override
    public void deleteBank(int bankId) throws SQLException, BankingException {
        String sql = "DELETE FROM Bank WHERE bank_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bankId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new BankingException("No bank found with ID: " + bankId);
            }
        }
    }
    
    @Override
    public double getTotalBalanceForBank(int bankId) throws SQLException, BankingException {
        String query = "SELECT total_balance_for_bank(?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bankId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
            throw new BankingException("Unable to retrieve total balance for bank ID: " + bankId);
        }
    }
}
