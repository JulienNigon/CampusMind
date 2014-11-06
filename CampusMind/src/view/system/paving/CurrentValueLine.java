package view.system.paving;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import agents.Percept;
import view.animation.JJComponent;
import view.animation.JJPanel;


public class CurrentValueLine extends JJComponent{

	static final int width = 1;
	
	private double start, end;
	private double scale;
	private Panel1DPaving panel;
	private Percept variable;
	
	public CurrentValueLine(JJPanel parent, double xx, double yy, Panel1DPaving panel, double scale, Percept variable) {
		super(parent, xx, yy, (double)width, 200/*panel.heightPavingContext * panel.getnContext()*/);
		this.scale = scale;
		this.panel = panel;
		this.variable = variable;
		

	}

	
	@Override
	public void paintComponent(Graphics g) 
	{   
		
		Graphics2D g2d = genererContexte(g);
		g2d.setColor(Color.BLACK);
		
		this.setBounds((int) (variable.getValue() - panel.getMin()), 0, width, Panel1DPaving.heightPavingContext * panel.getnContext());
		this.setXx((variable.getValue() - panel.getMin()));
		this.setYy(0);
		this.setW(width);
		
		this.setH(Panel1DPaving.heightPavingContext * panel.getnContext());
		g2d.fillRect(0, 0, (int)width, Panel1DPaving.heightPavingContext * panel.getnContext());


    	//this.setXx(-1*start);

//		g2d.setColor(new Color(209,182,6));
//		g2d.fillOval(border/2, border/2, radius-border, radius-border);

	}


	public void update(double min, double max) {
		start = min;
		end = max;
	}



	
	
	
}
