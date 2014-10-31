package agents;

import java.util.ArrayList;

import kernel.World;
import agents.messages.Message;

public abstract class SystemAgent extends Agent{

	
	protected World world;
	
	public void play() {
	//	System.out.println(this.getClass().getSimpleName());
		super.play();
	}

	
	public SystemAgent(World world) {
		this.world = world;
	//	System.out.println(this.getClass().getSimpleName());
		world.changeAgentNumber(1, this.getClass().getSimpleName());
		// TODO Auto-generated constructor stub
	}

	/*
	 * Return all possible agents for message sending
	 */
	abstract public ArrayList<? extends Agent> getTargets();


}
