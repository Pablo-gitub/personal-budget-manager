package main.view.expenditure;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.charts.LineChartBuilder;
import main.charts.PieChartBuilder;
import main.charts.TestChart;
import main.charts.TableBuilder;
import main.control.Controller;
import main.json.TransactionJson;
import main.jsonfile.AssetJson;
import main.jsonfile.BankAccountJson;
import main.jsonfile.InvestmentAccountJson;
import main.jsonfile.MoneyboxAccountJson;
import main.jsonfile.OperationJsonFile;
import main.jsonfile.User;
import main.view.BaseScene;
import main.view.MainScene;
import main.view.profile.LoginScene;

public class ExpenditureScene extends BaseScene{

    private static final int TEXT_DIM = 10;
    private static final int TITLE_DIM = 15;

    private final Scene scene;
    private final BorderPane root;
    private Queue<List<?>> updateables;
    private final DecimalFormat df = new DecimalFormat("###.##");
    private List<User> users = OperationJsonFile.readUsers();
    private User selectedUser;
    private List<main.jsonfile.TransactionJson> transactionChart;
    private List<main.jsonfile.TransactionJson> transactionTable;
    private String bankName;
    private String firstDate;
    private String secondDate;
    private String tableName;
    final ChoiceBox<String> accountName = new ChoiceBox<>();
    final ChoiceBox<String> accountNameTable = new ChoiceBox<>();
    int type = 0;
    private int set = 0;
    private int setnewUser = 0;
    private static final DateTimeFormatter DATATIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ExpenditureScene(final BorderPane root, final Stage primaryStage, final Controller controller) {
        super(primaryStage, controller);
        this.root = root;
        this.scene = getGadgets().createScene(root);
    }

    @Override
    public void updateEverythingNeeded(final Queue<List<?>> things) {
        this.updateables = things;
        super.updateScene();
        super.getPrimaryStage().setScene(this.scene);
    }


    @Override
    public Scene getScene() {
        return this.scene;
    }

    @Override
    protected void updateTop() {
        final Pane topLayout = getGadgets().createVerticalPanel();
        final ChoiceBox<String> choiceUser = new ChoiceBox<>();
        for (final User user : users) {
            choiceUser.getItems().add(user.getUsername());
        }
        final TextField password = new TextField("Password");
        final Button access = new Button("Accedi");
        final Text userSelection = new Text();
        userSelection.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        userSelection.setText("seleziona utente");
        final Text newUser = new Text();
        newUser.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        newUser.setText("Nuovo utente");
        final TextField nameUser = new TextField("Nome");
        final TextField lastnameUser = new TextField("Cognome");
        final TextField fiscalcodeUser = new TextField("Codice Fiscale");
        final TextField emailUser = new TextField("Email");
        final TextField passwordUser = new TextField("Password");
        final Button addUser = new Button("Aggiungi");
        final Button newUserButton = new Button("Nuovo Utente");
        final HBox userChoise = new HBox(userSelection, choiceUser, password, access);
        HBox userNew = null;
        newUserButton.setOnAction(event -> {
            setnewUser = 1;
            updateTop();
        });
        if (setnewUser == 1) {
            userNew = new HBox(newUser, nameUser, lastnameUser, fiscalcodeUser, emailUser, passwordUser, addUser);
        } else {
            userNew = new HBox(newUserButton);
        }
        final Text newAccount = new Text();
        newAccount.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        newAccount.setText("Apri nuovo conto");
        final ChoiceBox<String> typeOfTheNewAccount = new ChoiceBox<>();
        typeOfTheNewAccount.getItems().add("Bancario");
        typeOfTheNewAccount.getItems().add("Salvadanaio");
        typeOfTheNewAccount.getItems().add("Investimento");
        final TextField nameAccount = new TextField("Nome Conto");
        final Button add = new Button("Aggiungi");
        final HBox addingAccount = new HBox(typeOfTheNewAccount, nameAccount, add);
        access.setOnAction(event -> {
            if (OperationJsonFile.userPasswordCheck((String) choiceUser.getValue(), password.getText())) {
                accountName.getItems().clear();
                accountNameTable.getItems().clear();
                final String selectedUserName = (String) choiceUser.getValue();
                selectedUser = OperationJsonFile.readUser(selectedUserName);
                if (selectedUser != null) {
                    for (final BankAccountJson bank: selectedUser.getBanks()) {
                        accountName.getItems().add(bank.getName());
                        accountNameTable.getItems().add(bank.getName());
                    }
                    for (final MoneyboxAccountJson box: selectedUser.getMoneyboxes()) {
                        accountNameTable.getItems().add(box.getName());
                    }
                    for (final InvestmentAccountJson inv: selectedUser.getInvestmentAccounts()) {
                        accountNameTable.getItems().add(inv.getNameInvestmentAccount());
                    }
                }
                updateBottom();
            } else {
                accountName.getItems().clear();
                accountNameTable.getItems().clear();
                transactionChart.clear();
                transactionTable.clear();
            }
        });
        
        add.setOnAction(event -> {
            if (typeOfTheNewAccount.getValue().equals("Bancario")) {
                OperationJsonFile.newAccount(selectedUser.getUsername(), nameAccount.getText(), 0);
            } else if (typeOfTheNewAccount.getValue().equals("Salvadanaio")) {
                OperationJsonFile.newAccount(selectedUser.getUsername(), nameAccount.getText(), 1);
            } else if (typeOfTheNewAccount.getValue().equals("Investimento")) {
                OperationJsonFile.newAccount(selectedUser.getUsername(), nameAccount.getText(), 2);
            }
            updateBottom();
        });
        
        addUser.setOnAction(event -> {
            OperationJsonFile.initializeUser(nameUser.getText(), lastnameUser.getText(), 
                    fiscalcodeUser.getText(), emailUser.getText(), passwordUser.getText());
            users = OperationJsonFile.readUsers();
            setnewUser = 0;
            updateTop();
            updateBottom();
        });
        topLayout.getChildren().addAll(super.getMenuBar(), userChoise, userNew, newAccount, addingAccount);
        this.root.setTop(topLayout);
    }

