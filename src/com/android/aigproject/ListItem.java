package com.android.aigproject;

public class ListItem {
    public int icon;
    public String title;
    public String imageFileURL;
    
    public ListItem(){
        super();
    }
    
    public ListItem(int icon, String title, String imageFileURL) {
        super();
        this.icon = icon;
        this.title = title;
        this.imageFileURL = imageFileURL;
    }
}
