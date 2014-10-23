package view.system;

import java.awt.BorderLayout;
import java.awt.Button;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import kernel.Config;
import kernel.World;
import javafx.application.*;

public class MainPanel extends JPanel{
	
	
	/* ----ToolBar Components----*/
	private JToolBar toolBar;
	private JButton buttonPauseStart;
	private JButton buttonPlayOneStep;
	
	private MainTabbedPanel tabbedPanel;
	private World world;

	
	public MainPanel() {
		
		this.setLayout(new BorderLayout());
		toolBar = new JToolBar();
		
		buttonPauseStart = new JButton(Config.getIcon("arrow.png"));
		buttonPauseStart.addActionListener(e -> {togglePause(!world.getScheduler().isRunning());});
		toolBar.add(buttonPauseStart);

		buttonPlayOneStep = new JButton(Config.getIcon("arrow-step-over.png"));
		buttonPlayOneStep.addActionListener(e -> {oneStep();});
		toolBar.add(buttonPlayOneStep);

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
			buttonPauseStart.setIcon(Config.getIcon("arrow.png"));
		} else {
			buttonPauseStart.setIcon(Config.getIcon("control-pause.png"));
		}
	}
	
	public void setWorld(World world) {
		this.world = world;
		tabbedPanel.setWorld(world);
	}
	
	public void update() {
		tabbedPanel.update();
	}
}
