package application;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

public class Initiater {

	private List<NewSaleListener> listeners = new ArrayList<NewSaleListener>();
	public void addListener(NewSaleListener toAdd) {
		listeners.add(toAdd);
	}
	public void addSale() {
		for(NewSaleListener h1 : listeners) {
			h1.newSale();
		}
	}
	public void addMessage() {
		for(NewSaleListener h1 : listeners) {
			h1.newMessage();
		}
	}
	public void addOrder(JSONObject customer) {
		for(NewSaleListener h1 : listeners) {
			h1.newOrder(customer);
		}
	}
	
	public void updateStatus() {
		for(NewSaleListener h1 : listeners) {
			h1.statusUpdate();
		}
	}
	
}
