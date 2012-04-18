package com.android.aigproject;

public class ListItem {
    public int icon;
    public String title, imageFileURL, author, desc;
    Long creationTime, uploadTime; 
    public int numLikes, numViewed, numDownloads, numComments;
    
    
    public ListItem(){
        super();
    }
    
    public ListItem(int icon, 
    		String title, String imageFileURL, String author, String desc, 
    		Long creationTime, Long uploadTime,
    		int numLikes, int numViewed, int numDownloads, int numComments) {
        super();
        this.icon = icon;
        this.title = title;
        this.imageFileURL = imageFileURL;
        this.author = author;
        this.desc = desc;
        this.creationTime = creationTime;
        this.uploadTime = uploadTime;
        this.numLikes = numLikes;
        this.numViewed = numViewed;
        this.numDownloads = numDownloads;
        this.numComments = numComments;
    }
}
