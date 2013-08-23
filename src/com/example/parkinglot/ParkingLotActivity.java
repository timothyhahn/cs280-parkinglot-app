package com.example.parkinglot;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ParkingLotActivity extends Activity implements OnItemSelectedListener{
	private final String lotURL = "http://tyh25-cs280-parkinglot.herokuapp.com";
	private ArrayList<HashMap<String,String>> lotList;
	private ListView list;
	private String distance = "All";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parking_lot);
		lotList = new ArrayList<HashMap<String,String>>();
		parseLots(downloadLots());
		list = (ListView)findViewById(R.id.lot_list);
		LotAdapter adapter = new LotAdapter(this, lotList);
		list.setAdapter(adapter);
		
		Clicky clicky = new Clicky(this);
		Button addButton = (Button)findViewById(R.id.lot_list_add);
		addButton.setOnClickListener(clicky);
		
		Spinner distanceSpinner = (Spinner)findViewById(R.id.distance_spinner);
		ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.distance_array, android.R.layout.simple_spinner_item);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		distanceSpinner.setAdapter(arrayAdapter);
		distanceSpinner.setOnItemSelectedListener(this);
	}
	class Clicky implements OnClickListener{
		Activity activity;
		public Clicky(Activity activity){
			this.activity = activity;
		}
		@Override
		public void onClick(View arg0) {
			Intent addIntent = new Intent(this.activity, ParkingLotAddActivity.class);
			addIntent.putExtra("id", -1);
			activity.startActivity(addIntent);
		}
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		Spinner distanceSpinner = (Spinner)findViewById(R.id.distance_spinner);
		distance = distanceSpinner.getSelectedItem().toString();
		lotList = new ArrayList<HashMap<String,String>>();
		parseLots(downloadLots());
		list = (ListView)findViewById(R.id.lot_list);
		LotAdapter adapter = new LotAdapter(this, lotList);
		list.setAdapter(adapter);
		list.invalidateViews();
	}
	public String downloadLots(){
		LotTask lt = new LotTask();
		String lotJSON = "";
		try {
			if(distance.equals("All")){
				lt.execute(new URL(lotURL + "/lot"));
			} else {
				int distanceInt = 0;
				if(distance.equals("5 Miles")) {
					distanceInt = 5;
				} else if (distance.equals("25 Miles")) {
					distanceInt = 25;
				} else if (distance.equals("100 Miles")) {
					distanceInt = 100;
				} else {
					Log.v("parkinglot distance", distance);
				}
				
				// I could get the location programatically like I do in ParkingLotAddActivity, but it's not too important.
				float latitude = 10.032222f;
				float longitude = -12.422258f;
				lt.execute(new URL(lotURL + "/lot/distance?distance=" + distanceInt + "&latitude=" + latitude + "&longitude=" + longitude));
			}
			lotJSON = lt.get();
		} catch (Exception e){
			e.printStackTrace();
		}
		return lotJSON;
	}
	
	public void parseLots(String lotJSON){
		lotList.clear();
		JsonParser jp = new JsonParser();
		JsonElement root = null;
		root = jp.parse(lotJSON);
		JsonObject rootobj = root.getAsJsonObject();
		JsonArray lots = rootobj.get("lots").getAsJsonArray();
		for(int i = 0; i < lots.size(); i++){
			JsonObject lot = lots.get(i).getAsJsonObject();
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
			lotList.add(lotHash);
		}
		
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		distance = parent.getSelectedItem().toString();
		lotList = new ArrayList<HashMap<String,String>>();
		parseLots(downloadLots());
		list = (ListView)findViewById(R.id.lot_list);
		LotAdapter adapter = new LotAdapter(this, lotList);
		list.setAdapter(adapter);
		list.invalidateViews();
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}

}
