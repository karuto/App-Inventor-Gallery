package com.android.aigproject;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.android.aigproject.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemDetailActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemdetail);
        
        
        
        
        
        

        ImageView Image = (ImageView) findViewById(R.id.img);
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        TextView txtAuthor = (TextView) findViewById(R.id.txtAuthor);
        TextView txtDesc = (TextView) findViewById(R.id.txtDesc);
        TextView txtNumViewed = (TextView) findViewById(R.id.txtNumViewed);
        TextView txtNumLikes = (TextView) findViewById(R.id.txtNumLikes);
        TextView txtNumDownloads = (TextView) findViewById(R.id.txtNumDownloads);
        TextView txtNumComments = (TextView) findViewById(R.id.txtNumComments);
        Button btnClose = (Button) findViewById(R.id.btnClose);
 
        Intent i = getIntent();
 
//        // Receiving the Data
//        String imageURL = i.getStringExtra("imageURL");
////        imageURL = imageURL.substring(0, imageURL.length()-4);
//        String title = i.getStringExtra("title");
//        String author = i.getStringExtra("author");
//        String desc = i.getStringExtra("desc");
//        Long creationTime = i.getLongExtra("creationTime", 1334704639);
//        Long uploadTime = i.getLongExtra("uploadTime", 1334704639);
//        int numLikes = i.getIntExtra("numLikes", 0);
//        int numViewed = i.getIntExtra("numViewed", 0);
//        int numDownloads = i.getIntExtra("numDownloads", 0);
//        int numComments = i.getIntExtra("numComments", 0);
        
        String url = URLFactory.generate(URLFactory.Type.UID, String.valueOf(i.getIntExtra("uid", 0)));
        //String url = URLFactory.generate(URLFactory.Type.UID, String.valueOf(36001));
        ArrayList<HashMap<String, Object>> tmp = JsonGrabber.retrieveQueryArrayByUID(url);
        
        
        Log.d("uid1", tmp.toString());
        Log.d("uid2", tmp.get(0).toString());
        
        // Receiving the Data
        String imageURL = String.valueOf(tmp.get(0).get("image1"));
        
        if(tmp.get(0).get("image1") == null){
        	Log.d("image1", "null");
        }else{
        	Log.d("image1", tmp.get(0).get("image1").toString());
        }
        
        
//        imageURL = imageURL.substring(0, imageURL.length()-4);
        String title = String.valueOf(tmp.get(0).get("title"));
        String author = String.valueOf(tmp.get(0).get("displayName"));
        String desc = String.valueOf(tmp.get(0).get("description"));
        Long creationTime = i.getLongExtra("creationTime", 1334704639);
        Long uploadTime = i.getLongExtra("uploadTime", 1334704639);
        int numLikes = i.getIntExtra("numLikes", 0);
        int numViewed = i.getIntExtra("numViewed", 0);
        int numDownloads = i.getIntExtra("numDownloads", 0);
        int numComments = i.getIntExtra("numComments", 0);
        
        
//        String imageURL = i.getStringExtra("imageURL");
//        Log.d("Second Screen", numLikes + " + " + numViewed);
 
        Date creationDate = new Date( creationTime * 1000 );
        creationDate.toString();
        String date = new java.text.SimpleDateFormat
        ("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (creationTime));
        
//        Log.d("Detail", creationTime.toString() + " | " + creationDate.toString());
        
        // Displaying Received data
        txtTitle.setText(title);
        txtAuthor.setText(author);
        txtDesc.setText(desc);
        txtNumViewed.setText("" + numViewed);
        txtNumLikes.setText("" + numLikes);
        txtNumDownloads.setText("" + numDownloads);
        txtNumComments.setText("" + numComments);
        
        Image.setImageBitmap(loadImageByURL(imageURL));
 
        

		TextView globalHeader = (TextView) findViewById(R.id.globalHeader);
		globalHeader.setClickable(true);
		globalHeader.setOnClickListener(new View.OnClickListener() { 
            public void onClick(View arg0) {
                finish();
            }
        });
        // Binding Click event to Button
        btnClose.setOnClickListener(new View.OnClickListener() { 
            public void onClick(View arg0) {
                finish();
            }
        });
 
    }
    
    
    public Bitmap loadImageByURL(String imageFileURL) {
		try {
			URL url = new URL(imageFileURL);
			URLConnection conn = url.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = httpConn.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				inputStream.close();
				return bitmap;
			} else {
				// return null;
			}

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
