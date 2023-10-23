package main.jsonfile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public final class AssetJson {

    private String assetName;
    private String assetSymbol;
    private List<TransactionJson> transactions;
    private double amount;

    public void addTransaction(final TransactionJson transaction) {
        this.transactions.add(transaction);
        this.setAmount();
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(final String assetName) {
        this.assetName = assetName;
    }

    public List<TransactionJson> getTransactions() {
        return transactions;
    }

    public void setTransactions(final List<TransactionJson> transactions) {
        this.transactions = transactions;
    }

    public AssetJson() {
        super();
        this.amount = 0;
        this.assetSymbol = "";
    }

    public double getAmount() {
        return amount;
    }

    public AssetJson(final String assetName, final String assetSymbol, final List<TransactionJson> transactions) {
        super();
        this.assetName = assetName;
        this.assetSymbol = assetSymbol;
        this.transactions = transactions;
        this.setAmount();
    }

    public void setAmount() {
        double amount = 0;
        for (final TransactionJson i : this.transactions) {
            amount += i.getAmount();
        }
        this.amount = amount;
    }

    public String getAssetSymbol() {
        return assetSymbol;
    }

    public void setAssetSymbol(final String assetSymbol) {
        this.assetSymbol = assetSymbol;
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
