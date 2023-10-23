package main.jsonfile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        // Calculate total amount of previous transactions
        double previousTransactionsTotal = 0;
        for (final TransactionJson previousTransaction : this.transactions) {
            try {
                final Date previousDateTime = dateFormat.parse(previousTransaction.getDate() + " " + previousTransaction.getTime());
                if (previousDateTime.before(transactionDateTime)) {
                    previousTransactionsTotal += previousTransaction.getAmount();
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }

        // Check if the total of previous transactions is higher than the input transaction's amount
        return previousTransactionsTotal > Math.abs(transaction.getAmount());
    }

}
