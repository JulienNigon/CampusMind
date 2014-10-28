package view.system;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import view.blackbox.BlackBoxPanel;
import blackbox.BlackBox;
import kernel.World;

public class MainTabbedPanel extends JTabbedPane{

	private World world;
	private BlackBoxPanel blackBoxPanel;
	private SystemPanel systemPanel;
	

	public MainTabbedPanel() {
		super();

	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
		

		
		
		blackBoxPanel = new BlackBoxPanel(world);
		systemPanel = new SystemPanel(world);
	//	new JScrollPane(panelGroupManager,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.CENTER
		this.addTab("BlackBox", new JScrollPane(blackBoxPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
		this.addTab("System", systemPanel);
		this.addTab("Context", new JPanel());

		((Frame) this.getTopLevelAncestor()).pack();
	}
	
	public void update() {
		blackBoxPanel.update();
		systemPanel.update();
		world.getScheduler().setWaitForGUIUpdate(false);
	}
	
	public void setBlackBox(BlackBox blackBox) {
		blackBoxPanel.setBlackBox(blackBox);
	}
	
}
