package com.android.aigproject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryListAdapter extends ArrayAdapter<ListItem>{
    Context context; 
    int layoutResourceId;    
    ArrayList<ListItem> data;
    //ListItem data[] = null;
    Boolean isCategory;
    
    // Initializer
    public CategoryListAdapter(Context context, int layoutResourceId, 
    		ArrayList<ListItem> data, Boolean isCategory) {
    	super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.isCategory = isCategory;
    }

    /* This will be called for every item in the ListView,
     * to create views with their properties set as we want.
     * */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View row = convertView;
    	
    	/* This will be used to cache the ImageView and TextView
    	 *  so they can be reused for every row in the ListView
    	 */
    	ListItemHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ListItemHolder();
            
//            ImageView image = (ImageView)row.findViewById(R.id.thumbnail);
//            image.setImageBitmap(loadImageByURL(holder.imageFileURL));
//            holder.imgIcon = image;
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            
            row.setTag(holder);
        }
        else
        {
            holder = (ListItemHolder)row.getTag();
        }
    	

        ListItem source_item = data.get(position);
        if (isCategory) {
            holder.txtTitle.setText("Category - " + source_item.title);
        } else {
            holder.txtTitle.setText(source_item.title);        	
        }

//        holder.imgIcon.setImageResource(source_item.icon);
//        holder.imgIcon.setImageBitmap(loadImageByURL(source_item.imageFileURL));
    	return row;
    }
    
    /* Created temporarily for holding data into ListItem.
     * */
    static class ListItemHolder {
    	ImageView imgIcon;
    	TextView txtTitle;
    	String imageFileURL;
    }
    
    
    
}
