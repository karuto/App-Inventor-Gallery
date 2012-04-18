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
		
		
		String s;
		JSONArray results;
		s = UrlReader.search(URL);			
		if (s == null) {
			return null;
		}
		try {
			JSONObject o = new JSONObject(s);
			results = (JSONArray) o.get("result");
			Log.d("MAIN",String.valueOf(results.length()));
			return parseSearchResult(results);
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
	
}
