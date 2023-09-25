package test.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.File;
import java.util.List;

import main.jsonfile.*;
import main.model.account.NotEnoughFundsException;

public class testJson {
    final OperationJsonFile jsonOperator = new OperationJsonFile();
    final File f = new File("db.json");
    
    @Test
    public void testFilePresent() {
        try {
            if (!f.exists()) {
                fail("db.json file is inot present");
            }
        } catch (NotEnoughFundsException e){
            
        }
    }
    
    @Test
    void testUserPresent() {
        try {
            jsonOperator.initializeUser("mario", "super", "SPRMRA58B03E289A", "super@mario.cart", "marioEluigi");
            if(!jsonOperator.userExist("SPRMRA58B03E289A")) {
                fail("this is not a presentUser");
            }
        } catch (NotEnoughFundsException e){
            
        }
    }
    
    @Test
    public void testUserPassword() {
        try {
            if (!jsonOperator.userPasswordCheck("SPRMRA58B03E289A","marioEluigi")) {
                fail("user or password noit valid");
            }
        }catch (NotEnoughFundsException e){
            
        }
    }
    
    @Test
    public void testBanckAccount() {
        // Get the current date and time
        Date currentDate = new Date();
        // Create date and time format objects
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        // Format the current date and time
        String formattedDate = dateFormat.format(currentDate);
        String formattedTime = timeFormat.format(currentDate);
        try {
            jsonOperator.newAccount("SPRMRA58B03E289A","STAR BANK", 0);
            jsonOperator.newTransaction("SPRMRA58B03E289A", "STAR BANK", "regalo", 100, formattedDate, formattedTime);
            jsonOperator.newTransaction("SPRMRA58B03E289A", "STAR BANK", "supermercato", -50.65, formattedDate, formattedTime);
            jsonOperator.newTransaction("SPRMRA58B03E289A", "STAR BANK", "salvadanaio", -10, formattedDate, formattedTime);
        }catch (NotEnoughFundsException e){
            
        }
    }
    
    @Test
    public void testInvestmentAccount() {
        // Get the current date and time
        Date currentDate = new Date();
        // Create date and time format objects
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        // Format the current date and time
        String formattedDate = dateFormat.format(currentDate);
        String formattedTime = timeFormat.format(currentDate);
        try {
            jsonOperator.newAccount("SPRMRA58B03E289A","binance", 2);
            jsonOperator.newAsset("SPRMRA58B03E289A", "binance", "ETH", "ethereum");
            jsonOperator.newTransactionAsset("SPRMRA58B03E289A", "binance", "gift", "ETH", 5, formattedDate, formattedTime);
            jsonOperator.newTransactionAsset("SPRMRA58B03E289A", "binance", "salvadanio", "ETH", -2, formattedDate, formattedTime);
        }catch (NotEnoughFundsException e){
            
        }
    }
    
    @Test
    public void testMoneyBoxAccount() {
        // Get the current date and time
        Date currentDate = new Date();
        // Create date and time format objects
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        // Format the current date and time
        String formattedDate = dateFormat.format(currentDate);
        String formattedTime = timeFormat.format(currentDate);
        try {
            jsonOperator.newAccount("SPRMRA58B03E289A","salvadanaio", 1);
            jsonOperator.newTransaction("SPRMRA58B03E289A", "salvadanaio", "STAR BANK", "Euro", 10, formattedDate, formattedTime);
            jsonOperator.newTransaction("SPRMRA58B03E289A", "salvadanaio", "binance", "ETH",2, formattedDate, formattedTime);
        }catch (NotEnoughFundsException e){
            
        }
    }

    
}
