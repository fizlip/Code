package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.*;

import helper.ReadFile;

public class AmountSoldText {
	//Personbästa
	private static StringProperty personalBest = new SimpleStringProperty("10");
	public static StringProperty bestProperty() {return personalBest;}
	//Antal sälj
	
	private static Map<String, String> amountSold = ReadFile.getFile(
			"C:\\Users\\f_ill\\eclipse-workspace\\UsecDashboard\\src\\application\\Usec\\userData.txt");
	private static StringProperty salesAmount = new SimpleStringProperty(amountSold.get("salesToday"));
	public static StringProperty salesProperty() {return salesAmount;}
	
}
