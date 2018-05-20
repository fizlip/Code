package helper;

import application.Intermediate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.text.DecimalFormat;


public class SalaryText extends Intermediate{
	
	// Lön per sekund bool.
	private static boolean perSec = false;
	private static String suffix = " SEK";
	private static String format = "#.######";
	private static DecimalFormat newFormat = new DecimalFormat(format);
	private static double s = 0;
	
	//Lön och relaterade metoder
	private static StringProperty salary = new SimpleStringProperty("0.0" + suffix);
	public static StringProperty property() {return salary;}
	public static boolean keepRunning = false;
	public static double made = 0;
		
	public static void toggleSec() {
		// ??
		perSec = !perSec;
		if (perSec) {
			//Konvertera lön till sekunder (1 månad = 30 dagar)
			keepRunning = true;
			suffix = " SEK";
			s = Math.round((stringToDouble(salary)/(30*24*60*600)) * Math.pow(10, 7)) /Math.pow(10, 7);
			Thread perSecThread = new Thread("count money sec thread") {
				public void run() {
					double moneyMade = 0;
					while(keepRunning) {
						moneyMade += s;
						try {
							salary.set(newFormat.format(moneyMade) + suffix);
							sleep(600);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
			perSecThread.start();
		}
		else {
			keepRunning = false;
			// Konvertera lön till dagar (1 månad = 30 dagar)
			suffix = " SEK";
			s = Math.round(s*(30*24*60*600));
			salary.set(s + suffix);
		}	
	}
	
	public static void addToSalary(String data) {
		// Gör om lön till en double genom att ta bort valutan och parsa resultatet
		double currentSalary = Double.parseDouble(salary.get().split(" ")[0].replace(',', '.'));
		// Data är lönen som ska läggas till
		double addedSalary = Double.parseDouble(data);
		if (perSec){
			// Gör om tillaggd lön till sek om perSec är sann
			addedSalary = Math.round((addedSalary/(30*24*60*600)) * Math.pow(10, 7)) / Math.pow(10, 7);
		}
		// Addera gammal och tillagd lön och updatera salary.
		double newSalary = addedSalary + currentSalary;
		s += addedSalary;
		String newSalaryString = Double.toString(newSalary) + suffix;
		setProperty(property(), newSalaryString);
	}
	
}
