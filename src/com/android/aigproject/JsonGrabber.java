package com.android.aigproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonGrabber {
	
	public static ArrayList<HashMap<String, Object>> retrieveQueryArray(String URL){
		
		//URL = "http://app-inventor-gallery.appspot.com/rpc?tag=search:sports";
		String s = null;
		JSONArray results;
		Log.d("before: ", "Regular1");
		try {
			s = UrlReader.search(URL);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Log.d("Exception info:", e1.getMessage());
			return null;
		}
		
		if(s == null) return null;
		Log.d("got it", s);
		if(s.startsWith("<html>")){
			Log.d("yes", "got it");
			return null;
		}
		
	
		Log.d("after: ", "Regular2");
		
		try {
			Log.d("URL", URL);
			Log.d("JSon",String.valueOf(s));			
			JSONObject o = new JSONObject(s);
			results = (JSONArray) o.get("result");
			Log.d("MAIN",String.valueOf(results.length()));
			return parseSearchResult(results);
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return null;
		
		
	
		
	}
	
	
	public static ArrayList<HashMap<String, Object>> retrieveQueryArrayByUID(String URL){
		String s;
		JSONObject results;

		Log.d("before: ", "UID1");
		try {
			s = UrlReader.search(URL);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Log.d("Exception info:", e1.getMessage());
			return null;
		}
		
		if(s == null) return null;
		if(s.startsWith("<html>")) return null;
		
	
		Log.d("after: ", "UID2");
		try {
			JSONObject o = new JSONObject(s);
			results = (JSONObject) ((JSONObject) o.get("result")).get("app");
			Log.d("result", results.toString());
			Log.d("MAIN",String.valueOf(results.length()));
			return parseSearchResultForUID(results);
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	
	
	
	/*
	 * Returns an arraylist with JSON Objects as values, used for general browsing
	 * like searching, viewing a Apps-list, etc...
	 */
	private static ArrayList<HashMap<String, Object>> parseSearchResult(JSONArray results) {

		StringBuffer sb = new StringBuffer();

		ArrayList<HashMap<String, Object>> jSonInfo = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> newEle;
		try {
			for (int i = 0; i < results.length(); i++) {
				JSONObject singleLine = results.getJSONObject(i);

				// System.out.println(singleLine.get("title"));
				// System.out.println(singleLine.get("description"));
				// System.out.println(singleLine.get("image1"));
				// System.out.println(singleLine.get("sourceFileName"));
				// System.out.println("-----------------");
				newEle = new HashMap<String, Object>();
				Iterator it = singleLine.keys();
				while(it.hasNext()){
					String key = it.next().toString();					
					newEle.put(key, singleLine.get(key));									
				}				
//				newEle.put("title", singleLine.get("title"));
//				newEle.put("description", singleLine.get("description"));
//				newEle.put("image1", singleLine.get("image1"));
//				newEle.put("sourceFileName", singleLine.get("sourceFileName"));

				jSonInfo.add(newEle);

			}
		} catch (JSONException e) {
			sb.append("***************************************");
		}

		return jSonInfo;

	}
	
	
	
	/*
	 * Returns an arraylist with JSON Objects as values, used for general browsing
	 * like searching, viewing a Apps-list, etc...
	 */
	private static ArrayList<HashMap<String, Object>> parseSearchResultForUID(JSONObject result) {

		StringBuffer sb = new StringBuffer();

		ArrayList<HashMap<String, Object>> jSonInfo = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> newEle;
		try {
			//for (int i = 0; i < results.length(); i++) {
				JSONObject singleLine = result;

				// System.out.println(singleLine.get("title"));
				// System.out.println(singleLine.get("description"));
				// System.out.println(singleLine.get("image1"));
				// System.out.println(singleLine.get("sourceFileName"));
				// System.out.println("-----------------");
				newEle = new HashMap<String, Object>();
				Iterator it = singleLine.keys();
				while(it.hasNext()){
					String key = it.next().toString();					
					newEle.put(key, singleLine.get(key));									
				}				
//				newEle.put("title", singleLine.get("title"));
//				newEle.put("description", singleLine.get("description"));
//				newEle.put("image1", singleLine.get("image1"));
//				newEle.put("sourceFileName", singleLine.get("sourceFileName"));
				Log.d("newHash", newEle.toString());
				jSonInfo.add(newEle);

			//}
		} catch (JSONException e) {
			sb.append("***************************************");
		}

		return jSonInfo;

	}
}
