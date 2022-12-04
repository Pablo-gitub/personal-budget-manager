package main.charts;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.json.TransactionJson;

public class TableBuilder {
    
    
    /**
     * 
     * this method build a TableView of the transaction did in a determinate period
     * 
     * @param transaction is a array of TransactionJson 
     * @param sDate1 is a string corresponding to the star date of the period
     * @param sDate2 is a string corresponding to the end date of the period
     * 
     * @return TableView with the only data of the input period,
     *          that's a graphic showing of all the transaction did with their name and data time
     * 
     * */
    
    @SuppressWarnings({ "null", "unchecked" })
    public static TableView<TransactionJson> buildTable(TransactionJson[] transaction, String sDate1, String sDate2) throws ParseException{ 
        
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
        Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);    
        
        TableView<TransactionJson> tableView = new TableView<TransactionJson>();
        
        final Label label = new Label("Spese");
        label.setFont(new Font("Arial", 20));
 
        tableView.setEditable(true);
 
        TableColumn<TransactionJson, String> nameTransactionCol = new TableColumn<>("Transazione");
        nameTransactionCol.setMinWidth(100);
 
        TableColumn<TransactionJson, String> dateCol = new TableColumn<>("Data");
        dateCol.setMinWidth(50);
        
 
        TableColumn<TransactionJson, String> timeCol = new TableColumn<>("Ora");
        timeCol.setMinWidth(50);
        
        TableColumn<TransactionJson, Number> amountCol = new TableColumn<>("Importo");
        amountCol.setMinWidth(70);
        

        nameTransactionCol.setCellValueFactory(new PropertyValueFactory<>("nameTransaction"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        
        tableView.getColumns().addAll(nameTransactionCol, dateCol, timeCol, amountCol);
        
        ObservableList<TransactionJson> data1 = FXCollections.observableArrayList();
        
        for(int i=0; i<transaction.length; i++) {
            
            Date dateTrans = new SimpleDateFormat("dd/MM/yyyy").parse(transaction[i].getDate());
            
            if(dateTrans.after(date1) && dateTrans.before(date2)) {
                
                data1.addAll(transaction[i]);
                
            }
            
        }
 
        tableView.setItems(data1);
        
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        return tableView;
    }
    
    public static TableView<main.jsonfile.TransactionJson> buildTableList(final List<main.jsonfile.TransactionJson> transactions, String sDate1, String sDate2) throws ParseException{ 
        
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
        Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);  
        
        final TableView<main.jsonfile.TransactionJson> tableView = new TableView<>();
        
        final Label label = new Label("Spese");
        label.setFont(new Font("Arial", 20));
 
        tableView.setEditable(true);
 
        final TableColumn<main.jsonfile.TransactionJson, String> nameTransactionCol = new TableColumn<>("Transazione");
        nameTransactionCol.setMinWidth(100);
        final TableColumn<main.jsonfile.TransactionJson, String> dateCol = new TableColumn<>("Data");
        dateCol.setMinWidth(40);
        final TableColumn<main.jsonfile.TransactionJson, String> timeCol = new TableColumn<>("Ora");
        timeCol.setMinWidth(40);
        final TableColumn<main.jsonfile.TransactionJson, Number> amountCol = new TableColumn<>("Importo");
        amountCol.setMinWidth(50);
        final TableColumn<main.jsonfile.TransactionJson, String> currencyCol = new TableColumn<>("valuta");
        currencyCol.setMinWidth(40);
        
        nameTransactionCol.setCellValueFactory(new PropertyValueFactory<>("nameTransaction"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        currencyCol.setCellValueFactory(new PropertyValueFactory<>("currency"));
        tableView.getColumns().addAll(nameTransactionCol, dateCol, timeCol, amountCol, currencyCol);
        
        final ObservableList<main.jsonfile.TransactionJson> data1 = FXCollections.observableArrayList();
        
        for(final main.jsonfile.TransactionJson transaction : transactions) {
            
            Date dateTrans = new SimpleDateFormat("dd/MM/yyyy").parse(transaction.getDate());
            
            if(dateTrans.after(date1) && dateTrans.before(date2)) {
                String inverseDate = transaction.getDate().substring(6) + transaction.getDate().substring(2,6) + transaction.getDate().substring(0,2);
                transaction.setDate(inverseDate);
                data1.addAll(transaction);
                
            }
            
        }
        
        tableView.setItems(data1);
        
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        return tableView;
    }
}
