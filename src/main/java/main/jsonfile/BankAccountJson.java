package main.jsonfile;

import java.util.List;

public final class BankAccountJson {

    private String name;
    private List<TransactionJson> transactions;
    private double totalAmount;

    public void addTransaction(final TransactionJson transaction) {
        this.transactions.add(transaction);
        this.setTotalAmount();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount() {
        double totalAmount = 0;
        for (final TransactionJson i : this.transactions) {
            totalAmount += i.getAmount();
        }
        this.totalAmount = totalAmount;
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

}
