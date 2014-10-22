package blackbox;

import java.util.ArrayList;

import agents.Agent;
import agents.messages.Message;
import agents.messages.MessageType;

public class Input extends BlackBoxAgent{

	private Function func;
	private double value;
	
	
	public void play() {
		super.play();
		for (Agent target : targets) {
			sendMessage(new Message(value,MessageType.VALUE,this),target);
		}
	}


	public Function getFunc() {
		return func;
	}

	public void setFunc(Function func) {
		this.func = func;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}


	@Override
	public void computeAMessage(Message m) {
		if (m.getType() == MessageType.VALUE) {
			value = (double) m.getContent();
		}
		
	}

	
}
