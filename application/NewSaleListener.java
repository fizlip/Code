package application;

import org.json.simple.JSONObject;

interface NewSaleListener{
	void newSale();
	void newMessage();
	void newOrder(JSONObject customer);
	void statusUpdate();
}

