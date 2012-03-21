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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AIGProjectActivity extends Activity implements OnClickListener {

	Button search;
	TextView result;
	EditText query;
	Button switchTo;

	private ListView listView1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		search = (Button) findViewById(R.id.button1);
		search.setOnClickListener(this);
		query = (EditText) findViewById(R.id.editText1);
		result = (TextView) findViewById(R.id.textView1);

		ListItem listview_data[] = new ListItem[20];
		String imageFileURL = "http://lh6.ggpht.com/JL2goqwVeu8ds9vGYTUPPcw4pF93TEgAnt0YG6eAaxzSE8W2sLa6cmw5bFoxNcgTPPCgJZ6SQWZE0dAj9Fo6trLft9S8pWOdPQ=s100";

		for (int i = 0; i < 20; i++) {
			String text = "API Picture " + i;
			listview_data[i] = new ListItem(R.drawable.ic_launcher, text,
					imageFileURL);
		}
		
//		switchTo = (Button) findViewById(R.id.button2);
//		switchTo.setOnClickListener(this);

		
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
		MainListAdapter adapter = new MainListAdapter(this, R.layout.list_item,
				listview_data);

		listView1 = (ListView) findViewById(R.id.listView1);

		View header = (View) getLayoutInflater().inflate(R.layout.list_header,
				null);
		listView1.addHeaderView(header);
		listView1.setAdapter(adapter);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String URL = "http://app-inventor-gallery.appspot.com/rpc?tag=search:";

		if (v == search) {
			ArrayList<HashMap<String, Object>> jSonInfo = processQuery(query
					.getText().toString(), URL);
			ListItem listview_data[] = new ListItem[jSonInfo.size()];
			for (int i = 0; i < jSonInfo.size(); i++) {
				listview_data[i] = new ListItem(R.drawable.ic_launcher, (String) jSonInfo.get(i).get("title"),
						(String) jSonInfo.get(i).get("image1"));
				
			}

			MainListAdapter adapter = new MainListAdapter(this,
					R.layout.list_item, listview_data);
			listView1.setAdapter(adapter);

		}
//		else if (v == switchTo){
////			Intent it = new Intent(v.getContext(), Activity2.class);
////			startActivity(it);
//			  
//			   
//		}
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

	private ArrayList<HashMap<String, Object>> processQuery(String query,
			String URL) {
		String s = UrlReader.search(query, URL);

		if (s == null) {

			return null;
		}
		JSONArray results;
		try {
			JSONObject o = new JSONObject(s);

			results = (JSONArray) o.get("result");

			return ParseResult(results);

			// result.setText(formatResult(results).toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	private ArrayList<HashMap<String, Object>> ParseResult(JSONArray results) {

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