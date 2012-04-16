package com.android.aigproject;


public class URLFactory {
	
	
	public static enum Type {
	    TITLE,
	    DESCRIPTION,
	    TAG,
	    AUTHORID
	}
	
	
	
	public static String generate(Type type, String query){
		
		if(type == Type.TITLE){
			return getByTitle(query);
		}else if(type == Type.DESCRIPTION){
			return getByDescription(query);
		}else if(type == Type.TAG){
			return getByTag(query);
		}else if(type == Type.AUTHORID){
			return getByAuthorId(query);
		}
		
		
		return query;
		
	}
	
	private static String getByTitle(String query){
		
		String s = "http://app-inventor-gallery.appspot.com/rpc?tag=search:" + query;
		
		return s;
		
	}
	
	
	private static String getByDescription(String query){
		
		String s = "http://app-inventor-gallery.appspot.com/rpc?tag=getinfo:" + query;
		
		return s;
		
	}
	
	
	private static String getByTag(String query){
		
		String s = "http://app-inventor-gallery.appspot.com/rpc?tag=tag:" + query;
		
		return s;
		
	}
	
	private static String getByAuthorId(String query){
		
		String s = "http://app-inventor-gallery.appspot.com/rpc?tag=by_developer:" + query;
		
		return s;
		
	}
	
	
	
}
