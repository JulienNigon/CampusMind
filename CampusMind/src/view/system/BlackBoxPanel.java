package view.system;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

import view.animation.JJPanel;
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.JDesktopPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;
import javax.swing.table.DefaultTableModel;

import blackbox.BlackBox;
import blackbox.BlackBoxAgent;

import kernel.World;

public class BlackBoxPanel extends JPanel{

	DefaultTableModel tableModel;
	private World world;

	
	public BlackBoxPanel(World world) {
		this.world = world;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JSplitPane splitPane_1 = new JSplitPane();
		add(splitPane_1);
		
		JTextPane textPane = new JTextPane();
		splitPane_1.setLeftComponent(textPane);
		
		String col[] = {"Type","Name","ID","Value"};
		tableModel = new DefaultTableModel(col, 0);
		
		update();
		
		JTable table = new JTable(tableModel);
		splitPane_1.setRightComponent(table);
		
	}
	
	
	public void update(){
		tableModel.setRowCount(0);
		
		Object[] entete = {"TYPE","NAME","ID","VALUE"};
		tableModel.addRow(entete);
		
		BlackBox bb = world.getBlackbox();
		HashMap<String, BlackBoxAgent> hashAgents = bb.getBlackBoxAgents();
		for (String s : hashAgents.keySet()) {
			BlackBoxAgent a = hashAgents.get(s);
			Object[] data = {
					a.getClass().getSimpleName(),
					a.getName(),
					a.getID(),
					a.getValue()
					};
			tableModel.addRow(data);
		}
		
		
	}


	public DefaultTableModel getTableModel() {
		return tableModel;
	}


	public void setTableModel(DefaultTableModel tableModel) {
		this.tableModel = tableModel;
	}


	public World getWorld() {
		return world;
	}


	public void setWorld(World world) {
		this.world = world;
	}
	
	
}
