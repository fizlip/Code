package helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javafx.scene.chart.*;
import java.util.Date;

import javafx.scene.chart.XYChart;
import javafx.scene.shape.Rectangle;

public class DateHandler {
	
	public static String getCurrentTime() {
		DateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return currentTime.format(date);
	}
	
	public static List<Long> getFormattedDate(Map<String, Number> mapping){
	    
		/*
		 * convert time to seconds
		 */
		
		//Get formatted date for sorting purposes
	    List<Long> contained = new ArrayList<>();
	    contained.add(0, 0L);
	    for(Map.Entry<String, Number> mappingInfo : mapping.entrySet()) {
			String date = mappingInfo.getKey();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			try {
	    		//Put date in the contained list for later counting
				LocalDate lDate = LocalDate.parse(date, formatter);
	    		contained.add(lDate.getLong(ChronoField.EPOCH_DAY));
			}
			catch(DateTimeParseException e){}
	    }
		return sortDate(contained);
	}
    
    public static List<Long> sortDate(List<Long> minValues) {
    	
    	for(int i = 1; i < minValues.size()-1; i++) {
    		int oldI = i;
    		while(minValues.get(i) < minValues.get(i-1) && i > 1) {
    			Long oldVal = minValues.get(i-1);
    			minValues.set(i-1, minValues.get(i));
    			minValues.set(i, oldVal);
    			i--;
    		}
    		
    		i = oldI;
    	}
    	return minValues;
    }
    
    public static XYChart.Series<String, Number> getDateSeries(XYChart.Series<String, Number> series, 
    														Map<String, Number> mapping, List<Long> contained){
    	
    	contained = getFormattedDate(mapping);
	    for(int j = 0; j < contained.size()-1; j++) {   				
			//Format time to milliseconds
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    	String formatedTime = formatter.format(TimeUnit.DAYS.toMillis(contained.get(j)));
	    	Number amountSold = mapping.get(formatedTime);
	    	
	    	if(amountSold != null) {
	    		
	    		XYChart.Data<String, Number> input = new XYChart.Data<>(formatedTime, amountSold);
	    		Rectangle rect = new Rectangle(0, 0);
	    		rect.setVisible(false);
	    		input.setNode(rect);
	    		
	    		series.getData().add(input);
	    		
	    	}
		}
	    return series;
	}
	
}
