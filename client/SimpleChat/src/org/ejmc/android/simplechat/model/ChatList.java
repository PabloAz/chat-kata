package org.ejmc.android.simplechat.model;

import java.util.ArrayList;


/**
 * List off chat messages..
 * 
 * @author startic
 *
 */
public class ChatList {
	
	
	ArrayList<Message> listaChat;
	int last_seq;
	
	public ChatList() {
		listaChat = new ArrayList<Message>();
		last_seq = 0;
	}
	

	public ChatList(ArrayList<Message> listaChat, int last_seq) {
		super();
		this.listaChat = listaChat;
		this.last_seq = last_seq;
	}

	public ArrayList<Message> getListaChat() {
		return listaChat;
	}

	public void setListaChat(ArrayList<Message> listaChat) {
		this.listaChat = listaChat;
	}
	
	public void addMessage(Message msg){
		listaChat.add(msg);
	}


	public int getLast_seq() {
		return last_seq;
	}


	public void setLast_seq(int last_seq) {
		this.last_seq = last_seq;
	}
	
	
	
}
