package chat.kata

import java.lang.reflect.Array
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantReadWriteLock



class ChatService {
	
	
	/*  Store all the messages */
	Collection<ChatMessage> allMessages = new ArrayList()
	
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock r = rwl.readLock();
	private final Lock w = rwl.writeLock();
	
	
	/**
	 * Collects chat messages in the provided collection
	 * 
	 * @param if specified messages are collected from the provided sequence (exclusive)
	 * @param messages the collection where to add collected messages
	 * 
	 * @return the sequence of the last message collected.
	 */
	Integer collectChatMessages(Collection<ChatMessage> collector, Integer fromSeq = null){
		
		/* Return the position of the current message */
		
		Integer currentMess = 0
		
		if(fromSeq == null){
			currentMess = 0
		}else{
			currentMess = fromSeq+1
		}
		
		r.lock()
		
		
		try{
		
			while(currentMess < allMessages.size()){
				collector.add(allMessages[currentMess])
				currentMess++
			}

		}catch (Exception e) {
			log.error("Exception: " + e.toString())
		}finally{
		
			/* Si se produce una excepción desbloqueamos la lectura */
			r.unlock()

		}

		
		currentMess--
		return currentMess
		
	}
	
	/**
	 * Puts a new message at the bottom of the chat
	 * 
	 * @param message the message to add to the chat
	 */
	void putChatMessage(ChatMessage message){
	
		/* Add the last message to the bottom of the collector */
		
		/* Antes de escribir bloqueamos la lectura y la escritura */
		w.lock()
		
		try{
			
			allMessages.add(message);
		
		}finally{
		
			w.unlock()
		
		}
	}
	
	
}
