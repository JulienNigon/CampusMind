package agents.controler;

import java.util.ArrayList;

import kernel.World;

import blackbox.Input;

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
	private Criterion criticalCriterion;
	private Context bestContext;
	private Input blackBoxInput;


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
		if (contexts.size() > 0) selectBestContext();
		if (bestContext != null) {
		//	System.out.println("Messages reçus! " + criticalCriterion.getName());
			
		//	System.out.println(blackBoxInput.getName());
		//	System.out.println(bestContext.getName());

			blackBoxInput.sendMessage(bestContext.getAction(), MessageType.VALUE, blackBoxInput);
		}
		else
		{
			blackBoxInput.sendMessage(0.1, MessageType.VALUE, blackBoxInput);  //TODO test only purpose
			Context context = new Context(world, this);
			context.setName(String.valueOf(context.hashCode()));
			world.startAgent(context);
		}
		contexts.clear();
		criticalCriterion = null;

		
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
			if (context.getPredictionFor(criticalCriterion) > bc.getPredictionFor(criticalCriterion)) {
				bc = context;
			}
		}
		bestContext = bc;
		System.out.println("The best context is : " + bestContext.getName());
	}

	public Context getBestContext() {
		return bestContext;
	}

	public void setBestContext(Context bestContext) {
		this.bestContext = bestContext;
	}

}
