package main.charts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import main.json.TransactionJson;

public class PieChartBuilder {
    
    /**
     * 
     * this method build a PieChart of the transaction did in a determinate period
     * 
     * @param transaction is a array of TransactionJson 
     * @param sDate1 is a string corresponding to the star date of the period
     * @param sDate2 is a string corresponding to the end date of the period
     * 
     * @return PieChart with the only data of the input period, with the total profit and total expenditure 
     * 
     * */
    
    public static PieChart builderChart(TransactionJson[] transaction, String sDate1, String sDate2) throws ParseException {
        
        Date date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(sDate1);
        Date date2 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(sDate2);
        
        double ricavi = 0;
        double spese = 0;
        
        for(int i=0; i<transaction.length; i++) {
            Date dateTrans = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(transaction[i].getDate()+" "+transaction[i].getTime());
            if(dateTrans.after(date1) && dateTrans.before(date2)) {
                if(transaction[i].getAmount() < 0) {
                    spese += (-1*transaction[i].getAmount());
                } else {
                    ricavi += transaction[i].getAmount();
                };
            }
        }
        
      //Preparing ObservbleList object         
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
           new PieChart.Data("Ricavi " + ricavi + "$", ricavi), 
           new PieChart.Data("Spese " + spese + "$", spese)); 
         
        //Creating a Pie chart 
        PieChart pieChart = new PieChart(pieChartData); 
                
        //Setting the title of the Pie chart 
        pieChart.setTitle("Entrate e Uscite"); 
         
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
    
    public static PieChart builderChartList(final String name, final List<main.jsonfile.TransactionJson> transactions, final String sDate1, final String sDate2)
            throws ParseException {
        
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

}
