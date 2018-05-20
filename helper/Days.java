package helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Days {
	
	public List<Rectangle> createDayGrid(AnchorPane days) {
    	List<Rectangle> rects = new ArrayList<>();
    	int m = 0;
    	int d = 0;
    	for(int y = 0; y < 5; y++) {
    		Text day = new Text();
    		d += 1;
			Text month = new Text();
			month.setText(Integer.toString(d));
			month.setY(y*20 + 30);
			month.setX(100);
			days.getChildren().add(month);
    	}
    	for(int i = 1; i <= 52; i++) {
    		for(int j = 1; j <= 5; j++) {
    			if((5*i + j) % 23 == 0 || i + j == 2) {
    				m += 1;
    				Text month = new Text();
    				month.setText(Integer.toString(m));
    				month.setY(0);
    				month.setX(i*20 + 100);
    				days.getChildren().add(month);
    			}
				Rectangle rect = new Rectangle();
    			rect.setHeight(15);
    			rect.setWidth(15);
    			rect.setY(j*20);
    			rect.setX(i*20 + 100);
    			rect.setSmooth(true);
    			rect.setArcWidth(5);
    			rect.setArcHeight(5);
    			rect.setFill(Color.LIGHTGRAY);
    			//days.getChildren().add(rect);
    			rects.add(rect);
			}
		}
    	return rects;
    }
    
    public List<Rectangle> paint(List<Rectangle> rects, int[][] points) {
    	for(int i = 0; i < points.length; i++) {
    		if(points[i][0] <= 260) {
	    		Rectangle rect = rects.get(points[i][0]);
	    		if(points[i][1] > 30) {
	    			rect.setFill(Color.BLACK);
	    		}
	    		else if(points[i][1] > 20) {
	    			rect.setFill(Color.DARKBLUE);
	    		}
	    		else if(points[i][1] > 10) {
	    			rect.setFill(Color.DODGERBLUE);
	    		}
	    		else {
	    			rect.setFill(Color.SKYBLUE);
	    		}
    		}
    	}
    	return rects;
    }
    
    public int[][] getPoints(ObservableList<XYChart.Data<String, Number>> data) throws java.text.ParseException {
    	int[][] result = new int[data.size()][2];
    	System.out.println("DATA SIZE: " + data.size());
    	for(int i = 0; i < data.size(); i++) {
			XYChart.Data<String, Number> point = data.get(i);
			String input = point.getXValue();
			String f = "yyyy-MM-dd";

			SimpleDateFormat df = new SimpleDateFormat(f);
			Date date = df.parse(input);

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int day = cal.get(Calendar.DAY_OF_YEAR);
			int[] p = {day-1, (int) point.getYValue()};
			result[i] = p;
		}
    	return result;
    }
}
