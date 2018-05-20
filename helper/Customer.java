package helper;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.util.*;

//JSON
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Customer extends Object{
	
	
	private Map<String, StringProperty> mapping = new HashMap<>();
	
	public Customer() {}
	
	//Namn
	private StringProperty nameText = new SimpleStringProperty("Namn");
	public StringProperty nameTextProperty() {return nameText;}
	//Personnummer
	private StringProperty personalIDText = new SimpleStringProperty("Personnummer");
	public StringProperty personalIDTextProperty() {return this.personalIDText;}
	//Telefon
	private StringProperty telephoneText = new SimpleStringProperty("Telefon");
	public StringProperty telephoneTextProperty() {return telephoneText;}
	//Date
	private StringProperty dateText = new SimpleStringProperty("Datum");
	public StringProperty dateTextProperty() {return dateText;}
	//Postnummer
	private StringProperty postalCodeText = new SimpleStringProperty("Postnummer");
	public StringProperty postalCodeTextProperty() {return postalCodeText;}
	//Ort
	private StringProperty stateText = new SimpleStringProperty("Ort");
	public StringProperty stateTextProperty() {return stateText;}
	//E-mail
	private StringProperty eMailText = new SimpleStringProperty("E-mail");
	public StringProperty eMailTextProperty() {return eMailText;}
	//Mailfaktura
	private StringProperty mailBillingText = new SimpleStringProperty("Mailfaktura");
	public StringProperty mailBillingTextProperty() {return mailBillingText;}
	//FSG
	private StringProperty FSGText = new SimpleStringProperty("FSG (SEK)");
	public StringProperty FSGTextProperty() {return FSGText;}
	//Betalt
	private StringProperty paidText = new SimpleStringProperty("Betalt");
	public StringProperty paidTextProperty() {return paidText;}
	//Mak
	private StringProperty makText = new SimpleStringProperty("Mak");
	public StringProperty makTextProperty() {return makText;}
	//Status
	private StringProperty statusText = new SimpleStringProperty("status");
	public StringProperty statusTextProperty() {return statusText;}
	//Räddat
	private StringProperty savedText = new SimpleStringProperty("Räddat");
	public StringProperty savedTextProperty() {return savedText;}
	//Paydate
	private StringProperty payDateText = new SimpleStringProperty("Betaldatum");
	public StringProperty payDateTextProperty() {return payDateText;}
	//Tjänst
	private StringProperty serviceText = new SimpleStringProperty("Tjänst");
	public StringProperty serviceTextProperty() {return serviceText;}
	//Customernumber
	private StringProperty customerNumberText = new SimpleStringProperty("Kundnummer");
	public StringProperty customerNumberTextProperty() {return customerNumberText;}
	//Kommentar
	private StringProperty commentText = new SimpleStringProperty("Kommentar");
	public StringProperty commentTextProperty() {return commentText;} 
	//Adress
	private StringProperty adressText = new SimpleStringProperty("Adress");
	public StringProperty adressTextProperty() {return adressText;} 
	//Kundpris
	private StringProperty customerPriceText = new SimpleStringProperty("Kundpris");
	public StringProperty customerPriceTextProperty() {return customerPriceText;}
	//Kund service kommentar
	private StringProperty customerServiceCommentText = new SimpleStringProperty("Kommentar1");
	public StringProperty customerServiceCommentTextProperty() {return customerServiceCommentText;}
	//Bricknummer
	private StringProperty badgeNumberText = new SimpleStringProperty("Bricknummer");
	public StringProperty badgeNumberTextProperty() {return badgeNumberText;}
	
	public String get(String var) {
		if(mapping.get(var) != null) {return mapping.get(var).get();}
		return "0";
	}
	
	public void put(String key, String value) {
		try {
			mapping.get(key).set(value);
		}
		catch(NullPointerException e) {
			updateText(key, value);
		}
	}
	public void updateText(String var, String text) {
		if(var.equals("Personnummer")) {personalIDTextProperty().set(text);mapping.put("Personnummer", personalIDText);}
		else if(var.equals("Namn")) {nameTextProperty().set(text);mapping.put("Namn", nameTextProperty());}
		else if(var.equals("Telefon")) {telephoneTextProperty().set(text);mapping.put("Telefon", telephoneTextProperty());}
		else if(var.equals("Datum")) {dateTextProperty().set(text);mapping.put("Datum", dateTextProperty());}
		else if(var.equals("Postnummer")) {postalCodeTextProperty().set(text);mapping.put("Postnummer", postalCodeTextProperty());}
		else if(var.equals("Ort")) {stateTextProperty().set(text);mapping.put("Ort", stateTextProperty());}
		else if(var.equals("E-mail")) {eMailTextProperty().set(text);mapping.put("E-mail", eMailTextProperty());}
		else if(var.equals("Mailfaktura")) {mailBillingTextProperty().set(text);mapping.put("Mailfaktura", mailBillingTextProperty());}
		else if(var.equals("FSG (SEK)")) {FSGTextProperty().set(text);mapping.put("FSG (SEK)", FSGTextProperty());}
		else if(var.equals("Betalt")) {paidTextProperty().set(text);mapping.put("Betalt", paidTextProperty());}
		else if(var.equals("Mak")) {makTextProperty().set(text);mapping.put("Mak", makTextProperty());}
		else if(var.equals("Status")) {statusTextProperty().set(text);mapping.put("Status", statusTextProperty());}
		else if(var.equals("Räddat")) {savedTextProperty().set(text);mapping.put("Räddat", savedTextProperty());}
		else if(var.equals("Betaldatum")) {payDateTextProperty().set(text);mapping.put("Betaldatum", payDateTextProperty());}
		else if(var.equals("Tjänst")) {serviceTextProperty().set(text);mapping.put("Tjänst", serviceTextProperty());}
		else if(var.equals("Kundnummer")) {customerNumberTextProperty().set(text);mapping.put("Kundnummer", customerNumberTextProperty());}
		else if(var.equals("Kommentar")) {commentTextProperty().set(text);mapping.put("Kommentar", commentTextProperty());}
		else if(var.equals("Adress")) {adressTextProperty().set(text);mapping.put("Adress", adressTextProperty());}
		else if(var.equals("Kundpris")) {customerPriceTextProperty().set(text);mapping.put("Kundpris", customerPriceTextProperty());}
		else if(var.equals("Kommentar1")) {customerServiceCommentTextProperty().set(text);mapping.put("Kommentar1", customerServiceCommentTextProperty());}
		else if(var.equals("Bricknummer")) {badgeNumberTextProperty().set(text);mapping.put("Bricknummer", badgeNumberTextProperty());}
	}
	
}
