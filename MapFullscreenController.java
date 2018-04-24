package app;

import com.lynden.gmapsfx.GoogleMapView;
import javafx.fxml.FXML;
import com.lynden.gmapsfx.GoogleMapView;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import Http.GetLatLong;

import com.lynden.gmapsfx.MapComponentInitializedListener;

public class MapFullscreenController implements MapComponentInitializedListener, Initializable{
    
	private GoogleMap map;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GoogleMapView gMap;
    
    public static Map<Double[], String[]> postionMap = new HashMap<>();

    @Override
    public void mapInitialized() {
    	MapOptions mapOptions = new MapOptions();
        
        mapOptions.center(new LatLong(62.5359, 16.3751))
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .streetViewControl(false)
                .scaleControl(false)
                .zoom(4);

        map = gMap.createMap(mapOptions);
        JSONArray latLongData = GetLatLong.getLatLongData();
        for(Map.Entry<Double[], String[]> indvPos : postionMap.entrySet()) {
        	Double lat = indvPos.getKey()[0];
        	Double lng = indvPos.getKey()[1];
        	LatLong pos = new LatLong(lat, lng);
        	String name = indvPos.getValue()[0];
        	String status = indvPos.getValue()[1];
        	addMarker(pos, name, status);
        }
        
    }
    
    private void addMarker(LatLong pos, String name, String status) {
    	MarkerOptions options = new MarkerOptions();
    	options.position(pos);
    	if(status.equals("Order")) {
	    	//Order
	    	options.icon("https://upload.wikimedia.org/wikipedia/commons/thumb/3/35/Location_dot_blue.svg/4px-Location_dot_blue.svg.png");
    	}
    	else if(status.equals("Makulerad")) {
		    //Makulerad
	    	options.icon("https://upload.wikimedia.org/wikipedia/commons/thumb/9/92/Location_dot_red.svg/4px-Location_dot_red.svg.png");
    	}
    	else if(status.equals("Kund")) {
		    //Kund
	    	options.icon("https://upload.wikimedia.org/wikipedia/commons/thumb/6/60/Lightgreen_pog.svg/4px-Lightgreen_pog.svg.png");
    	}
    	else if(status.equals("Fakturerad")) {
		    //Fakturerad
	    	options.icon("https://upload.wikimedia.org/wikipedia/commons/thumb/0/01/Yellow_ffff00_pog.svg/4px-Yellow_ffff00_pog.svg.png");
    	}
    	else {
		    //Inget
	    	options.icon("https://upload.wikimedia.org/wikipedia/commons/thumb/c/c0/Location_dot_black.svg/4px-Location_dot_black.svg.png");
    	}
	    Marker marker = new Marker(options);
        marker.setPosition(pos);
        marker.setTitle(name);
        map.addMarker(marker);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	gMap.addMapInializedListener(this);
        assert gMap != null : "fx:id=\"gMap\" was not injected: check your FXML file 'MapFullscreen.fxml'.";

    }
}
