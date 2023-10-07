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

    public static AreaChart<String, Number> createAreaChart(List<TransactionJson> transactions, String StringStartDate, String StringEndDate) throws ParseException {
        Date startDate = DATE_FORMAT_TIME.parse(StringStartDate);
        Date endDate = DATE_FORMAT_TIME.parse(StringEndDate);
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        yAxis.setLabel("Transaction");
        AreaChart<String, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setTitle("Transaction during " + DATE_FORMAT.format(startDate) + "-" + DATE_FORMAT.format(endDate));

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series1.setName("Guadagni");
        series2.setName("Spese");

        transactions.stream()
                .filter(transaction -> isWithinDateRange(transaction, startDate, endDate))
                .forEach(transaction -> {
                    String date = transaction.getDate();
                    double amount = transaction.getAmount();
                    if (amount < 0) {
                        series2.getData().add(new XYChart.Data<>(date, -amount));
                    } else {
                        series1.getData().add(new XYChart.Data<>(date, amount));
                    }
                });

        areaChart.getData().addAll(series1, series2);
        return areaChart;
    }

    public static PieChart createPieChart(String name, List<TransactionJson> transactions, String StringStartDate, String StringEndDate) throws ParseException {
        Date startDate = DATE_FORMAT_TIME.parse(StringStartDate);
        Date endDate = DATE_FORMAT_TIME.parse(StringEndDate);
        double ricavi = 0;
        double spese = 0;

        for (TransactionJson transaction : transactions) {
            if (isWithinDateRange(transaction, startDate, endDate)) {
                double amount = transaction.getAmount();
                if (amount < 0) {
                    spese += -amount;
                } else {
                    ricavi += amount;
                }
            }
        }

        PieChart.Data ricaviData = new PieChart.Data("Ricavi", ricavi);
        PieChart.Data speseData = new PieChart.Data("Spese", spese);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(ricaviData, speseData);

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Entrate e Uscite " + name);
        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(50);
        pieChart.setLabelsVisible(true);
        pieChart.setStartAngle(180);

        return pieChart;
    }

    public static TableView<TransactionJson> createTransactionTable(List<TransactionJson> transactions, String StringStartDate, String StringEndDate) throws ParseException {
        Date startDate = DATE_FORMAT.parse(StringStartDate);
        Date endDate = DATE_FORMAT.parse(StringEndDate);
        TableView<TransactionJson> tableView = new TableView<>();
        Label label = new Label("Spese");
        label.setFont(new Font("Arial", 20));
        tableView.setEditable(true);

        TableColumn<TransactionJson, String> nameTransactionCol = new TableColumn<>("Transazione");
        nameTransactionCol.setMinWidth(100);
        TableColumn<TransactionJson, String> dateCol = new TableColumn<>("Data");
        dateCol.setMinWidth(40);
        TableColumn<TransactionJson, String> timeCol = new TableColumn<>("Ora");
        timeCol.setMinWidth(40);
        TableColumn<TransactionJson, Number> amountCol = new TableColumn<>("Importo");
        amountCol.setMinWidth(50);
        TableColumn<TransactionJson, String> currencyCol = new TableColumn<>("Valuta");
        currencyCol.setMinWidth(40);

        nameTransactionCol.setCellValueFactory(new PropertyValueFactory<>("nameTransaction"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        currencyCol.setCellValueFactory(new PropertyValueFactory<>("currency"));

        List<TransactionJson> filteredTransactions = transactions.stream()
                .filter(transaction -> isWithinDateRange(transaction, startDate, endDate))
                .collect(Collectors.toList());

        tableView.getItems().addAll(filteredTransactions);
        tableView.getColumns().addAll(nameTransactionCol, dateCol, timeCol, amountCol, currencyCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return tableView;
    }

    private static boolean isWithinDateRange(TransactionJson transaction, Date startDate, Date endDate) {
        try {
            Date transactionDate = DATE_FORMAT.parse(transaction.getDate());
            return !transactionDate.before(startDate) && !transactionDate.after(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}