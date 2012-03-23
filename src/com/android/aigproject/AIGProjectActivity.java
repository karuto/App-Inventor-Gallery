package com.android.aigproject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AIGProjectActivity extends Activity implements OnClickListener {

	Button search;
	TextView result;
	EditText query;
	Button switchTo;
	ImageView waiting;

	private ListView listView1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		search = (Button) findViewById(R.id.button1);
		search.setOnClickListener(this);
		waiting = (ImageView) findViewById(R.id.waiting);
		waiting.setVisibility(View.INVISIBLE); 


		query = (EditText) findViewById(R.id.editText1);
		result = (TextView) findViewById(R.id.textView1);

		// ListItem listview_data[] = new ListItem[20];
		// String imageFileURL =
		// "http://lh6.ggpht.com/JL2goqwVeu8ds9vGYTUPPcw4pF93TEgAnt0YG6eAaxzSE8W2sLa6cmw5bFoxNcgTPPCgJZ6SQWZE0dAj9Fo6trLft9S8pWOdPQ=s100";
		//
		// for (int i = 0; i < 20; i++) {
		// String text = "API Picture " + i;
		// listview_data[i] = new ListItem(R.drawable.ic_launcher, text,
		// imageFileURL);
		// }

		// switchTo = (Button) findViewById(R.id.button2);
		// switchTo.setOnClickListener(this);

		// ListItem listview_data[] = new ListItem[]
		// {
		// new ListItem(R.drawable.ic_launcher, "S1"),
		// new ListItem(R.drawable.ic_launcher, "S2"),
		// new ListItem(R.drawable.ic_launcher, "S3"),
		// new ListItem(R.drawable.ic_launcher, "S4"),
		// new ListItem(R.drawable.ic_launcher, "S5"),
		// new ListItem(R.drawable.ic_launcher, "S6"),
		// new ListItem(R.drawable.ic_launcher, "S4")
		// };

		// Defines the layout of each row in ListView.
		ListItem listview_data2[] = new ListItem[0]; /*
													 * begin with 0, so nothing
													 * in there
													 */
		MainListAdapter adapter = new MainListAdapter(this, R.layout.list_item,
				listview_data2);

		listView1 = (ListView) findViewById(R.id.listView1);

		View header = (View) getLayoutInflater().inflate(R.layout.list_header,
				null);
		listView1.addHeaderView(header);
		listView1.setAdapter(adapter);

		listView1.setOnItemClickListener(new MyListViewListener());

		
		View.OnClickListener buttonhandler =new View.OnClickListener() {
			public void onClick(View v) {
//		    	   Log.d("km-main","enter onClick");
				   switch(v.getId()) { 
				   // Now, which button did they press, and take me to that class/activity
				       case R.id.buttoncats:    
//				    	   Log.d("km-main","enter button");
				    	   Intent gotoCategory = new Intent(AIGProjectActivity.this,
									CategoryActivity.class);
				    	   startActivity(gotoCategory);				    	   
				    	   
				       break;
				   }				
				
			}
		};
		
		// bind listener to button
		Button catb = (Button) findViewById(R.id.buttoncats);
		catb.setOnClickListener(buttonhandler);

		
		
	}

	private void createNewThread(final Context context) {

		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					requestSearchingData(context);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();

	}

	public void requestSearchingData(final Context context) throws MalformedURLException,
			IOException {

		Runnable r = new Runnable() {
			public void run() {
				String URL = "http://app-inventor-gallery.appspot.com/rpc?tag=search:";
				ArrayList<HashMap<String, Object>> jSonInfo = retrieveQueryArray(URL, query
						.getText().toString());
				ListItem listview_data[] = new ListItem[jSonInfo.size()];
				for (int i = 0; i < jSonInfo.size(); i++) {
					listview_data[i] = new ListItem(R.drawable.ic_launcher,
							(String) jSonInfo.get(i).get("title"),
							(String) jSonInfo.get(i).get("image1"));

				}

				MainListAdapter adapter = new MainListAdapter(context,
						R.layout.list_item, listview_data);
				listView1.setAdapter(adapter);
			}
		};
		//r.run();
		
		
		runOnUiThread(r);
            

	}

	


	
	
	
	
	
	class MyListViewListener implements OnItemClickListener {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub

			Intent nextScreen = new Intent(getApplicationContext(),
					SecondScreenActivity.class);

			// Sending data to another Activity

			ListItem curItem = (ListItem) (parent.getAdapter()
					.getItem(position));

			// Log.e("n", inputName.getText()+"."+ inputEmail.getText());
			nextScreen.putExtra("name", curItem.title);
			nextScreen.putExtra("imageURL", curItem.imageFileURL);
			Toast.makeText(AIGProjectActivity.this,
					"position of " + curItem.title, Toast.LENGTH_SHORT).show();

			startActivity(nextScreen);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_up_out);

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String URL = "http://app-inventor-gallery.appspot.com/rpc?tag=search:";

		if (v == search) { 
//			visible 0 Visible on screen; the default value.  
//			invisible 1 Not displayed, but taken into account during layout (space is left for it).  
//			gone 2 Completely hidden, as if the view had not been added.  
			
			
			
			
			//v.setBackgroundColor(Color.RED);
			v.setClickable(false);
			waiting.setVisibility(View.VISIBLE);
			createNewThread(this);
			v.setClickable(true);
			waiting.setVisibility(View.INVISIBLE);
			
			
			
			
			
			
			
			//v.setBackgroundColor(Color.BLUE);
//			ArrayList<HashMap<String, Object>> jSonInfo = processQuery(query
//					.getText().toString(), URL);
//			ListItem listview_data[] = new ListItem[jSonInfo.size()];
//			for (int i = 0; i < jSonInfo.size(); i++) {
//				listview_data[i] = new ListItem(R.drawable.ic_launcher,
//						(String) jSonInfo.get(i).get("title"),
//						(String) jSonInfo.get(i).get("image1"));
//
//			}
//
//			MainListAdapter adapter = new MainListAdapter(this,
//					R.layout.list_item, listview_data);
//			listView1.setAdapter(adapter);

		}
		// else if (v == switchTo){
		// // Intent it = new Intent(v.getContext(), Activity2.class);
		// // startActivity(it);
		//
		//
		// }
	}

	private Bitmap loadImageByURL(String imageFileURL) {
		try {
			URL url = new URL(imageFileURL);
			URLConnection conn = url.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = httpConn.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				inputStream.close();
				return bitmap;
			} else {
				// return null;
			}

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static ArrayList<HashMap<String, Object>> retrieveQueryArray(String URL) {
		String s;
		JSONArray results;
		s = UrlReader.generalGet(URL);
		if (s == null) {
			return null;
		}
		try {
			JSONObject o = new JSONObject(s);
			results = (JSONArray) o.get("result");
			Log.d("MAIN",String.valueOf(results.length()));
			return parseArrayResult(results);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;

	
	
	}
	
	public static ArrayList<HashMap<String, Object>> retrieveQueryArray(String URL, String query) {
		String s;
		JSONArray results;
		s = UrlReader.search(query, URL);			
		if (s == null) {
			return null;
		}
		try {
			JSONObject o = new JSONObject(s);
			results = (JSONArray) o.get("result");
			Log.d("MAIN",String.valueOf(results.length()));
			return parseSearchResult(results);					

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;

	}
	
	/*
	 * Returns an arraylist with String as keys and no objects. 
	 * ONLY USED FOR TRAVERSING CATEGORY. (as of now, 2012/03/22)
	 */
	private static ArrayList<HashMap<String, Object>> parseArrayResult(JSONArray results) {

		StringBuffer sb = new StringBuffer();
		ArrayList<HashMap<String, Object>> jSonInfo = new ArrayList<HashMap<String, Object>>();

		Log.d("MAIN", "ARRAY ### ");
		
		HashMap<String, Object> newEle;
		String s;
		try {
			for (int i = 0; i < results.length(); i++) {
				s = results.getString(i);
				newEle = new HashMap<String, Object>();
				newEle.put(s,null);
				Log.d("MAIN", "newele ### "+newEle.toString());
				jSonInfo.add(newEle);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("MAIN", "ARRAY FAILED");
			sb.append("***************************************");
		}

		return jSonInfo;
	
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
				newEle.put("title", singleLine.get("title"));
				newEle.put("description", singleLine.get("description"));
				newEle.put("image1", singleLine.get("image1"));
				newEle.put("sourceFileName", singleLine.get("sourceFileName"));

				jSonInfo.add(newEle);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			sb.append("***************************************");
		}

		return jSonInfo;

	}

}