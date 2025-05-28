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

    public double getAmount() {
        return type.equalsIgnoreCase("income") ? amount : -amount;
    }

    public String getType() { return type; }
    public double getRawAmount() { return amount; }
    public String getDescription() { return description; }
    public String getDate() { return date; }

    public void setDate(String date) {
        this.date = date;
    }
}