package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.ListView;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Circle;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import helper.ReadCustomerData;
import helper.Register;

public class CustomerWindowController {

	public ObservableList<String> comments = FXCollections.observableArrayList();
	private JSONObject customer;
	private String id;
	public static BooleanProperty customerUpdate = new SimpleBooleanProperty(false);
	public static StringProperty currentId = new SimpleStringProperty();
	public static String currentCustomer;
	public static tcpCom.NetworkConnection connection;

	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private AnchorPane background;
    
    @FXML
    private Text statusText;
    
    @FXML
    private Text emailText;
    
    @FXML
    private Text salesmanName;
    
    @FXML
    private Text customerNameText;

    @FXML
    private Text personalIdText;

    @FXML
    private Text adressText;

    @FXML
    private Text postalIdAndStateText;

    @FXML
    private TextField personalIdTextField;

    @FXML
    private TextField adressTextField;

    @FXML
    private TextField postalIdAndStateTextField;

    @FXML
    private Text latestChangeText;

    @FXML
    private ListView<?> changeList;

    @FXML
    private Text customerIdText;

    @FXML
    private Text telephoneText;

    @FXML
    private Text badgeIdText;
    
    @FXML
    private Text serviceText;
    
    @FXML
    private Text priceText;
    
    @FXML
    private Text dateText;

    @FXML
    private TextField customerIdTextField;

    @FXML
    private TextField telephoneTextField;

    @FXML
    private TextField badgeIdTextField;
    
    @FXML
    private Rectangle orderRect;
    
    @FXML
    private Rectangle customerRect;
    
    @FXML
    private Rectangle billedRect;
    
    @FXML
    private Rectangle makRect;
    
    @FXML
    private ListView<String> commentList;
    
    @FXML
    private Group editCustomerGroup;
    
    @FXML
    private Group commentGroup;
    
    @FXML
    private TextArea commentArea;
    
    @FXML
    private Circle editCircle;
    
    //Status rect actions
    private void nullifyRects() {
    	billedRect.setHeight(11);
    	customerRect.setHeight(11);
    	makRect.setHeight(11);
    	orderRect.setHeight(11);
    }
    
    @FXML
    void billedRectPressed(MouseEvent event) {
    	nullifyRects();
    	billedRect.setHeight(20);
    	latestChangeText.setText("Senaste ändring: Sälj fakturerad" );
    }

    @FXML
    void makRectPressed(MouseEvent event) {
    	nullifyRects();
    	makRect.setHeight(20);
    	latestChangeText.setText("Senaste ändring: Sälj makulerad" );
    }

    @FXML
    void customerRectPressed(MouseEvent event) {
    	nullifyRects();
    	customerRect.setHeight(20);
    	latestChangeText.setText("Senaste ändring: Sälj blir kund");
    }
    
    @FXML
    void orderRectPressed(MouseEvent event) {
    	nullifyRects();
    	orderRect.setHeight(20);
    	latestChangeText.setText("Senaste ändring: Order");
    }
    
    @FXML
    void adressPressed(MouseEvent event) {
    	openTextField(adressTextField, adressText);
    }

    @FXML
    void badgeIdPressed(MouseEvent event) {
    	openTextField(badgeIdTextField, badgeIdText);
    }

    @FXML
    void closeAdress(KeyEvent event) {
    	customer.put("Adress", adressTextField.getText());
    	closeField(adressTextField, event, adressText);
    }

    @FXML
    void closeBadgeId(KeyEvent event) {
    	customer.put("Bricknummer", badgeIdTextField.getText());
    	closeField(badgeIdTextField, event, badgeIdText);
    }

    @FXML
    void closeCustomerId(KeyEvent event) {
    	customer.put("Kundnummer", customerIdTextField.getText());
    	closeField(customerIdTextField, event, customerIdText);
    }

    @FXML
    void closePersonalId(KeyEvent event) {
    	customer.put("Personnummer", personalIdTextField.getText());
    	closeField(personalIdTextField, event, personalIdText);
    }

    @FXML
    void closePostalIdAndState(KeyEvent event) {
    	customer.put("Postnummer", postalIdAndStateTextField.getText());
    	closeField(postalIdAndStateTextField, event, postalIdAndStateText);
    }

    @FXML
    void closeTelephone(KeyEvent event) {
    	customer.put("Telefon", telephoneTextField.getText());
    	closeField(telephoneTextField, event, telephoneText);
    }

    @FXML
    void customerIdPressed(MouseEvent event) {
    	openTextField(customerIdTextField, customerIdText);
    }

    @FXML
    void personalIdPressed(MouseEvent event) {
    	openTextField(personalIdTextField, personalIdText);
    }

    @FXML
    void postalIdAndStatePressed(MouseEvent event) {
    	openTextField(postalIdAndStateTextField, postalIdAndStateText);
    }

    @FXML
    void telephonePressed(MouseEvent event) {
    	openTextField(telephoneTextField, telephoneText);

    }
    
    @FXML
    void updateCustomer(MouseEvent event) {    	
    	commentGroup.setVisible(true);
    	//System.out.println(customer.toJSONString());
    }
    
