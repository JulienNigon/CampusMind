package view.system;

import java.awt.BorderLayout;
import java.awt.Button;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import blackbox.BlackBox;
import kernel.Config;
import kernel.World;
import javafx.application.*;

public class MainPanel extends JPanel{
	
	
	/* ----ToolBar Components----*/
	private JToolBar toolBar;
	private JButton buttonPauseStart;
	private JButton buttonPlayOneStep;
	private JButton buttonExit;
	
	private MainTabbedPanel tabbedPanel;
	private World world;

	
	public MainPanel() {
		
		this.setLayout(new BorderLayout());
		toolBar = new JToolBar();
		
		buttonExit = new JButton(Config.getIcon("cross-circle.png"));
		buttonExit.addActionListener(e -> {System.exit(0);});
		toolBar.add(buttonExit);
		
		toolBar.addSeparator();
		
		buttonPauseStart = new JButton(Config.getIcon("control.png"));
		buttonPauseStart.addActionListener(e -> {togglePause(!world.getScheduler().isRunning());});
		toolBar.add(buttonPauseStart);

		buttonPlayOneStep = new JButton(Config.getIcon("control-stop.png"));
		buttonPlayOneStep.addActionListener(e -> {oneStep();});
		toolBar.add(buttonPlayOneStep);
		
		toolBar.addSeparator();

		this.add(toolBar,BorderLayout.NORTH);
		this.tabbedPanel = new MainTabbedPanel();
		this.add(tabbedPanel,BorderLayout.CENTER);
		
		
	}
	
	
	/**
	 * Play one step of simulation.
	 * Toggle pause off if running after one step.
	 */
	public void oneStep() {
		togglePause(false);
		world.getScheduler().playOneStep();
	}
	
	/**
	 * Pause/Unpause the simulation and adapt the button accordingly.
	 */
	public void togglePause(boolean newState) {
		world.getScheduler().setRunning(newState);
		boolean running = world.getScheduler().isRunning();
		if (!running) {
			buttonPauseStart.setIcon(Config.getIcon("control.png"));
		} else {
			buttonPauseStart.setIcon(Config.getIcon("control-pause.png"));
		}
	}
	
	public void setWorld(World world) {
		this.world = world;
		tabbedPanel.setWorld(world);
	}
	
	public void setBlackBox(BlackBox blackBox) {
		tabbedPanel.setBlackBox(blackBox);
	}
	
	public void update() {
		tabbedPanel.update();
	}
}
