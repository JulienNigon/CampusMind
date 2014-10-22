package blackbox;

import java.util.ArrayList;

import agents.Agent;
import agents.messages.Message;
import agents.messages.MessageType;

public class Output extends BlackBoxAgent{

	private Function func;
	private double value;
	
	public void play() {
		super.play();
	//	System.out.println(this.getName() + " : " + value);
	}
	
	
	@Override
	public void computeAMessage(Message m) {
		if (m.getType() == MessageType.VALUE) {
			value = (double) m.getContent();
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

	


}
