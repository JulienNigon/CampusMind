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

import kernel.World;

public class MainPanel extends JTabbedPane{

	private World world;
	private BlackBoxPanel blackBoxPanel;
	private SystemPanel systemPanel;
	
	private JToolBar toolBar;
	private JButton buttonPauseStart;

	public MainPanel() {
		super();

	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
		
		toolBar = new JToolBar();
		buttonPauseStart = new JButton("test");
		toolBar.add(buttonPauseStart);
		this.add(toolBar);
		
		
		blackBoxPanel = new BlackBoxPanel(world);
		systemPanel = new SystemPanel(world);
	//	new JScrollPane(panelGroupManager,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.CENTER
		this.addTab("BlackBox", new JScrollPane(blackBoxPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
		this.addTab("System", new JScrollPane(systemPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
		this.addTab("Context", new JPanel());

		((Frame) this.getTopLevelAncestor()).pack();
	}
	
	public void update() {
		blackBoxPanel.update();
		systemPanel.update();
		world.getScheduler().setWaitForGUIUpdate(false);
	}
	
	
}
