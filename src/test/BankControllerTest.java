package test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import exceptions.AccountNotFoundException;
import model.Bank;
import model.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import controller.BankController;
import service.AccountService;
import service.BankService;
import service.TransactionService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

public class BankControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private BankService bankService;

    @InjectMocks
    private BankController bankController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper method to simulate user input for testing
    private void setInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }

    // Test for viewDailyTransactionSummary
    @Test
    public void testViewDailyTransactionSummary_WithValidDateAndTransactions() throws Exception {
        // Simulate the date input
        setInput("2024-11-05\n");
        
        // Expected summary list
        List<String> summary = List.of("Deposit: Count = 2, Total Amount = 500.00", "Withdrawal: Count = 1, Total Amount = 200.00");

        // Mocking the behavior of transactionService to return summary
        when(transactionService.getDailyTransactionSummary(any(Date.class))).thenReturn(summary);

        // Run the method without parameters
        assertDoesNotThrow(() -> bankController.viewDailyTransactionSummary());
        verify(transactionService).getDailyTransactionSummary(any(Date.class));
    }

    @Test
    public void testViewDailyTransactionSummary_WithNoTransactions() throws Exception {
        // Simulate the date input
        setInput("2024-11-05\n");

        // Mocking the behavior of transactionService to return an empty list
        when(transactionService.getDailyTransactionSummary(any(Date.class))).thenReturn(Collections.emptyList());

        // Run the method without parameters
        assertDoesNotThrow(() -> bankController.viewDailyTransactionSummary());
        verify(transactionService).getDailyTransactionSummary(any(Date.class));
    }

    // Additional tests for createAccount, deposit, etc., would remain the same as previously provided.
}
