package com.android.aigproject;


import java.util.ArrayList;
import java.util.HashMap;

import com.android.aigproject.AIGProjectActivity.SearchType;
import com.android.aigproject.URLFactory.Type;

import android.app.Service;

import android.content.Intent;

import android.os.IBinder;
import android.util.Log;


public class MyService extends Service {

	final static String MY_ACTION = "MY_ACTION";
	

	private static MyService instance;
	private ArrayList<HashMap<String, Object>> jSonInfo;
	

    public static MyService getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
    }
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

//		MyThread myThread = new MyThread();
//		myThread.start();
		
	
		return super.onStartCommand(intent, flags, startId);
	}
	
	public synchronized void continueService(SearchType currentType, String querySingle, Type[] types, String[] queries){
		
		MyThread myThread = new MyThread(currentType, querySingle, types, queries);
		myThread.start();
	}

	
	public synchronized ArrayList<HashMap<String, Object>> getResult(){
		return this.jSonInfo;
	}
	
	
	public void donothing() {
        return;
    }
	
	
	public class MyThread extends Thread {
		
		SearchType currentType;
		String querySingle;
		Type[] types;
		String[] queries;
		
		
		MyThread(SearchType currentType, String querySingle, Type[] types, String[] queries){
			this.currentType = currentType;
			this.querySingle = querySingle;
			this.types = types;
			this.queries = queries;
			
	
		}
		
		
		
		
		
		@Override
		public void run() {
			
			
			jSonInfo = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> tmp;

			if (currentType == SearchType.ALL) { // Search ALL
				String URL = URLFactory.generate(URLFactory.Type.ALL,
						querySingle);
				tmp = JsonGrabber.retrieveQueryArray(URL);
				if (tmp != null) {
					jSonInfo.addAll(tmp);
				}

				
			} else if (currentType == SearchType.SPECIFIC) { // b == false
																// //Search
																// Specific
				for (int i = 0; i < queries.length; i++) {
					if (queries[i] != null) {

						String URL = URLFactory.generate(types[i],
								queries[i]);
						tmp = JsonGrabber.retrieveQueryArray(URL);
						if (tmp != null) {
							jSonInfo.addAll(tmp);
						}
					}
				}
				
			}
			
			
			

				
			Intent intent = new Intent();
			intent.setAction(MY_ACTION);
			sendBroadcast(intent);
			
			stopSelf();

			
			Log.d("thread died", this.getName());
			
		}

	}
	
	

	
	
	

}