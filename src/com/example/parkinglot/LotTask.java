package com.example.parkinglot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import android.os.AsyncTask;

public class LotTask extends AsyncTask<URL, Integer, String>{
	
	@Override
	protected String doInBackground(URL... urls) {
		URL lotURL = urls[0];
		String lotJSON = "";
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(lotURL.openStream()));
			String jsonLine;
			while((jsonLine = in.readLine()) != null){
				lotJSON += jsonLine;
			}
			in.close();
		} catch(IOException ie){
			ie.printStackTrace();
		}
		return lotJSON;
	}

}