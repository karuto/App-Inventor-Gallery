package com.android.aigproject;


import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

   
public class AIGProjectActivity extends SherlockActivity implements OnClickListener, ActionBar.OnNavigationListener {

	public static enum SearchType {
		DEFAULT, ALL, SPECIFIC
	}

	public static enum ListOperation {
		CREATE, APPEND, CLEAR
	}

	Button search;
	TextView result;
	EditText query;
	EditText title;
	EditText description;
	EditText tag;
	EditText authorId;

	int defaultStart = 0;
	int defaultCount = 10;
	int defaultIncrement = 10;

	int allStart = 0;
	int allCount = 10;
	int allIncrement = 10;

	View headerView;
	View footerView;

	TextView listfooterEmpty;

	Button switchTo;
	ImageView waiting;

	SearchType currentType = SearchType.DEFAULT;

	boolean loadingMore = false; // for dynamic list loading
	MainListAdapter adapter;
	private ListView mainListView;
	ArrayList<ListItem> listitem_holder = new ArrayList<ListItem>();
	// ListItem listitem_holder[];

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
		
		// Turn of strict mode by default since we are targeting 2.x
		int apiLevel = android.os.Build.VERSION.SDK_INT;
		if (apiLevel >= 10) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);			
		}
		
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
		ArrayList<ListItem> defaultListView = new ArrayList<ListItem>(); /*
																		 * begin
																		 * with
																		 * 0, so
																		 * nothing
																		 * in
																		 * there
																		 */
		adapter = new MainListAdapter(this, R.layout.list_item, defaultListView);

		mainListView = (ListView) findViewById(R.id.listView1);

		headerView = (View) getLayoutInflater().inflate(R.layout.list_header,
				null);
		headerView.setOnClickListener(this);
		mainListView.addHeaderView(headerView);
		footerView = ((LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.listfooter, null, false);
		footerView.setOnClickListener(this);

		mainListView.addFooterView(footerView);

		mainListView.setAdapter(adapter);

		mainListView.setOnItemClickListener(new MyListViewListener());

		int[] colors = { 0, 0xFFBBBBBB, 0 };
		mainListView.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT,
				colors));
		mainListView.setDividerHeight(2);

		createAsyncThread(this, SearchType.DEFAULT, ListOperation.CREATE);

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
					// Thread thread = new Thread(null, loadMoreListItems);
					// thread.start();
				}
			}
		});

		// Intent i = new Intent(this, MyService.class);
		// startService(i);

		// Log.e("MyService now", String.valueOf(MyService.getInstance()));
		progressBar = (ProgressBar) findViewById(R.id.progressSearch);
		progressBar.setVisibility(View.GONE);

		listfooterEmpty = (TextView) findViewById(R.id.empty);
		
        Context context = getSupportActionBar().getThemedContext();
        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, R.array.locations, R.layout.sherlock_spinner_item);
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(list, this);
		
		

	}

	// // Runnable to load the items
	// private Runnable loadMoreListItems = new Runnable() {
	// @Override
	// public void run() {
	// // Set flag so we cant load new items 2 at the same time
	// loadingMore = true;
	//
	// // Reset the array that holds the new items
	// ArrayList<ListItem> listitem_holder = new ArrayList<ListItem>();
	//
	// // Simulate a delay, delete this on a production environment!
	// try {
	// Thread.sleep(2000);
	// } catch (InterruptedException e) {
	// }
	//
	// // Get 15 new listitems (fixed number hard-code for now)
	// for (int i = 0; i < 50; i++) {
	// String text = "Reload - " + i;
	// listitem_holder[i] = new ListItem(R.drawable.ic_launcher, text,
	// null, "author",
	// "Da da dadadada, da da dadadada, da dada da.", null,
	// null, 11, 22, 33, 44, 333);
	// }
	//
	// // Done! now continue on the UI thread
	// runOnUiThread(returnRes);
	//
	// }
	// };

	// // Since we cant update our UI from a thread this Runnable takes care of
	// // that!
	// private Runnable returnRes = new Runnable() {
	// @Override
	// public void run() {
	//
	// // Loop thru the new items and add them to the adapter
	// if (listitem_holder != null && listitem_holder.size() > 0) {
	// adapter = new MainListAdapter(getApplicationContext(),
	// R.layout.list_item, listitem_holder);
	// }
	//
	// // Tell to the adapter that changes have been made, this will cause
	// // the list to refresh
	// adapter.notifyDataSetChanged();
	// // Done loading more.
	// loadingMore = false;
	// }
	// };

	@Override
	public void onStart() {
		super.onStart();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
//		closeKeyBoardFocus();
//		query.clearFocus();
//
//		InputMethodManager imm = (InputMethodManager) this
//				.getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.hideSoftInputFromWindow(this.query.getApplicationWindowToken(), 0);
	}

	@Override
	public void onResume() {
		super.onResume();
//		closeKeyBoardFocus();
//		query.clearFocus();
	}

	private void createAsyncThread(final Context context,
			final SearchType type, ListOperation listOperation) {

		Log.d("should create new thread", " should");

		// try {
		boolean success = grabsQueries();

		if (success) {
			Log.d("haha", "before");
			new DownloadFilesTask(this, listOperation, type).execute();
			// requestSearchingData(context, type);
			Log.d("haha", "after");
		}

		// } catch (MalformedURLException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		//
		// Thread thread = new Thread(new Runnable() {
		// public void run() {
		// Runnable r = new Runnable() {
		// public void run() {
		// try {
		// boolean success = grabsQueries();
		//
		// if (success) {
		// Log.d("haha", "before");
		// requestSearchingData(context, type);
		// Log.d("haha", "after");
		// }
		//
		// } catch (MalformedURLException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// };
		//
		// runOnUiThread(r);
		// }
		// });
		// thread.start();

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

	// public void requestSearchingData(final Context context, SearchType type)
	// throws MalformedURLException, IOException {
	//
	// new DownloadFilesTask(this, ListOperation.CREATE).execute();
	//
	// }

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

			allCount = 10;
			listfooterEmpty.setText("load more data...");

			closeKeyBoardFocus();

			waiting.setVisibility(View.GONE);
			searchSpecific.setVisibility(View.GONE);
			searchAll.setVisibility(View.GONE);
			query.setVisibility(View.GONE);
			search.setVisibility(View.GONE);

			radioGroup.getCheckedRadioButtonId();

			querySingle = query.getText().toString().trim();
			TextView header = (TextView) findViewById(R.id.txtHeader);
			header.setText("Search results for " + querySingle);
			
			currentType = SearchType.ALL;
			

			createAsyncThread(this, SearchType.ALL, ListOperation.CREATE);
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

		} else if (v == footerView) {

			Log.d("newstuff", "new blood is coming");
			if (currentType == SearchType.DEFAULT) {

				if (defaultStart == 0) { // should be impossible
					createAsyncThread(this, SearchType.DEFAULT,
							ListOperation.CREATE);
				} else {
					createAsyncThread(this, SearchType.DEFAULT,
							ListOperation.APPEND);

				}
			} else if (currentType == SearchType.ALL) {
				if (allStart == 0) { // should be impossible
					createAsyncThread(this, SearchType.ALL,
							ListOperation.CREATE);
				} else {
					createAsyncThread(this, SearchType.ALL,
							ListOperation.APPEND);

				}

			} else if (currentType == SearchType.SPECIFIC) {

			}

		}

	}

	private void closeKeyBoardFocus() {
		if (this.getCurrentFocus() != null) {

			InputMethodManager inputManager = (InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		} else {
			Log.d("KeyBoard", "this is null");
		}

	}

	private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {

		ArrayList<HashMap<String, Object>> jSonInfo = new ArrayList<HashMap<String, Object>>();
		Context context;
		ListOperation listOperation;
		SearchType type;

		public DownloadFilesTask(AIGProjectActivity aigProjectActivity,
				ListOperation listOperation, SearchType type) {
			this.context = aigProjectActivity;
			this.listOperation = listOperation;
			this.type = type;
			Log.d("constructor", "sdfsdf");

			if (listOperation == ListOperation.APPEND) {
				footerView.setBackgroundColor(Color.BLACK);
			}

		}

		@Override
		protected Long doInBackground(URL... urls) {
			Log.d("doinBackGround", "sdfsdf");
			ArrayList<HashMap<String, Object>> tmp;

			// progressBar.setVisibility(View.VISIBLE);

			if (type == SearchType.DEFAULT) {
				String URL = URLFactory.generate(URLFactory.Type.DEFAULT, null,
						defaultStart, defaultCount);
				defaultStart += defaultIncrement;
				Log.d("URL", "URL: " + URL);

				tmp = JsonGrabber.retrieveQueryArray(URL);
				if (tmp != null) {
					jSonInfo.addAll(tmp);
				} else {
					listfooterEmpty
							.setText("Internet Connection Problem occurs, please check your wift/3G connection");
					listfooterEmpty.setTextColor(Color.RED);
				}
			} else if (type == SearchType.ALL) { // Search ALL
				String URL = URLFactory.generate(URLFactory.Type.ALL,
						querySingle, allStart, allCount);
				allCount += allIncrement;
				tmp = JsonGrabber.retrieveQueryArray(URL);
				if (tmp != null) {
					jSonInfo.addAll(tmp);
				} else {
					listfooterEmpty
							.setText("Internet Connection Problem occurs, please check your wift/3G connection");
					listfooterEmpty.setTextColor(Color.RED);
				}

				Log.e("Type", "True");
			} else if (type == SearchType.SPECIFIC) { // b == false
				// //Search
				// Specific
				for (int i = 0; i < queries.length; i++) {
					if (queries[i] != null) {
						String URL = URLFactory.generate(types[i], queries[i]);
						tmp = JsonGrabber.retrieveQueryArray(URL);
						if (tmp != null) {
							jSonInfo.addAll(tmp);
						} else {
							listfooterEmpty
									.setText("Internet Connection Problem occurs, please check your wift/3G connection");
							listfooterEmpty.setTextColor(Color.RED);
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
			ArrayList<ListItem> listview_data = new ArrayList<ListItem>(
					jSonInfo.size());
			// ListItem listview_data[] = new ListItem[jSonInfo.size()];
			for (int i = 0; i < jSonInfo.size(); i++) {
				listview_data.add(new ListItem(
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
						(Integer) jSonInfo.get(i).get("uid")));
			}
			MainListAdapter adapter = null;
			if (listOperation == ListOperation.CREATE) {
				adapter = new MainListAdapter(context, R.layout.list_item,
						listview_data);
				mainListView.setAdapter(adapter);
				if (listview_data.size() == 0) {
					listfooterEmpty.setText("No results found");
					footerView.setBackgroundColor(Color.WHITE);
					return;
				}

				Log.d("CREATE", "CREATE");

			} else if (listOperation == ListOperation.APPEND) {

				HeaderViewListAdapter hva = (HeaderViewListAdapter) mainListView
						.getAdapter();
				adapter = (MainListAdapter) hva.getWrappedAdapter();

				Log.d("beforeAdd: ",
						"keyyyyy" + String.valueOf(adapter.getCount()));

				if (listview_data.size() == 0) {
					listfooterEmpty.setText("No more data");
					footerView.setBackgroundColor(Color.WHITE);
					return;
				}

				for (ListItem item : listview_data) {
					adapter.add(item);
				}
				// Log.d("AfterAdd: ", "keyyyyy" + String.valueOf(i));

				Log.d("AfterAdd: ",
						"keyyyyy" + String.valueOf(adapter.getCount()));
				Log.d("APPEND", "APPEND");
			}

			footerView.setBackgroundColor(Color.WHITE);

			Log.d("onPost", "finished");
			// progressBar.setVisibility(View.GONE);

		}
	}

	   @Override
	    public boolean onCreateOptionsMenu(Menu menu) {

//	        menu.add("Category")
//	            .setIcon(R.drawable.ic_compose)
//	            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//	        menu.add("User")
//	        .setIcon(R.drawable.ic_compose)
//	        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//	        menu.add("Search")
//	        	.setIcon(R.drawable.ic_search)
//	            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

	        MenuInflater inflater = getSupportMenuInflater();
	        inflater.inflate(R.menu.menu, menu);
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item)
	    {
	        switch (item.getItemId())
	        {
	            case R.id.menu_category:
	    			Intent cateScreen = new Intent(getApplicationContext(),
	    					CategoryActivity.class);
	    			startActivity(cateScreen);
	    			overridePendingTransition(R.anim.push_left_in, R.anim.push_up_out);
	                return true;
	                
	            case R.id.menu_search:
	    			query.setVisibility(View.VISIBLE);
	    			searchAll.setVisibility(View.VISIBLE);
	    			search.setVisibility(View.VISIBLE);
	    			searchSpecific.setVisibility(View.GONE);
	    			currentType = SearchType.ALL;
	                return true;
	                
	            default:
	                return super.onOptionsItemSelected(item);
	        }
	    }
	    
	    
	    @Override
	    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
//	        mSelected.setText("Selected: " + mLocations[itemPosition]);
	        return true;
	    }

}