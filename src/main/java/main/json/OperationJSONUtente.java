package main.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
/*
 * I've try many kind of library for write and read file json 
 * at the end I've chose to use json.simple
 * 
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
*/
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import main.model.profile.Password;
import main.model.profile.ProfileCredentials;
import main.model.profile.SimplePassword;

public class OperationJSONUtente {
    
    // get a file from the resources folder
    // works everywhere, IDEA, unit test and JAR file.
    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }
    
    // print input stream
    private static void printInputStream(InputStream is) {

        try (InputStreamReader streamReader =
                    new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    
    /**
     * this method return a boolean true if the searched username exist in the json file false otherwise
     * 
     * @param username it correspond at the username of the user, we notice the mistake 
     * the username instead fc (fiscal code) too late for change, but the meaning doesn't change, both 
     * username and fc has the same meaning to identify a only user 
     * 
     * */
    static boolean userExist(String username) {

        JSONParser parser = new JSONParser();

        try {
            OperationJSONUtente app = new OperationJSONUtente();
            //String fileName = "database.properties";
            String fileName = "utente.json";
            InputStream is = app.getFileFromResourceAsStream(fileName);
            printInputStream(is);
            
            // create jsonArray from file
            File input = new File(OperationJSONUtente.class.getResource("utente.json").toURI());
            FileReader reader = new FileReader(input);
            JSONArray users = (JSONArray) parser.parse(reader);
            
            
            // read user
            for (Object user : users) {
                JSONObject person = (JSONObject) user;

                String userName = (String) person.get("username");

                if (userName.equals(username)) {
                    return true;
                }

            }

            return false;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
    * this method check if the email and password are correct for a user in the
    * JSON file utente
    * 
    * */
    public boolean userPasswordCheck(String email, String password) {

        JSONParser parser = new JSONParser();

        try {
            // create jsonArray from file
            JSONArray users = (JSONArray) parser
                    .parse(new FileReader(new File(getClass().getClassLoader().getResource("utente.json").toURI())));

            // read user
            for (Object user : users) {
                
                JSONObject person = (JSONObject) user;

                String eMail = (String) person.get("email");

                String psw = (String) person.get("password");

                if (eMail.equals(email) && psw.equals(password)) {
                    return true;
                }

            }

            return false;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    
    /**
     * This method return a ProfileCredential object setting all field
     * 
     * this method would be useful after the checking email password
     * 
     * @param username the user unique identifier
     * 
     * */
    
    public ProfileCredentials setProfileData(String username) {
        ProfileCredentials profile = null;
        
        JSONParser parser = new JSONParser();

        try {
            // create jsonArray from file
            JSONArray users = (JSONArray) parser
                    .parse(new FileReader(new File(getClass().getClassLoader().getResource("utente.json").toURI())));

            // read user
            for (Object user : users) {
                JSONObject person = (JSONObject) user;

                String userName = (String) person.get("username");

                if (userName.equals(username)) {
                    String name = (String) person.get("name");
                    String surname = (String) person.get("lastName");
                    String email = (String) person.get("email");
                    Password password = new SimplePassword((String) person.get("password"));
                    
                    profile = new ProfileCredentials(name, surname, userName, email, password);
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return profile;
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
    public void initializeUser(String name, String lastName, String username, String email, String password) {

        JSONParser parser = new JSONParser();

        try {
            File input = new File(getClass().getClassLoader().getResource("utente.json").toURI());

            FileReader reader = new FileReader(input);
            // create jsonArray from file
            JSONArray users = (JSONArray) parser.parse(reader);

            JSONObject utente = new JSONObject();
            utente.put("name", name);
            utente.put("lastName", lastName);
            utente.put("username", username);
            utente.put("email", email);
            utente.put("password", password);

            JSONArray list = new JSONArray();

            utente.put("banckAccounts", list);

            utente.put("moneyBoxes", list);

            utente.put("InvestimentAccounts", list);

            users.add(utente);

            FileWriter file = new FileWriter(input, false);

            System.out.println("scrivo su file");
            
            file.write(users.toJSONString());
            file.flush();
            file.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    /**
     * this method initialize a new banckAccount in the file json
     * 
     * @param username the unique identifier of the user 
     * @param nameBanckAccount the name of the new banck account for example "banco posta"
     * 
     * */

    public void newBanckAccount(String username, String nameBanckAccount) {

        JSONParser parser = new JSONParser();

        try {
            File input = new File(getClass().getClassLoader().getResource("utente.json").toURI());

            FileReader reader = new FileReader(input);
            // create jsonArray from file
            JSONArray users = (JSONArray) parser.parse(reader);

            // read user
            for (Object user : users) {
                JSONObject person = (JSONObject) user;

                String userName = (String) person.get("username");

                if (userName.equals(username)) {

                    JSONArray banckAccounts = (JSONArray) person.get("banckAccounts");

                    JSONObject banckAccount = new JSONObject();
                    banckAccount.put("nameBanckAccount", nameBanckAccount);

                    JSONArray list = new JSONArray();
                    banckAccount.put("transactions", list);

                    banckAccounts.addAll(Arrays.asList(banckAccount));

                    person.put("banckAccounts", banckAccounts);

                    FileWriter file = new FileWriter(input, false);

                    System.out.println("scrivo su file");
                    
                    file.write(users.toJSONString());
                    file.flush();
                    file.close();
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * this method initialize a new money box in the file json
     * 
     * @param username the unique identifier of the user 
     * @param nameMoneyBox the name of the new money box for example "vacation"
     * 
     * */

    public void newMoneyBox(String username, String nameMoneyBox) {

        JSONParser parser = new JSONParser();

        try {
            File input = new File(getClass().getClassLoader().getResource("utente.json").toURI());

            FileReader reader = new FileReader(input);
            // create jsonArray from file
            JSONArray users = (JSONArray) parser.parse(reader);

            // read user
            for (Object user : users) {
                
                JSONObject person = (JSONObject) user;

                String userName = (String) person.get("username");

                if (userName.equals(username)) {

                    JSONArray moneyBoxes = (JSONArray) person.get("moneyBoxes");

                    JSONObject moneyBox = new JSONObject();
                    moneyBox.put("nameMoneyBox", nameMoneyBox);

                    JSONArray list = new JSONArray();
                    moneyBox.put("transactions", list);

                    moneyBoxes.addAll(Arrays.asList(moneyBox));

                    person.put("moneyBoxes", moneyBoxes);
                    
                    FileWriter file = new FileWriter(input, false);

                    file.write(users.toJSONString());
                    file.flush();
                    file.close();
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * this method initialize a new investment account in the file json
     * 
     * @param username the unique identifier of the user 
     * @param nameInvestimentAccount the name of the new investment account for example "Binance"
     * 
     * */

    public void newInvestimentAccount(String username, String nameInvestimentAccount) {

        JSONParser parser = new JSONParser();

        try {
            File input = new File(getClass().getClassLoader().getResource("utente.json").toURI());

            FileReader reader = new FileReader(input);
            // create jsonArray from file
            JSONArray users = (JSONArray) parser.parse(reader);

            // read user
            for (Object user : users) {
                JSONObject person = (JSONObject) user;

                String userName = (String) person.get("username");

                if (userName.equals(username)) {

                    JSONArray investimentAccounts = (JSONArray) person.get("investimentAccounts");

                    JSONObject investimentAccount = new JSONObject();
                    investimentAccount.put("nameInvestimentAccount", nameInvestimentAccount);

                    JSONArray list = new JSONArray();
                    investimentAccount.put("Asset", list);

                    investimentAccounts.addAll(Arrays.asList(investimentAccount));

                    person.put("investimentAccounts", investimentAccounts);

                    FileWriter file = new FileWriter(input, false);

                    file.write(users.toJSONString());
                    file.flush();
                    file.close();
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
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

    public void newAsset(String username, String nameInvestimentAccount, String symbolAsset, String nameAsset) {

        JSONParser parser = new JSONParser();

        try {
            File input = new File(getClass().getClassLoader().getResource("utente.json").toURI());

            FileReader reader = new FileReader(input);
            // create jsonArray from file
            JSONArray users = (JSONArray) parser.parse(reader);

            // read user
            for (Object user : users) {
                JSONObject person = (JSONObject) user;

                String userName = (String) person.get("username");

                if (userName.equals(username)) {

                    JSONArray investimentAccounts = (JSONArray) person.get("InvestimentAccounts");

                    for (Object a : investimentAccounts) {

                        JSONObject investimentAccount = (JSONObject) a;

                        String nameInvAcc = (String) investimentAccount.get("nameInvestimentAccount");

                        if (nameInvAcc.equals(nameInvestimentAccount)) {

                            JSONArray assets = (JSONArray) investimentAccount.get("assets");

                            JSONObject asset = new JSONObject();
                            asset.put("nameAsset", nameAsset);
                            asset.put("symbolAsset", symbolAsset);

                            JSONArray list = new JSONArray();
                            asset.put("transactions", list);

                            assets.addAll(Arrays.asList(asset));

                            investimentAccount.put("assets", assets);
                            
                            FileWriter file = new FileWriter(input, false);

                            file.write(users.toJSONString());
                            file.flush();
                            file.close();

                        }

                    }

                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    /**
     * this method initialize a bank transaction in the file json
     * 
     * @param username the unique identifier of the user 
     * @param nameBanckAccount the name of the bank account we want to add a transaction
     * @param nameTransaction the object of the transaction "supermarket"
     * @param amount the amount of the transaction
     * @param date the date of the transaction
     * @param time the time of the transaction 
     * 
     * */

    public void newBanckTransaction(String username, String nameBanckAccount, String nameTransaction, double amount,
            String date, String time) {

        JSONParser parser = new JSONParser();

        try {
            File input = new File(getClass().getClassLoader().getResource("utente.json").toURI());

            FileReader reader = new FileReader(input);
            // create jsonArray from file
            JSONArray users = (JSONArray) parser.parse(reader);

            // read user
            for (Object user : users) {
                JSONObject person = (JSONObject) user;

                String userName = (String) person.get("username");

                if (userName.equals(username)) {

                    JSONArray banckAccounts = (JSONArray) person.get("banckAccounts");

                    for (Object c : banckAccounts) {

                        JSONObject banckAccount = (JSONObject) c;
                        String nameAccount = (String) banckAccount.get("nameBanckAccount");

                        if (nameAccount.equals(nameBanckAccount)) {

                            JSONArray transactions = (JSONArray) banckAccount.get("transactions");

                            JSONObject transaction = new JSONObject();

                            transaction.put("nameTransaction", nameTransaction);
                            transaction.put("amount", amount);
                            transaction.put("date", date);
                            transaction.put("time", time);

                            transactions.addAll(Arrays.asList(transaction));

                            banckAccount.put("transactions", transactions);

                            System.out.println("il nuovo file json");
                            System.out.println(users);

                            FileWriter file = new FileWriter(input, false);

                            file.write(users.toJSONString());
                            file.flush();
                            file.close();

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
     * @param nameMoneyBox the name of the money box account we want to add a transaction
     * @param nameTransaction the object of the transaction "Revolut Bank", 
     *        this string could identify a real cost or the name of the bank sender/receiver
     * @param currency this parameter identify the currency of the transaction, 
     *        because we might choose to deposit not only the currency FIAT from our bank but also
     *        a asset from the investment accounts
     * @param amount the amount of the transaction
     * @param date the date of the transaction
     * @param time the time of the transaction 
     * 
     * */

    public void newMoneyBoxTransaction(String username, String nameMoneyBox, String nameTransaction, String currency,
            double amount, String date, String time) {

        JSONParser parser = new JSONParser();

        try {
            File input = new File(getClass().getClassLoader().getResource("utente.json").toURI());

            FileReader reader = new FileReader(input);
            // create jsonArray from file
            JSONArray users = (JSONArray) parser.parse(reader);

            // read user
            for (Object user : users) {
                JSONObject person = (JSONObject) user;

                String userName = (String) person.get("username");

                if (userName.equals(username)) {

                    JSONArray moneyBoxes = (JSONArray) person.get("moneyBoxes");

                    for (Object c : moneyBoxes) {

                        JSONObject moneyBox = (JSONObject) c;
                        String nameMBox = (String) moneyBox.get("nameMoneyBox");

                        if (nameMBox.equals(nameMoneyBox)) {

                            JSONArray transactions = (JSONArray) moneyBox.get("transactions");

                            JSONObject transaction = new JSONObject();

                            transaction.put("nameTransaction", nameTransaction);
                            transaction.put("amount", amount);
                            transaction.put("currency", currency);
                            transaction.put("date", date);
                            transaction.put("time", time);

                            transactions.addAll(Arrays.asList(transaction));

                            moneyBox.put("transactions", transactions);

                            System.out.println("il nuovo file json");
                            System.out.println(users);

                            FileWriter file = new FileWriter(input, false);

                            file.write(users.toJSONString());
                            file.flush();
                            file.close();

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
     * @param nameInvestimentAccount the name of the investment account we want to add a transaction
     * @param nameTransaction the object of the transaction "supermarket"
     * @param symbolAsset the symbol asset of the transaction we want to add for example "ETH" "BTC" "AAPL"
     * @param amount the amount of the transaction
     * @param date the date of the transaction
     * @param time the time of the transaction 
     * 
     * */

    public void newAssetTransaction(String username, String nameInvestimentAccount, String nameTransaction, String symbolAsset,
            double amount, String date, String time) {

        JSONParser parser = new JSONParser();

        try {
            File input = new File(getClass().getClassLoader().getResource("utente.json").toURI());

            FileReader reader = new FileReader(input);
            // create jsonArray from file
            JSONArray users = (JSONArray) parser.parse(reader);

            // read user
            for (Object user : users) {
                JSONObject person = (JSONObject) user;

                String userName = (String) person.get("username");

                if (userName.equals(username)) {

                    JSONArray investimentAccounts = (JSONArray) person.get("InvestimentAccounts");

                    for (Object a : investimentAccounts) {

                        JSONObject investimentAccount = (JSONObject) a;

                        String nameInvAcc = (String) investimentAccount.get("nameInvestimentAccount");

                        if (nameInvAcc.equals(nameInvestimentAccount)) {

                            JSONArray assets = (JSONArray) investimentAccount.get("assets");

                            for (Object as : assets) {

                                JSONObject asset = (JSONObject) as;

                                String assetSymbol = (String) asset.get("symbolAsset");

                                if (assetSymbol.equals(symbolAsset)) {

                                    JSONArray transactions = (JSONArray) asset.get("transactions");

                                    JSONObject transaction = new JSONObject();

                                    transaction.put("contractor", nameTransaction);
                                    transaction.put("amount", amount);
                                    transaction.put("date", date);
                                    transaction.put("time", time);

                                    transactions.addAll(Arrays.asList(transaction));

                                    asset.put("transactions", transactions);

                                    FileWriter file = new FileWriter(input, false);

                                    file.write(users.toJSONString());
                                    file.flush();
                                    file.close();
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
    
    
    /**
     * this method read all the transaction of a specific asset in a specific investment account 
     * in the file json and put them in a array of TranssactionJson
     * 
     * @param username the unique identifier of the user 
     * @param nameInvestimentAccount the name of the investment account we want to read the transactions
     * @param symbolAsset the symbol asset we want to read all the transactions for example "ETH" "BTC" "AAPL"
     * 
     * 
     * */

    public TransactionJson[] readAssetTransaction(String username, String nameInvestimentAccount, String symbolAsset) {

        TransactionJson[] Transaction = null;
        JSONParser parser = new JSONParser();

        try {
            File input = new File(getClass().getClassLoader().getResource("utente.json").toURI());

            FileReader reader = new FileReader(input);
            // create jsonArray from file
            JSONArray users = (JSONArray) parser.parse(reader);

            // read user
            for (Object user : users) {
                JSONObject person = (JSONObject) user;

                String userName = (String) person.get("username");
                // System.out.println("input utente = " + username + ", utente file json = " +
                // userName);

                if (userName.equals(username)) {

                    JSONArray investimentAccounts = (JSONArray) person.get("InvestimentAccounts");

                    for (Object a : investimentAccounts) {

                        JSONObject investimentAccount = (JSONObject) a;

                        String nameInvAcc = (String) investimentAccount.get("nameInvestimentAccount");

                        if (nameInvAcc.equals(nameInvestimentAccount)) {

                            JSONArray assets = (JSONArray) investimentAccount.get("assets");

                            for (Object as : assets) {

                                JSONObject asset = (JSONObject) as;

                                String assetSymbol = (String) asset.get("symbolAsset");

                                if (assetSymbol.equals(symbolAsset)) {

                                    JSONArray transactions = (JSONArray) asset.get("transactions");

                                    for (int i = 0; i < transactions.size(); i++) {
                                        JSONObject transaction = (JSONObject) transactions.get(i);

                                        Transaction[i].setAmount((double) transaction.get("amount"));
                                        Transaction[i].setNameTransaction((String) transaction.get("nameTransaction"));
                                        Transaction[i].setDate((String) transaction.get("date"));
                                        Transaction[i].setTime((String) transaction.get("time"));
                                        Transaction[i].setCurrency((String) asset.get("symbolAsset"));
                                    }

                                }
                            }

                        }

                    }

                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return Transaction;
    }
    
    /**
     * this method read all the transaction of every asset in a specific investment account 
     * in the file json and put them in a matrix TranssactionJson where each line is a asset with it's symbol
     * 
     * @param username the unique identifier of the user 
     * @param nameInvestimentAccount the name of the investment account we want to read the transactions
     * @param symbolAsset the symbol asset we want to read all the transactions for example "ETH" "BTC" "AAPL"
     * 
     * 
     * */


    public TransactionJson[][] readAssetsTransaction(String username, String nameInvestimentAccount) {

        TransactionJson[][] Transaction = null;
        JSONParser parser = new JSONParser();

        try {
            File input = new File(getClass().getClassLoader().getResource("utente.json").toURI());

            FileReader reader = new FileReader(input);
            // create jsonArray from file
            JSONArray users = (JSONArray) parser.parse(reader);

            // read user
            for (Object user : users) {
                JSONObject person = (JSONObject) user;

                String userName = (String) person.get("username");
                
                if (userName.equals(username)) {

                    JSONArray investimentAccounts = (JSONArray) person.get("InvestimentAccounts");

                    for (Object a : investimentAccounts) {

                        JSONObject investimentAccount = (JSONObject) a;

                        String nameInvAcc = (String) investimentAccount.get("nameInvestimentAccount");

                        if (nameInvAcc.equals(nameInvestimentAccount)) {

                            JSONArray assets = (JSONArray) investimentAccount.get("assets");

                            for (int i = 0; i < assets.size(); i++) {

                                JSONObject asset = (JSONObject) assets.get(i);

                                String assetSymbol = (String) asset.get("symbolAsset");

                                JSONArray transactions = (JSONArray) asset.get("transactions");

                                for (int j = 0; j < transactions.size(); j++) {
                                    JSONObject transaction = (JSONObject) transactions.get(i);

                                    Transaction[i][j].setAmount((double) transaction.get("amount"));
                                    Transaction[i][j].setNameTransaction((String) transaction.get("nameTransaction"));
                                    Transaction[i][j].setDate((String) transaction.get("date"));
                                    Transaction[i][j].setTime((String) transaction.get("time"));
                                    Transaction[i][j].setCurrency(assetSymbol);
                                }

                            }
                        }

                    }

                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return Transaction;
    }
    
    
    /**
     * this method read all the transaction of a specific moneyBox in the file json and put them in a 
     * array of TranssactionJson
     * 
     * @param username the unique identifier of the user 
     * @param nameMoneyBox the name of the investment account we want to read the transactions
     * 
     * */


    public TransactionJson[] readMoneyBoxTransaction(String username, String nameMoneyBox) {

        TransactionJson[] Transaction = null;
        JSONParser parser = new JSONParser();

        try {
            File input = new File(getClass().getClassLoader().getResource("utente.json").toURI());

            FileReader reader = new FileReader(input);
            // create jsonArray from file
            JSONArray users = (JSONArray) parser.parse(reader);

            // read user
            for (Object user : users) {
                JSONObject person = (JSONObject) user;

                String userName = (String) person.get("username");

                if (userName.equals(username)) {

                    JSONArray moneyBoxes = (JSONArray) person.get("moneyBoxes");

                    for (Object a : moneyBoxes) {

                        JSONObject moneyBox = (JSONObject) a;

                        String nameBox = (String) moneyBox.get("nameMoneyBoxes");

                        if (nameBox.equals(nameMoneyBox)) {

                            JSONArray transactions = (JSONArray) moneyBox.get("transactions");

                            for (int i = 0; i < transactions.size(); i++) {
                                JSONObject transaction = (JSONObject) transactions.get(i);

                                Transaction[i].setAmount((double) transaction.get("amount"));
                                Transaction[i].setNameTransaction((String) transaction.get("nameTransaction"));
                                Transaction[i].setDate((String) transaction.get("date"));
                                Transaction[i].setTime((String) transaction.get("time"));
                                Transaction[i].setCurrency((String) transaction.get("currency"));
                            }

                        }

                    }

                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return Transaction;
    }
    
    /**
     * this method read all the transaction of a specific asset in the file json and put them in a 
     * array of TranssactionJson
     * 
     * @param username the unique identifier of the user 
     * @param nameBanckAccount the name of the bank account we want to read the transactions
     * 
     * */

    
    public static TransactionJson[] readBanckTransaction(String username, String nameBanckAccount) {

        TransactionJson[] Transaction = null;
        JSONParser parser = new JSONParser();

        try {
            File input = new File(OperationJSONUtente.class.getResource("utente.json").toURI());

            FileReader reader = new FileReader(input);
            // create jsonArray from file
            JSONArray users = (JSONArray) parser.parse(reader);

            // read user
            for (Object user : users) {
                JSONObject person = (JSONObject) user;

                String userName = (String) person.get("username");

                if (userName.equals(username)) {

                    JSONArray banckAccounts = (JSONArray) person.get("banckAccounts");

                    for (Object a : banckAccounts) {

                        JSONObject banckAccount = (JSONObject) a;

                        String banckName = (String) banckAccount.get("nameBanckAccount");

                        if (banckName.equals(nameBanckAccount)) {

                            JSONArray transactions = (JSONArray) banckAccount.get("transactions");

                            for (int i = 0; i < transactions.size(); i++) {
                                JSONObject transaction = (JSONObject) transactions.get(i);

                                double amount = (double) transaction.get("amount");
                                String nameTransaction = (String) transaction.get("nameTransaction");
                                String date = (String) transaction.get("date");
                                String time = (String) transaction.get("time");
                                String currency = "euro";

                                TransactionJson temp = new TransactionJson(nameTransaction, date, time, amount,
                                        currency);

                                Transaction[i] = temp;

                            }

                        }

                    }

                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return Transaction;
    }
    
    /**
     * this method return the total amount of a bank account 
     * 
     * @param username the unique identifier user
     * @param nameBanckAccount the name of the bank we want to know the total amount
     * 
     * */

    public double getTotalAmountBank(String username, String nameBanckAccount) {

        double totalAmount = 0;

        TransactionJson[] BanckTransaction = readBanckTransaction(username, nameBanckAccount);

        for (TransactionJson i : BanckTransaction) {
            totalAmount += i.getAmount();
        }

        return totalAmount;
    }
    
    /**
     * this method return the array AssetsJson where each amount is the total amount of this asset in this investment account
     * and the symbol is the currency 
     * 
     * @param username the unique identifier user
     * @param nameinvestimentAccount the name of the investment account we want to know the total amount of the assets
     * 
     * */

    @SuppressWarnings("null")
    public AssetJson[] getTotalAssetsAccount(String username, String nameInvestimentAccount) {
        
        TransactionJson[][] AssetsTransaction = readAssetsTransaction(username, nameInvestimentAccount);

        AssetJson[] totalAssetsAccount = new AssetJson[AssetsTransaction.length];
        
        for (int i = 0; i < AssetsTransaction.length; i++) {
            totalAssetsAccount[i].setAssetSymbol(AssetsTransaction[i][0].getCurrency());
            for (int j = 0; j < AssetsTransaction[i].length; j++) {
                totalAssetsAccount[i].setAmount(totalAssetsAccount[i].getAmount() + AssetsTransaction[i][j].getAmount());
            }
        }

        return totalAssetsAccount;
    }
    
    /**
     * this method return the total amount of a specific asset in this investment account
     * and the symbol is the currency 
     * 
     * @param username the unique identifier user
     * @param nameinvestimentAccount the name of the investment account we want to know the total amount of the assets
     * 
     * */

    public double getTotalAsset(String username, String nameInvestimentAccount, String symbolAsset) {

        double totalAmount = 0;

        TransactionJson[] BanckTransaction = readAssetTransaction(username, nameInvestimentAccount, symbolAsset);

        for (TransactionJson i : BanckTransaction) {
            totalAmount += i.getAmount();
        }

        return totalAmount;
    }

}
