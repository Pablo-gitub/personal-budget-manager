package main.jsonfile;

import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonControl {
    protected static final Logger LOGGER = Logger.getLogger(JsonControl.class.getName());
    protected static final String DB_NAME = "db.json";
    protected static final String USERNAME_KEY = "username";
    protected static final String PASSWORD_KEY = "password";
    protected static final String ASSETS = "assets";
    protected static final String SYMBOL_ASSET = "symbolAsset";
    protected static final String[] MONEY_ACCOUNTS = {"banckAccounts", "moneyBoxes", "InvestimentAccounts"};
    protected static final String[] ACCOUNT_NAMES = {"nameBanckAccount", "nameMoneyBox", "nameInvestimentAccount"};

    private static JSONArray readJsonFile() throws Exception {
        try (FileReader fileReader = new FileReader(DB_NAME)) {
            JSONParser parser = new JSONParser();
            return (JSONArray) parser.parse(fileReader);
        }
    }

    private static boolean checkUserProperty(JSONArray users, String username, String propertyName, String propertyValue) {
        for (Object user : users) {
            JSONObject person = (JSONObject) user;
            String userUsername = (String) person.get(USERNAME_KEY);
            String userPropertyValue = (String) person.get(propertyName);
            if (userUsername.equals(username) && userPropertyValue.equals(propertyValue)) {
                return true;
            }
        }
        return false;
    }

    public static boolean userExist(String username) {
        try {
            JSONArray users = readJsonFile();
            return checkUserProperty(users, username, USERNAME_KEY, username);
        } catch (Exception e) {
            handleException("Error checking user existence", e);
            return false;
        }
    }

    protected static boolean userAccountExist(String username, String accountName, int accountTypeIndex) {
        try {
            JSONArray users = readJsonFile();
            return checkUserProperty(users, username, MONEY_ACCOUNTS[accountTypeIndex], accountName);
        } catch (Exception e) {
            handleException("Error checking user account existence", e);
            return false;
        }
    }

    protected static boolean userAccountAssetExist(String username, String investmentAccountName, String assetSymbol) {
        try {
            JSONArray users = readJsonFile();
            return checkUserProperty(users, username, ACCOUNT_NAMES[2], investmentAccountName) &&
                    checkAssetExist(users, username, investmentAccountName, assetSymbol);
        } catch (Exception e) {
            handleException("Error checking user account asset existence", e);
            return false;
        }
    }

    private static boolean checkAssetExist(JSONArray users, String username, String investmentAccountName, String assetSymbol) {
        for (Object user : users) {
            JSONObject person = (JSONObject) user;
            String userUsername = (String) person.get(USERNAME_KEY);
            if (userUsername.equals(username)) {
                JSONArray investmentAccounts = (JSONArray) person.get(MONEY_ACCOUNTS[2]);
                for (Object investmentAccount : investmentAccounts) {
                    JSONObject account = (JSONObject) investmentAccount;
                    String accountName = (String) account.get(ACCOUNT_NAMES[2]);
                    if (accountName.equals(investmentAccountName)) {
                        JSONArray assets = (JSONArray) account.get(ASSETS);
                        for (Object asset : assets) {
                            JSONObject assetObject = (JSONObject) asset;
                            String symbol = (String) assetObject.get(SYMBOL_ASSET);
                            if (symbol.equals(assetSymbol)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean userPasswordCheck(String username, String password) {
        try {
            JSONArray users = readJsonFile();
            return checkUserProperty(users, username, PASSWORD_KEY, password);
        } catch (Exception e) {
            handleException("Error checking user password", e);
            return false;
        }
    }

    private static void handleException(String message, Exception e) {
        LOGGER.log(Level.SEVERE, message + ": " + e.getMessage(), e);
    }
}
