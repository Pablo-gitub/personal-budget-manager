package main.jsonfile;

import java.io.FileReader;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public final class User {
    private String name;
    private String lastname;
    private String email;
    private String username;
    private String password;
    private List<BankAccountJson> banks;
    private List<MoneyboxAccountJson> moneyboxes;
    private List<InvestmentAccountJson> investmentAccounts;
    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(final String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(final String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(final String password) {
        this.password = password;
    }
    public List<BankAccountJson> getBanks() {
        return banks;
    }
    public void setBanks(final List<BankAccountJson> banks) {
        this.banks = banks;
    }
    public List<MoneyboxAccountJson> getMoneyboxes() {
        return moneyboxes;
    }
    public void setMoneyboxes(final List<MoneyboxAccountJson> moneyboxes) {
        this.moneyboxes = moneyboxes;
    }
    public List<InvestmentAccountJson> getInvestmentAccounts() {
        return investmentAccounts;
    }
    public void setInvestmentAccounts(final List<InvestmentAccountJson> investmentAccounts) {
        this.investmentAccounts = investmentAccounts;
    }
    public User(final String name, final String lastname, final String email, final String username, 
            final String password, final List<BankAccountJson> banks, final List<MoneyboxAccountJson> moneyboxes,
            final List<InvestmentAccountJson> investmentAccounts) {
        super();
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.banks = banks;
        this.moneyboxes = moneyboxes;
        this.investmentAccounts = investmentAccounts;
    }

}
