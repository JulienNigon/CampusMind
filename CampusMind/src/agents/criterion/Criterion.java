package agents.criterion;

import java.util.ArrayList;

import kernel.World;
import agents.Agent;
import agents.SystemAgent;
import agents.controler.Controller;
import agents.messages.Message;
import agents.messages.MessageType;

public class Criterion extends SystemAgent{

	public Criterion(World world) {
		super(world);
		// TODO Auto-generated constructor stub
	}


	private double criticity;
	private CriticityType criticityType;
	private double reference;
	private ArrayList<Agent> targets = new ArrayList<Agent>();

	public void play() {
		super.play();
		for (Agent agent : targets) {
			this.sendMessage(new Message(criticity, MessageType.VALUE, this), agent);
		}
	}
	
	
	@Override
	public void computeAMessage(Message m) {
		if (m.getType() == MessageType.VALUE) {
			criticity = criticityType.computeCriticity((double)m.getContent(), reference);
		}
		if(m.getType() == MessageType.REGISTER) {
			targets.add(m.getSender());
		}
		
	}

	public double getCriticity() {
		return criticity;
	}

	public void setCriticity(double criticity) {
		this.criticity = criticity;
	}

	public CriticityType getCriticityType() {
		return criticityType;
	}

	public void setCriticityType(CriticityType criticityType) {
		this.criticityType = criticityType;
	}

	public double getReference() {
		return reference;
	}

	public void setReference(double reference) {
		this.reference = reference;
	}


	public void setTargets(ArrayList<Agent> controlers) {
		this.targets = controlers;
	}


	@Override
	public ArrayList<? extends Agent> getTargets() {
		return targets;
	}


	public void addTarget(Agent a) {
		targets.add(a);
	}


}
