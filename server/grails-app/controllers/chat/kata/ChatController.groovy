package chat.kata

import java.util.Collection;


class ChatController {
	
	ChatService chatService

	def list(Integer seq) {
		
		if(hasErrors()){
			
			log.error("Invalid seq: ${errors.getFieldError('seq').rejectedValue}")
        	//TODO: send error about invalid seq
			
			render(status:400,contentType: "text/json") {
				error = "Invalid seq parameter"
			}
			
		}
		
		else{
		
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
		
	}

	def send(){
		
		if(!request.JSON){
			
			render(status:400,contentType: "text/json") {
				error = "Invalid body"
			}
		}
		
		else{
			
			ChatMessage chatMsg = new ChatMessage(request.JSON)
			
			if(!chatMsg.validate()){
				
				if (chatMsg.errors.hasFieldErrors("nick")) {
					render(status:400,contentType: "text/json") {
						error = "Missing nick parameter"
					}
				}
				
				if (chatMsg.errors.hasFieldErrors("message")) {
					render(status:400,contentType: "text/json") {
						error = "Missing message parameter"
					}
				}
				
			}
			
			else{
				chatService.putChatMessage(new ChatMessage(request.JSON))
				render(status:201)
			}
		}
		
	}
}
