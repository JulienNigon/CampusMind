package view.system;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

import view.animation.JJPanel;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JDesktopPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;
import javax.swing.table.DefaultTableModel;

import agents.SystemAgent;
import agents.Variable;
import agents.context.Context;
import agents.controler.Controller;
import agents.criterion.Criterion;
import blackbox.BlackBox;
import blackbox.BlackBoxAgent;
import kernel.World;

public class SystemPanel extends JPanel{

	DefaultTableModel tableModel;
	GrapheSystemPanel graphSystemPanel;
	private World world;

	
	public SystemPanel(World world) {
		this.world = world;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JSplitPane splitPane_1 = new JSplitPane();
		add(splitPane_1);
		
		graphSystemPanel = new GrapheSystemPanel();
		splitPane_1.setLeftComponent(graphSystemPanel);
		
		Object[] col = {"TYPE","NAME","ID","1","2","3"};
		tableModel = new DefaultTableModel(col, 0);
		
		graphSystemPanel.setWorld(world);
		update();
		
		JTable table = new JTable(tableModel);
		splitPane_1.setRightComponent(new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
		table.setDefaultRenderer(Object.class, new JTableRenderer_Agents());

		
	}
	
	
	public void update(){
		tableModel.setRowCount(0);
		
		//Object[] entete = {"TYPE","NAME","ID","XXX"};
		//tableModel.addRow(entete);
		
		int nVariable = 0;
		int nCriterion = 0;
		int nController = 0;
		int nContext = 0;
		
		HashMap<String, SystemAgent> hashAgents = world.getAgents();
		Object[] data = new Object[6];
	//	System.out.println(hashAgents.keySet());
		for (String s : hashAgents.keySet()) {
			SystemAgent a = hashAgents.get(s);
			data[0] = a.getClass().getSimpleName();
			data[1] = a.getName();
			data[2] = a.getID();
			data[3] = a.getMessagesBin();
			if (a instanceof Controller) {
				data[4] = ((Controller) a).getBestContext();
				nController++;
				tableModel.insertRow(nCriterion+nVariable,data);

			}
			if (a instanceof Variable) {
				data[4] = ((Variable) a).getValue();
				nVariable++;
				tableModel.insertRow(nCriterion,data);
			}
			if (a instanceof Criterion) {
				data[4] = ((Criterion) a).getCriticity();
				data[5] = "Var : " + ((Criterion) a).getVariation();
				nCriterion++;
				tableModel.insertRow(0,data);
			} else if (a instanceof Context) {
				nContext++;
				tableModel.addRow(data);				
			}

		}
		
		graphSystemPanel.update();
		
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
