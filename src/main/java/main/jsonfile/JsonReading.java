package main.jsonfile;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonReading extends JsonControl{

    protected static final String INVESTMENT_ACCOUNTS_KEY = "InvestimentAccounts";
    protected static final String TRANSACTIONS = "transactions";
    protected static final String NAME_ASSET = "nameAsset";
    protected static final String EURO = "Euro";
    protected static final String[] TRANSACTION_DATA = {"nameTransaction", "date", "time", "amount", "currency"};
    protected static final String[] USER_DATA = {"name", "lastName", "email"};
    
    /**
     * this method read all the transaction of a specific asset in a specific investment account 
     * in the file json and put them in a array of TranssactionJson
     * 
     * @param username the unique identifier of the user 
     * @param nameInvestmentAccount the name of the investment account we want to read the transactions
     * @param symbolAsset the symbol asset we want to read all the transactions for example "ETH" "BTC" "AAPL"
     * @return transactions list related to the symbolAsset into the investment account
     * 
     * */
    public List<TransactionJson> readAssetTransactions(final String username, 
            final String nameInvestmentAccount, final String symbolAsset) {
        final List<TransactionJson> transactions = new ArrayList<>();
        try {
            final JSONArray users = readJsonFile();
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String userName = (String) person.get(USERNAME_KEY);
                if (userName.equals(username)) {
                    final JSONArray investmentAccounts = (JSONArray) person.get(INVESTMENT_ACCOUNTS_KEY);
                    for (final Object account : investmentAccounts) {
                        final JSONObject investmentAccount = (JSONObject) account;
                        final String accountName = (String) investmentAccount.get(ACCOUNT_NAMES[2]);
                        if (accountName.equals(nameInvestmentAccount)) {
                            final JSONArray assets = (JSONArray) investmentAccount.get(ASSETS);
                            for (final Object asset : assets) {
                                final JSONObject assetObject = (JSONObject) asset;
                                final String assetSymbol = (String) assetObject.get(SYMBOL_ASSET);
                                if (assetSymbol.equals(symbolAsset)) {
                                    final JSONArray assetTransactions = (JSONArray) assetObject.get(TRANSACTIONS);
                                    for (final Object transaction : assetTransactions) {
                                        final JSONObject transactionObject = (JSONObject) transaction;
                                        transactions.add(parseTransaction(transactionObject, assetSymbol));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            handleException("Error reading asset transactions", ex);
        }
        return transactions;
    }

    // Helper method to parse transaction JSON object into TransactionJson instance
    private static TransactionJson parseTransaction(final JSONObject transactionObject, final String currency) {
        return new TransactionJson(
                (String) transactionObject.get(TRANSACTION_DATA[0]),
                (String) transactionObject.get(TRANSACTION_DATA[1]),
                (String) transactionObject.get(TRANSACTION_DATA[2]),
                ((Number) transactionObject.get(TRANSACTION_DATA[3])).doubleValue(),
                currency
        );
    }
    
    private static TransactionJson parseTransaction(final JSONObject transactionObject) {
        return new TransactionJson(
                (String) transactionObject.get(TRANSACTION_DATA[0]),
                (String) transactionObject.get(TRANSACTION_DATA[1]),
                (String) transactionObject.get(TRANSACTION_DATA[2]),
                ((Number) transactionObject.get(TRANSACTION_DATA[3])).doubleValue(),
                (String) transactionObject.get(TRANSACTION_DATA[4])
        );
    }
    
    

    /**
     * this method read all the transaction of every asset in a specific investment account 
     * in the file json and put them in a matrix TranssactionJson where each line is a asset with it's symbol
     * 
     * @param username the unique identifier of the user 
     * @param nameInvestmentAccount the name of the investment account we want to read the transactions
     * @param symbolAsset the symbol asset we want to read all the transactions for example "ETH" "BTC" "AAPL"
     * 
     * 
     * */

    public static List<AssetJson> readAssetsTransactions(final String username, final String nameInvestmentAccount) {
        final List<AssetJson> assetsTransactions = new ArrayList<>();
        try {
            final JSONArray users = readJsonFile();
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String userName = (String) person.get(USERNAME_KEY);
                if (userName.equals(username)) {
                    final JSONArray investmentAccounts = (JSONArray) person.get(INVESTMENT_ACCOUNTS_KEY);
                    for (final Object account : investmentAccounts) {
                        final JSONObject investmentAccount = (JSONObject) account;
                        final String accountName = (String) investmentAccount.get(ACCOUNT_NAMES[2]);
                        if (accountName.equals(nameInvestmentAccount)) {
                            final JSONArray assets = (JSONArray) investmentAccount.get(ASSETS);
                            for (final Object asset : assets) {
                                assetsTransactions.add(parseAssetJson((JSONObject) asset));
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            handleException("Error reading assets transactions", ex);
        }
        return assetsTransactions;
    }
    
    // Helper method to parse asset JSON object into AssetJson instance
    private static AssetJson parseAssetJson(final JSONObject assetObject) {
        final String assetName = (String) assetObject.get(NAME_ASSET);
        final String assetSymbol = (String) assetObject.get(SYMBOL_ASSET);
        final List<TransactionJson> transactions = new ArrayList<>();
        final JSONArray assetTransactions = (JSONArray) assetObject.get(TRANSACTIONS);
        for (final Object transaction : assetTransactions) {
            transactions.add(parseTransaction((JSONObject) transaction, assetSymbol));
        }
        return new AssetJson(assetName, assetSymbol, transactions);
    }

    public final static List<InvestmentAccountJson> readInvestmentAccount(final String username) {
        final List<InvestmentAccountJson> listInvestmentAccount = new ArrayList<>(); 
        final JSONParser parser = new JSONParser();

        try {
            // create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(DB_NAME));
            // read user
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String userName = (String) person.get(USERNAME_KEY);

                if (userName.equals(username)) {
                    final JSONArray investimentAccounts = (JSONArray) person.get(MONEY_ACCOUNTS[2]);

                    for (final Object a : investimentAccounts) {
                        final JSONObject investimentAccount = (JSONObject) a;
                        final String nameInvAcc = (String) investimentAccount.get(ACCOUNT_NAMES[2]);
                        final InvestmentAccountJson account = new InvestmentAccountJson();
                        account.setNameInvestmentAccount(nameInvAcc);
                        account.setAssets(readAssetsTransactions(username, nameInvAcc));
                        listInvestmentAccount.add(account);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listInvestmentAccount;
    }
    
    /**
     * this method read all the transaction of a specific moneyBox in the file json and put them in a 
     * array of TranssactionJson
     * 
     * @param username the unique identifier of the user 
     * @param nameMoneyBox the name of the investment account we want to read the transactions
     * @return read all the transactions related to a nameMoneyBox
     * */


    public static List<TransactionJson> readMoneyBoxTransaction(final String username, final String nameMoneyBox) {
        final List<TransactionJson> transactions = new ArrayList<>();
        try {
            final JSONArray users = readJsonFile();
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String userName = (String) person.get(USERNAME_KEY);
                if (userName.equals(username)) {
                    final JSONArray moneyBoxes = (JSONArray) person.get(MONEY_ACCOUNTS[1]);
                    for (final Object moneyBox : moneyBoxes) {
                        final JSONObject moneyBoxObject = (JSONObject) moneyBox;
                        final String moneyBoxName = (String) moneyBoxObject.get(ACCOUNT_NAMES[1]);
                        if (moneyBoxName.equals(nameMoneyBox)) {
                            final JSONArray moneyBoxTransactions = (JSONArray) moneyBoxObject.get(TRANSACTIONS);
                            for (final Object transaction : moneyBoxTransactions) {
                                transactions.add(parseTransaction((JSONObject) transaction));
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            handleException("Error reading moneyBox transactions", ex);
        }
        return transactions;
    }

    public final static List<MoneyboxAccountJson> readMoneyBoxes(final String username) {
        final List<MoneyboxAccountJson> listMoneyboxes = new ArrayList<>();
        final JSONParser parser = new JSONParser();

        try {
            // create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(DB_NAME));
            // read user
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String userName = (String) person.get(USERNAME_KEY);

                if (userName.equals(username)) {
                    final JSONArray moneyBoxes = (JSONArray) person.get(MONEY_ACCOUNTS[1]);

                    for (final Object a : moneyBoxes) {
                        final JSONObject moneyBox = (JSONObject) a;
                        final String moneyBoxName = (String) moneyBox.get(ACCOUNT_NAMES[1]);
                        final MoneyboxAccountJson account = new MoneyboxAccountJson();
                        account.setName(moneyBoxName);
                        final List<TransactionJson> transactions = readMoneyBoxTransaction(username, moneyBoxName);
                        account.setTransactions(transactions);
                        account.setAmount();
                        listMoneyboxes.add(account);
                    }
                }
            }
        } catch (Exception ex) {
            handleException("Error reading moneyBoxes", ex);
        }
        return listMoneyboxes;
    }

    /**
     * this method read all the transaction of a specific asset in the file json and put them in a 
     * array of TranssactionJson
     * 
     * @param username the unique identifier of the user 
     * @param nameBankAccount the name of the bank account we want to read the transactions
     * @return the transactions related to a nameBankAccount
     * */
    public static List<TransactionJson> readBankTransaction(final String username, final String nameBankAccount) {
        final List<TransactionJson> transactions = new ArrayList<>();
        try {
            final JSONArray users = readJsonFile();
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String userName = (String) person.get(USERNAME_KEY);
                if (userName.equals(username)) {
                    final JSONArray bankAccounts = (JSONArray) person.get(MONEY_ACCOUNTS[0]);
                    for (final Object bankAccount : bankAccounts) {
                        final JSONObject bankAccountObject = (JSONObject) bankAccount;
                        final String bankAccountName = (String) bankAccountObject.get(ACCOUNT_NAMES[0]);
                        if (bankAccountName.equals(nameBankAccount)) {
                            final JSONArray bankAccountTransactions = (JSONArray) bankAccountObject.get(TRANSACTIONS);
                            for (final Object transaction : bankAccountTransactions) {
                                transactions.add(parseTransaction((JSONObject) transaction, EURO));
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            handleException("Error reading bank account transactions", ex);
        }
        return transactions;
    }

    public final static List<BankAccountJson> readBanks(final String username) {
        final List<BankAccountJson> listBanks = new ArrayList<>();
        final JSONParser parser = new JSONParser();

        try {
            // create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(DB_NAME));
            // read user
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String userName = (String) person.get(USERNAME_KEY);

                if (userName.equals(username)) {
                    final JSONArray banckAccounts = (JSONArray) person.get(MONEY_ACCOUNTS[0]);

                    for (final Object a : banckAccounts) {
                        final JSONObject banckAccount = (JSONObject) a;
                        final String bankName = (String) banckAccount.get(ACCOUNT_NAMES[0]);
                        final BankAccountJson account = new BankAccountJson();
                        account.setName(bankName);
                        final List<TransactionJson> transactions = readBankTransaction(username, bankName);
                        account.setTransactions(transactions);
                        account.setTotalAmount();
                        listBanks.add(account);
                    }
                }
            }
        } catch (Exception ex) {
            handleException("Error reading banks", ex);
        }
        return listBanks;
    }
    
    /**
     * this method return the total amount of a bank account 
     * 
     * @param username the unique identifier user
     * @param nameBanckAccount the name of the bank we want to know the total amount
     * @return the amount of a bank account
     * */

    public double getTotalAmountBank(final String username, final String nameBanckAccount) {

        double totalAmount = 0;
        final List<TransactionJson> banckTransaction = readBankTransaction(username, nameBanckAccount);

        for (final TransactionJson i : banckTransaction) {
            totalAmount += i.getAmount();   
        }
        return totalAmount;
    }

    public final static User readUser(final String username) {
        final JSONParser parser = new JSONParser();
        String name = null;
        String lastname = null;
        String email = null;
        String password = null;
        List<BankAccountJson> banks = null;
        List<MoneyboxAccountJson> moneyboxes = null;
        List<InvestmentAccountJson> investmentAccounts = null;

        if (!userExist(username)) {
            return null;
        } else {
            try {
                // create jsonArray from file
                final JSONArray users = (JSONArray) parser.parse(new FileReader(DB_NAME));
                // read user
                for (final Object user : users) {
                    final JSONObject person = (JSONObject) user;
                    final String userName = (String) person.get(USERNAME_KEY);

                    if (userName.equals(username)) {
                        name = (String) person.get(USER_DATA[0]);
                        lastname = (String) person.get(USER_DATA[1]);
                        email = (String) person.get(USER_DATA[2]);
                        password = (String) person.get(PASSWORD_KEY);
                        banks = readBanks(userName);
                        moneyboxes = readMoneyBoxes(userName);
                        investmentAccounts = readInvestmentAccount(userName);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return new User(name, lastname, email, username, password, banks, moneyboxes, investmentAccounts);
    }
    
    public final static List<User> readUsers() {
        final JSONParser parser = new JSONParser();
        final List<User> usersList = new ArrayList<>();

        try {
            // create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(DB_NAME));
            // read user
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String username = (String) person.get(USERNAME_KEY);
                final User userObject = readUser(username);
                if (userObject != null) {
                    usersList.add(userObject);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return usersList;
    }


}
