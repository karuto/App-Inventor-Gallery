package com.android.aigproject;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class UrlReader {

	private static String read(String url) throws IOException {
		StringBuffer html = new StringBuffer();

		URL addrUrl = null;
		URLConnection urlConn = null;
		BufferedReader br = null;
		try {
			addrUrl = new URL(url);
			urlConn = addrUrl.openConnection();
			br = new BufferedReader(new InputStreamReader(
					urlConn.getInputStream()));

			String buf = null;
			while ((buf = br.readLine()) != null) {
				html.append(buf + "\r\n");
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}

		return html.toString();
	}
	
	public static String generalGet(String URL) {
		String html = null;
		try {
			html = UrlReader.read(URL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return html;		
		
	}
	

	public static String search(String tagValue, String URL) {

		//String URL = "http://app-inventor-gallery.appspot.com/rpc?tag=search:";

		String html = null;

		String urlTotal = URL + tagValue;

		try {
			html = UrlReader.read(urlTotal);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return html;

	}

}