    private void openTextField(TextField t, Text binding) {
    	t.setVisible(true);
    	t.setText(binding.getText());
    }
    

    private void closeField(TextField field, KeyEvent event, Text writeText) {
    	if(event.getCode().equals(KeyCode.ENTER)) {
    		if (!writeText.getText().equals(field.getText())) {
    			latestChangeText.setText("Senaste ändring 2018-03-19 Filip Zlatoidsky ändrade " + writeText.getText() + " till " + field.getText());
    			writeText.setText(field.getText());
    			editCircle.setStrokeWidth(5);
    			editCircle.setStroke(Color.valueOf("#0c6dce"));
    			//items.add("2018-03-19 Filip Zlatoidsky ändrade " + writeText.getText() + " till " + field.getText());
	    		//commentPane.setVisible(true);
    		}
    		field.setVisible(false);
    	}
    }
    
    @FXML
    void commentFinished(KeyEvent event) {
    	if(event.getCode().equals(KeyCode.ENTER)) {
    		if(commentArea.getText().length() != 0) {
    			comments.add(commentArea.getText());
    			commentGroup.setVisible(false);
    			editCircle.setStrokeWidth(1);
    			customer.put("Kommentar", customer.get("Kommentar") + " " + commentArea.getText());
    			editCircle.setStroke(Color.valueOf("#242424"));
    			customerUpdate.set(true);
    			currentId.set(customerIdText.getText());
    			currentCustomer = customer.toJSONString();
    			try {
					connection.send(
							LoginController.user + 
							" - UPDATE: " + currentId.get() + ": "
							+ salesmanName.getText() + ": " + 
							currentCustomer + ": " + "EMPTY");
				} catch (Exception e) {
					e.printStackTrace();
				}
    			//Register.add(customer, id, id);
    		}
    	}
    }
    
    public void initWindow(String id){
    	JSONObject main = (JSONObject) ReadCustomerData.readData("C:\\Users\\f_ill\\eclipse-workspace\\NewSystem\\data.json");
    	JSONObject isgg = (JSONObject) main.get("isgg");
    	JSONObject sales = (JSONObject) isgg.get("försäljning");
    	Object[] names = sales.keySet().toArray();
    	for(Object n : names) {
    		String name = n.toString();
    		JSONObject salesMan = (JSONObject) sales.get(name);
    		try {
    			JSONObject customers = (JSONObject) salesMan.get("kunder");
    			Object[] ids = customers.keySet().toArray();
	    		for(Object i : ids) {
	    			String cId = i.toString();
	    			if(cId.contains(id.trim())) {
	    				System.out.println("Customer ID: " + cId);
	    				customer = (JSONObject) customers.get(id);
	    				this.id = id;
	    				System.out.println(customer.toJSONString());
	    				//Set customer specific text
	    				customerNameText.setText(customer.get("Namn").toString());
	    				personalIdText.setText(customer.get("Personnummer").toString());
	    				adressText.setText(customer.get("Adress").toString());
	    				postalIdAndStateText.setText(customer.get("Postnummer") + " " + customer.get("Ort"));
	    				customerIdText.setText(customer.get("Kundnummer").toString());
	    				telephoneText.setText(customer.get("Telefon").toString());
	    				badgeIdText.setText(customer.get("Bricknummer").toString());
	    				serviceText.setText(customer.get("Tjänst").toString());
	    				priceText.setText(customer.get("Kundpris").toString());
	    				dateText.setText(customer.get("Datum").toString());
	    				emailText.setText("E-Mail: " + customer.get("E-Mail").toString());
	    				
	    				String paid = customer.get("Status").toString();
	    				if(paid.equals("Kund")) {
	    					statusText.setText(customer.get("Betaldatum").toString() + " Betalt: " + customer.get("Betalt").toString());
	    				}
	    				else if(paid.equals("Makulerad")) {
	    					statusText.setText("Mak: " + customer.get("Mak").toString());
	    				}
	    				else {
	    					statusText.setVisible(false);
	    				}
	    				
	    				salesmanName.setText(name);
	    				
	    				comments.add(customer.get("Datum") + ": " + customer.get("Kommentar").toString());
	    				if(!customer.get("Kommentar1").toString().equals("")) {
	    					comments.add(customer.get("Kommentar1").toString());
	    				}
	    				
	    				commentList.setItems(comments);
	
	    				if(customer.get("Status").toString().equals("Makulerad")) {
	    					makRect.setHeight(20);
	    				}
	    				else if(customer.get("Status").toString().equals("Kund")) {
	    					customerRect.setHeight(20);
	    				}
	    				else if(customer.get("Status").toString().equals("Order")) {
	    					orderRect.setHeight(20);
	    				}
	    				else if(customer.get("Status").toString().equals("Fakturerad")) {
	    					billedRect.setHeight(20);
	    				}
	    				else {
	    					System.out.println("inget");
	    				}
	    			}
	    		}
    		}catch(NullPointerException e) {}
    	}
    	
    }
    
    public void giveConnection(tcpCom.NetworkConnection conn) {
    	connection = conn;
    }
    
    @FXML
    void initialize() {
    	connection = CustomerPaneController.connection;
    }
}