package agents.controler;

import java.util.ArrayList;
import java.util.HashMap;

import kernel.World;
import blackbox.Input;
import agents.Agent;
import agents.SystemAgent;
import agents.Variable;
import agents.context.Context;
import agents.criterion.Criterion;
import agents.messages.Message;
import agents.messages.MessageType;

public class Controller extends SystemAgent{

	public Controller(World world) {
		super(world);
		// TODO Auto-generated constructor stub
	}

	private ArrayList<Context> contexts = new ArrayList<Context>();
	private HashMap<Criterion,Double> criticity = new HashMap<Criterion,Double>();
	private HashMap<Criterion,Double> oldCriticity = new HashMap<Criterion,Double>();
	private Criterion criticalCriterion;
	private Context bestContext;
	private Input blackBoxInput;
	private double action;

	

	@Override
	public void computeAMessage(Message m) {
		//contexts.clear();

		if (m.getType() == MessageType.PROPOSAL) { //Value useless
	//		System.out.println("add  a context " + contexts.size());
			contexts.add((Context) m.getSender());
		}
		if (m.getType() == MessageType.VALUE) { //Criterion
		//	System.out.println("message reçu du criterion");
			Criterion newCriterion = (Criterion) m.getSender();
			criticity.put((Criterion) m.getSender(), (Double)m.getContent());
			if (criticalCriterion == null) {
				criticalCriterion = newCriterion;
			} else if (criticalCriterion.getCriticity() < newCriterion.getCriticity()) {
				criticalCriterion = newCriterion;
			}
		}
	}
	
	public void play() {
		bestContext = null;
		super.play();
	//	System.out.println("context size : " + contexts.size());
		if (oldCriticity.size() > 0) {			/*Let some time for initialisation*/
			if (contexts.size() > 0) selectBestContext();
			if (bestContext != null && bestContext.getPredictionFor(criticalCriterion) < 0) {
				//	System.out.println("Messages reçus! " + criticalCriterion.getName());
				
				//	System.out.println(blackBoxInput.getName());
				//	System.out.println(bestContext.getName());
				action = bestContext.getAction();
				sendMessage(action, MessageType.VALUE, blackBoxInput);
				Object[] infos = {criticalCriterion, criticalCriterion.getCriticity()};
				sendMessage(infos, MessageType.SELECTION, bestContext);
			}
			else
			{
				action = Math.random() * 10 - 5;
				sendMessage(action, MessageType.VALUE, blackBoxInput);  //TODO test only purpose
				Context context = new Context(world, this);
				context.setName(String.valueOf(context.hashCode()));
				world.startAgent(context);
				//	context.play();  //TODO dirty
			}
		}
		contexts.clear();
		criticalCriterion = null;

		
	}

	/**
	 * Save the old criticity before reading messages.
	 */
	public void readMessage() {
		oldCriticity = new HashMap<Criterion,Double>(criticity);

		super.readMessage();
//		System.out.println("Old contro : "  + oldCriticity.toString());
//		System.out.println("New contro : " + criticity.toString());
	}
	
	public ArrayList<Context> getContexts() {
		return contexts;
	}

	public void setContexts(ArrayList<Context> contexts) {
		this.contexts = contexts;
	}

	public Input getBlackBoxInput() {
		return blackBoxInput;
	}

	public void setBlackBoxInput(Input blackBoxInput) {
		this.blackBoxInput = blackBoxInput;
	}

	private void selectBestContext() {
		Context bc = contexts.get(0);
		for (Context context : contexts) {
			if (context.getPredictionFor(criticalCriterion) < bc.getPredictionFor(criticalCriterion)) {
				bc = context;
			}
		}
		bestContext = bc;
		//System.out.println("The best context is : " + bestContext.getName());
	}

	public Context getBestContext() {
		return bestContext;
	}

	public void setBestContext(Context bestContext) {
		this.bestContext = bestContext;
	}

	@Override
	public ArrayList<? extends Agent> getTargets() {
		return contexts;
	}

	public HashMap<Criterion, Double> getCriticity() {
		return criticity;
	}

	public void setCriticity(HashMap<Criterion, Double> criticity) {
		this.criticity = criticity;
	}

	public HashMap<Criterion, Double> getOldCriticity() {
		return oldCriticity;
	}

	public void setOldCriticity(HashMap<Criterion, Double> oldCriticity) {
		this.oldCriticity = oldCriticity;
	}

	public Criterion getCriticalCriterion() {
		return criticalCriterion;
	}

	public void setCriticalCriterion(Criterion criticalCriterion) {
		this.criticalCriterion = criticalCriterion;
	}

	public double getAction() {
		return action;
	}

	public void setAction(double action) {
		this.action = action;
	}

	
}
