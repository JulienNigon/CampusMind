package agents.criterion;

public enum CriticityType {

	LOWER, HIGHER, FOCUS, MAX, MIN;
	
	public double computeCriticity(double value, double ref) {
		
		switch (this) {

		case LOWER:
			//System.out.println("Seuil : " + (Math.log10(Math.max(0,value-ref)) - 1));
			//System.out.println("ref : " + ref);
			return between0_100(Math.log10(Math.max(0,value-ref+1)));
			
		case HIGHER:
			//System.out.println("Seuil : " + (Math.log10(Math.max(0,value-ref)) - 1));
			//System.out.println("ref : " + ref);
			return between0_100(Math.log10(Math.max(0,-1*(value-ref-1))));
			
		case FOCUS:
			return between0_100(Math.log10(Math.max(0,Math.abs((value-ref+1)))));
			
		default:
			return 0;

		}
	}
	
	private double between0_100(double d) {
		if (d < 0) return 0;
		if (d > 100) return 100;
		return d;
	}
}
