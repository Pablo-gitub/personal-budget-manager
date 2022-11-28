package main.jsonfile;

import java.io.File;
import java.util.List;

public final class Test {

    public static void testing() {
        final OperationJsonFile prova = new OperationJsonFile();
        final File f = new File("db.json");

        if (!f.exists()) {
            System.out.println("test 0");
            prova.initializeJsonFile();
        }
        System.out.println("test 1");
        if (prova.userExist("GNIPNI58B03E289A") == true) {
            System.out.println("utente esiste");
            System.out.println("controllo riuscito");
        } else {
            System.out.println("controllo fallito");
        }
        System.out.println("test 4");
        prova.initializeUser("mario", "super", "SPRMRA58B03E289A", "super@mario.cart", "marioEluigi");
        System.out.println("test 5");
        if (prova.userExist("SPRMRA58B03E289A")==true) {
            System.out.println("controllo riuscito");
        } else {
            System.out.println("controllo fallito");
        }
        System.out.println("test 6");
        if (prova.userPasswordCheck("SPRMRA58B03E289A","marioEluigi")==true) {
            System.out.println("controllo riuscito");
        } else {
            System.out.println("controllo fallito");
        }
        System.out.println("test 7");
        prova.newAccount("SPRMRA58B03E289A","STAR BANK", 0);
        System.out.println("test 8");
        prova.newAccount("SPRMRA58B03E289A","binance", 2);
        System.out.println("test 9");
        prova.newAccount("SPRMRA58B03E289A","salvadanaio", 1);
        System.out.println("test 10");
        prova.newAsset("SPRMRA58B03E289A", "binance", "ETH", "ethereum");
        System.out.println("test 11");
        /*
        prova.newTransaction("SPRMRA58B03E289A", "STAR BANK", "incasso", 100, "10/10/2022", "10:20");
        prova.newTransaction("SPRMRA58B03E289A", "STAR BANK", "salvadanaio", -10, "10/10/2022", "10:22");
        prova.newTransaction("SPRMRA58B03E289A", "salvadanaio", "STAR BANK", "Euro", 10, "10/10/2022", "10:22");
        prova.newTransactionAsset("SPRMRA58B03E289A", "binance", "gift", "ETH", 500, "10/10/2022", "10:30");
        
        TransactionJson[][] Assets = prova.readAssetsTransaction("SPRMRA58B03E289A", "binance");
        for(TransactionJson[] Asset : Assets ) {
        	System.out.println("print asset ...");
        	for(TransactionJson transaction : Asset) {
            	System.out.println(transaction.getNameTransaction() + " " + 
            			transaction.getAmount() + " " + transaction.getCurrency() + " " +
            			transaction.getDate() + " " + transaction.getTime());
        	}
        }*/
        System.out.println("test 12");
        List<TransactionJson> asset = prova.readAssetTransactions("SPRMRA58B03E289A", "binance", "ETH");
        for(TransactionJson as:asset) {
        	System.out.println(as.getNameTransaction() + " " + as.getAmount() + " " + 
        			as.getCurrency() + " " + as.getDate() + " " + as.getTime());
        }
        System.out.println("test 13");
        List<AssetJson> assets = prova.readAssetsTransactions("GNIPNI58B03E289A", "Coinbase");
        for(AssetJson nas :assets) {
        	System.out.println("transactions of the asset:");
        	for(TransactionJson tr:nas.getTransactions()) {
            	System.out.println(tr.getNameTransaction() + " " + tr.getAmount() + " " + 
            			tr.getCurrency() + " " + tr.getDate() + " " + tr.getTime());        		
        	}
        }
        System.out.println("test 14");
        List<TransactionJson> transactionsSalvadanaio = prova.readMoneyBoxTransaction("SPRMRA58B03E289A", "salvadanaio");
        for(TransactionJson tr:transactionsSalvadanaio) {
        	System.out.println(tr.getNameTransaction() + " " + tr.getAmount() + " " + 
        			tr.getCurrency() + " " + tr.getDate() + " " + tr.getTime());
        }
        System.out.println("test 15");
        List<TransactionJson> transactionsStarBank = prova.readBankTransaction("SPRMRA58B03E289A", "STAR BANK");
        for(TransactionJson tr:transactionsStarBank) {
        	System.out.println(tr.getNameTransaction() + " " + tr.getAmount() + " " + 
        			tr.getCurrency() + " " + tr.getDate() + " " + tr.getTime());
        }
        System.out.println("test 16");
        List<BankAccountJson> banks = prova.readBanks("GNIPNI58B03E289A");
        for(BankAccountJson bank: banks) {
        	System.out.println(bank.getName());
        	System.out.println(bank.getTotalAmount());
        	for(TransactionJson tr:bank.getTransactions()) {
            	System.out.println(tr.getNameTransaction() + " " + tr.getAmount() + " " + 
            			tr.getCurrency() + " " + tr.getDate() + " " + tr.getTime());
            }
        }
        prova.newTransaction("GNIPNI58B03E289A", "Vacanze", "prova euro", "Euro", 10, "10/10/2022", "10:22");
        System.out.println("test 17");
        List<MoneyboxAccountJson> moneyBoxes = prova.readMoneyBoxes("GNIPNI58B03E289A");
        for(MoneyboxAccountJson box: moneyBoxes) {
        	System.out.println(box.getName());
        	for(TransactionJson tr:box.getTransactions()) {
            	System.out.println(tr.getNameTransaction() + " " + tr.getAmount() + " " + 
            			tr.getCurrency() + " " + tr.getDate() + " " + tr.getTime());
            }
        	System.out.println(box.getTotalAmount());
        }
        /*prova.newTransaction("GNIPNI58B03E289A", "Intesa Sanpaolo", "incasso", 100, "10/10/2022", "10:20");
        System.out.println("test 18");
        banks = prova.readBanks("GNIPNI58B03E289A");
        for(AccountJson bank: banks) {
        	System.out.println(bank.getName());
        	System.out.println(bank.getTotalAmount());
        	for(TransactionJson tr:bank.getTransactions()) {
            	System.out.println(tr.getNameTransaction() + " " + tr.getAmount() + " " + 
            			tr.getCurrency() + " " + tr.getDate() + " " + tr.getTime());
            }
        	bank.setTotalAmount();
        	System.out.println(bank.getTotalAmount());
        }*/
        List<InvestmentAccountJson> invAccount = prova.readInvestmentAccount("GNIPNI58B03E289A");
        System.out.println("test 19");
        for(InvestmentAccountJson account: invAccount) {
        	System.out.println(account.getNameInvestmentAccount());
			for(AssetJson asset1: account.getAssets()) {
        		System.out.println(asset1.getAssetName() + " "+ asset1.getAssetSymbol() + " " + asset1.getAmount());
        		for(TransactionJson transaction : asset1.getTransactions()) {
        			System.out.println(transaction.getNameTransaction() + " " + transaction.getAmount() + " " + 
        					transaction.getCurrency() + " " + transaction.getDate() + " " + transaction.getTime());
        		}
        	}
        }
        System.out.println("test 20");
        User man = prova.readUser("GNIPNI58B03E289A");
        System.out.println(man.getName() + " " + man.getLastname() + " " + man.getEmail() + " " + man.getPassword() + " " + man.getUsername());
        for(BankAccountJson bank: man.getBanks()) {
            System.out.println(bank.getName());
            System.out.println(bank.getTotalAmount());
            for(TransactionJson tr:bank.getTransactions()) {
                System.out.println(tr.getNameTransaction() + " " + tr.getAmount() + " " + 
                        tr.getCurrency() + " " + tr.getDate() + " " + tr.getTime());
            }
        }
        for(MoneyboxAccountJson box: man.getMoneyboxes()) {
            System.out.println(box.getName());
            for(TransactionJson tr:box.getTransactions()) {
                System.out.println(tr.getNameTransaction() + " " + tr.getAmount() + " " + 
                        tr.getCurrency() + " " + tr.getDate() + " " + tr.getTime());
            }
            System.out.println(box.getTotalAmount());
        }
        for(InvestmentAccountJson account: man.getInvestmentAccounts()) {
            System.out.println(account.getNameInvestmentAccount());
            for(AssetJson asset1: account.getAssets()) {
                System.out.println(asset1.getAssetName() + " "+ asset1.getAssetSymbol() + " " + asset1.getAmount());
                for(TransactionJson transaction : asset1.getTransactions()) {
                    System.out.println(transaction.getNameTransaction() + " " + transaction.getAmount() + " " + 
                            transaction.getCurrency() + " " + transaction.getDate() + " " + transaction.getTime());
                }
            }
        }
	}

}
