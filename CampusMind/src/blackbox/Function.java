package blackbox;

import java.util.ArrayList;

import agents.Agent;
import agents.messages.Message;
import agents.messages.MessageType;

public class Function extends BlackBoxAgent{

	private MathFunction func;
	private Agent agentA;
	private Agent agentB;
	private double a;
	private double b;
	
	public Function() {
		
	}

	public void play() {
		super.play();
		double result = func.compute(a, b);
		for (Agent target : targets) {
			sendMessage(new Message(result,MessageType.VALUE,this),target);
		}	
	}
	


	public MathFunction getFunc() {
		return func;
	}

	public void setFunc(MathFunction func) {
		this.func = func;
	}

	@Override
	public void computeAMessage(Message m) {
		if (m.getType() == MessageType.VALUE) {
			//Agent ag = (Agent) m.getContent();
			if (m.getSender() == agentA) {
				a = (double) m.getContent();
			} else if (m.getSender() == agentB) {
				b = (double) m.getContent();
			}
		}		
	}

	public Agent getAgentA() {
		return agentA;
	}

	public void setAgentA(Agent agentA) {
		this.agentA = agentA;
	}

	public Agent getAgentB() {
		return agentB;
	}

	public void setAgentB(Agent agentB) {
		this.agentB = agentB;
	}

	public double getValue() {
		return func.compute(a, b);
	}
	
	

	
	
	
	
}
