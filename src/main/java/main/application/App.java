package main.application;

import java.io.File;

//import org.apache.log4j.BasicConfigurator;

import javafx.application.Application;
import main.view.JavaFxView;
import main.json.TestJson;
import main.jsonfile.OperationJsonFile;
import main.jsonfile.Test;

/**
 * Main place for launch this app.
 *
 */
public final class App {

    private App() {
    }

    public static void main(final String[] args) {
        //BasicConfigurator.configure();
        final OperationJsonFile prova = new OperationJsonFile();
        final File f = new File("db.json");

        if (!f.exists()) {
            System.out.println("test 0");
            prova.initializeJsonFile();
        }
        Test.testing();
        Application.launch(JavaFxView.class, args);

    }
}
