package agents.context;

import java.util.ArrayList;
import java.util.HashMap;

import ncs.NCS;
import kernel.World;
import agents.Agent;
import agents.SystemAgent;
import agents.Variable;
import agents.controler.Controller;
import agents.criterion.Criterion;
import agents.messages.Message;
import agents.messages.MessageType;

public class Context extends SystemAgent{



	private HashMap<Variable,Range> ranges = new HashMap<Variable,Range>();
	//TODO : hashmap is probably too much
	private HashMap<Criterion,Double> predictions = new HashMap<Criterion,Double>();
	private HashMap<Criterion,Double> criticity = new HashMap<Criterion,Double>();
	private HashMap<Criterion,Double> oldCriticity = new HashMap<Criterion,Double>();
	private Controller controller;
	private double action = -1.1;
	private int nSelection = 0;
	private Criterion criticalCriterion;
	private double lastCriticity;
	private boolean selected = false;
	private final double acceptedError = 0.000000001;  //TODO : to improve
	private boolean unselectable = false;
	private boolean needPredictions = true;
	private boolean valid = false;
	private boolean aborted = false;

	public Context(World world, Controller controller) {
		super(world);
		action = controller.getAction();
		this.controller = controller;
		ArrayList<Variable> var = (ArrayList<Variable>) world.getAllAgentInstanceOf(Variable.class);
		for (Variable v : var) {
			ranges.put(v, new Range(v.getValue()-200,v.getValue()+200)); //TODO start range
			ranges.get(v).setValue(v.getValue());
			sendExpressMessage(null,MessageType.REGISTER,v);
//			System.out.println("REQUEST SEND");
		}
		ArrayList<Criterion> tempCrit = (ArrayList<Criterion>) world.getAllAgentInstanceOf(Criterion.class);
		for (Criterion c : tempCrit) {
			System.out.println("put prediction : " + c.getName());
			predictions.put(c, 1.00); //TODO start range
			sendExpressMessage(null,MessageType.REGISTER,c);
		}
		criticity = new HashMap<Criterion,Double>(controller.getCriticity());
		oldCriticity = new HashMap<Criterion,Double>(controller.getOldCriticity());
	//	System.out.println("Old : "  + oldCriticity.toString());
	//	System.out.println("New : " + criticity.toString());
	}
	
	/**
	 * Save the old criticity before reading messages.
	 */
	public void readMessage() {
		
//		if (!needPredictions) oldCriticity = new HashMap<Criterion,Double>(criticity);
		super.readMessage();
	}
	
	@Override
	public void computeAMessage(Message m) {
		if (m.getType() == MessageType.VALUE) {
			if(m.getSender() instanceof Variable) {
	//			System.out.println(m.getContent());
				ranges.get(m.getSender()).setValue((Double)m.getContent());
			}
			else if(m.getSender() instanceof Criterion) {
				oldCriticity.put((Criterion) m.getSender(), criticity.get((Criterion) m.getSender()));
				criticity.put((Criterion) m.getSender(), (Double)m.getContent());
			}
			
			//TODO add variable
		} else if (m.getType() == MessageType.SELECTION) {
			nSelection++;
			selected = true;
			//TODO dirty
			criticalCriterion = (Criterion) ((Object[])(m.getContent()))[0];
			lastCriticity = (double) ((Object[])(m.getContent()))[1];
		} else if (m.getType() == MessageType.ABORT) {
			aborted = true;
		}
	}
	


	public void play() {
		super.play();
		
		if(!selected) nSelection = 0;
		
		/*Init the new predictions*/
		if (this.needPredictions) {
			for (Criterion c : predictions.keySet()) {
//				System.out.println("Old : "  + oldCriticity.toString());
//				System.out.println("New : " + criticity.toString());
				predictions.put(c, criticity.get(c) - oldCriticity.get(c));
			}
			needPredictions = false;
		}
		
//		System.out.println(this.getName() + " : " + (criticity.get(criticity.keySet().toArray()[0]) - oldCriticity.get(oldCriticity.keySet().toArray()[0])));
		
		
		boolean predictionValidity = checkPredictionValidity();
		
		if (nSelection > 0) {  /*The context was selected*/
			if (!predictionValidity) {  /*If predictions are false*/
				if (checkForNCS_FalsePredictions()) {
					unselectable = true;
					solveNCS_falsePredictions();
				} else {
					solveNCS_inexactPredictions();
				}
			}
			//solveNCS_inexactPredictions();
			//TODO :improve
		}
		
		if (nSelection >= 2) {
			solveNCS_improductivity();
		}
		
		/*Update the validity statue of the context*/
		valid = checkRanges();
		
		/* If the context is valid, send a message to the controller.*/
		if (valid && !unselectable) {
			sendMessage(action, MessageType.PROPOSAL, controller);   //TODO : notifier des changements plutot?
		}
		
		if (predictionValidity && aborted) {
			solveNCS_Inproductive_Range();
		}
		
		for (Variable key : ranges.keySet()) {
			if (ranges.get(key).isTooSmall()) {
				solveNCS_uselessness();
			}
		}
		
		criticalCriterion = null;
		lastCriticity = 0.00;
		
		selected = false;
		unselectable = false;
		aborted = false;
	}
	
	private void solveNCS_uselessness() {
		NCS.CONTEXT_USELESSNESS.raiseNCS(world);
		this.die();
	}

