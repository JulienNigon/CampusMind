package view.system;

import java.awt.BorderLayout;
import java.awt.Button;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import kernel.Config;
import kernel.World;

public class MainPanel extends JPanel{
	
	private JToolBar toolBar;
	private JButton buttonPauseStart;
	
	private MainTabbedPanel tabbedPanel;

	
	public MainPanel() {
		
		this.setLayout(new BorderLayout());
		toolBar = new JToolBar();
		buttonPauseStart = new JButton(Config.getIcon("arrow.png"));
		toolBar.add(buttonPauseStart);
		this.add(toolBar,BorderLayout.NORTH);  // TODO ugly tool bar
		this.tabbedPanel = new MainTabbedPanel();
		this.add(tabbedPanel,BorderLayout.CENTER);
		
		
	}
	
	public void setWorld(World world) {
		tabbedPanel.setWorld(world);
	}
	
	public void update() {
		tabbedPanel.update();
	}
}
