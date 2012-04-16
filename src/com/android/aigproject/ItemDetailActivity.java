package com.android.aigproject;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ItemDetailActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemdetail);

        TextView txtName = (TextView) findViewById(R.id.txtName);
        TextView txtAuthor = (TextView) findViewById(R.id.txtAuthor);
        TextView txtNumViewed = (TextView) findViewById(R.id.txtNumViewed);
        TextView txtNumLikes = (TextView) findViewById(R.id.txtNumLikes);
        TextView txtNumDownloads = (TextView) findViewById(R.id.txtNumDownloads);
        TextView txtNumComments = (TextView) findViewById(R.id.txtNumComments);
        Button btnClose = (Button) findViewById(R.id.btnClose);
 
        Intent i = getIntent();
        // Receiving the Data
        String name = i.getStringExtra("name");
        String author = i.getStringExtra("author");
        int numLikes = i.getIntExtra("numLikes", 0);
        int numViewed = i.getIntExtra("numViewed", 0);
        int numDownloads = i.getIntExtra("numDownloads", 0);
        int numComments = i.getIntExtra("numComments", 0);
//        String imageURL = i.getStringExtra("imageURL");
        Log.d("Second Screen", numLikes + " + " + numViewed);
 
        // Displaying Received data
        txtName.setText(name);
        txtAuthor.setText(author);
        txtNumViewed.setText("" + numViewed);
        txtNumLikes.setText("" + numLikes);
        txtNumDownloads.setText("" + numDownloads);
        txtNumComments.setText("" + numComments);
 
        // Binding Click event to Button
        btnClose.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View arg0) {
                //Closing SecondScreen Activity
                finish();
            }
        });
 
    }
}
