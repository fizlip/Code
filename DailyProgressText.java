package app;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DailyProgressText extends Intermediate{
	
	//Egenskap f�r dagsm�taren
	private static DoubleProperty progress = new SimpleDoubleProperty(0.0);
	public static DoubleProperty progressProperty() {return progress;}
	//Egenskap f�r texten av dagsm�let
	private static StringProperty goalText = new SimpleStringProperty("5000 SEK");
	public static StringProperty goalProperty() {return goalText;}
	
	public static void subFromGoal(StringProperty p, String data) {
		//Ta fram texten fr�n egenskapen
		String goalText = (String)getProperty(p);
		
		// G�r om texten till double och subtrahera den givna datan.
		double currentGoal = Double.parseDouble(goalText.
								substring(0, goalText.length()-4));
		String newGoal = Double.toString(currentGoal - Double.parseDouble(data)) + " SEK";
		
		// Skriv in nya m�let i egenskapen
		setProperty(p, newGoal);	
	}
}
