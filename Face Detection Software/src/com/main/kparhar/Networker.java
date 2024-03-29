package com.main.kparhar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Networker {
	
	public static String sendPOST(String urlForConnection, Map<String, String> parameters) throws IOException {
		URL url = new URL(urlForConnection);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0");
		connection.setRequestProperty("Content-Type", "application/json;");
		connection.setDoOutput(true);
		
		OutputStream os = connection.getOutputStream();
		os.write(createJSONFromParams(parameters).getBytes());
		os.flush();
		os.close();
		
		InputStream is = connection.getInputStream();
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream responseBuffer = new ByteArrayOutputStream();

		while(true) { 
		  int n = is.read(buffer, 0, buffer.length);
		  if(n <= 0) break;
		  responseBuffer.write(buffer, 0, n);
		}

		String response = responseBuffer.toString("UTF-8");
		return response;
	}

	public static String createURLFromParams(String urlForConnection, Map<String, String> parameters) {
		String url = "";
		url += urlForConnection;
		url += "?";
		
		for(Map.Entry<String, String> param: parameters.entrySet()) {
			url += param.getKey() + "=" + param.getValue();
			url += "&";
		}
		url = url.substring(0, url.length()-1);
		
		return url;
	}
	
	public static String createJSONFromParams(Map<String, String> parameters) {
		String json = "{\n";
		for(Map.Entry<String, String> param: parameters.entrySet()) {
			json += "\t\"" + param.getKey() + "\": \"" + param.getValue() + "\",\n";
		}
		json+="}";
		return json;
	}

}
