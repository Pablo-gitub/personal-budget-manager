package main.json;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.scene.chart.XYChart;

/**
 * This is a tester class i used like a tester for show me if the behavior of the methods 
 * of the class OperationJSONUtente was like I expected 
 * 
 * */



public class TestJson {
    
    public static void proviamo() {
        OperationJSONUtente prova = new OperationJSONUtente();

        if (OperationJSONUtente.userExist("GNIPNI58B03E289A")==true) {
            System.out.println("utente esiste");
            System.out.println("controllo riuscito");
        } else {
            System.out.println("controllo fallito");
        }
        /*if (prova.userPasswordCheck("GNIPNI58B03E289A","cara")==true) {
            System.out.println("utente password corretti");
            System.out.println("controllo riuscito");
        } else {
            System.out.println("controllo fallito");
        }
        if (prova.userPasswordCheck("GNIPNI58B03E289A","mda")==false) {
            System.out.println("utente corretto password sbagliata ");
            System.out.println("controllo riuscito");
        } else {
            System.out.println("controllo fallito");
        }
        //prova.initializeUser("mario", "super", "luigi", "super@mario.cart", "marioEluigi");
        if (prova.userPasswordCheck("luigi","marioEluigi")==true) {
            System.out.println("il file è cambiato ma non lo leggo ");
            System.out.println("controllo riuscito");
        } else {
            System.out.println("controllo fallito");
        }
        prova.initializeUser("han", "solo", "jedy", "han@solo.jedy", "starwars");
        if (prova.userPasswordCheck("luigi","marioEluigi")==true) {
            System.out.println("il file è cambiato ma non lo leggo ");
            System.out.println("controllo riuscito");
        } else {
            System.out.println("controllo fallito");
        }
        if (prova.userPasswordCheck("jedy","starwars")==true) {
            System.out.println("il file è cambiato ma non lo leggo ");
            System.out.println("controllo riuscito");
        } else {
            System.out.println("controllo fallito");
        }
        TransactionJson[] transaction = OperationJSONUtente.readBanckTransaction("Gin", "BPER");
        for(int i=0; i<transaction.length; i++) {
            System.out.println(transaction[i].getDate());
        }
        System.out.println("nuovo conto");
        prova.newBanckAccount("jedy","STAR BANK");
        System.out.println("altro nuovo conto");
        prova.newBanckAccount("jedy","banca dei ribelli");
        System.out.println("nuova operazione");
        prova.newBanckTransaction("jedy","STAR BANK", "acquisto millennium falco", -10000.56, "16/08/2022", "04:22");
        System.out.println("altra nuova operazione");
        prova.newBanckTransaction("jedy","STAR BANK", "blaster", -100, "17/08/2022", "00:22");*/
    }

    

}
