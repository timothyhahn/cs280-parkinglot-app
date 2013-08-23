package com.example.parkinglot;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

public class LotPostTask extends AsyncTask<String, Integer,String>{

	@Override
	protected String doInBackground(String... params) {
		HttpClient httpclient;
		HttpPost httppost;
		
		String postURL = params[0];
		
		int id = Integer.parseInt(params[1]);
		String name = params[2];
		String pricing = params[3];
		String hours = params[4];
		String latitude = params[5];
		String longitude = params[6];
		
		httpclient = new DefaultHttpClient();
		
		
		httppost = new HttpPost(postURL);
		List<NameValuePair> formValues = new ArrayList<NameValuePair>();
		formValues.add(new BasicNameValuePair("name",name));
		formValues.add(new BasicNameValuePair("pricing",pricing));
		formValues.add(new BasicNameValuePair("hours",hours));
		formValues.add(new BasicNameValuePair("latitude",latitude));
		formValues.add(new BasicNameValuePair("longitude",longitude));
		
		
		try {
			httppost.setEntity(new UrlEncodedFormEntity(formValues, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}

}
