package kernel;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import org.jdom2.*;

import view.system.MainPanel;
import view.system.MainTabbedPanel;
import view.system.PanelCriterion;
import blackbox.BlackBox;

public class Launcher {

	static MainPanel mainPanel;

	public static void main(String[] args) {
		
		//Use the advanced graph viewer from GraphStream
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
	//	PanelCriterion pan = new PanelCriterion();
	//	JFrame f = new JFrame();
	//	world.getScheduler().addScheduledItem(pan);
	//	f.setContentPane(pan);
	//	f.setVisible(true);
		
        


		setSystemLookAndFeel();
		

//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
				


				JFrame frame = new JFrame("Proto");
				mainPanel = new MainPanel();

				frame.setContentPane(mainPanel);
		//		frame.getContentPane().add(new JLabel("plouf"),BorderLayout.CENTER);
				
				frame.setVisible(true);


//			}
//		});
		
		Scheduler scheduler = new Scheduler();

		BlackBox blackBox = new BlackBox(scheduler, new File(System
				.getProperty("user.dir")
				+ "/bin/ressources/Equation_2_inconnues.xml"));
		//		+ "/bin/ressources/Boltzman_simulator.xml"));
		//		+ "/bin/ressources/BlackBox.xml"));
		World world = new World(scheduler,
				new File(System.getProperty("user.dir")
						+ "/bin/ressources/Solver_equation_2_inconnues.xml"),
		//				+ "/bin/ressources/Boltzman.xml"),
		//				+ "/bin/ressources/System.xml"),
						blackBox);
		world.setBlackBox(blackBox);

		mainPanel.setWorld(world);
		mainPanel.setBlackBox(blackBox);
		frame.pack();
		
		scheduler.setView(mainPanel);
		
		scheduler.setWorld(world);
		scheduler.start(false);



		
	}
	
	
	private static void setSystemLookAndFeel() {

		try {
			// Set System L&F
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} 
		catch (UnsupportedLookAndFeelException e) {
			// handle exception
		}
		catch (ClassNotFoundException e) {
			// handle exception
		}
		catch (InstantiationException e) {
			// handle exception
		}
		catch (IllegalAccessException e) {
			// handle exception
		}

	}

}