    @Override
    protected void updateBottom() {
        // selecting user
        /*final ChoiceBox<String> choiceUser = new ChoiceBox<>();
        for (final User user : users) {
            choiceUser.getItems().add(user.getUsername());
        }
        final HBox userChoise = new HBox(choiceUser);*/
        
        //selecting type
        
        
        final HBox accountType = new HBox(accountName);
        final HBox accountTypeTable = new HBox(accountNameTable);
        accountName.setOnAction((event) -> {
            final String selectedAccount = (String) accountName.getValue();
            
            for (final BankAccountJson bank : selectedUser.getBanks()) {
                if (selectedAccount.equals(bank.getName())) {
                    transactionChart = bank.getTransactions();
                    bankName = selectedAccount;
                    updateCenter();
                    updateLeft();
                }
            }
        });
        accountNameTable.setOnAction((event) -> {
            final String selectedAccount = (String) accountNameTable.getValue();
            tableName = selectedAccount;
            set = 0;
            for (final BankAccountJson bank : selectedUser.getBanks()) {
                if (selectedAccount.equals(bank.getName())) {
                    type = 1;
                    transactionTable = bank.getTransactions();
                    bankName = selectedAccount;
                    updateRight();
                }
            }
            for (final MoneyboxAccountJson box: selectedUser.getMoneyboxes()) {
                if (selectedAccount.equals(box.getName())) {
                    type = 2;
                    transactionTable = box.getTransactions();
                    updateRight();
                }
            }
            for (final InvestmentAccountJson inv: selectedUser.getInvestmentAccounts()) {
                if (selectedAccount.equals(inv.getNameInvestmentAccount())) {
                    transactionTable.clear();
                    type = 3;
                    for (final AssetJson asset : inv.getAssets()) {
                        transactionTable.addAll(asset.getTransactions());
                        updateRight();
                    }
                }
            }
        });
        final Button button = new Button("Show");
        //final TextField startDate = new TextField("01/01/2022 00:00");
        //final TextField endDate = new TextField("31/12/2022 23:59");
        final DateTimePicker startDate = new DateTimePicker();
        final DateTimePicker endDate = new DateTimePicker();
        firstDate = startDate.getDateTimeValue().format(DATATIME);
        secondDate = endDate.getDateTimeValue().format(DATATIME);
        button.setOnAction(event -> {
            firstDate = startDate.getDateTimeValue().format(DATATIME);
            secondDate = endDate.getDateTimeValue().format(DATATIME);
            updateCenter();
            updateLeft();
            updateRight();
        });
        final HBox dates = new HBox(startDate, endDate);
        dates.setMaxWidth(300);
        final Pane bottomLayout = getGadgets().createHorizontalPanel();
        bottomLayout.getChildren().addAll(/*userChoise,*/ accountType, accountTypeTable, dates, button);
        this.root.setBottom(bottomLayout);
    }
    
