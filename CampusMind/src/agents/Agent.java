package agents;

import java.util.ArrayList;
import java.util.Stack;

import kernel.World;

import agents.messages.Message;
import agents.messages.MessageType;

public abstract class Agent {
	
	protected int ID;
	protected String name;
	protected Stack<Message> messages = new Stack<Message>();
	protected Stack<Message> messagesBin = new Stack<Message>();


	public void play() {
	//	readMessage();
		//System.out.println("ID :" + ID + " is playing. My name is : " + name);
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Stack<Message> getMessages() {
		return messages;
	}

	public void setMessages(Stack<Message> messages) {
		this.messages = messages;
	}
	
	public void sendMessage(Message message, Agent a) {
		a.receiveMessage(message);
	}
	
	public void sendMessage(Object object, MessageType type, Agent a) {
		a.receiveMessage(new Message(object,type,this));
	}
	
	public void sendExpressMessage(Object object, MessageType type, Agent a) {
		a.receiveExpressMessage(new Message(object,type,this));
	}

	//TODO Improve?
	private void receiveExpressMessage(Message message) {
		computeAMessage(message);
	}
	
	private void receiveMessage(Message message) {
		messages.push(message);
	}

	public void readMessage() {
		messagesBin.clear();
		while (!messages.isEmpty()) {
			Message m = messages.pop();
			computeAMessage(m);
			messagesBin.push(m);
		}
	}
	
	
	
	public Stack<Message> getMessagesBin() {
		return messagesBin;
	}

	public void setMessagesBin(Stack<Message> messagesBin) {
		this.messagesBin = messagesBin;
	}

	public abstract void computeAMessage (Message m);

}
