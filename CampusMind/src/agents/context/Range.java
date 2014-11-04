package agents.context;

public class Range {

	private double start;
	private double end;
	private double value;
	private double oldValue;
	public static final double mininimalRange = 1.0;
	private final double percentFitness = 0.05;

	
	
	
	public Range(double start, double end) {
		super();
		this.start = start;
		this.end = end;
	}
	
	public boolean isInRange (double value) {
		return (value > start && value < end);
	}
	
	public boolean isChecked () {
		return (value > start && value < end);
	}
	
	public double getStart() {
		return start;
	}
	public void setStart(double start) {
		this.start = start;
	}
	public double getEnd() {
		return end;
	}
	public void setEnd(double end) {
		this.end = end;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.oldValue = this.value;
		this.value = value;
	}
	
	public void fit() {
		//TODO +% is good?
		if (Math.abs(oldValue-start) > Math.abs(oldValue-end)) {
			end = oldValue 
					/*+ percentFitness*(oldValue-end)*/;
		}
		else {
			start = oldValue
					/*+ percentFitness*(oldValue-start)*/;
		}
	}
	
	public String toString() {
		return "["+start+","+end+"]" + "("+value+")" + " " + isChecked();
	}
	
	public boolean isTooSmall() {
		return (end - start) < mininimalRange;
	}
	
	
	
}
