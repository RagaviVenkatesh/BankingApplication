package controller;

import service.AccountService;
import service.TransactionService;
import service.BankService;
import utility.TransactionHistoryUtil;
import exceptions.*;

import model.Account;
import model.SavingsAccount;
import model.CurrentAccount;
import model.Bank;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class BankController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final BankService bankService;
    private final Scanner scanner;

    public BankController() {
        this.accountService = new AccountService();
        this.transactionService = new TransactionService();
        this.bankService = new BankService();
        this.scanner = new Scanner(System.in);
    }

    public void start() throws AccountNotFoundException, InvalidTransactionAmountException, BankingException, InvalidAccountTypeException, ParseException {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        deposit();
                        break;
                    case 3:
                        withdraw();
                        break;
                    case 4:
                        transfer();
                        break;
                    case 5:
                        viewCustomerDetails();
                        break;
                    case 6:
                        viewTransactionHistory();
                        break;
                    case 7:
                        viewTotalBalance();
                        break;
                    case 8:
                        viewAccountCountByType();
                        break;
                    case 9:
                        viewDailyTransactionSummary();
                        break;
                    case 10:
                        viewFullTransactionHistory(); // New option to view transaction history from file
                        break;
                    case 0:
                        running = false;
                        transactionService.shutdownExecutorService();
                        System.out.println("Exiting the application. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (SQLException | InsufficientFundsException | InterruptedException | ExecutionException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n--- Banking Application ---");
        System.out.println("1. Create Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. View Customer Details");
        System.out.println("6. View Transaction History for Account");
        System.out.println("7. View Total Balance of All Accounts");
        System.out.println("8. View Account Count by Type");
        System.out.println("9. View Daily Transaction Summary");
        System.out.println("10. View Full Transaction History from File"); // New option
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    public void createAccount() throws SQLException, BankingException, InvalidAccountTypeException {
        System.out.print("Enter Customer ID: ");
        int customerId = scanner.nextInt();
        System.out.print("Enter Bank ID: ");
        int bankId = scanner.nextInt();
        Bank bank = bankService.getBankById(bankId);
        System.out.print("Enter Account Type (Savings/Current): ");
        String accountType = scanner.next();
        System.out.print("Enter Initial Balance: ");
        double balance = scanner.nextDouble();

        if ("Savings".equalsIgnoreCase(accountType)) {
            System.out.print("Enter Interest Rate: ");
            double interestRate = scanner.nextDouble();
            accountService.createAccount(new SavingsAccount(0, customerId, bank, accountType, balance, interestRate));
        } else if ("Current".equalsIgnoreCase(accountType)) {
            System.out.print("Enter Overdraft Limit: ");
            double overdraftLimit = scanner.nextDouble();
            accountService.createAccount(new CurrentAccount(0, customerId, bank, accountType, balance, overdraftLimit));
        } else {
            System.out.println("Invalid account type.");
        }
    }

    public void deposit() throws SQLException, ExecutionException, InterruptedException, InvalidTransactionAmountException {
        System.out.print("Enter Account ID: ");
        int accountId = scanner.nextInt();
        System.out.print("Enter Deposit Amount: ");
        double amount = scanner.nextDouble();

        Future<?> future = transactionService.deposit(accountId, amount);
        future.get(); // Wait for the deposit operation to complete
    }

    private void withdraw() throws SQLException, InsufficientFundsException, ExecutionException, InterruptedException, InvalidTransactionAmountException {
        System.out.print("Enter Account ID: ");
        int accountId = scanner.nextInt();
        System.out.print("Enter Withdrawal Amount: ");
        double amount = scanner.nextDouble();

        Future<?> future = transactionService.withdraw(accountId, amount);
        future.get(); // Wait for the withdrawal operation to complete
    }

    private void transfer() throws SQLException, InsufficientFundsException, ExecutionException, InterruptedException, InvalidTransactionAmountException {
        System.out.print("Enter Source Account ID: ");
        int fromAccountId = scanner.nextInt();
        System.out.print("Enter Destination Account ID: ");
        int toAccountId = scanner.nextInt();
        System.out.print("Enter Transfer Amount: ");
        double amount = scanner.nextDouble();

        Future<?> future = transactionService.transfer(fromAccountId, toAccountId, amount);
        future.get(); // Wait for the transfer operation to complete
    }

    private void viewCustomerDetails() throws SQLException, AccountNotFoundException, InvalidAccountTypeException, BankingException {
        System.out.print("Enter Account ID: ");
        int accountId = scanner.nextInt();
        Account account = accountService.getAccountById(accountId);
        System.out.println(account);
    }

    private void viewTransactionHistory() throws SQLException {
        System.out.print("Enter Account ID: ");
        int accountId = scanner.nextInt();
        List<String> history = transactionService.getTransactionHistoryFromFile();
        if (history.isEmpty()) {
            System.out.println("No transaction history found.");
        } else {
            history.forEach(System.out::println);
        }
    }

    private void viewTotalBalance() throws SQLException, BankingException {
        System.out.print("Enter Bank ID: ");
        int bankId = scanner.nextInt();
        double totalBalance = bankService.getTotalBalanceForBank(bankId);
        System.out.println("Total Balance for Bank ID " + bankId + ": " + totalBalance);
    }

    private void viewAccountCountByType() throws SQLException, InvalidAccountTypeException, BankingException {
        System.out.print("Enter Bank ID: ");
        int bankId = scanner.nextInt();
        List<Account> accounts = accountService.getAccountsByBankId(bankId);
        System.out.println("Total accounts for Bank ID " + bankId + ": " + accounts.size());
    }

    public void viewDailyTransactionSummary() throws SQLException, ParseException, BankingException {
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String dateInput = scanner.next();
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateInput); // Convert String to Date
        try {
            List<String> summary = transactionService.getDailyTransactionSummary(date);
            if (summary.isEmpty()) {
                System.out.println("No transactions found for this date.");
            } else {
                summary.forEach(System.out::println);
            }
        } catch (SQLException | BankingException e) {
            System.err.println("Error retrieving daily transaction summary: " + e.getMessage());
        }
    }

    private void viewFullTransactionHistory() {
        System.out.println("\n--- Full Transaction History ---");
        List<String> transactionHistory = TransactionHistoryUtil.retrieveTransactionHistory();
        if (transactionHistory.isEmpty()) {
            System.out.println("No transaction history found.");
        } else {
            for (String record : transactionHistory) {
                System.out.println(record);
            }
        }
    }
}
