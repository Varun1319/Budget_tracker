import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.util.List;

public class BudgetTrackerGUI extends JFrame {
    private BudgetManager manager;
    private JLabel balanceLabel;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JTextField amountField, descriptionField;

    public BudgetTrackerGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Native look
        } catch (Exception ignored) {}

        manager = new BudgetManager();

        setTitle("Budget Tracker");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top: Balance Display
        balanceLabel = new JLabel();
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 22));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        balanceLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(balanceLabel, BorderLayout.NORTH);

        // Center: Transaction Table
        tableModel = new DefaultTableModel(new String[]{"Date", "Type", "Amount", "Description"}, 0);
        transactionTable = new JTable(tableModel);
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom: Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 1, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel fieldsPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        amountField = new JTextField();
        descriptionField = new JTextField();
        fieldsPanel.add(new JLabel("Amount:"));
        fieldsPanel.add(amountField);
        fieldsPanel.add(new JLabel("Description:"));
        fieldsPanel.add(descriptionField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JButton addIncomeBtn = new JButton("Add Income");
        JButton addExpenseBtn = new JButton("Add Expense");
        buttonPanel.add(addIncomeBtn);
        buttonPanel.add(addExpenseBtn);

        inputPanel.add(fieldsPanel);
        inputPanel.add(buttonPanel);
        add(inputPanel, BorderLayout.SOUTH);

        // Action Listeners
        addIncomeBtn.addActionListener(e -> handleAdd("income"));
        addExpenseBtn.addActionListener(e -> handleAdd("expense"));

        updateUI();
        setVisible(true);
    }

    private void handleAdd(String type) {
        String amountText = amountField.getText().trim();
        String desc = descriptionField.getText().trim();

        if (amountText.isEmpty() || desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in both fields.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) throw new NumberFormatException();

            manager.addTransaction(type, amount, desc);
            amountField.setText("");
            descriptionField.setText("");
            updateUI();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid positive number for amount.");
        }
    }

    private void updateUI() {
        // Update Balance
        double balance = manager.getBalance();
        balanceLabel.setText(String.format("Current Balance: $%.2f", balance));
        balanceLabel.setForeground(balance >= 0 ? new Color(0, 128, 0) : Color.RED);

        // Update Table
        tableModel.setRowCount(0);
        List<Transaction> transactions = manager.getTransactions();
        for (Transaction t : transactions) {
            tableModel.addRow(new Object[]{
                    t.toString().split(" \\| ")[0],  // Date
                    t.toString().split(" \\| ")[1],  // Type
                    t.toString().split(" \\| ")[2],  // Amount
                    t.toString().split(" \\| ")[3]   // Description
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BudgetTrackerGUI::new);
    }
}
