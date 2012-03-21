package com.android.aigproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainListAdapter extends ArrayAdapter<ListItem>{
    Context context; 
    int layoutResourceId;    
    ListItem data[] = null;
    
    // Initializer
    public MainListAdapter(Context context, int layoutResourceId, ListItem[] data) {
    	super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    /* This shit will be called for every item in the ListView,
     * to create views with their properties set as we want.
     * */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View row = convertView;
    	
    	/* This shit will be used to cache the ImageView and TextView
    	 *  so they can be reused for every row in the ListView
    	 */
    	ListItemHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ListItemHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            
            row.setTag(holder);
        }
        else
        {
            holder = (ListItemHolder)row.getTag();
        }
    	
    	
        ListItem weather = data[position];
        holder.txtTitle.setText(weather.title);
        holder.imgIcon.setImageResource(weather.icon);
    	return row;
    }
    
    /* Created temporarily for holding data into ListItem.
     * */
    static class ListItemHolder {
    	ImageView imgIcon;
    	TextView txtTitle;
    }
    
}
