package org.ejmc.android.simplechat.net;

import org.ejmc.android.simplechat.model.ChatList;
import org.ejmc.android.simplechat.model.RequestError;


/**
 * Empty response handler.
 * 
 * Base class for Net Response handlers.
 * 
 * @author startic
 * 
 * @param <Response>
 */
public class NetResponseHandler<Response> {

	
	private ChatList listaMsg;
	
	/**
	 * Handles a successful request
	 * */
	public void onSuccess(Response response) {
		
		listaMsg = (ChatList)response;
		
	}

	/**
	 * Handles a network error.
	 */
	public void onNetError() {

	}

	/**
	 * Handles a request error.
	 */
	public void onRequestError(RequestError error) {

	}
	

	public ChatList getMsg() {
		return listaMsg;
	}

	public void setMsg(ChatList msg) {
		this.listaMsg = msg;
	}	
}
