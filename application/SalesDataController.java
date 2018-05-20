package application;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SalesDataController {

	public static JSONObject data;
	public static JSONObject customers;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane dataTable;

    private String workMonth() {
    	int salesCount = 0;
    	for(Data<String, Number> date : AdminController.series.getData()) {
    		String input = date.getXValue();
			String f = "yyyy-MM-dd";

			SimpleDateFormat df = new SimpleDateFormat(f);
			Date d;
			try {
				d = df.parse(input);
				Calendar cal = Calendar.getInstance();
				cal.setTime(d);
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date now = new Date();
				Calendar nowC = Calendar.getInstance();
				nowC.setTime(dateFormat.parse(dateFormat.format(now)));
				int nowMonth = nowC.get(Calendar.MONTH);
				int month = cal.get(Calendar.MONTH);
				if(nowMonth == month) {salesCount += 1;}
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	}
    	return Integer.toString(salesCount);
    }
    
    private String soldMonth() {
    	int salesCount = 0;
    	for(Data<String, Number> date : AdminController.series.getData()) {
    		String input = date.getXValue();
			String f = "yyyy-MM-dd";

			SimpleDateFormat df = new SimpleDateFormat(f);
			Date d;
			try {
				d = df.parse(input);
				Calendar cal = Calendar.getInstance();
				cal.setTime(d);
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date now = new Date();
				Calendar nowC = Calendar.getInstance();
				nowC.setTime(dateFormat.parse(dateFormat.format(now)));
				int nowMonth = nowC.get(Calendar.MONTH);
				int month = cal.get(Calendar.MONTH);
				if(nowMonth == month) {salesCount += date.getYValue().intValue();}
			} catch (ParseException e) {
				e.printStackTrace();
			}
    	}
    	return Integer.toString(salesCount);
    }
    
    private String fsgMonth() {
    	double fsg = 0.0;
    	int month = 0;
    	String input = "";
    	String datum = "";
    	String status = "";
    	String f = "yyyy-MM-dd";
    	
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date now = new Date();
		Calendar nowC = Calendar.getInstance();
		try {
			nowC.setTime(dateFormat.parse(dateFormat.format(now)));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		int nowMonth = nowC.get(Calendar.MONTH);
		
    	for(Object id : customers.keySet()) {
    		JSONObject customer = (JSONObject) customers.get(id);
    		
    		try {
	    		status = (String) customer.get("Status");
	    		datum = (String) customer.get("Datum");
	    		input = datum;
    		}catch(Exception e) {
    			status = Long.toString( (Long) customer.get("Status"));
	    		datum = (String) customer.get("Datum");
	    		input = datum;
    		}

			SimpleDateFormat df = new SimpleDateFormat(f);
			Date d;
			try {
				d = df.parse(datum);
				Calendar cal = Calendar.getInstance();
				cal.setTime(d);
				month = cal.get(Calendar.MONTH);
			}catch(ParseException e) {
				System.out.println("Detta är datumet: " + datum);
				e.printStackTrace();
			}
    		
			
			System.out.println("THIS IS FSG:");
			System.out.println(status);
			System.out.println(nowMonth);
			System.out.println(month);
			
    		if(status.equals("Kund") && nowMonth == month) {
    			String cFsg = (String) customer.get("FSG (SEK)");
    			System.out.println("THIS IS FSG");
    			try {
    				Double cash = Double.parseDouble(cFsg.replace(',', '.'));
    				fsg += cash;
    			}catch(NumberFormatException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	System.out.println("FSG Month: " + fsg);
    	return Double.toString(fsg);
    }
    
    @FXML
    void initialize() {
    	
    	String amountSold = (String) data.get("tot sälj").toString();
    	String fsg;
    	String workDays;
    	String mvp;
    	String amountWorkMonth;
    	String soldMonth;
    	String fsgMonth;
    	
    	try {
    		fsg = (String) data.get("FSG").toString();
    	}
    	catch(NullPointerException e) {
    		fsg = "0";
    	}
    	try {
    		workDays = Integer.toString(AdminController.series.getData().size());
    	}
    	catch(NullPointerException e) {
    		workDays = "0";
    	}
    	try {
    		mvp = (String) data.get("mvp").toString();
    	}
    	catch(NullPointerException e) {
    		mvp = "0";
    	}
    	try {
        	amountWorkMonth = workMonth();

    	}
    	catch(Exception e) {
    		amountWorkMonth = "0";
    	}
    	try {
    		soldMonth = soldMonth();
    	}
    	catch(Exception e) {
    		soldMonth = "0";
    	}
    	try {
    		fsgMonth = fsgMonth();
    	}
    	catch(Exception e) {
    		fsgMonth = "0";
    	}
    	
    	Text soldText = new Text(amountSold);
    	soldText.setFont(Font.font("Times New Roman", 14));
    	Text fsgText = new Text(fsg);
    	fsgText.setFont(Font.font("Times New Roman", 14));
    	Text workText = new Text(workDays);
    	workText.setFont(Font.font("Times New Roman", 14));
    	Text avgText = new Text("medel pris");
    	avgText.setFont(Font.font("Times New Roman", 14));
    	Text mvpText = new Text(mvp);
    	mvpText.setFont(Font.font("Times New Roman", 14));
    	Text monthWorkText = new Text(amountWorkMonth);
    	monthWorkText.setFont(Font.font("Times New Roman", 14));
    	Text soldMonthText = new Text(soldMonth);
    	soldMonthText.setFont(Font.font("Times New Roman", 14));
    	Text fsgMonthText = new Text(fsgMonth);
    	fsgMonthText.setFont(Font.font("Times New Roman", 14));
    	
    	dataTable.add(soldText, 1, 1);
    	dataTable.add(fsgText, 2, 1);
    	dataTable.add(workText, 3, 1);
    	dataTable.add(avgText, 4, 1);
    	dataTable.add(mvpText, 5, 1);
    	dataTable.add(soldMonthText, 1, 2);
    	dataTable.add(fsgMonthText, 2, 2);
    	dataTable.add(monthWorkText, 3, 2);
    }
}
