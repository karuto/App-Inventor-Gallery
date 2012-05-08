package com.android.aigproject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AIGProjectActivity extends Activity implements OnClickListener {

	public static enum SearchType {
		DEFAULT, ALL, SPECIFIC
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

	SearchType currentType = SearchType.DEFAULT;

	boolean loadingMore = false; // for dynamic list loading
	MainListAdapter adapter;
	private ListView mainListView;
	ListItem listitem_holder[];

	String querySingle = null;
	String[] queries = new String[4]; /* title, description, tag, AuthorId */
	EditText[] editTextList = new EditText[4];
	URLFactory.Type[] types = new URLFactory.Type[4];

	LinearLayout searchAll;
	LinearLayout searchSpecific;

	RadioGroup radioGroup;
	RadioButton searchAllRadioButton;
	RadioButton searchSpecificRadioButton;
	ProgressBar progressBar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// search type
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		radioGroup.setOnClickListener(this);
		searchAllRadioButton = (RadioButton) findViewById(R.id.radioAll);
		searchAllRadioButton.setOnClickListener(this);
		searchSpecificRadioButton = (RadioButton) findViewById(R.id.radioSpecific);
		searchSpecificRadioButton.setOnClickListener(this);

		// search layout
		searchAll = (LinearLayout) findViewById(R.id.SearchAllField);
		searchSpecific = (LinearLayout) findViewById(R.id.SearchSpecificField);

		searchAll.setVisibility(View.VISIBLE);
		searchSpecific.setVisibility(View.GONE);

		search = (Button) findViewById(R.id.buttonSearch);
		search.setOnClickListener(this);
		waiting = (ImageView) findViewById(R.id.waiting);
		waiting.setVisibility(View.INVISIBLE);

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
		adapter = new MainListAdapter(this, R.layout.list_item, listview_data2);

		mainListView = (ListView) findViewById(R.id.listView1);

		View header = (View) getLayoutInflater().inflate(R.layout.list_header,
				null);
		mainListView.addHeaderView(header);
		View footerView = ((LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.listfooter, null, false);
		mainListView.addFooterView(footerView);

		mainListView.setAdapter(adapter);

		mainListView.setOnItemClickListener(new MyListViewListener());

		int[] colors = { 0, 0xFFBBBBBB, 0 };
		mainListView.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT,
				colors));
		mainListView.setDividerHeight(2);

		createAsyncThread(this, SearchType.DEFAULT);

		mainListView.setOnScrollListener(new OnScrollListener() {

			// useless here, skip!
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			// dumdumdum
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				// what is the bottom item that is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;
				// is the bottom item visible & not loading more already ? Load
				// more !
				if ((lastInScreen == totalItemCount) && !(loadingMore)) {
					Log.d("Scroll", "HIHI");
					Thread thread = new Thread(null, loadMoreListItems);
					thread.start();
				}
			}
		});

		Intent i = new Intent(this, MyService.class);
		startService(i);

		Log.e("MyService now", String.valueOf(MyService.getInstance()));
		progressBar = (ProgressBar) findViewById(R.id.progressSearch);
		progressBar.setVisibility(View.GONE);
	}

	// Runnable to load the items
	private Runnable loadMoreListItems = new Runnable() {
		@Override
		public void run() {
			// Set flag so we cant load new items 2 at the same time
			loadingMore = true;

			// Reset the array that holds the new items
			listitem_holder = new ListItem[50];

			// Simulate a delay, delete this on a production environment!
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}

			// Get 15 new listitems (fixed number hard-code for now)
			for (int i = 0; i < 50; i++) {
				String text = "Reload - " + i;
				listitem_holder[i] = new ListItem(R.drawable.ic_launcher, text,
						null, "author",
						"Da da dadadada, da da dadadada, da dada da.", null,
						null, 11, 22, 33, 44, 333);
			}

			// Done! now continue on the UI thread
			runOnUiThread(returnRes);

		}
	};

	// Since we cant update our UI from a thread this Runnable takes care of
	// that!
	private Runnable returnRes = new Runnable() {
		@Override
		public void run() {

			// Loop thru the new items and add them to the adapter
			if (listitem_holder != null && listitem_holder.length > 0) {
				adapter = new MainListAdapter(getApplicationContext(),
						R.layout.list_item, listitem_holder);
			}

			// Tell to the adapter that changes have been made, this will cause
			// the list to refresh
			adapter.notifyDataSetChanged();
			// Done loading more.
			loadingMore = false;
		}
	};

	private void createAsyncThread(final Context context, final SearchType type) {
		
		Log.d("should create new thread", " should");
		
		
		//try {
			boolean success = grabsQueries();

			if (success) {
				Log.d("haha", "before");
				new DownloadFilesTask(this).execute();
				//requestSearchingData(context, type);
				Log.d("haha", "after");
			}

//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		
//		
//		Thread thread = new Thread(new Runnable() {
//			public void run() {
//				Runnable r = new Runnable() {
//					public void run() {
//						try {
//							boolean success = grabsQueries();
//
//							if (success) {
//								Log.d("haha", "before");
//								requestSearchingData(context, type);
//								Log.d("haha", "after");
//							}
//
//						} catch (MalformedURLException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				};
//
//				runOnUiThread(r);
//			}
//		});
//		thread.start();

	}

	private boolean grabsQueries() {

		boolean success = false;
		// if searchAll

		if (currentType == SearchType.DEFAULT) {
			success = true;
		}

		else if (currentType == SearchType.ALL) {
			if (query.getText() != null
					&& query.getText().toString().trim().length() != 0) {
				querySingle = query.getText().toString().trim();
				success = true;
			} else {

			}
		}

		// else if searchSpecific
		else if (currentType == SearchType.SPECIFIC) { // b = false;
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
			throws MalformedURLException, IOException {

		new DownloadFilesTask(this).execute();

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
			nextScreen.putExtra("creationTime", curItem.creationTime);
			nextScreen.putExtra("uploadTime", curItem.uploadTime);
			nextScreen.putExtra("numLikes", curItem.numLikes);
			nextScreen.putExtra("numViewed", curItem.numViewed);
			nextScreen.putExtra("numDownloads", curItem.numDownloads);
			nextScreen.putExtra("numComments", curItem.numComments);
			nextScreen.putExtra("uid", curItem.uid);

			Toast.makeText(AIGProjectActivity.this,
					"position of " + curItem.title, Toast.LENGTH_SHORT).show();

			startActivity(nextScreen);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_up_out);

		}

	}

	@Override
	public void onClick(View v) {

		if (v == search) {
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

			querySingle = query.getText().toString().trim();
			TextView header = (TextView) findViewById(R.id.txtHeader);
			header.setText("Search results for " + querySingle);

			createAsyncThread(this, SearchType.ALL);
			v.setClickable(true);

		} else if (v == searchAllRadioButton) {
			query.setVisibility(View.VISIBLE);
			searchAll.setVisibility(View.VISIBLE);
			search.setVisibility(View.VISIBLE);
			searchSpecific.setVisibility(View.GONE);

			currentType = SearchType.ALL;

		} else if (v == searchSpecificRadioButton) {
			searchAll.setVisibility(View.GONE);
			searchSpecific.setVisibility(View.VISIBLE);
			search.setVisibility(View.VISIBLE);

			currentType = SearchType.SPECIFIC;

		}

	}

	private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {

		ArrayList<HashMap<String, Object>> jSonInfo = new ArrayList<HashMap<String, Object>>();
		Context context;

		public DownloadFilesTask(AIGProjectActivity aigProjectActivity) {
			this.context = aigProjectActivity;
			Log.d("constructor", "sdfsdf");
		}

		@Override
		protected Long doInBackground(URL... urls) {
			Log.d("doinBackGround", "sdfsdf");
			ArrayList<HashMap<String, Object>> tmp;

			if (currentType == SearchType.DEFAULT) {
				String URL = URLFactory.generate(URLFactory.Type.DEFAULT, null);
				tmp = JsonGrabber.retrieveQueryArray(URL);
				if (tmp != null) {
					jSonInfo.addAll(tmp);
				}
			} else if (currentType == SearchType.ALL) { // Search ALL
				String URL = URLFactory.generate(URLFactory.Type.ALL,
						querySingle);
				tmp = JsonGrabber.retrieveQueryArray(URL);
				if (tmp != null) {
					jSonInfo.addAll(tmp);
				}

				Log.e("Type", "True");
			} else if (currentType == SearchType.SPECIFIC) { // b == false
				// //Search
				// Specific
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

			Log.d("doinBackGround", "finished");

			// Looper.loop();

			return null;
			// Log.e(tag, msg);
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// setProgressPercent(progress[0]);
		}

		@Override
		protected void onPostExecute(Long result) {
			// showDialog("Downloaded " + result + " bytes");
			
			Log.d("onPostExecute", "resdg");
			
			
			ListItem listview_data[] = new ListItem[jSonInfo.size()];
			for (int i = 0; i < jSonInfo.size(); i++) {
				listview_data[i] = new ListItem(
						R.drawable.ic_launcher, // dummy icon
						(String) jSonInfo.get(i).get("title"),
						(String) jSonInfo.get(i).get("image1"), // imageFileURL,
						// thumb
						(String) jSonInfo.get(i).get("displayName"), // author
						(String) jSonInfo.get(i).get("description"),
						(Long) jSonInfo.get(i).get("creationTime"),
						(Long) jSonInfo.get(i).get("uploadTime"),
						(Integer) jSonInfo.get(i).get("numLikes"),
						(Integer) jSonInfo.get(i).get("numViewed"),
						(Integer) jSonInfo.get(i).get("numDownloads"),
						(Integer) jSonInfo.get(i).get("numComments"),
						(Integer) jSonInfo.get(i).get("uid"));
			}

			MainListAdapter adapter = new MainListAdapter(context,
					R.layout.list_item, listview_data);
			mainListView.setAdapter(adapter);
			Log.d("length: ", String.valueOf(jSonInfo.size()));
			Log.d("onPost", "finished");

		}
	}

}