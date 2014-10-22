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
	private Controller controller;
	private double action;
	
	public Context(World world, Controller controller) {
		super(world);
		this.controller = controller;
		ArrayList<Variable> var = (ArrayList<Variable>) world.getAllAgentInstanceOf(Variable.class);
		for (Variable v : var) {
			ranges.put(v, new Range(v.getValue()-10,v.getValue()+10)); //TODO start range
			sendMessage(null,MessageType.REGISTER,v);
//			System.out.println("REQUEST SEND");
		}
		ArrayList<Criterion> tempCrit = (ArrayList<Criterion>) world.getAllAgentInstanceOf(Criterion.class);
		for (Criterion c : tempCrit) {
			predictions.put(c, 1.00); //TODO start range
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
		//		System.out.println(m.getContent());
				ranges.get(m.getSender()).setValue((Double)m.getContent());
			}
			
			//TODO add variable
		}
	}
	
	public void play() {
		//System.out.println("play");
		super.play();
		if (checkRanges()) {
			sendMessage(action, MessageType.PROPOSAL, controller);
		}
	}

	private boolean checkRanges() {
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
	
	



}
