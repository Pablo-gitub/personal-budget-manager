package main.jsonfile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    
    public boolean validTransaction(final TransactionJson transaction) {
        // If the transaction amount is positive, it is always valid
        if (transaction.getAmount() >= 0) {
            return true;
        }

        // Convert transaction date and time strings to Date objects
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date transactionDateTime;
        try {
            transactionDateTime = dateFormat.parse(transaction.getDate() + " " + transaction.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        // Calculate total amount of previous transactions with the same currency
        double previousTransactionsTotal = 0;
        for (final TransactionJson previousTransaction : this.transactions) {
            try {
                final Date previousDateTime = dateFormat.parse(previousTransaction.getDate() + " " + previousTransaction.getTime());
                if (previousDateTime.before(transactionDateTime) && previousTransaction.getCurrency().equals(transaction.getCurrency())) {
                    previousTransactionsTotal += previousTransaction.getAmount();
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }

        // Check if the total of previous transactions with the same currency is higher than the input transaction's amount
        return previousTransactionsTotal > Math.abs(transaction.getAmount());
    }

}
