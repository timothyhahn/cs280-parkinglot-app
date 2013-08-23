package com.example.parkinglot;

import java.net.URL;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ParkingLotAddActivity extends Activity  implements LocationListener {
	private final String lotURL = "http://tyh25-cs280-parkinglot.herokuapp.com";

	private String latitude = "0.0000";
	private String longitude = "0.0000";


	private LocationManager locationManager;
	private String provider;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_lot);
		Intent intent = getIntent();
		int id = intent.getIntExtra("id", 0);

		Button changeButton = (Button)findViewById(R.id.lot_add);
		if(id > 0){
			changeButton.setText("Edit");
			HashMap<String,String> lot = parseLot(downloadLot(id));
			EditText name = (EditText)findViewById(R.id.add_lot_name);
			EditText price = (EditText)findViewById(R.id.add_lot_price);
			EditText hours = (EditText)findViewById(R.id.add_lot_hours);
			name.setText(lot.get("name"));
			price.setText(lot.get("pricing"));
			hours.setText(lot.get("hours"));
		} else{
			changeButton.setText("Add");
		}
		Clicky clicky = new Clicky(this, id);
		changeButton.setOnClickListener(clicky);
		

	   locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, false);
	    Location location = locationManager.getLastKnownLocation(provider);
	    if(location != null) {
	    	onLocationChanged(location);
	    } 
	}

	  @Override
	  protected void onResume() {
	    super.onResume();
	    locationManager.requestLocationUpdates(provider, 400, 1, this);
	  }
    
	  @Override
	  public void onLocationChanged(Location location) {
		  longitude = String.valueOf(location.getLongitude());
		  latitude = String.valueOf(location.getLatitude());
		    Log.v("parkinglot", String.valueOf(location.getLongitude()));
		    Log.v("parkinglot", String.valueOf(location.getLatitude()));
	  }
	  
	class Clicky implements OnClickListener{
		Activity activity;
		int id;
		public Clicky(Activity activity, int id){
			this.activity = activity;
			this.id = id;
		}
		@Override
		public void onClick(View arg0) {
			
			LotPostTask lpt = new LotPostTask();

			String postURL = lotURL + "/lot";
			if(id > 0)
				postURL += "/" + id;
			Log.v("parkinglot", postURL);
			EditText nameE = (EditText)findViewById(R.id.add_lot_name);
			EditText priceE = (EditText)findViewById(R.id.add_lot_price);
			EditText hoursE = (EditText)findViewById(R.id.add_lot_hours);
			String name = nameE.getText().toString();
			String pricing = priceE.getText().toString();
			String hours = hoursE.getText().toString();
			Log.v("parkinglot", pricing);
			
			lpt.execute(postURL, String.valueOf(id), name, pricing, hours, latitude, longitude);
			activity.finish();
		}
		
	}

	public String downloadLot(int id){
		LotTask lt = new LotTask();
		String lotJSON = "";
		try {
			lt.execute(new URL(lotURL + "/lot/" + id));
			lotJSON = lt.get();
		} catch (Exception e){
			e.printStackTrace();
		}
		return lotJSON;
	}
	
	public HashMap<String, String> parseLot(String lotJSON){
		JsonParser jp = new JsonParser();
		JsonElement root = null;
		root = jp.parse(lotJSON);
		JsonObject lot = root.getAsJsonObject();
			String id = lot.get("id").getAsString();
			String name = lot.get("name").getAsString();
			String hours = lot.get("hours").getAsString();
			String pricing = lot.get("pricing").getAsString();
			String latitude = lot.get("latitude").getAsString();
			String longitude = lot.get("longitude").getAsString();
			
			HashMap<String, String>lotHash = new HashMap<String, String>();
			lotHash.put("id", id);
			lotHash.put("name", name);
			lotHash.put("hours", hours);
			lotHash.put("pricing", pricing);
			lotHash.put("latitude", latitude);
			lotHash.put("longitude", longitude);
			
		return lotHash;
	}


	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}


}
