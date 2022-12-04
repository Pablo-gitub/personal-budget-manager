package main.charts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import main.json.TransactionJson;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class LineChartBuilder {
    
    /**
     * 
     * this method build a LineChart of the transaction did in a determinate period
     * 
     * @param transaction is a array of TransactionJson 
     * @param sDate1 is a string corresponding to the star date of the period
     * @param sDate2 is a string corresponding to the end date of the period
     * 
     * @return the Line chart with the only data of the input period
     * 
     * */
    
    public static LineChart<String, Number> chartNumberCategory(TransactionJson[] transaction, String sDate1, String sDate2) throws ParseException {
        
        Date date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(sDate1);
        Date date2 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(sDate2);  
        
        //Defining the x an y axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        //Setting labels for the axes
        xAxis.setLabel("Date");
        yAxis.setLabel("Transaction");
        //Creating a line chart
        LineChart<String, Number> linechart = new LineChart<String, Number>(xAxis, yAxis);
        //Preparing the data points for the line1
        XYChart.Series series1 = new XYChart.Series();
        
        for(int i=0; i<transaction.length; i++) {
            Date dateTrans = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(transaction[i].getDate()+" "+transaction[i].getTime());
            if(dateTrans.after(date1) && dateTrans.before(date2)) {
                series1.getData().add(new XYChart.Data(transaction[i].getDate(), transaction[i].getAmount()));
            }
        }
        //Setting the name to the line (series)
        series1.setName(transaction[0].getCurrency());
        //Setting the data to Line chart
        linechart.getData().addAll(series1);
        
        linechart.setTitle("Transaction");
        
        return linechart;
    }
    
    /**
     * 
     * this method build a AreaChart of the transaction did in a determinate period
     * 
     * @param transaction is a array of TransactionJson 
     * @param sDate1 is a string corresponding to the star date of the period
     * @param sDate2 is a string corresponding to the end date of the period
     * 
     * @return AreaChart with the only data of the input period, with two different color
     *          one for the expenditure and one for the profit
     * 
     * */
    
    public static AreaChart<String, Number> areaChartBuilder(TransactionJson[] transaction, String sDate1, String sDate2) throws ParseException {
        
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
        Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);  
        
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
        
        double uscite = 0;
        double entrate = 0;
        
        
        for(int i=0; i<transaction.length; i++) {
            Date dateTrans = new SimpleDateFormat("dd/MM/yyyy").parse(transaction[i].getDate());
            
            if(dateTrans.after(date1) && dateTrans.before(date2)) {
                if(transaction[i].getAmount() < 0) {
                    uscite += -1*transaction[i].getAmount();
                    series2.getData().add(new XYChart.Data(transaction[i].getDate(), uscite));
                    series1.getData().add(new XYChart.Data(transaction[i].getDate(), entrate));
                } else {
                    entrate += transaction[i].getAmount();
                    series1.getData().add(new XYChart.Data(transaction[i].getDate(), entrate));
                    series2.getData().add(new XYChart.Data(transaction[i].getDate(), uscite));
                }
            } 
        }
            
        //Setting the name to the line (series)
        series1.setName("Guadagni (" + entrate + "$)");
        series2.setName("Spese (" + uscite + "$)");
        //Setting the data to Line chart
        areaChart.getData().addAll(series1, series2);
        
        return areaChart;
    }
    
    public static AreaChart<String, Number> areaChartBuilderList(final List<main.jsonfile.TransactionJson> transactions, final String sDate1, final String sDate2) throws ParseException {
        
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
    
}
