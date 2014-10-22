package agents;

import java.util.ArrayList;
import java.util.Stack;

import kernel.World;

import agents.messages.Message;
import agents.messages.MessageType;

public abstract class Agent {
	
	private int ID;
	private String name;
	private Stack<Message> messages = new Stack<Message>();
	

	public void play() {
		readMessage();
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

	private void receiveMessage(Message message) {
		messages.push(message);
	}

	public void readMessage() {
		while (!messages.isEmpty()) {
			computeAMessage(messages.pop());
		}
	}
	
	public abstract void computeAMessage (Message m);

}
