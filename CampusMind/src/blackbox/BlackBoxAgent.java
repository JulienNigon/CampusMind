package blackbox;

import java.util.ArrayList;

import agents.Agent;
import agents.messages.Message;

public abstract class BlackBoxAgent extends Agent{

	protected ArrayList<Agent> targets = new ArrayList<Agent>();
	
	@Override
	public abstract void computeAMessage(Message m);

	public ArrayList<Agent> getTargets() {
		return targets;
	}

	public void setTargets(ArrayList<Agent> targets) {
		this.targets = targets;
	}
	
	public void readMessage() {
		super.readMessage();
		fastPlay();
	}
	
	public abstract double getValue();
	
	public abstract void fastPlay();
	
}
