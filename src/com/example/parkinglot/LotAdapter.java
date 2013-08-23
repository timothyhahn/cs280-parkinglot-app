package com.example.parkinglot;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LotAdapter extends BaseAdapter{
	private Activity activity;
	private ArrayList<HashMap<String,String>> lotList;
	private static LayoutInflater inflater = null;
	
	public LotAdapter(Activity activity, ArrayList<HashMap<String,String>> data){
		this.activity = activity;
		this.lotList = data;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return lotList.size();
	}
	@Override
	public HashMap<String,String> getItem(int position) {
		return lotList.get(position);
	}
	@Override
	public long getItemId(int position) {
		return Long.parseLong(lotList.get(position).get("id"));
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(convertView == null)
			view = inflater.inflate(R.layout.list_row, null);
		TextView name = (TextView)view.findViewById(R.id.lot_name);
		TextView price = (TextView)view.findViewById(R.id.lot_price);
		final HashMap<String,String> lot = lotList.get(position);
		name.setText(lot.get("name"));
		price.setText(lot.get("pricing"));
		Clicker clicky = new Clicker(activity, Integer.parseInt(lot.get("id")));
		
		view.setOnClickListener(clicky);
		return view;
	}
	public class Clicker implements OnClickListener {
		Activity activity;
		int id;
		public Clicker(Activity activity, int id){
			this.activity = activity;
			this.id = id;
		}
		@Override
		public void onClick(View v) {
			Log.v("clicky", "clicky");
			Intent showLot = new Intent(this.activity, ParkingLotViewActivity.class);
			showLot.putExtra("id", id);
			activity.startActivity(showLot);
		}
		
	}

}
