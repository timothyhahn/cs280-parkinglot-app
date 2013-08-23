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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ParkingLotViewActivity extends Activity {
	private final String lotURL = "http://tyh25-cs280-parkinglot.herokuapp.com";
	private int id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_lot);
		Intent intent = getIntent();
		id = intent.getIntExtra("id", 1);
		HashMap<String,String> lot = parseLot(downloadLot(id));
		TextView lotName = (TextView)findViewById(R.id.detail_lot_name);
		TextView lotPrice = (TextView)findViewById(R.id.detail_lot_price);
		TextView lotHours = (TextView)findViewById(R.id.detail_lot_hours);
		
		lotName.setText(lot.get("name"));
		lotPrice.setText(lot.get("pricing"));
		lotHours.setText(lot.get("hours"));
		Clicky clicky = new Clicky(this,id);
		Button editButton = (Button)findViewById(R.id.detail_lot_edit);
		editButton.setOnClickListener(clicky);
		
		DeleteClicky deleteClicky = new DeleteClicky(this,id);
		Button deleteButton = (Button)findViewById(R.id.detail_lot_delete);
		deleteButton.setOnClickListener(deleteClicky);
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
			Intent addIntent = new Intent(this.activity, ParkingLotAddActivity.class);
			addIntent.putExtra("id", id);
			activity.startActivity(addIntent);
		}
		
	}
	class DeleteClicky implements OnClickListener{
		Activity activity;
		int id;
		public DeleteClicky(Activity activity, int id){
			this.activity = activity;
			this.id = id;
		}
		@Override
		public void onClick(View arg0) {
			LotPostTask lpt = new LotPostTask();

			String postURL = lotURL + "/lot/delete/" + id;
			
			lpt.execute(postURL, String.valueOf(id), "", "", "", "", "");
			activity.finish();
		}
		
	}
	@Override
	protected void onResume(){
		super.onResume();
		HashMap<String,String> lot = parseLot(downloadLot(id));
		TextView lotName = (TextView)findViewById(R.id.detail_lot_name);
		TextView lotPrice = (TextView)findViewById(R.id.detail_lot_price);
		TextView lotHours = (TextView)findViewById(R.id.detail_lot_hours);
		
		lotName.setText(lot.get("name"));
		lotPrice.setText(lot.get("pricing"));
		lotHours.setText(lot.get("hours"));
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

}
