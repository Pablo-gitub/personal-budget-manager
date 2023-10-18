package main.jsonfile;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class JsonWriting extends JsonReading {
    
    protected static void writeJsonFile(final JSONArray text) {
        try {
            // Writing to a file
            final File file = new File(DB_NAME);
            file.createNewFile();
            final FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(text.toJSONString());
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * this method initialize a new user it take from input:
     * 
     * @param name the name of the new user 
     * @param lastName last name of the new user
     * @param username the unique identifier of the new user 
     * @param email the eMail of the new user 
     * @param password new user password 
     * 
     * this method doesn't has return because it just write the new user in the file 
     * 
     * */

    @SuppressWarnings("unchecked")
    public static void initializeUser(final String name, final String lastName, final String username, final String email, final String password) {
        final JSONParser parser = new JSONParser();

        if (!userExist(username)) {
            try {
                // create jsonArray from file
                final JSONArray users = (JSONArray) parser.parse(new FileReader(DB_NAME));
                final JSONObject utente = new JSONObject();
                utente.put("name", name);
                utente.put("lastName", lastName);
                utente.put("username", username);
                utente.put("email", email);
                utente.put("password", password);
                final JSONArray list = new JSONArray();
                utente.put("banckAccounts", list);
                utente.put("moneyBoxes", list);
                utente.put("InvestimentAccounts", list);
                users.add(utente);
                // Writing to a file
                writeJsonFile(users);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * this method initialize a new banckAccount moneyBoxAccount or investmentAccount
     * depending on whether the input i is 0 1 or 2 in the file json
     * 
     * if the @param i is over 2 it will be changed automatically in 2
     * 
     * @param username the unique identifier of the user 
     * @param nameAccount the name of the new account for example "banco posta" "binance"
     * @param type refer to the account type
     * 
     * */
    @SuppressWarnings("unchecked")
    public static void newAccount(final String username, final String nameAccount, final int type) {
        int i = type;
        if (i > 2) {
            i = 2;
        }
        final JSONParser parser = new JSONParser();
        final String[] moneyAccounts = {"banckAccounts", "moneyBoxes", "InvestimentAccounts"};
        final String[] nameMoneyAccount = {"nameBanckAccount", "nameMoneyBox", "nameInvestimentAccount"};

        if (!userAccountExist(username, nameAccount, i)) {

            try {
                // create jsonArray from file
                final JSONArray users = (JSONArray) parser.parse(new FileReader(DB_NAME));
                // read user
                for (final Object user : users) {

                    final JSONObject person = (JSONObject) user;
                    final String userName = (String) person.get("username");

                    if (userName.equals(username)) {

                        final JSONArray accounts = (JSONArray) person.get(moneyAccounts[i]);
                        final JSONObject account = new JSONObject();
                        account.put(nameMoneyAccount[i], nameAccount);
                        final JSONArray list = new JSONArray();
                        if (i == 0 || i == 1) {
                            account.put("transactions", list);
                        } else {
                            account.put("assets", list);
                        }

                        accounts.addAll(Arrays.asList(account));
                        person.put(moneyAccounts[i], accounts);
                        // Writing to file
                        writeJsonFile(users);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * this method initialize a new asset account in the file json
     * 
     * @param username the unique identifier of the user 
     * @param nameInvestimentAccount the name of the investment account we want to add a asset
     * @param symbolAsset the symbol of the new asset we want to add to our investment account 
     * @param nameAsset the name of the new asset, technical this name could be even a "" void string,
     *          this is just for the database, for all operation we use the symbol, I chose to add this field 
     *          for make it more clearly and more complete. In the future we might like to see this field and 
     *          use it for show some graphical.
     * 
     * */
    @SuppressWarnings("unchecked")
    public static void newAsset(final String username, final String nameInvestimentAccount, final String symbolAsset, final String nameAsset) {
        final JSONParser parser = new JSONParser();

        if (!userAccountAssetExist(username, nameInvestimentAccount, symbolAsset)) {
            try {
                // create jsonArray from file
                final JSONArray users = (JSONArray) parser.parse(new FileReader(DB_NAME));
                // read user
                for (final Object user : users) {
                    final JSONObject person = (JSONObject) user;
                    final String userName = (String) person.get("username");

                    if (userName.equals(username)) {    
                        final JSONArray investimentAccounts = (JSONArray) person.get("InvestimentAccounts");
    
                        for (final Object a : investimentAccounts) {
                            final JSONObject investimentAccount = (JSONObject) a;
                            final String nameInvAcc = (String) investimentAccount.get("nameInvestimentAccount");
    
                            if (nameInvAcc.equals(nameInvestimentAccount)) {
                                final JSONArray assets = (JSONArray) investimentAccount.get("assets");
                                final JSONObject asset = new JSONObject();
                                asset.put("nameAsset", nameAsset);
                                asset.put("symbolAsset", symbolAsset);
                                final JSONArray list = new JSONArray();
                                asset.put("transactions", list);
                                assets.addAll(Arrays.asList(asset));
                                investimentAccount.put("assets", assets);
                                // Writing to file
                                writeJsonFile(users);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * this method initialize a bank transaction in the file json
     * 
     * @param username the unique identifier of the user 
     * @param nameAccountTransaction the name of the bank account we want to add a transaction
     * @param nameTransaction the object of the transaction "supermarket"
     * @param amount the amount of the transaction
     * @param date the date of the transaction
     * @param time the time of the transaction 
     * 
     * */
    @SuppressWarnings("unchecked")
    public static void newTransaction(final String username, final String nameAccountTransaction, final String nameTransaction, 
            final double amount, final String date, final String time) {
        final JSONParser parser = new JSONParser();

        try {
            // create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(DB_NAME));
            // read user
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String userName = (String) person.get("username");

                if (userName.equals(username)) {
                    final JSONArray accounts = (JSONArray) person.get("banckAccounts");

                    for (final Object c : accounts) {
                        final JSONObject account = (JSONObject) c;
                        final String nameAccount = (String) account.get("nameBanckAccount");

                        if (nameAccount.equals(nameAccountTransaction)) {
                            final JSONArray transactions = (JSONArray) account.get("transactions");
                            final JSONObject transaction = new JSONObject();
                            transaction.put("nameTransaction", nameTransaction);
                            transaction.put("amount", amount);
                            transaction.put("date", date);
                            transaction.put("time", time);
                            transaction.put("currency", "Euro");
                            transactions.addAll(Arrays.asList(transaction));
                            account.put("transactions", transactions);
                            // Writing to file
                            writeJsonFile(users);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * this method initialize a new money box transaction in the file json
     * 
     * @param username the unique identifier of the user 
     * @param nameAccountTransaction the name of the money box account we want to add a transaction
     * @param nameTransaction the object of the transaction "Revolut Bank", 
     *        this string could identify a real cost or the name of the bank sender/receiver
     * @param symbolAsset this parameter identify the currency of the transaction, 
     *        because we might choose to deposit not only the currency FIAT from our bank but also
     *        a asset from the investment accounts
     * @param amount the amount of the transaction
     * @param date the date of the transaction
     * @param time the time of the transaction 
     * 
     * */

    @SuppressWarnings("unchecked")
    public static void newTransaction(final String username, final String nameAccountTransaction, final String nameTransaction, 
            final String symbolAsset, final double amount, final String date, final String time) {

        final JSONParser parser = new JSONParser();

        try {
            // create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(DB_NAME));
            // read user
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String userName = (String) person.get("username");

                if (userName.equals(username)) {
                    final JSONArray accounts = (JSONArray) person.get("moneyBoxes");

                    for (final Object c : accounts) {
                        final JSONObject account = (JSONObject) c;
                        final String nameAccount = (String) account.get("nameMoneyBox");

                        if (nameAccount.equals(nameAccountTransaction)) {
                            final JSONArray transactions = (JSONArray) account.get("transactions");
                            final JSONObject transaction = new JSONObject();
                            transaction.put("nameTransaction", nameTransaction);
                            transaction.put("amount", amount);
                            transaction.put("date", date);
                            transaction.put("time", time);
                            transaction.put("currency", symbolAsset);
                            transactions.addAll(Arrays.asList(transaction));
                            account.put("transactions", transactions);
                            // Writing to file
                            writeJsonFile(users);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * this method initialize a new asset transaction in the file json
     * 
     * @param username the unique identifier of the user 
     * @param nameAccountTransaction the name of the investment account we want to add a transaction
     * @param nameTransaction the object of the transaction "supermarket"
     * @param symbolAsset the symbol asset of the transaction we want to add for example "ETH" "BTC" "AAPL"
     * @param amount the amount of the transaction
     * @param date the date of the transaction
     * @param time the time of the transaction 
     * 
     * */

    @SuppressWarnings("unchecked")
    public static void newTransactionAsset(final String username, final String nameAccountTransaction, 
            final String nameTransaction, final String symbolAsset, final double amount,
            final String date, final String time) {

        final JSONParser parser = new JSONParser();

        try {
            // create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(DB_NAME));
            // read user
            for (Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String userName = (String) person.get("username");

                if (userName.equals(username)) {
                    final JSONArray accounts = (JSONArray) person.get("InvestimentAccounts");

                    for (final Object c : accounts) {
                        final JSONObject account = (JSONObject) c;
                        final String nameAccount = (String) account.get("nameInvestimentAccount");

                        if (nameAccount.equals(nameAccountTransaction)) {
                            final JSONArray assets = (JSONArray) account.get("assets");

                            for (final Object a : assets) {
                                final JSONObject asset = (JSONObject) a;
                                final String symbol = (String) asset.get("symbolAsset");

                                if (symbol.equals(symbolAsset)) {

                                    final JSONArray transactions = (JSONArray) asset.get("transactions");
                                    final JSONObject transaction = new JSONObject();
                                    transaction.put("nameTransaction", nameTransaction);
                                    transaction.put("amount", amount);
                                    transaction.put("date", date);
                                    transaction.put("time", time);
                                    transactions.addAll(Arrays.asList(transaction));
                                    asset.put("transactions", transactions);
                                    // Writing to file
                                    writeJsonFile(users);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
