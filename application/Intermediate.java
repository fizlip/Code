package application;


import javafx.beans.property.*;

interface IntermediateInterface{
	
}

/*
 * Abstrakt klass för objekt som ska uppdateras mellan fönster
 */
public abstract class Intermediate {
	
	// Sätt given data till egenskapen med p.
	public static void setProperty(StringProperty p, String data) {p.set(data);}
	public static void setProperty(DoubleProperty p, double data) {p.set(data);}
	// Få ut data ifrån given egenskap p.
	public static String getProperty(StringProperty p) {return p.get();}
	public static double getProperty(DoubleProperty p) {return p.get();}
	
	public static double stringToDouble(StringProperty p) {
		String salaryString = p.get();
		String salaryNoCur = salaryString.split(" ")[0];
		Double s = Double.parseDouble(salaryNoCur.replace(',', '.'));
		return s;
	}
	
	
}
