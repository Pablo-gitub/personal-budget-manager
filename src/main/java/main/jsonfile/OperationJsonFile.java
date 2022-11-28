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

public class OperationJsonFile {

    private static String dbName = "db.json";

    // get a file from the resources folder
    // works everywhere, IDEA, unit test and JAR file.
    private InputStream getFileFromResourceAsStream(final String fileName) {

        // The class loader that loaded the class
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    private void writeJsonFile(final JSONArray text) {
        try {
            // Writing to a file
            final File file = new File(dbName);
            file.createNewFile();
            final FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(text.toJSONString());
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public final void initializeJsonFile() {
        final JSONParser parser = new JSONParser();
        final String fileName = "utente.json";

        try {
            // create jsonArray from file
            final OperationJsonFile app = new OperationJsonFile();
            final InputStream is = app.getFileFromResourceAsStream(fileName);
            final JSONArray users = (JSONArray) parser.parse(new InputStreamReader(is, "UTF-8"));
            // Writing to a file
            writeJsonFile(users);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	/**
     * this method return a boolean true if the searched username exist in the json file false otherwise
     * 
     * @param username it correspond at the username of the user, we notice the mistake 
     * the username instead fc (fiscal code) too late for change, but the meaning doesn't change, both 
     * username and fc has the same meaning to identify a only user 
     * 
     * @return boolean true if the searched username exist
     * 
     * */
	
    public boolean userExist(final String username) {
        final JSONParser parser = new JSONParser();

        try {
            // create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
            // read user
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String userName = (String) person.get("username");
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

    private boolean userAccountExist(final String username, final String nameaccount, final int i) {

    	final JSONParser parser = new JSONParser();
        final String[] moneyAccounts = {"banckAccounts", "moneyBoxes", "InvestimentAccounts"};
    	final String[] nameMoneyAccount = {"nameBanckAccount", "nameMoneyBox", "nameInvestimentAccount"};

        try {
        	// create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
            // read user
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String userName = (String) person.get("username");

                if (userName.equals(username)) {
                    final JSONArray accounts = (JSONArray) person.get(moneyAccounts[i]);

                    for (final Object c : accounts) {
                        final JSONObject account = (JSONObject) c;
                        final String nameAccount = (String) account.get(nameMoneyAccount[i]);

                        if (nameAccount.equals(nameaccount)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return false;
    }

    private boolean userAccountAssetExist(final String username, final String nameInvestimentAccount, final String symbolAsset) {
    	final JSONParser parser = new JSONParser();

        try {
        	// create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
            // read user
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String userName = (String) person.get("username");

                if (userName.equals(username)) {
                    final JSONArray accounts = (JSONArray) person.get("InvestimentAccounts");

                    for (final Object c : accounts) {
                        final JSONObject account = (JSONObject) c;
                        final String nameAccount = (String) account.get("nameInvestimentAccount");

                        if (nameAccount.equals(nameInvestimentAccount)) {
                        	final JSONArray assets = (JSONArray) account.get("assets");

                            for (final Object a : assets) {
                                final JSONObject asset = (JSONObject) a;
                                final String symbol = (String) asset.get("symbolAsset");

                                if (symbol.equals(symbolAsset)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
		return false;
    }

    public final boolean userPasswordCheck(final String username, final String password) {
        final JSONParser parser = new JSONParser();

        try {
            // create jsonArray from file
        	final JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
            // read user
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String fc = (String) person.get("username");
                final String psw = (String) person.get("password");

                if (fc.equals(username) && psw.equals(password)) {
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
     * 
    
    public ProfileCredentials setProfileData(String username) {
        
        ProfileCredentials profile = null;
        JSONParser parser = new JSONParser();

        try {
            // create jsonArray from file
        	JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
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
    */
    
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
    public void initializeUser(final String name, final String lastName, final String username, final String email, final String password) {
        final JSONParser parser = new JSONParser();

        if (!userExist(username)) {
        	try {
            	// create jsonArray from file
        	    final JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
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
    public void newAccount(final String username, final String nameAccount, final int type) {
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
                final JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
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
    public void newAsset(final String username, final String nameInvestimentAccount, final String symbolAsset, final String nameAsset) {
        final JSONParser parser = new JSONParser();

        if (!userAccountAssetExist(username, nameInvestimentAccount, symbolAsset)) {
	        try {
	        	// create jsonArray from file
	            final JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
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
    public void newTransaction(final String username, final String nameAccountTransaction, final String nameTransaction, 
            final double amount, final String date, final String time) {
        final JSONParser parser = new JSONParser();

        try {
        	// create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
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
    public void newTransaction(final String username, final String nameAccountTransaction, final String nameTransaction, 
            final String symbolAsset, final double amount, final String date, final String time) {

        final JSONParser parser = new JSONParser();

        try {
        	// create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
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
    public void newTransactionAsset(final String username, final String nameAccountTransaction, 
            final String nameTransaction, final String symbolAsset, final double amount,
            final String date, final String time) {

        final JSONParser parser = new JSONParser();

        try {
        	// create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
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

    /**
     * this method read all the transaction of a specific asset in a specific investment account 
     * in the file json and put them in a array of TranssactionJson
     * 
     * @param username the unique identifier of the user 
     * @param nameInvestimentAccount the name of the investment account we want to read the transactions
     * @param symbolAsset the symbol asset we want to read all the transactions for example "ETH" "BTC" "AAPL"
     * @return transactions list related to the symbolAsset into the investment account
     * 
     * */
    public List<TransactionJson> readAssetTransactions(final String username, 
            final String nameInvestimentAccount, final String symbolAsset) {
        final List<TransactionJson> listTransactions = new ArrayList<>(); 
        final JSONParser parser = new JSONParser();

        try {
        	// create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
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

                            for (final Object as : assets) {
                                final JSONObject asset = (JSONObject) as;
                                final String assetSymbol = (String) asset.get("symbolAsset");

                                if (assetSymbol.equals(symbolAsset)) {
                                    final JSONArray transactions = (JSONArray) asset.get("transactions");

                                    for (final Object tr : transactions) {
                                        final JSONObject transaction = (JSONObject) tr;
                                        final TransactionJson element = new TransactionJson(
                                        		(String) transaction.get("nameTransaction"), (String) transaction.get("date"),
                                        		(String) transaction.get("time"), ((Number) transaction.get("amount")).doubleValue(), 
                                        		(String) asset.get("symbolAsset"));
                                        listTransactions.add(element);
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
    	return listTransactions;
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
    /*
    public List<List<TransactionJson>> readAssetsTransactions(String username, String nameInvestimentAccount){
    	
    	List<List<TransactionJson>> listAssetsTransactions= new ArrayList<List<TransactionJson>>(); 
        JSONParser parser = new JSONParser();

        try {
        	// create jsonArray from file
            JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
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

                            for (Object as: assets) {

                                JSONObject asset = (JSONObject) as;
                                String assetSymbol = (String) asset.get("symbolAsset");
                                List<TransactionJson> assetTransactions = new ArrayList<TransactionJson>();
                                JSONArray transactions = (JSONArray) asset.get("transactions");

                                for (Object tr: transactions) {
                                	
                                    JSONObject transaction = (JSONObject) tr;
                                    TransactionJson element = new TransactionJson(
                                    		(String) transaction.get("nameTransaction"),(String) transaction.get("date"),
                                    		(String) transaction.get("time"),((Number)transaction.get("amount")).doubleValue(), 
                                    		assetSymbol);
                                    assetTransactions.add(element);
                                    
                                }
                                
                                listAssetsTransactions.add(assetTransactions);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listAssetsTransactions;
    }*/

    public List<AssetJson> readAssetsTransactions(final String username, final String nameInvestimentAccount) {
        final List<AssetJson> listAssetsTransactions = new ArrayList<>(); 
        final JSONParser parser = new JSONParser();

        try {
        	// create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
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

                            for (final Object as: assets) {
                                final JSONObject asset = (JSONObject) as;
                                final String assetSymbol = (String) asset.get("symbolAsset");
                                final String assetName = (String) asset.get("nameAsset");
                                final AssetJson token;
                                final List<TransactionJson> assetTransactions = new ArrayList<TransactionJson>();
                                final JSONArray transactions = (JSONArray) asset.get("transactions");

                                for (final Object tr: transactions) {
                                    final JSONObject transaction = (JSONObject) tr;
                                    final TransactionJson element = new TransactionJson(
                                    		(String) transaction.get("nameTransaction"), (String) transaction.get("date"),
                                    		(String) transaction.get("time"), ((Number) transaction.get("amount")).doubleValue(), 
                                    		assetSymbol);
                                    assetTransactions.add(element);
                                }
                                token = new AssetJson(assetName, assetSymbol, assetTransactions);
                                listAssetsTransactions.add(token);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listAssetsTransactions;
    }

    public final List<InvestmentAccountJson> readInvestmentAccount(final String username) {
        final List<InvestmentAccountJson> listInvestmentAccount = new ArrayList<>(); 
        final JSONParser parser = new JSONParser();

        try {
        	// create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
            // read user
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String userName = (String) person.get("username");

                if (userName.equals(username)) {
                    final JSONArray investimentAccounts = (JSONArray) person.get("InvestimentAccounts");

                    for (final Object a : investimentAccounts) {
                        final JSONObject investimentAccount = (JSONObject) a;
                        final String nameInvAcc = (String) investimentAccount.get("nameInvestimentAccount");
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

	
    /*
    private int[] countAssets(String username, String nameInvestimentAccount) {
		
    	int[] count= new int [2];
    	int assetsCount = 0;
    	int maxTransactions = 0;
    	int numTransactions = 0;
		JSONParser parser = new JSONParser();

        try {
        	// create jsonArray from file
            JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
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

                            for (Object o : assets) {
                            	
                            	assetsCount++;
                            	numTransactions = 0; 
                                JSONObject asset = (JSONObject) o;
                                JSONArray transactions = (JSONArray) asset.get("transactions");

                                for (@SuppressWarnings("unused") Object t : transactions) {
                                	
                                    numTransactions++;
                                                                        
                                }
                                
                                if(numTransactions>maxTransactions) {
                                	
                                	maxTransactions = numTransactions;
                                
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        count[0] = assetsCount;
        count[1] = maxTransactions;
        System.out.println("assets:" + count[0] + "maxTransactions" +count[1]);
		return count;
		
	}*/

	/**
     * this method read all the transaction of a specific moneyBox in the file json and put them in a 
     * array of TranssactionJson
     * 
     * @param username the unique identifier of the user 
     * @param nameMoneyBox the name of the investment account we want to read the transactions
     * @return read all the transactions related to a nameMoneyBox
     * */


	public List<TransactionJson> readMoneyBoxTransaction(final String username, final String nameMoneyBox) {
	    final List<TransactionJson> listTransactions = new ArrayList<>();
	    final JSONParser parser = new JSONParser();

        try {
        	// create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
            // read user
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String userName = (String) person.get("username");

                if (userName.equals(username)) {
                    final JSONArray moneyBoxes = (JSONArray) person.get("moneyBoxes");

                    for (final Object a : moneyBoxes) {
                        final JSONObject moneyBox = (JSONObject) a;
                        final String nameBox = (String) moneyBox.get("nameMoneyBox");

                        if (nameBox.equals(nameMoneyBox)) {
                            final JSONArray transactions = (JSONArray) moneyBox.get("transactions");

                            for (final Object tr: transactions) {
                                final JSONObject transaction = (JSONObject) tr;
                                final TransactionJson element = new TransactionJson(
                                		(String) transaction.get("nameTransaction"), (String) transaction.get("date"),
                                		(String) transaction.get("time"), ((Number) transaction.get("amount")).doubleValue(), 
                                		(String) transaction.get("currency"));
                                listTransactions.add(element);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listTransactions;
    }

    public final List<MoneyboxAccountJson> readMoneyBoxes(final String username) {
        final List<MoneyboxAccountJson> listMoneyboxes = new ArrayList<>();
        final JSONParser parser = new JSONParser();

        try {
        	// create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
            // read user
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String userName = (String) person.get("username");

                if (userName.equals(username)) {
                    final JSONArray moneyBoxes = (JSONArray) person.get("moneyBoxes");

                    for (final Object a : moneyBoxes) {
                        final JSONObject moneyBox = (JSONObject) a;
                        final String moneyBoxName = (String) moneyBox.get("nameMoneyBox");
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
            ex.printStackTrace();
        }
    	return listMoneyboxes;
    }

    /**
     * this method read all the transaction of a specific asset in the file json and put them in a 
     * array of TranssactionJson
     * 
     * @param username the unique identifier of the user 
     * @param nameBanckAccount the name of the bank account we want to read the transactions
     * @return the transactions related to a nameBankAccount
     * */
	public List<TransactionJson> readBankTransaction(final String username, final String nameBanckAccount) {
	    final List<TransactionJson> listTransactions = new ArrayList<>();
	    final JSONParser parser = new JSONParser();

        try {
        	// create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
            // read user
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String userName = (String) person.get("username");

                if (userName.equals(username)) {
                    final JSONArray banckAccounts = (JSONArray) person.get("banckAccounts");

                    for (final Object a : banckAccounts) {
                        final JSONObject banckAccount = (JSONObject) a;
                        final String banckName = (String) banckAccount.get("nameBanckAccount");

                        if (banckName.equals(nameBanckAccount)) {
                            final JSONArray transactions = (JSONArray) banckAccount.get("transactions");

                            for (final Object tr : transactions) {
                                final JSONObject transaction = (JSONObject) tr;
                                final TransactionJson element = new TransactionJson(
                                		(String) transaction.get("nameTransaction"), (String) transaction.get("date"),
                                		(String) transaction.get("time"), ((Number) transaction.get("amount")).doubleValue());
                                listTransactions.add(element);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listTransactions;
    }

    public final List<BankAccountJson> readBanks(final String username) {
        final List<BankAccountJson> listBanks = new ArrayList<>();
        final JSONParser parser = new JSONParser();

        try {
        	// create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
            // read user
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                final String userName = (String) person.get("username");

                if (userName.equals(username)) {
                    final JSONArray banckAccounts = (JSONArray) person.get("banckAccounts");

                    for (final Object a : banckAccounts) {
                        final JSONObject banckAccount = (JSONObject) a;
                        final String bankName = (String) banckAccount.get("nameBanckAccount");
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
            ex.printStackTrace();
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

    public final User readUser(final String username) {
        final JSONParser parser = new JSONParser();
        String name = null;
        String lastname = null;
        String email = null;
        String password = null;
        List<BankAccountJson> banks = null;
        List<MoneyboxAccountJson> moneyboxes = null;
        List<InvestmentAccountJson> investmentAccounts = null;

        if (userExist(username) == false) {
            return null;
        } else {
            try {
                // create jsonArray from file
                final JSONArray users = (JSONArray) parser.parse(new FileReader(dbName));
                // read user
                for (final Object user : users) {
                    final JSONObject person = (JSONObject) user;
                    final String userName = (String) person.get("username");

                    if (userName.equals(username)) {
                        name = (String) person.get("name");
                        lastname = (String) person.get("lastName");
                        email = (String) person.get("email");
                        password = (String) person.get("password");
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

    /**
     * this method return the array AssetsJson where each amount is the total amount of this asset in this investment account
     * and the symbol is the currency 
     * 
     * @param username the unique identifier user
     * @param nameinvestimentAccount the name of the investment account we want to know the total amount of the assets
     * 
     * */

    /*public List<AssetJson> getTotalAssetsAccount(String username, String nameInvestimentAccount) {
        
        List<List<TransactionJson>> AssetsTransaction = readAssetsTransactions(username, nameInvestimentAccount);
        List<AssetJson> totalAssetsAccount = new ArrayList<AssetJson>();
        
        for(List<TransactionJson> i : AssetsTransaction) {
        	
        	AssetJson asset = new AssetJson();
        	asset.setAssetSymbol(i.get(0).getCurrency());
        	
        	for(TransactionJson j : i) {
        		
        		asset.setAmount(asset.getAmount() + j.getAmount());
        	
        	}
        	
        	totalAssetsAccount.add(asset);
        }
        
        return totalAssetsAccount;
    }*/

    /**
     * this method return the total amount of a specific asset in this investment account
     * and the symbol is the currency 
     * 
     * @param username the unique identifier user
     * @param nameinvestimentAccount the name of the investment account we want to know the total amount of the assets
     * 
     * */

    /*public double getTotalAsset(String username, String nameInvestimentAccount, String symbolAsset) {

        double totalAmount = 0;
        List<TransactionJson> BanckTransaction = readAssetTransactions(username, nameInvestimentAccount, symbolAsset);

        for (TransactionJson i : BanckTransaction) {
            totalAmount += i.getAmount();
        }

        return totalAmount;
    }*/

}
