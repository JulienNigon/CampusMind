package agents.context;

import java.util.ArrayList;
import java.util.HashMap;

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
	private HashMap<Criterion,Double> predictions = new HashMap<Criterion,Double>();
	private HashMap<Criterion,Double> criticity = new HashMap<Criterion,Double>();
	private Controller controller;
	private double action = -1.1;
	private int nSelection = 0;
	private Criterion criticalCriterion;
	private double lastCriticity;
	private boolean selected = false;
	
	public Context(World world, Controller controller) {
		super(world);
		this.controller = controller;
		ArrayList<Variable> var = (ArrayList<Variable>) world.getAllAgentInstanceOf(Variable.class);
		for (Variable v : var) {
			ranges.put(v, new Range(v.getValue()-1000,v.getValue()+1000)); //TODO start range
			ranges.get(v).setValue(v.getValue());
			sendMessage(null,MessageType.REGISTER,v);
//			System.out.println("REQUEST SEND");
		}
		ArrayList<Criterion> tempCrit = (ArrayList<Criterion>) world.getAllAgentInstanceOf(Criterion.class);
		for (Criterion c : tempCrit) {
			predictions.put(c, 1.00); //TODO start range
			sendMessage(null,MessageType.REGISTER,c);
	//		System.out.println("size crit : " + predictions.size());
	//		sendMessage(null,MessageType.REGISTER,v);
//			System.out.println("REQUEST SEND");
		}
	}
	
	
	@Override
	public void computeAMessage(Message m) {
		if (m.getType() == MessageType.VALUE) {
		//	System.out.println(m.getContent());
			if(m.getSender() instanceof Variable) {
				ranges.get(m.getSender()).setValue((Double)m.getContent());
			}
			else if(m.getSender() instanceof Criterion) {
				criticity.put((Criterion) m.getSender(), (Double)m.getContent());
			}
			
			//TODO add variable
		} else if (m.getType() == MessageType.SELECTION) {
			nSelection++;
			selected = true;
			//TODO dirty
			criticalCriterion = (Criterion) ((Object[])(m.getContent()))[0];
			lastCriticity = (double) ((Object[])(m.getContent()))[1];
		}
	}
	
	private void solveNCS_improductivity() {
		// TODO raise NCS
		// TODO AVT
		action += (0.1*action);
	}


	public void play() {
		//System.out.println("play");
		super.play();
		
		if (nSelection > 0 && !selected) {
			if (getRealChange() <= predictions.get(criticalCriterion) + 0.01   //TODO parametrize
					&& getRealChange() >= predictions.get(criticalCriterion) - 0.01) {
				
			}
		}
		
		if (nSelection >= 2) {
			solveNCS_improductivity();
		}
		
		if (checkRanges()) {
			sendMessage(action, MessageType.PROPOSAL, controller);
		}
		criticalCriterion = null;
		lastCriticity = 0.00;
		selected = false;
	}
	
	public double getRealChange() {
		return criticity.get(criticalCriterion) - lastCriticity;
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
	
	



}
