package view.system.paving;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import view.animation.JJComponent;
import view.animation.JJPanel;


public class MonoDimensionLine extends JJComponent{

	static final int width = 4;
	
	private double start, end;
	private double scale;

	
	public MonoDimensionLine(JJPanel parent, double xx, double yy, double start, double end, double scale) {
		super(parent, xx, yy, (end - start)*scale, (double)width);
		this.start = start;
		this.end = end;
		this.scale = scale;
	}

	
	@Override
	public void paintComponent(Graphics g) 
	{   
//		System.out.println("Paint mono dimension line");

		Graphics2D g2d = genererContexte(g);
		g2d.setColor(Color.BLACK);
		setW(((end - start)*scale));
		g2d.fillRect(0, 0, (int)((end - start)*scale), width);
		if (start < 0) {
			g2d.setColor(Color.RED);
			g2d.fillRect((int) -start, 0, (int)2, width);
		}

    	//this.setXx(-1*start);

//		g2d.setColor(new Color(209,182,6));
//		g2d.fillOval(border/2, border/2, radius-border, radius-border);

	}


	public void update(double min, double max) {
		start = min;
		end = max;
	}



	
	
	
}
