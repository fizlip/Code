package helper;

import app.Intermediate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SalaryText extends Intermediate{
	
	// Lön per sekund bool.
	private static boolean perSec = false;
	private static String suffix = " SEK";
	
	//Lön och relaterade metoder
	private static StringProperty salary = new SimpleStringProperty("0.0" + suffix);
	public static StringProperty property() {return salary;}
		
	public static void toggleSec() {
		// ??
		perSec = !perSec;
		if (perSec) {
			//Konvertera lön till sekunder (1 månad = 30 dagar)
			suffix = " SEK/s";
			double s = Math.round((stringToDouble(salary)/(30*24*60*60)) * Math.pow(10, 7)) /Math.pow(10, 7);
			salary.set(s + suffix);
		}
		else {
			// Konvertera lön till dagar (1 månad = 30 dagar)
			suffix = " SEK";
			double s = Math.round(stringToDouble(salary)*(30*24*60*60));
			salary.set(s + suffix);
		}	
	}
	
	public static void addToSalary(String data) {
		// Gör om lön till en double genom att ta bort valutan och parsa resultatet
		double currentSalary = Double.parseDouble(salary.get().split(" ")[0]);
		// Data är lönen som ska läggas till
		double addedSalary = Double.parseDouble(data);
		if (perSec){
			// Gör om tillaggd lön till sek om perSec är sann
			addedSalary = Math.round((addedSalary/(30*24*60*60)) * Math.pow(10, 7)) / Math.pow(10, 7);
		}
		// Addera gammal och tillagd lön och updatera salary.
		double newSalary = addedSalary + currentSalary;
		String newSalaryString = Double.toString(newSalary) + suffix;
		setProperty(property(), newSalaryString);
	}
	
}
