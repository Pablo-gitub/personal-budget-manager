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

public class JsonControl {
    protected static String dbName = "db.json";

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
    
    public static boolean userExist(final String username) {
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

    protected static boolean userAccountExist(final String username, final String nameaccount, final int i) {

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

    protected static boolean userAccountAssetExist(final String username, final String nameInvestimentAccount, final String symbolAsset) {
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

    public static final boolean userPasswordCheck(final String username, final String password) {
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
    
}
