package agents.criterion;

import java.util.ArrayList;

import kernel.World;
import agents.Agent;
import agents.SystemAgent;
import agents.controler.Controller;
import agents.messages.Message;
import agents.messages.MessageType;

public class Criterion extends SystemAgent{



	private double criticity;
	private double variation;
	private CriticityType criticityType;
	private double reference;
	private ArrayList<Agent> targets = new ArrayList<Agent>();
	private double value;

	public Criterion(World world) {
		super(world);
	}
	public void play() {
		super.play();
		for (Agent agent : targets) {
			this.sendMessage(new Message(criticity, MessageType.VALUE, this), agent);
		}
	}
	
	
	@Override
	public void computeAMessage(Message m) {
		if (m.getType() == MessageType.VALUE) {
			setValue((double)m.getContent());
			double temp = criticityType.computeCriticity((double)m.getContent(), reference);
			variation = temp - criticity;
			criticity = temp;
//			System.out.println(this.getName() + " : " + temp);
		}
		if(m.getType() == MessageType.REGISTER) {
			targets.add(m.getSender());
		} else if(m.getType() == MessageType.UNREGISTER) {
			targets.remove(m.getSender());
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


	public double getVariation() {
		return variation;
	}


	public void setVariation(double variation) {
		this.variation = variation;
	}


	public double getValue() {
		return value;
	}


	public void setValue(double value) {
		this.value = value;
	}

	

}
