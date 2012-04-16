package com.android.aigproject;

public class ListItem {
    public int icon;
    public String title;
    public String imageFileURL;
    public String author;
    public String desc;
    public int numLikes;
    
    public ListItem(){
        super();
    }
    
    public ListItem(int icon, String title, String imageFileURL,
    		String author, String desc, int numLikes) {
        super();
        this.icon = icon;
        this.title = title;
        this.imageFileURL = imageFileURL;
        this.author = author;
        this.desc = desc;
        this.numLikes = numLikes;
    }
}
