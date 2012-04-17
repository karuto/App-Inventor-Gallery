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
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AIGProjectActivity extends Activity implements OnClickListener {

	public static enum SearchType {
		ALL, SPECIFIC
	}

	Button search;
	TextView result;
	EditText query;
	EditText title;
	EditText description;
	EditText tag;
	EditText authorId;

	Button switchTo;
	ImageView waiting;
	
	SearchType currentType = SearchType.ALL;   //change here
	

	private ListView mainListView;

	String querySingle = null;				//change here
	
	String[] queries = new String[4]; /* title, description, tag, AuthorId */
	EditText[] editTextList = new EditText[4];
	URLFactory.Type[] types = new URLFactory.Type[4];

	/**
	 * Tab Layout
	 */
	LinearLayout searchAll;							//change here
	LinearLayout searchSpecific;					//change here

	RadioGroup radioGroup;							//change here
	RadioButton searchAllRadioButton;				//change here
	RadioButton searchSpecificRadioButton;			//change here

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// search type
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);				//change here
		radioGroup.setOnClickListener(this);				//change here
		searchAllRadioButton = (RadioButton) findViewById(R.id.radioAll);				//change here
		searchAllRadioButton.setOnClickListener(this);				//change here
		searchSpecificRadioButton = (RadioButton) findViewById(R.id.radioSpecific);				//change here
		searchSpecificRadioButton.setOnClickListener(this);				//change here

		// search layout
		searchAll = (LinearLayout) findViewById(R.id.SearchAllField);				//change here
		searchSpecific = (LinearLayout) findViewById(R.id.SearchSpecificField);				//change here

		searchAll.setVisibility(View.VISIBLE);				//change here
		searchSpecific.setVisibility(View.GONE);				//change here

		search = (Button) findViewById(R.id.buttonSearch);				//change here
		search.setOnClickListener(this);				//change here
		waiting = (ImageView) findViewById(R.id.waiting);				//change here
		waiting.setVisibility(View.INVISIBLE);				//change here

		query = (EditText) findViewById(R.id.editText1);
		title = (EditText) findViewById(R.id.editTextTitle);
		description = (EditText) findViewById(R.id.editTextDescription);
		tag = (EditText) findViewById(R.id.editTextTag);
		authorId = (EditText) findViewById(R.id.editTextAuthorID);
		

		
		

		editTextList[0] = title;
		editTextList[1] = description;
		editTextList[2] = tag;
		editTextList[3] = authorId;

		types[0] = URLFactory.Type.TITLE;
		types[1] = URLFactory.Type.DESCRIPTION;
		types[2] = URLFactory.Type.TAG;
		types[3] = URLFactory.Type.AUTHORID;


		// Defines the layout of each row in ListView.
		ListItem listview_data2[] = new ListItem[0]; /*
													 * begin with 0, so nothing
													 * in there
													 */
		MainListAdapter adapter = new MainListAdapter(this, R.layout.list_item,
				listview_data2);

		mainListView = (ListView) findViewById(R.id.listView1);

		View header = (View) getLayoutInflater().inflate(R.layout.list_header,
				null);
		mainListView.addHeaderView(header);
		mainListView.setAdapter(adapter);

		mainListView.setOnItemClickListener(new MyListViewListener());

		int[] colors = { 0, 0xFFBBBBBB, 0 };
		mainListView.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT,
				colors));
		mainListView.setDividerHeight(2);

		
		//change here
		//uncomment codes below
		
		// View.OnClickListener buttonhandler =new View.OnClickListener() {
		// public void onClick(View v) {
		// // Log.d("km-main","enter onClick");
		// switch(v.getId()) {
		// // Now, which button did they press, and take me to that
		// class/activity
		// case R.id.buttoncats:
		// // Log.d("km-main","enter button");
		// Intent gotoCategory = new Intent(AIGProjectActivity.this,
		// CategoryActivity.class);
		// startActivity(gotoCategory);
		//
		// break;
		// }
		//
		// }
		// };

		// // bind listener to button
		// Button catb = (Button) findViewById(R.id.buttoncats);
		// catb.setOnClickListener(buttonhandler);

	}

	private void createAsyncThread(final Context context, final SearchType type) {

		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					boolean success = grabsQueries();

					if (success) {
						requestSearchingData(context, type);
					}

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

	private boolean grabsQueries() {				//change here

		boolean success = false;

		// if searchAll
		
		if(currentType == SearchType.ALL){
			if (query.getText() != null
					&& query.getText().toString().trim().length() != 0) {
//				for (int i = 0; i < queries.length; i++) {
//					queries[i] = query.getText().toString().trim();
//				}
				
				querySingle = query.getText().toString().trim();
				
				success = true;
			}
		}
		
		
		

		// else if searchSpecific
		else if(currentType == SearchType.SPECIFIC){ // b = false;
			for (int i = 0; i < queries.length; i++) {
				if (editTextList[i].getText() != null
						&& editTextList[i].getText().toString().trim().length() != 0) {
					queries[i] = title.getText().toString().trim();
					success = true;
				} else {
					queries[i] = null;
				}
			}
		}
		return success;
	}

	public void requestSearchingData(final Context context, SearchType type)
			throws MalformedURLException, IOException {				//change here

		Runnable r = new Runnable() {
			public void run() {
				ArrayList<HashMap<String, Object>> jSonInfo = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> tmp;
				
				if(currentType == SearchType.ALL){ //Search ALL
					String URL = URLFactory.generate(URLFactory.Type.ALL, querySingle);
					tmp = JsonGrabber.retrieveQueryArray(URL);
					if (tmp != null) {
						jSonInfo.addAll(tmp);
					}
					
					Log.e("Type", "True");
				}else if(currentType == SearchType.SPECIFIC){ //b == false   //Search Specific
					for (int i = 0; i < queries.length; i++) {
						if (queries[i] != null) {

							String URL = URLFactory.generate(types[i], queries[i]);
							tmp = JsonGrabber.retrieveQueryArray(URL);
							if (tmp != null) {
								jSonInfo.addAll(tmp);
							}
						}
					}
					Log.e("Type", "false");
				}
				

				ListItem listview_data[] = new ListItem[jSonInfo.size()];
				for (int i = 0; i < jSonInfo.size(); i++) {
					listview_data[i] = new ListItem(R.drawable.ic_launcher,	// dummy icon
							(String) jSonInfo.get(i).get("title"),
							(String) jSonInfo.get(i).get("image1"),	// imageFileURL, thumb
							(String) jSonInfo.get(i).get("displayName"),	// author
							(String) jSonInfo.get(i).get("description"),
							(Integer) jSonInfo.get(i).get("numLikes"),
							(Integer) jSonInfo.get(i).get("numViewed"),
							(Integer) jSonInfo.get(i).get("numDownloads"),
							(Integer) jSonInfo.get(i).get("numComments"));		
				}

				MainListAdapter adapter = new MainListAdapter(context,
						R.layout.list_item, listview_data);
				mainListView.setAdapter(adapter);
			}
		};
		//r.run();
		
		
		runOnUiThread(r);
            

	}

	class MyListViewListener implements OnItemClickListener {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Intent nextScreen = new Intent(getApplicationContext(),
					ItemDetailActivity.class);

			// Sending data to another Activity
			ListItem curItem = (ListItem) (parent.getAdapter()
					.getItem(position));

			nextScreen.putExtra("title", curItem.title);
			nextScreen.putExtra("imageURL", curItem.imageFileURL);
			nextScreen.putExtra("author", curItem.author);
			nextScreen.putExtra("desc", curItem.desc);
			nextScreen.putExtra("numLikes", curItem.numLikes);
			nextScreen.putExtra("numViewed", curItem.numViewed);
			nextScreen.putExtra("numDownloads", curItem.numDownloads);
			nextScreen.putExtra("numComments", curItem.numComments);
			
			Toast.makeText(AIGProjectActivity.this,
					"position of " + curItem.title, Toast.LENGTH_SHORT).show();

			startActivity(nextScreen);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_up_out);

		}

	}

	@Override
	public void onClick(View v) {

		if (v == search) {				//change here
			// visible 0 Visible on screen; the default value.
			// invisible 1 Not displayed, but taken into account during layout
			// (space is left for it).
			// gone 2 Completely hidden, as if the view had not been added.

			// v.setBackgroundColor(Color.RED);
			v.setClickable(false);
			waiting.setVisibility(View.GONE);
			searchSpecific.setVisibility(View.GONE);
			searchAll.setVisibility(View.GONE);
			query.setVisibility(View.GONE);
			search.setVisibility(View.GONE);

			radioGroup.getCheckedRadioButtonId();

//			if (radioGroup.getCheckedRadioButtonId() == R.id.SearchAllField) {
//				createAsyncThread(this, SearchType.ALL);
//			} else if (radioGroup.getCheckedRadioButtonId() == R.id.SearchSpecificField) {
//				createAsyncThread(this, SearchType.SPECIFIC);
//			}
			createAsyncThread(this, SearchType.ALL);
			v.setClickable(true);
//			waiting.setVisibility(View.INVISIBLE);

		} else if (v == searchAllRadioButton) {				//change here
			query.setVisibility(View.VISIBLE);
			searchAll.setVisibility(View.VISIBLE);
			search.setVisibility(View.VISIBLE);
			searchSpecific.setVisibility(View.GONE);

//			query.setText("");
//			title.setText("");
//			description.setText("");
//			tag.setText("");
//			authorId.setText("");
			
			currentType = SearchType.ALL;

		} else if (v == searchSpecificRadioButton) {				//change here
			searchAll.setVisibility(View.GONE);
			searchSpecific.setVisibility(View.VISIBLE);
			search.setVisibility(View.VISIBLE);



			
			currentType = SearchType.SPECIFIC;

		}

	}

}