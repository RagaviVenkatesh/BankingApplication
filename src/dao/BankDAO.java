package dao;

import model.Bank;
import exceptions.BankingException;
import java.sql.SQLException;
import java.util.List;

public interface BankDAO {
    void createBank(Bank bank) throws SQLException, BankingException;
    Bank getBankById(int bankId) throws SQLException, BankingException;
    List<Bank> getAllBanks() throws SQLException, BankingException;
    void updateBank(Bank bank) throws SQLException, BankingException;
    void deleteBank(int bankId) throws SQLException, BankingException;
	double getTotalBalanceForBank(int bankId) throws SQLException, BankingException;
}
