package main.jsonfile;

public class TransactionJson {

    private String nameTransaction;
    private String date;
    private String time;
    private String currency;
    private double amount;

    public TransactionJson(final String nameTransaction, final String date, final String time, final double amount) {
        this.setAmount(amount);
        this.setNameTransaction(nameTransaction);
        this.setDate(date);
        this.setTime(time);
        this.setCurrency("euro");
    }

    public TransactionJson(final String nameTransaction, final String date, final String time, final double amount, final String currency) {
        this.setAmount(amount);
        this.setNameTransaction(nameTransaction);
        this.setDate(date);
        this.setTime(time);
        this.setCurrency(currency);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNameTransaction() {
        return nameTransaction;
    }

    public void setNameTransaction(String nameTransaction) {
        this.nameTransaction = nameTransaction;
    }
    

}
