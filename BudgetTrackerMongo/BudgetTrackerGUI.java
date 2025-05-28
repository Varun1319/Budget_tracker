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
    setTitle("Budget Tracker (MongoDB Atlas)");
    setSize(700, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // UI shell is visible early
    balanceLabel = new JLabel("Connecting to database...");
    balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
    balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
    add(balanceLabel, BorderLayout.NORTH);
    setVisible(true);

    // Background MongoDB connection
    new Thread(() -> {
        try {
            manager = new BudgetManager(); // connect to MongoDB
            SwingUtilities.invokeLater(this::setupUI); // GUI logic safely invoked
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this,
                        "Error connecting to MongoDB:\n" + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            });
        }
    }).start();
}
private void setupUI() {
    // transaction table setup
    tableModel = new DefaultTableModel(new String[]{"Date", "Type", "Amount", "Description"}, 0);
    transactionTable = new JTable(tableModel);
    transactionTable.setEnabled(false);
    add(new JScrollPane(transactionTable), BorderLayout.CENTER);

    // input panel setup
    JPanel inputPanel = new JPanel(new GridLayout(2, 1, 10, 10));
    JPanel form = new JPanel(new GridLayout(1, 4, 10, 10));
    amountField = new JTextField();
    descriptionField = new JTextField();
    form.add(new JLabel("Amount:"));
    form.add(amountField);
    form.add(new JLabel("Description:"));
    form.add(descriptionField);

    JPanel buttons = new JPanel(new GridLayout(1, 2, 10, 10));
    JButton incomeBtn = new JButton("Add Income");
    JButton expenseBtn = new JButton("Add Expense");
    buttons.add(incomeBtn);
    buttons.add(expenseBtn);

    inputPanel.add(form);
    inputPanel.add(buttons);
    add(inputPanel, BorderLayout.SOUTH);

    incomeBtn.addActionListener(e -> handleAdd("income"));
    expenseBtn.addActionListener(e -> handleAdd("expense"));

    updateUI();
}


    private void handleAdd(String type) {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String description = descriptionField.getText().trim();
            if (amount <= 0 || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter valid values.");
                return;
            }
            manager.addTransaction(type, amount, description);
            amountField.setText("");
            descriptionField.setText("");
            updateUI();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Amount must be a number.");
        }
    }

    private void updateUI() {
        List<Transaction> transactions = manager.getTransactions();
        tableModel.setRowCount(0);
        for (Transaction t : transactions) {
            tableModel.addRow(new Object[]{
                    t.getDate(), t.getType(), t.getRawAmount(), t.getDescription()
            });
        }
        balanceLabel.setText(String.format("Current Balance: $%.2f", manager.getBalance()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BudgetTrackerGUI::new);
    }
}