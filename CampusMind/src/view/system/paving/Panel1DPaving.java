package view.system.paving;

import java.util.ArrayList;

import view.animation.JJPanel;
import view.system.ScheduledItem;
import kernel.World;
import agents.Variable;
import agents.context.Context;

public class Panel1DPaving extends JJPanel implements ScheduledItem{

	private Variable variable;
	private World world;
	private ArrayList<Context> referencedContexts = new ArrayList<Context>();
	private ArrayList<PavingContext> pavingContexts = new ArrayList<PavingContext>();
	private MonoDimensionLine monoDimensionLine;
	
	private double length;
	private double min;
	private double max;
	
	private int nContext = 0;
	
	public Panel1DPaving(Variable variable, World world) {
		this.world = world;
		this.variable = variable;
		
		drawPaving();

	}

	
	private void drawPaving() {
		
		
		ArrayList<Context> contexts = (ArrayList<Context>) world.getAllAgentInstanceOf(Context.class);
		double temp;
		
		System.out.println("Size : " + contexts.size());
		
		if (contexts.size() > 0) {
			
			/*Init min et max*/
			min = contexts.get(0).getRanges().get(variable).getStart();
			max = contexts.get(0).getRanges().get(variable).getEnd();
			for (Context context : contexts) {
				temp = context.getRanges().get(variable).getStart();
				if (temp < min) {
					min = temp;
				}
				temp = context.getRanges().get(variable).getEnd();
				if (temp > max) {
					max = temp;
				}
			}
		
			System.out.println(min + " " + max);
			if (monoDimensionLine == null) {
				this.add(new MonoDimensionLine(this, 0, 0, min, max,1.0));
			}
			else {
				monoDimensionLine.update(min,max);
			}

			/*Draw the context*/
			for (Context context : contexts) {
				if (!referencedContexts.contains(context)) {
					PavingContext pavingContext = new PavingContext(this, 0, 0, context, 1.0, this, nContext);  //TODO : no perf!
					this.add(pavingContext);
					this.referencedContexts.add(context);
					this.pavingContexts.add(pavingContext);
					nContext++;
				}
				//TODO remove context!
			}
			
			
		}
		
		
		
	//	this.add(new MonoDimensionLine(this, 0, 0, 600));
	//	this.add(new PavingContext(this, 0, 0));
	}
	
	@Override
	public void update() {

		drawPaving();
	}


	public double getLength() {
		return length;
	}


	public void setLength(double length) {
		this.length = length;
	}


	public double getMin() {
		return min;
	}


	public void setMin(double min) {
		this.min = min;
	}


	public double getMax() {
		return max;
	}


	public void setMax(double max) {
		this.max = max;
	}


	public Variable getVariable() {
		return variable;
	}


	public void setVariable(Variable variable) {
		this.variable = variable;
	}
	
	

}
