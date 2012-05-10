package com.android.aigproject;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.android.aigproject.AIGProjectActivity.MyListViewListener;
import com.android.aigproject.URLFactory.Type;

public class GenericListActivity extends SherlockActivity implements OnClickListener {


	ListView categoryListView;
	JSONArray results;
	String[] rawcate = new String[20];
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		View header = (View) getLayoutInflater().inflate(R.layout.category_header, null);		
		CategoryListAdapter adapter = new CategoryListAdapter(
				this, R.layout.category_item,	fetchResultByCategory(), false);
		categoryListView = (ListView) findViewById(R.id.listViewCats);
		categoryListView.addHeaderView(header);
		categoryListView.setAdapter(adapter);
		
	}
	
	private ArrayList<ListItem> fetchResultByCategory() {

		Intent intent = getIntent();
		String caturl = URLFactory.generate(Type.TITLE, intent.getStringExtra("category"));
		
		// based on selected category, fetch all items belongs to this category into catsArray
		ArrayList<HashMap<String, Object>> catsArray = new ArrayList<HashMap<String, Object>>();
		catsArray = JsonGrabber.retrieveQueryArray(caturl);
		ArrayList<ListItem> listview_data = new ArrayList<ListItem>(catsArray.size());
		
		for (int i = 0; i < catsArray.size(); i++) {
			listview_data.add(new ListItem(
					R.drawable.ic_launcher, // dummy icon
					(String) catsArray.get(i).get("title"),
					(String) catsArray.get(i).get("image1"), // imageFileURL,
					(String) catsArray.get(i).get("displayName"), // author
					(String) catsArray.get(i).get("description"),
					(Long) catsArray.get(i).get("creationTime"),
					(Long) catsArray.get(i).get("uploadTime"),
					(Integer) catsArray.get(i).get("numLikes"),
					(Integer) catsArray.get(i).get("numViewed"),
					(Integer) catsArray.get(i).get("numDownloads"),
					(Integer) catsArray.get(i).get("numComments"),
					(Integer) catsArray.get(i).get("uid")));
		}
		// now we get the data, we can start put it in the ListView
		return listview_data;		
		
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


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}
