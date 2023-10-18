package main.jsonfile;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonReading extends JsonControl{
    
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

    public static List<AssetJson> readAssetsTransactions(final String username, final String nameInvestimentAccount) {
        final List<AssetJson> listAssetsTransactions = new ArrayList<>(); 
        final JSONParser parser = new JSONParser();

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

    public final static List<InvestmentAccountJson> readInvestmentAccount(final String username) {
        final List<InvestmentAccountJson> listInvestmentAccount = new ArrayList<>(); 
        final JSONParser parser = new JSONParser();

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
        final List<TransactionJson> listTransactions = new ArrayList<>();
        final JSONParser parser = new JSONParser();

        try {
            // create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(DB_NAME));
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

    public final static List<MoneyboxAccountJson> readMoneyBoxes(final String username) {
        final List<MoneyboxAccountJson> listMoneyboxes = new ArrayList<>();
        final JSONParser parser = new JSONParser();

        try {
            // create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(DB_NAME));
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
    public static List<TransactionJson> readBankTransaction(final String username, final String nameBanckAccount) {
        final List<TransactionJson> listTransactions = new ArrayList<>();
        final JSONParser parser = new JSONParser();

        try {
            // create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(DB_NAME));
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

    public final static List<BankAccountJson> readBanks(final String username) {
        final List<BankAccountJson> listBanks = new ArrayList<>();
        final JSONParser parser = new JSONParser();

        try {
            // create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(DB_NAME));
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

    public final static User readUser(final String username) {
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
                final JSONArray users = (JSONArray) parser.parse(new FileReader(DB_NAME));
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
    
    public final static List<User> readUsers() {
        final JSONParser parser = new JSONParser();
        String name = null;
        String lastname = null;
        String email = null;
        String username = null;
        String password = null;
        List<BankAccountJson> banks = null;
        List<MoneyboxAccountJson> moneyboxes = null;
        List<InvestmentAccountJson> investmentAccounts = null;
        List<User> usersList = new ArrayList<>();

        try {
            // create jsonArray from file
            final JSONArray users = (JSONArray) parser.parse(new FileReader(DB_NAME));
            // read user
            for (final Object user : users) {
                final JSONObject person = (JSONObject) user;
                name = (String) person.get("name");
                lastname = (String) person.get("lastName");
                email = (String) person.get("email");
                username = (String) person.get("username");
                password = (String) person.get("password");
                banks = readBanks(username);
                moneyboxes = readMoneyBoxes(username);
                investmentAccounts = readInvestmentAccount(username);
                usersList.add(new User(name, lastname, email, username, password, banks, moneyboxes, investmentAccounts));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return usersList;
    }


}
