package agents;

import java.util.ArrayList;

import kernel.World;

import blackbox.BlackBoxAgent;
import agents.messages.Message;
import agents.messages.MessageType;

public class Variable extends SystemAgent{



	private double value;
	private BlackBoxAgent sensor;
	protected ArrayList<Agent> targets = new ArrayList<Agent>();

	public Variable(World world) {
		super(world);
		// TODO Auto-generated constructor stub
	}
	
	public void play() {
		super.play();
		value = sensor.getValue();  //TODO real acquisition
		for (Agent target : targets) {
			sendMessage(new Message(value,MessageType.VALUE,this),target);
			//System.out.println("Receveur : " + target.getClass().getSimpleName() + targets.size() + target.getName());
		}
	}
	
	@Override
	public void computeAMessage(Message m) {
		if(m.getType() == MessageType.REGISTER) {
		//	System.out.println("register nex context" + targets.size());
			targets.add(m.getSender());
		//	System.out.println(targets.size());
		}
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public BlackBoxAgent getSensor() {
		return sensor;
	}

	public void setSensor(BlackBoxAgent sensor) {
		this.sensor = sensor;
	}

	public ArrayList<Agent> getTargets() {
		return targets;
	}

	public void setTargets(ArrayList<Agent> targets) {
		this.targets = targets;
	}

	
	


	
}
