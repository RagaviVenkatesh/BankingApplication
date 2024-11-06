import java.text.ParseException;

import controller.BankController;
import exceptions.AccountNotFoundException;
import exceptions.BankingException;
import exceptions.InvalidAccountTypeException;
import exceptions.InvalidTransactionAmountException;

public class Main {
    public static void main(String[] args) throws InvalidTransactionAmountException, AccountNotFoundException, BankingException, InvalidAccountTypeException, ParseException {
        BankController controller = new BankController();
        controller.start();
    }
}
