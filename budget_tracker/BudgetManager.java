import java.io.*;
import java.util.*;

public class BudgetManager {
    private List<Transaction> transactions;
    private final String filePath = "transactions.txt";

    public BudgetManager() {
        transactions = new ArrayList<>();
        loadTransactions();
    }

    private void loadTransactions() {
        File file = new File(filePath);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                transactions.add(Transaction.fromFileString(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveTransaction(Transaction t) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(t.toFileString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTransaction(String type, double amount, String description) {
        Transaction t = new Transaction(type, amount, description);
        transactions.add(t);
        saveTransaction(t);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public double getBalance() {
        double balance = 0;
        for (Transaction t : transactions) {
            balance += t.getAmount();
        }
        return balance;
    }
}
