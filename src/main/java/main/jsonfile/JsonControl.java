package main.jsonfile;

import java.io.FileReader;
import java.util.Arrays;
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

    protected static JSONArray readJsonFile() throws Exception {
        try (FileReader fileReader = new FileReader(DB_NAME)) {
            final JSONParser parser = new JSONParser();
            return (JSONArray) parser.parse(fileReader);
        }
    }

    private static boolean checkUserProperty(final JSONArray users, final String username, final String propertyName, final String propertyValue) {
        for (final Object user : users) {
            if (user != null) {
                final JSONObject person = (JSONObject) user;
                final String userUsername = (String) person.get(USERNAME_KEY);
                final String userPropertyValue = (String) person.get(propertyName);
                if (userUsername != null && userPropertyValue != null &&
                        userUsername.equals(username) && userPropertyValue.equals(propertyValue)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean checkUserAccount(final JSONArray users, final String username, final String propertyName, final String propertyValue) {
        for (final Object user : users) {
            final JSONObject person = (JSONObject) user;
            final String userUsername = (String) person.get(USERNAME_KEY);
            final JSONArray accounts = (JSONArray) person.get(propertyName);
            if (userUsername.equals(username)) {
                for (final var account : accounts) {
                    final JSONObject tempAccount = (JSONObject) account;
                    final int index = indexInArray(MONEY_ACCOUNTS, propertyName);
                    final String name = ACCOUNT_NAMES[index];
                    final String tempName = (String) tempAccount.get(name);
                    if (tempName.equals(propertyValue)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private static <X> int indexInArray(final X[] array, final X element) {
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(element)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static boolean userExist(final String username) {
        try {
            final JSONArray users = readJsonFile();
            return checkUserProperty(users, username, USERNAME_KEY, username);
        } catch (Exception e) {
            handleException("Error checking user existence", e);
            return false;
        }
    }

    protected static boolean userAccountExist(final String username, final String accountName, final int accountTypeIndex) {
        try {
            final JSONArray users = readJsonFile();
            return checkUserAccount(users, username, MONEY_ACCOUNTS[accountTypeIndex], accountName);
        } catch (Exception e) {
            handleException("Error checking user account existence", e);
            return false;
        }
    }

    protected static boolean userAccountAssetExist(final String username, final String investmentAccountName, final String assetSymbol) {
        try {
            final JSONArray users = readJsonFile();
            return checkUserProperty(users, username, ACCOUNT_NAMES[2], investmentAccountName) &&
                    checkAssetExist(users, username, investmentAccountName, assetSymbol);
        } catch (Exception e) {
            handleException("Error checking user account asset existence", e);
            return false;
        }
    }

    private static boolean checkAssetExist(final JSONArray users, final String username, final String investmentAccountName, final String assetSymbol) {
        for (final Object user : users) {
            final JSONObject person = (JSONObject) user;
            final String userUsername = (String) person.get(USERNAME_KEY);
            if (userUsername.equals(username)) {
                final JSONArray investmentAccounts = (JSONArray) person.get(MONEY_ACCOUNTS[2]);
                for (final Object investmentAccount : investmentAccounts) {
                    final JSONObject account = (JSONObject) investmentAccount;
                    final String accountName = (String) account.get(ACCOUNT_NAMES[2]);
                    if (accountName.equals(investmentAccountName)) {
                        final JSONArray assets = (JSONArray) account.get(ASSETS);
                        for (final Object asset : assets) {
                            final JSONObject assetObject = (JSONObject) asset;
                            final String symbol = (String) assetObject.get(SYMBOL_ASSET);
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

    public static boolean userPasswordCheck(final String username, final String password) {
        try {
            final JSONArray users = readJsonFile();
            return checkUserProperty(users, username, PASSWORD_KEY, password);
        } catch (Exception e) {
            handleException("Error checking user password", e);
            return false;
        }
    }

    protected static void handleException(final String message, final Exception e) {
        LOGGER.log(Level.SEVERE, message + ": " + e.getMessage(), e);
    }
}
