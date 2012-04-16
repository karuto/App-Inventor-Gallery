package com.android.aigproject;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

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

public class CategoryActivity extends Activity{

	private ListView categoryListView;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);

		
		
		String source = "http://app-inventor-gallery.appspot.com/rpc?tag=get_categories";
		Log.d("CATS","###########################");
		
		ArrayList<HashMap<String, Object>> catsArray = new ArrayList<HashMap<String, Object>>();
		catsArray = JsonGrabber.retrieveQueryArray(source);
		
		String categories[] = new String[20];
		int counter = 0;
		for ( HashMap<String, Object> h : catsArray ) {
			categories[counter] = h.keySet().toString().substring(1, h.keySet().toString().length()-1);
			counter++;
		}
		
//		TextView cat = (TextView) findViewById(R.id.cats);
//		String allofthem = "";
//		for ( String s : categories ) {
//			allofthem += s;
//			allofthem +=" ";
//		}
//		cat.setText(allofthem);
//		cat.setVisibility(View.INVISIBLE);
		
		
		ListItem listview_cats[] = new ListItem[20];
		String imageFileURL = "http://lh6.ggpht.com/JL2goqwVeu8ds9vGYTUPPcw4pF93TEgAnt0YG6eAaxzSE8W2sLa6cmw5bFoxNcgTPPCgJZ6SQWZE0dAj9Fo6trLft9S8pWOdPQ=s100";

		for (int i = 0; i < 20; i++) {
			String text = "Category - " + categories[i];
			listview_cats[i] = new ListItem(R.drawable.ic_launcher, text,
					imageFileURL, null, null, 1);
		}
		
		CategoryListAdapter adapter = new CategoryListAdapter(this, R.layout.list_item,	listview_cats);

		categoryListView = (ListView) findViewById(R.id.listViewCats);

		View header = (View) getLayoutInflater().inflate(R.layout.list_header,
				null);
		categoryListView.addHeaderView(header);
		categoryListView.setAdapter(adapter);

//		categoryListView.setOnItemClickListener(new MyListViewListener());
	}
	
	
	class MyListViewListener implements OnItemClickListener {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			// get current ListItem selected
			ListItem curItem = (ListItem) (parent.getAdapter()
					.getItem(position));
			Toast.makeText(CategoryActivity.this,
					"position of " + curItem.title, Toast.LENGTH_SHORT).show();
			String URL = "http://app-inventor-gallery.appspot.com/rpc?tag=search:";
		}

	}

	
	
}
