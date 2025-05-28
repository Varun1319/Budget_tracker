import com.mongodb.client.*;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class BudgetManager {
    private final MongoClient mongoClient;
    private final MongoCollection<Document> collection;

    public BudgetManager() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("budgetDB");
        collection = database.getCollection("transactions");
    }

    public void addTransaction(String type, double amount, String description) {
        Document doc = new Document("type", type)
                .append("amount", amount)
                .append("description", description)
                .append("date", java.time.LocalDateTime.now().toString());
        collection.insertOne(doc);
    }

    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        FindIterable<Document> docs = collection.find().sort(Sorts.descending("date"));
        for (Document doc : docs) {
            Transaction t = new Transaction(
                    doc.getString("type"),
                    doc.getDouble("amount"),
                    doc.getString("description")
            );
            t.setDate(doc.getString("date"));
            transactions.add(t);
        }
        return transactions;
    }

    public double getBalance() {
        double balance = 0;
        for (Transaction t : getTransactions()) {
            balance += t.getAmount();
        }
        return balance;
    }
}
