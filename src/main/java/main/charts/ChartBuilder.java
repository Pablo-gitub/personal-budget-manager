package main.charts;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import main.jsonfile.TransactionJson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class ChartBuilder {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat DATE_FORMAT_TIME = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static AreaChart<String, Number> areaChartBuilderList(final List<main.jsonfile.TransactionJson> transactions, final String sDate1, final String sDate2) throws ParseException {
        if (transactions == null) {
            // Handle null transactions list (e.g., throw an exception or return a default chart)
            return new AreaChart<>(new CategoryAxis(), new NumberAxis());
        }
        
        Date date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(sDate1);
        Date date2 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(sDate2);  
        
        //Defining the x an y axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        //Setting labels for the axes
        xAxis.setLabel("Date");
        yAxis.setLabel("Transaction");
        //Creating a line chart
        AreaChart<String, Number> areaChart = new AreaChart<String, Number>(xAxis, yAxis);
        areaChart.setTitle("Transaction durin " + sDate1 + "-" + sDate2); 
        //Preparing the data points for the line1
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        
        series1.getData().clear();
        series2.getData().clear();
        double uscite = 0;
        double entrate = 0;
        
        
        for(final main.jsonfile.TransactionJson transaction : transactions) {
            final Date dateTrans = new SimpleDateFormat("dd/MM/yyyy").parse(transaction.getDate());
            
            if(dateTrans.after(date1) && dateTrans.before(date2)) {
                if(transaction.getAmount() < 0) {
                    uscite += -1*transaction.getAmount();
                    series2.getData().add(new XYChart.Data(transaction.getDate(), uscite));
                    series1.getData().add(new XYChart.Data(transaction.getDate(), entrate));
                } else {
                    entrate += transaction.getAmount();
                    series1.getData().add(new XYChart.Data(transaction.getDate(), entrate));
                    series2.getData().add(new XYChart.Data(transaction.getDate(), uscite));
                }
            } 
        }
            
        //Setting the name to the line (series)
        series1.setName("Guadagni (" + entrate + "€)");
        series2.setName("Spese (" + uscite + "€)");
        //Setting the data to Line chart
        areaChart.getData().addAll(series1, series2);
        
        return areaChart;
    }

    public static PieChart builderChartList(final String name, final List<main.jsonfile.TransactionJson> transactions, final String sDate1, final String sDate2)
            throws ParseException {
        if (transactions == null) {
            // Handle null transactions list (e.g., return an empty PieChart)
            return new PieChart();
        }
        
        final Date date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(sDate1);
        final Date date2 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(sDate2);
        
        double ricavi = 0;
        double spese = 0;
        
        for (final main.jsonfile.TransactionJson transaction : transactions) {
            final Date dateTrans = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(transaction.getDate() 
                    + " " + transaction.getTime());
            if (dateTrans.after(date1) && dateTrans.before(date2)) {
                if (transaction.getAmount() < 0) {
                    spese += -1 * transaction.getAmount();
                } else {
                    ricavi += transaction.getAmount();
                }
            }
        }
        
      //Preparing ObservbleList object         
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
           new PieChart.Data("Ricavi " + ricavi + "€", ricavi), 
           new PieChart.Data("Spese " + spese + "€", spese)); 
         
        //Creating a Pie chart 
        final PieChart pieChart = new PieChart(pieChartData); 
                
        //Setting the title of the Pie chart 
        pieChart.setTitle("Entrate e Uscite " + name); 
         
        //setting the direction to arrange the data 
        pieChart.setClockwise(true); 
         
        //Setting the length of the label line 
        pieChart.setLabelLineLength(50); 

        //Setting the labels of the pie chart visible  
        pieChart.setLabelsVisible(true); 
         
        //Setting the start angle of the pie chart  
        pieChart.setStartAngle(180);     
           
        return pieChart;
        
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