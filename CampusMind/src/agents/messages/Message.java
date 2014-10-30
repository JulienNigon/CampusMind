package agents.messages;

import agents.Agent;


public class Message {

	private Object content;
	private MessageType type;
	private Agent sender;
	
	public Message (Object content, MessageType type, Agent sender) {
		this.content = content;
		this.type = type;
		this.sender = sender;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public Agent getSender() {
		return sender;
	}

	public void setSender(Agent sender) {
		this.sender = sender;
	}
	
	
	
}
