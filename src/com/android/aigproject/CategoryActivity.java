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

public class CategoryActivity extends SherlockActivity{

	ListView categoryListView;
	JSONArray results;
	String[] rawcate = new String[20];
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		
		String source = "http://app-inventor-gallery.appspot.com/rpc?tag=get_categories";		
//		ArrayList<HashMap<String, Object>> catsArray = new ArrayList<HashMap<String, Object>>();
//		catsArray = JsonGrabber.retrieveQueryArray(source);
		
		
		try {
			source = UrlReader.generalGet(source);
			JSONObject o = new JSONObject(source);
			Log.d("CATE", o.toString());
			results = (JSONArray) o.get("result");
			Log.d("CATE", results.toString() + String.valueOf(results.length()));
			for (int i = 0; i < results.length(); i++) {
				String singleCate = results.getString(i);
				rawcate[i] = singleCate;
				Log.d("RAW CATE", rawcate[i]);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ListItem listview_cats[] = new ListItem[18];
		String imageFileURL = "http://lh6.ggpht.com/JL2goqwVeu8ds9vGYTUPPcw4pF93TEgAnt0YG6eAaxzSE8W2sLa6cmw5bFoxNcgTPPCgJZ6SQWZE0dAj9Fo6trLft9S8pWOdPQ=s100";

		for (int i = 0; i < listview_cats.length; i++) {
			String text = "Category - " + rawcate[i];
			listview_cats[i] = new ListItem(R.drawable.ic_launcher, text,
					imageFileURL, null, null,
					null, null, 11, 22, 33, 44, 333);
		}

		View header = (View) getLayoutInflater().inflate(R.layout.category_header, null);
		CategoryListAdapter adapter = new CategoryListAdapter(this, R.layout.category_item,	listview_cats);
		categoryListView = (ListView) findViewById(R.id.listViewCats);
		categoryListView.addHeaderView(header);
		categoryListView.setAdapter(adapter);
		categoryListView.setOnItemClickListener(new MyListViewListener());
	}
	
	
	class MyListViewListener implements OnItemClickListener {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// get current ListItem selected
			ListItem curItem = (ListItem) (parent.getAdapter().getItem(position));
			Toast.makeText(CategoryActivity.this,
					"position of " + curItem.title, Toast.LENGTH_SHORT).show();
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
