package kernel;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import view.system.GrapheSystemPanel;
import view.system.MainPanel;
import view.system.MainTabbedPanel;
import view.system.ScheduledItem;
import agents.Agent;
import agents.controler.Controller;

public class Scheduler {

	private ArrayList<Agent> agents = new ArrayList<Agent>();
	private ArrayList<Agent> waitList = new ArrayList<Agent>();
	private ArrayList<Agent> killList = new ArrayList<Agent>();
	private ArrayList<ScheduledItem> scheduled = new ArrayList<ScheduledItem>();

	private boolean running = false;
	private int tick;
	private int nextID = 0;
	private MainPanel view;
	private boolean waitForGUIUpdate = false;
	private boolean playOneStep = false;
	private World world;
	private GrapheSystemPanel graphSystemPanel;
	
	public Scheduler() {
		agents = new ArrayList<Agent>();
		this.world = world;
	}
	
	public void registerAgent(Agent a) {
		waitList.add(a);
	//	if (graphSystemPanel != null) graphSystemPanel.newAgent(a);
	}
	
	public void start(boolean running) {
		this.running = running;
		run();
	}
	
	private void run() {
		while(true){
			
			if (!running) {
			    try {
			        Thread.sleep(1);
			    } catch (InterruptedException ignore) {
			    }
			}
			
			while (running) {
				
				for (Agent a : waitList) {
					a.setID(nextID);
					nextID++;
					int i = 0;
					while ((i < agents.size() && agents.get(i).getClass() != a.getClass()) &&  !(agents.get(i) instanceof Controller)) i++;
					agents.add(i,a);
				}
				waitList.clear();

				for (Agent agent : killList) {
					agents.remove(agent);
				}
				killList.clear();
				
				//Compute message
				for (Agent agent : agents) {
					agent.readMessage();
					agent.play();

				}
				
				//Act
		//		for (Agent agent : agents) {
		//			agent.play();
		//		}
				
				//Scheduled item
				for (ScheduledItem item : scheduled) {
					item.update();
				}

				if (view != null) {
					try {
						SwingUtilities.invokeAndWait(new Runnable() {
							public void run() {
								view.update();
							}
						});
					} catch (InvocationTargetException | InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				world.manageWorld();
				
				tick++;
				if (playOneStep) {
					playOneStep = false;
					running = false;
				}
			}

				// if (tick == 15) System.exit(0);
			}
		
	
	}
	
	public void killAgent(Agent a) {
		killList.add(a);
		//agents.remove(a);
	}

	public ArrayList<Agent> getAgents() {
		return agents;
	}

	public void setAgents(ArrayList<Agent> agents) {
		this.agents = agents;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public int getTick() {
		return tick;
	}

	public void setTick(int tick) {
		this.tick = tick;
	}

	public int getNextID() {
		return nextID;
	}

	public void setNextID(int nextID) {
		this.nextID = nextID;
	}

	public MainPanel getView() {
		return view;
	}

	public void setView(MainPanel view) {
		this.view = view;
	}

	public boolean isWaitForGUIUpdate() {
		return waitForGUIUpdate;
	}

	public void setWaitForGUIUpdate(boolean waitForGUIUpdate) {
		this.waitForGUIUpdate = waitForGUIUpdate;
	}

	public void playOneStep() {
		setRunning(true);
		playOneStep = true;
		
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public GrapheSystemPanel getGraphSystemPanel() {
		return graphSystemPanel;
	}

	public void setGraphSystemPanel(GrapheSystemPanel graphSystemPanel) {
		this.graphSystemPanel = graphSystemPanel;
	}
	
	public void addScheduledItem(ScheduledItem item) {
		scheduled.add(item);
	}
	

	
}
