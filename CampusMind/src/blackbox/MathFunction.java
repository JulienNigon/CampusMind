package blackbox;

public enum MathFunction {

	PLUS, MULT, MINUS, DIVIDE;
	
	public double compute(double a, double b) {
		
		switch(this) {
		
		case PLUS :
			return a + b;
			
		case MINUS :
			return a - b;
			
		case DIVIDE :
			return a / b;
			
		case MULT :
			return a * b;
		
		default :
			return 0.0;
		}
	}
}
