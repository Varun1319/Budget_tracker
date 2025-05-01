import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    private String type;
    private double amount;
    private String description;
    private String date;

    public Transaction(String type, double amount, String description) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public String toString() {
        return date + " | " + type.toUpperCase() + " | $" + amount + " | " + description;
    }

    public String toFileString() {
        return type + "," + amount + "," + description + "," + date;
    }

    public static Transaction fromFileString(String line) {
        String[] parts = line.split(",", 4);
        Transaction t = new Transaction(parts[0], Double.parseDouble(parts[1]), parts[2]);
        t.date = parts[3];
        return t;
    }

    public double getAmount() {
        return type.equals("income") ? amount : -amount;
    }
}
