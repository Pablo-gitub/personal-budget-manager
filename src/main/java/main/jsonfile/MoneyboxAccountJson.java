package main.jsonfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MoneyboxAccountJson {

    private String name;
    private List<TransactionJson> transactions;
    private Map<String, Double> totalAmount;

    public void addTransaction(final TransactionJson transaction) {
        this.transactions.add(transaction);
        this.setAmount();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<TransactionJson> getTransactions() {
        return transactions;
    }

    public void setTransactions(final List<TransactionJson> transactions) {
        this.transactions = transactions;
    }

    public Map<String, Double> getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(final Map<String, Double> totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setAmount() {
        final Map<String, Double> amount = new HashMap<>();
        for (final TransactionJson i : this.transactions) {
            if (amount.get(i.getCurrency()) == null) {
                amount.put(i.getCurrency(), i.getAmount());
            } else {
                amount.put(i.getCurrency(), amount.get(i.getCurrency()) + i.getAmount());
            }
        }
        this.totalAmount = amount;
    }

}
