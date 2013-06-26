package org.ejmc.android.simplechat.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.ejmc.android.simplechat.model.ChatList;
import org.ejmc.android.simplechat.model.Message;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.util.Log;


/**
 * Proxy to remote API.
 * 
 * @author startic
 * 
 */
public class NetRequests extends AsyncTask<Void, Void, String>{
		
	/**
	 * Gets chat messages from sequence number.
	 * 
	 * @param seq
	 * @param handler
	 */
	
	public void chatGET(int seq, NetResponseHandler<ChatList> handler, String ip) {
		
		String url = "http://" + ip + "/chat-kata/api/chat?seq=" + seq;
		
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response;
	
		try {
			
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {

	            // A Simple JSON Response Read
	            InputStream instream = entity.getContent();
	            String result= convertStreamToString(instream);
	            // now you have the string representation of the HTML request
	            instream.close(); 
	            
	            handler.onSuccess(getChatList(result));
	            
	        }
			
		} catch (Exception e) {
			Log.e("Exception: ", e.toString());
			
		}
	}
	
	private static String convertStreamToString(InputStream is) {
	    /*
	     * To convert the InputStream to String we use the BufferedReader.readLine()
	     * method. We iterate until the BufferedReader return null which means
	     * there's no more data to read. Each line will appended to a StringBuilder
	     * and returned as String.
	     */
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
	
	/* Método que parsea el JSON para construis el ChatList */
	private static ChatList getChatList(String result) throws JSONException{
		
		JSONObject jsonObject = new JSONObject(result);
        JSONArray jsonMainArr = new JSONArray();
        jsonMainArr = jsonObject.getJSONArray("messages");
        
        /* Recorremos el JSONArray con los mensajes */
        
        Message auxMessage;
        ChatList listaMensajes = new ChatList();
        
        for(int i=0; i<jsonMainArr.length(); i++){
        	JSONObject childJson = jsonMainArr.getJSONObject(i);
        	auxMessage = new Message(childJson.getString("nick"), childJson.getString("message"));
        	listaMensajes.addMessage(auxMessage);
        }
        
        int last_seq = Integer.parseInt(jsonObject.getString("last_seq"));
        listaMensajes.setLast_seq(last_seq);
        
        return listaMensajes;
	}
	
	/**
	 * POST message to chat.
	 * 
	 * @param message
	 * @param handler
	 */
	public void chatPOST(Message message, NetResponseHandler<Message> handler, String ip) {

		String url = "http://" + ip + "/chat-kata/api/chat";
		
		try {
			
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(url);
			httppost.setHeader("content-type", "application/json");
			JSONObject datoChat = new JSONObject();
			datoChat.put("nick", message.getNick());
			datoChat.put("message", message.getMessage());

			StringEntity entity = new StringEntity(datoChat.toString());
			httppost.setEntity(entity);

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity responseEntity = response.getEntity();

			if (responseEntity != null) {
				String responseString = EntityUtils.toString(responseEntity);
				Log.i("responseString", responseString);
			}

			Log.i("chatPOST: OK", "" + null);
		} catch (Exception e) {
			Log.e("Net", "Error in network call", e);
		}
	}
	
	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
