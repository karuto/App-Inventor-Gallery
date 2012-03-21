package com.android.aigproject;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AIGProjectActivity extends Activity {

    private ListView listView1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListItem listview_data[] = new ListItem[20];
        
        for (int i = 0; i < 20; i++) {
        	String text = "Title "+i;        	
        	listview_data[i] = new ListItem(R.drawable.ic_launcher, text);
        }
        
        
//        ListItem listview_data[] = new ListItem[]
//        {
//            new ListItem(R.drawable.ic_launcher, "S1"),
//            new ListItem(R.drawable.ic_launcher, "S2"),
//            new ListItem(R.drawable.ic_launcher, "S3"),
//            new ListItem(R.drawable.ic_launcher, "S4"),
//            new ListItem(R.drawable.ic_launcher, "S5"),
//            new ListItem(R.drawable.ic_launcher, "S6"),
//            new ListItem(R.drawable.ic_launcher, "S4")
//        };
        
        // Defines the layout of each row in ListView.
        MainListAdapter adapter = new MainListAdapter(this, 
                R.layout.list_item, listview_data);
        
        
        listView1 = (ListView)findViewById(R.id.listView1);
         
        View header = (View)getLayoutInflater().inflate(R.layout.list_header, null);
        listView1.addHeaderView(header);
        
        listView1.setAdapter(adapter);
    }
}