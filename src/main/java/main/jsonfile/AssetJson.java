package main.jsonfile;

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
}
