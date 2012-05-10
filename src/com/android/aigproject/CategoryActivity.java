package com.android.aigproject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.aigproject.AIGProjectActivity.MyListViewListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CategoryActivity extends SherlockActivity {

	ListView categoryListView;
	JSONArray results;
	ArrayList<ListItem> rawcate = new ArrayList<ListItem>();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		
		String source = "http://app-inventor-gallery.appspot.com/rpc?tag=get_categories";		
		String imageFileURL = "http://lh6.ggpht.com/JL2goqwVeu8ds9vGYTUPP" +
				"cw4pF93TEgAnt0YG6eAaxzSE8W2sLa6cmw5bFoxNcgTPPCgJZ6SQWZE0dAj9Fo6trLft9S8pWOdPQ=s100";
		
		
		// start fetch the arraylist data, for this we only need 1 string for each.
		try {
			source = UrlReader.generalGet(source);
			JSONObject o = new JSONObject(source);
			Log.d("CATE", o.toString());
			results = (JSONArray) o.get("result");
			Log.d("CATE", results.toString() + String.valueOf(results.length()));
			
			for (int i = 0; i < results.length(); i++) {
				try {
					rawcate.add(new ListItem(R.drawable.ic_launcher, results.getString(i),
							imageFileURL, null, null, null, null, 0, 0, 0, 0, 0));
				} catch (Exception e) {
					e.printStackTrace();
				}			
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		

		View header = (View) getLayoutInflater().inflate(R.layout.category_header, null);
		CategoryListAdapter adapter = new CategoryListAdapter(this, R.layout.category_item,	rawcate, true);
		categoryListView = (ListView) findViewById(R.id.listViewCats);
		categoryListView.addHeaderView(header);
		categoryListView.setAdapter(adapter);
		categoryListView.setOnItemClickListener(new MyListViewListener());
	}
	
	
	class MyListViewListener implements OnItemClickListener {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// get current ListItem selected
			Intent nextScreen = new Intent(getApplicationContext(), GenericListActivity.class);
			ListItem curItem = (ListItem) (parent.getAdapter().getItem(position));
			Toast.makeText(CategoryActivity.this,
					"position of " + curItem.title, Toast.LENGTH_SHORT).show();

			nextScreen.putExtra("category", curItem.title);
			startActivity(nextScreen);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_up_out);
			
		}

	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {   			
            default:
        		finish();  
                return super.onOptionsItemSelected(item);
        }
    }
	
	
}
