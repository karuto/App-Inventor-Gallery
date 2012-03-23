package com.android.aigproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class CategoryActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);

		Log.d("CATS","###########################");
		TextView cat = (TextView) findViewById(R.id.cats);
		String s = UrlReader.generalGet("http://app-inventor-gallery.appspot.com/rpc?tag=get_categories");
		Log.d("CATS","$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		
		cat.setText(s);
	}
	
}
