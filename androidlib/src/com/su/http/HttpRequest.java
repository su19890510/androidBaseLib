package com.su.http;
/**
 * zhaohui.su
 * 2016-6-15
 * 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import com.su.http.HttpType;
public class HttpRequest {

	private static HttpRequest  _httpRequest = null;
	private final int TIME_OUT = 6000;
	public static HttpRequest getInstance()
	{
		if(_httpRequest == null)
		{
			_httpRequest = new HttpRequest();
		}
		return _httpRequest;
	}
	
	public String sendRequest(String url,String data ,HttpType type)
	{
		String ret = "";
		if(type == HttpType.GET)
		{
			ret = sendGetRequest(url);
		}
		else if (type == HttpType.POST)
		{
			ret = sendPostRequest(url, data);
		}
		return ret;
	}
	public String sendGetRequest(String url)
	{
		String ret = "";
		HttpURLConnection conn = null;
		try{
			URL murl = new URL(url);
			conn = (HttpURLConnection) murl.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			
			int responseCode = conn.getResponseCode();
			if(responseCode == 200)
			{
				InputStream in = conn.getInputStream();
				ret = getStringFromInputStream(in);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			
		}finally{
			if(conn != null )
			{
				conn.disconnect();
			}
			
		}
		return ret;
	}
	public String sendPostRequest(String url ,String data)
	{
		String ret = "";
		HttpURLConnection conn = null;
		try {
			URL murl = new URL(url);
			conn = (HttpURLConnection) murl.openConnection();
			conn.setRequestMethod("POST");
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			OutputStream out = conn.getOutputStream();
			out.write(data.getBytes());
			out.flush();
			out.close();
			
			int responseCode = conn.getResponseCode();
			if(responseCode == 200)
			{
				InputStream is = conn.getInputStream();
				ret = getStringFromInputStream(is);
			}
			else{
				ret ="{result:"+ String.valueOf(responseCode) + "}";
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if(conn != null)
			{
				conn.disconnect();
			}
		}
		return ret;
	}
	private String getStringFromInputStream(InputStream in)
	{
		String ret = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		try {
				while((line = reader.readLine() )!= null)
				{
					buffer.append(line);
				}
				in.close();
				reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ret = buffer.toString();
		return ret;
	}
	
}
