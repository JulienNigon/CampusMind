package view.blackbox;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import kernel.Config;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;

import agents.Agent;
import agents.Percept;
import agents.criterion.Criterion;
import blackbox.BlackBox;
import blackbox.BlackBoxAgent;
import blackbox.Function;
import blackbox.Input;

public class GrapheBlackBoxPanel extends JPanel implements MouseInputListener, ViewerListener{
	
	Graph graph;
	Viewer viewer;
	BlackBox blackBox;
	
	/* ----ToolBar Components----*/
	private JToolBar toolBar;
	private JButton buttonShowValue;
	private JButton buttonShowDefault;
	private JButton buttonShowName;
	
	private int viewMode = 0;
	
	/*Interaction with simulator*/
	private ViewerPipe pipe;
	private MouseEvent mouseEvent;
	Boolean rightClick = false;

	public GrapheBlackBoxPanel() {
		setLayout(new BorderLayout());

		this.setMinimumSize(new Dimension(400,400));

		toolBar = new JToolBar(null, JToolBar.VERTICAL);

		buttonShowDefault = new JButton(Config.getIcon("tag--plus.png"));
		buttonShowDefault.addActionListener(e -> {showDefault();});
		toolBar.add(buttonShowDefault);

		buttonShowValue = new JButton(Config.getIcon("tag--exclamation.png"));
		buttonShowValue.addActionListener(e -> {showValue();});
		toolBar.add(buttonShowValue);

		buttonShowName = new JButton(Config.getIcon("tag.png"));
		buttonShowName.addActionListener(e -> {showName();});
		toolBar.add(buttonShowName);
		
		this.add(toolBar,BorderLayout.WEST);

		
		//update();
		
	}
	
	public void showValue() {
		viewMode = 1;
		if (blackBox != null) {
			for (String name : blackBox.getBlackBoxAgents().keySet()) {
				BlackBoxAgent bba = blackBox.getBlackBoxAgents().get(name);	
				graph.getNode(bba.getName()).setAttribute("ui.label", bba.getValue());		
			}
		}
	}
	
	public void showDefault() {
		viewMode = 0;
		if (blackBox != null) {
			for (String name : blackBox.getBlackBoxAgents().keySet()) {
				BlackBoxAgent bba = blackBox.getBlackBoxAgents().get(name);	
				graph.getNode(bba.getName()).setAttribute("ui.label", bba.getName() + " " + bba.getValue());		
			}
		}
	}
	
	public void showName() {
		viewMode = 2;
		if (blackBox != null) {
			for (String name : blackBox.getBlackBoxAgents().keySet()) {
				BlackBoxAgent bba = blackBox.getBlackBoxAgents().get(name);	
				graph.getNode(bba.getName()).setAttribute("ui.label", bba.getName());		
			}
		}
	}
	
	public void setBlackBox(BlackBox blackBox) {
		this.blackBox = blackBox;
		createGraph();
	}
	
	public void update () {
		switch(viewMode) {
		
		case 0 : 
			showDefault();
			break;
			
		case 1 : 
			showValue();
			break;
			
		case 2 :
			showName();
			break;
		}
	}
	
	
	private void createGraph() {
		graph = new SingleGraph("BLACK BOX");
		for (String name : blackBox.getBlackBoxAgents().keySet()) {
			BlackBoxAgent bba = blackBox.getBlackBoxAgents().get(name);
			graph.addAttribute("ui.stylesheet", "url('file:/home/nigon/git/CampusMind/CampusMind/src/styles/styleBlackBox.css')");
			
		/*	graph.addAttribute("ui.stylesheet", "node { stroke-mode: plain;"
					+ "fill-color: red;"
					+ "shape: box;"
					+ "stroke-color: yellow;"
					+ " }");//text-mode
			graph.addAttribute("ui.style", "shape: box;");*/

			//graph.addAttribute("ui.stylesheet", "node { text-mode: normal; }");//text-mode

			//graph.
			graph.addNode(bba.getName());
			graph.getNode(bba.getName()).addAttribute("ui.class", bba.getClass().getSimpleName());
			graph.getNode(bba.getName()).addAttribute("ui.label", bba.getName());
			//graph.getNode(bba.getName()).;
			//System.out.println(graph.getNode(bba.getName()).getLabe)());
			
		}

		//Draw edge
		for (String name : blackBox.getBlackBoxAgents().keySet()) {
			BlackBoxAgent bba = blackBox.getBlackBoxAgents().get(name);

			for (Agent target : bba.getTargets()) {
				graph.addEdge(bba.getName() + " " + target.getName(), bba.getName(), target.getName(), true);				
			}
			
		}

		
/*		graph.addNode("XXX");
		graph.addNode("YYY");
		graph.addNode("ZZZ");
		graph.addNode("A" );
		graph.addNode("B" );
		graph.addNode("C" );
		graph.addEdge("AB", "A", "B");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("CA", "C", "A");
		graph.addEdge("XY", "XXX", "YYY");**/

		viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_SWING_THREAD);
		viewer.addDefaultView(false);
		viewer.enableAutoLayout();
		viewer.getDefaultView().addMouseListener(this);
		

		pipe = viewer.newViewerPipe();
        pipe.addViewerListener(this);
        pipe.addSink(graph);

		viewer.getDefaultView().setMinimumSize(new Dimension(400,400));
		this.add(viewer.getDefaultView(),BorderLayout.CENTER);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		mouseEvent = e;
		if(SwingUtilities.isRightMouseButton(e)){
			rightClick = true;
			Robot bot;
			try {
				bot = new Robot();
				int mask = InputEvent.BUTTON1_DOWN_MASK;
				bot.mousePress(mask);  
				bot.mouseRelease(mask);  
			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
   
		}
		pipe.pump();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buttonPushed(String id) {
		System.out.println("node pushed : " + id);
		
		
		if (rightClick) {
			if (blackBox.getBlackBoxAgents().get(id) instanceof Input) {
				popupInput(id);
			}
			
			rightClick = false;
		}		
	}

	@Override
	public void buttonReleased(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void viewClosed(String arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void popupInput(String id){
		
		JPopupMenu popup = new JPopupMenu("Input");
		
		JMenuItem itemX2 = new JMenuItem("x2");
		itemX2.addActionListener(e -> {factorInput(2,id);});
		itemX2.setIcon(Config.getIcon("pencil.png"));
		popup.add(itemX2);
		
		JMenuItem itemDiv2 = new JMenuItem("/2");
		itemDiv2.addActionListener(e -> {factorInput(0.5,id);});
		itemDiv2.setIcon(Config.getIcon("pencil.png"));
		popup.add(itemDiv2);
		
		popup.show(this, this.getX() + mouseEvent.getX(), this.getY() + mouseEvent.getY());
	}
	
	private void factorInput(double factor, String id) {
		((Input)blackBox.getBlackBoxAgents().get(id)).multValue(factor);
	}
}