    /**
     * Here I set the center of the scene with the pie chart
     * */

    @SuppressWarnings("unchecked")
    @Override
    protected void updateCenter() {
        
        PieChart pie = null;
        
        try {
            //once reading json will be fixed we can call a get-Transaction method and put it into the first element
            //once set button on the view for chose date start and date end we will be able to see the asked data
            //pie = PieChartBuilder.builderChart(TestChart.esempioTransaction(), "00/01/2022 00:00", "00/02/2022 00:00");
            pie = PieChartBuilder.builderChartList(bankName, transactionChart, firstDate, secondDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //Creating a stack pane to hold the chart
        final StackPane pane = new StackPane(pie);
        pane.setPadding(new Insets(15, 15, 15, 15));
        pane.setStyle("-fx-background-color: BEIGE");
        

        final Pane centerLayout = getGadgets().createVerticalPanel();
        
        centerLayout.getChildren().addAll(pie);
        this.root.setCenter(centerLayout);
    }
    
    /**
     * Here I set the left side of the scene with the area chart
     * */

    @Override
    protected void updateLeft() {
        final Pane leftLayout = getGadgets().createVerticalPanel();
        
        AreaChart<String, Number> area = null;
        try {
            //once reading json will be fixed we can call a get-Transaction method and put it into the first element
            //once set button on the view for chose date start and date end we will be able to see the asked data
            area = LineChartBuilder.areaChartBuilderList(transactionChart, firstDate, secondDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //Creating a stack pane to hold the chart
        final StackPane pane = new StackPane(area);
        pane.setPadding(new Insets(15, 15, 15, 15));
        pane.setStyle("-fx-background-color: BEIGE");
        
        final Text amountResidue = new Text();
        amountResidue.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        for (final BankAccountJson bank : selectedUser.getBanks()) {
            if (bankName.equals(bank.getName())) {
                amountResidue.setText(bankName + " saldo attuale " + bank.getTotalAmount());
            }
        }
        /*final Text newAccount = new Text();
        newAccount.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        newAccount.setText("Apri nuovo conto");
        final ChoiceBox<String> typeOfTheNewAccount = new ChoiceBox<>();
        typeOfTheNewAccount.getItems().add("Bancario");
        typeOfTheNewAccount.getItems().add("Salvadanaio");
        typeOfTheNewAccount.getItems().add("Investimento");
        final TextField nameAccount = new TextField("Nome Conto");
        final Button add = new Button("Aggiungi");
        final HBox addingAccount = new HBox(typeOfTheNewAccount, nameAccount, add);
        add.setOnAction(event -> {
            if (typeOfTheNewAccount.getValue().equals("Bancario")) {
                OperationJsonFile.newAccount(selectedUser.getUsername(), nameAccount.getText(), 0);
            } else if (typeOfTheNewAccount.getValue().equals("Salvadanaio")) {
                OperationJsonFile.newAccount(selectedUser.getUsername(), nameAccount.getText(), 1);
            } else if (typeOfTheNewAccount.getValue().equals("Investimento")) {
                OperationJsonFile.newAccount(selectedUser.getUsername(), nameAccount.getText(), 2);
            }
            updateBottom();
        });*/
        
        leftLayout.getChildren().addAll(area, amountResidue/*, newAccount, addingAccount*/);
        this.root.setLeft(leftLayout);
    }
    
    /**
     * Here I set the right side of the screen with the table view
     * */

    @Override
    protected void updateRight() {
        
        final Text text = new Text();
        text.setText(tableName);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        final Pane rightLayout = getGadgets().createVerticalPanel();

        TableView<main.jsonfile.TransactionJson> tableView = null;
        try {
            //once reading json will be fixed we can call a get-Transaction method and put it into the first element
            //once set button on the view for chose date start and date end we will be able to see the asked data
            tableView = TableBuilder.buildTableList(transactionTable, firstDate, secondDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Button addAsset = null;
        final ChoiceBox<String> assetChoiser = new ChoiceBox<>();
        for (final InvestmentAccountJson inv: selectedUser.getInvestmentAccounts()) {
            assetChoiser.getItems().clear();
            if (tableName.equals(inv.getNameInvestmentAccount())) {
                for (final AssetJson asset : inv.getAssets()) {
                    assetChoiser.getItems().add(asset.getAssetSymbol());
                }
                addAsset = new Button("Aggiungi Asset");
            }
        }
        

        final TextField name = new TextField("nome transazione");
        //final TextField importo = new TextField("importo");
        final NumberTextField importo = new NumberTextField();
        final TextField currency = new TextField("valuta");
        final TextField nameNewAsset = new TextField("Nome asset");
        final TextField symbolNewAsset = new TextField("simbolo asset");
        //final TextField date = new TextField("01/01/2022");
        //final TextField time = new TextField("00:00");
        final Button button = new Button("Aggiungi");
        name.setMaxWidth(200);
        importo.setMaxWidth(100);
        currency.setMaxWidth(100);
        //date.setMaxWidth(100);
        //time.setMaxWidth(100);
        final DateTimePicker newDate = new DateTimePicker();
        final HBox tryDate = new HBox(newDate);
        tryDate.setMaxWidth(100);
        HBox hbox = null;
        HBox buttons = null;
        if (type == 1) {
            hbox = new HBox(name, tryDate, importo);
            buttons = new HBox(button);
        } else if (type == 2) {
            hbox = new HBox(name, tryDate, importo, currency);
            buttons = new HBox(button);
        } else if (type == 3) {
            hbox = new HBox(name, assetChoiser, tryDate, importo);
            buttons = new HBox(addAsset, button);
            addAsset.setOnAction(event -> {
                set = 1;
                updateRight();
            });
        }
        hbox.setMaxWidth(450);
        if(set == 1) {
            hbox = new HBox(name, tryDate, importo);
            buttons = new HBox(nameNewAsset, symbolNewAsset, button);
        }
        button.setOnAction(event -> {
            final String datetime = newDate.getDateTimeValue().format(DATATIME);
            String nameAsset = assetChoiser.getValue();
            String nameTransaction = name.getText();
            double amount  = importo.getNumber().doubleValue();
            if (set == 0) {
                if (type==1) {
                    OperationJsonFile.newTransaction(selectedUser.getUsername(), tableName, 
                            nameTransaction, amount, datetime.substring(0, 10), datetime.substring(11));
                    selectedUser = OperationJsonFile.readUser(selectedUser.getUsername());
                    for (final BankAccountJson bank : selectedUser.getBanks()) {
                        if (tableName.equals(bank.getName())) {
                            transactionTable = bank.getTransactions();
                        }
                        if (bankName.equals(bank.getName())) {
                            transactionChart = bank.getTransactions();
                        }
                    }
                }
                if (type==2) {
                    OperationJsonFile.newTransaction(selectedUser.getUsername(), tableName, 
                            nameTransaction, currency.getText(), amount, datetime.substring(0, 10),
                            datetime.substring(11));
                    selectedUser = OperationJsonFile.readUser(selectedUser.getUsername());
                    for (final MoneyboxAccountJson box: selectedUser.getMoneyboxes()) {
                        if (tableName.equals(box.getName())) {
                            transactionTable = box.getTransactions();
                        }
                    }
                }
                if (type==3) {
                    OperationJsonFile.newTransactionAsset(selectedUser.getUsername(), tableName, 
                            nameTransaction, assetChoiser.getValue(), amount, datetime.substring(0, 10), 
                            datetime.substring(11));
                    for (final InvestmentAccountJson inv: selectedUser.getInvestmentAccounts()) {
                        selectedUser = OperationJsonFile.readUser(selectedUser.getUsername());
                        if (tableName.equals(inv.getNameInvestmentAccount())) {
                            transactionTable.clear();
                            for (final AssetJson asset : inv.getAssets()) {
                                transactionTable.addAll(asset.getTransactions());
                            }
                        }
                    }
                }
            } else if (set == 1) {
                if (type==3) {
                    OperationJsonFile.newAsset(selectedUser.getUsername(), tableName, 
                            nameNewAsset.getText(), symbolNewAsset.getText());
                    OperationJsonFile.newTransactionAsset(selectedUser.getUsername(), tableName, 
                            nameTransaction, symbolNewAsset.getText(), amount, datetime.substring(0, 10), 
                            datetime.substring(11));
                    for (final InvestmentAccountJson inv: selectedUser.getInvestmentAccounts()) {
                        selectedUser = OperationJsonFile.readUser(selectedUser.getUsername());
                        if (tableName.equals(inv.getNameInvestmentAccount())) {
                            transactionTable.clear();
                            for (final AssetJson asset : inv.getAssets()) {
                                transactionTable.addAll(asset.getTransactions());
                            }
                        }
                    }
                }
            }
            set = 0;
            updateCenter();
            updateLeft();
            updateRight();
        });
        rightLayout.getChildren().addAll(text, tableView, hbox, buttons);
        this.root.setRight(rightLayout);
    }
}
