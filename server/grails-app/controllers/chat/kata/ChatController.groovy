package chat.kata

import java.util.Collection;


class ChatController {
	
	ChatService chatService

	def list(Integer seq) {
		if(hasErrors()){
			log.error("Invalid seq: ${errors.getFieldError('seq').rejectedValue}")
        	//TODO: send error about invalid seq
		}
		
		Collection<ChatMessage> newMessages = new ArrayList()
		Integer last_seq2 = chatService.collectChatMessages(newMessages, seq)
		
		
		render(contentType: "text/json"){
			messages = []
				
			for(m in newMessages){
				messages.add(nick:m.getNick(),message:m.getMessage())
			}
			
			last_seq = last_seq2	
			
		}
		
	}

	def send(){
		
		chatService.putChatMessage(new ChatMessage(request.JSON))
		render(status:201)
		
		
	}
}