	private void solveNCS_improductivity() {  /*NCS 7 in Jeremy thesis*/
		// TODO raise NCS
		// TODO AVT
		NCS.CONTEXT_IMPRODUCTIVE_ACTION.raiseNCS(world);
//		action += (0.01*action);
	}

	private void solveNCS_inexactPredictions() {  /*NCS 5 in Jeremy thesis*/
//		System.out.println("inexact prediction");
		NCS.CONTEXT_CONFLICT_INEXACT.raiseNCS(world);

		// TODO : smooth the change
		for (Criterion c : predictions.keySet()) {
			predictions.put(c, criticity.get(c) - oldCriticity.get(c));
		}
		
	}

	private void solveNCS_Inproductive_Range() {  /*NCS 8 in Jeremy thesis*/
		NCS.CONTEXT_IMPRODUCTIVE_RANGE.raiseNCS(world);
		for (Variable v : ranges.keySet()) {
			ranges.get(v).fit();
		}		
	}
	
	private void solveNCS_falsePredictions() {  /*NCS 4 in Jeremy thesis*/
	//	System.out.println("false prediction");
		NCS.CONTEXT_CONFLICT_FALSE.raiseNCS(world);
		for (Variable v : ranges.keySet()) {
			ranges.get(v).fit();
		}		
	}

	/**
	 * 
	 * @return false if all criticity change with the same sign that predictions, else true.
	 */
	public boolean checkForNCS_FalsePredictions() {
		for (Criterion c : criticity.keySet()) {
			if (criticity.get(c)-oldCriticity.get(c) >= 0){  /*Check if the sign of variation is good*/
	//			System.out.println(this.getName() + " est completement faux!!");
				return true;
			}
		}
//		System.out.println("INEXACT");
		return false;
	}
	
	/**
	 * 
	 * @return true if all criticity change according to predictions, else false.
	 */
	public boolean checkPredictionValidity() {
		for (Criterion c : criticity.keySet()) {
		//	System.out.println(oldCriticity.get(c) + predictions.get(c) + "  " + criticity.get(c)+ "  " + acceptedError);
			if (!equalWithMargin(oldCriticity.get(c) + predictions.get(c), criticity.get(c), acceptedError)){
		//		System.out.println("false prediction");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Utility to check a near equality.
	 */
	public static boolean equalWithMargin(double a, double b, double percentMargin) {
//		System.out.println("Result : " + a + "    " + (b - (percentMargin*b)));
//		System.out.println("Result : " + a + "    " + (b + (percentMargin*b)));
		return a >= b - (percentMargin*b) && a <= b + (percentMargin*b);	
	}

	public boolean checkRanges() {
		for (Variable v : ranges.keySet()) {
	//		System.out.println("-----------");
	//		System.out.println(ranges.get(v).getStart() + " " + ranges.get(v).getEnd() + " " + ranges.get(v).getValue());

			if (!ranges.get(v).isChecked()){
				return false;
			}
		}
	//	System.out.println("ranges checked");

		return true;
	}
	
	public HashMap<Variable, Range> getRanges() {
		return ranges;
	}

	public void setRanges(HashMap<Variable, Range> ranges) {
		this.ranges = ranges;
	}

	public Controller getControler() {
		return controller;
	}

	public void setControler(Controller controler) {
		this.controller = controler;
	}

	public HashMap<Criterion,Double> getPredictions() {
		return predictions;
	}

	public void setPredictions(HashMap<Criterion,Double> predictions) {
		this.predictions = predictions;
	}
	
	public double getPredictionFor(Criterion criterion) {
	//	System.out.println("size crit : " + predictions.size());
		if (criterion == null) System.out.println("null!!");
	//	System.out.println(criterion.getName());

		return predictions.get(criterion);
	}

	public double getAction() {
		return action;
	}

	public void setAction(double action) {
		this.action = action;
	}


	@Override
	public ArrayList<? extends Agent> getTargets() {
		ArrayList<Agent> arrayList = new ArrayList<Agent>();
		arrayList.add(controller);
		return arrayList;
	}


	public void init() {
		// TODO Auto-generated method stub
		
	}
	

	public String toString() {
		String s = "";
		s += "Context : " + getName() + "\n";
		for (Criterion c : predictions.keySet()) {
			s +=c.getName() + " will change by " + predictions.get(c).toString() + "\n";
		}
		for (Variable v : ranges.keySet()) {
			s +=v.getName() + " : " + ranges.get(v).toString() + "\n";
		}
		s += "Number of selections : " + nSelection + "\n";
		s += "Action proposed : " + action + "\n";

		return s;
	}

	public int getNSelection() {
		return nSelection;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	/**
	 * Remove all the references in other agents and remove the agent from the scheduler.
	 */
	public void die() {
		ArrayList<Variable> var = (ArrayList<Variable>) world.getAllAgentInstanceOf(Variable.class);
		for (Variable v : var) {
			sendExpressMessage(null,MessageType.UNREGISTER,v);
		}
		ArrayList<Criterion> tempCrit = (ArrayList<Criterion>) world.getAllAgentInstanceOf(Criterion.class);
		for (Criterion c : tempCrit) {
			sendExpressMessage(null,MessageType.UNREGISTER,c);
		}
		world.kill(this);
	}



}
