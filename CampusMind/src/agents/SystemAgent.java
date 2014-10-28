package agents;

import java.util.ArrayList;

import kernel.World;
import agents.messages.Message;

public abstract class SystemAgent extends Agent{

	
	protected World world;

	
	public SystemAgent(World world) {
		this.world = world;
		// TODO Auto-generated constructor stub
	}

	/*
	 * Return all possible agents for message sending
	 */
	abstract public ArrayList<? extends Agent> getTargets();


}
