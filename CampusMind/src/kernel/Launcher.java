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

import org.jdom2.*;

import view.system.MainPanel;
import view.system.MainTabbedPanel;

import blackbox.BlackBox;

public class Launcher {

	static MainPanel mainPanel;

	public static void main(String[] args) {
		

		setSystemLookAndFeel();
		

//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
				


				JFrame frame = new JFrame("Proto");
				mainPanel = new MainPanel();

				frame.setContentPane(mainPanel);
		//		frame.getContentPane().add(new JLabel("plouf"),BorderLayout.CENTER);
				frame.pack();
				frame.setVisible(true);


//			}
//		});
		
		Scheduler scheduler = new Scheduler();

		BlackBox blackbox = new BlackBox(scheduler, new File(System
				.getProperty("user.dir")
				+ "/bin/ressources/BlackBox.xml"));
		World world = new World(scheduler,
				new File(System.getProperty("user.dir")
						+ "/bin/ressources/System.xml"),
						blackbox);
		world.setBlackBox(blackbox);

		mainPanel.setWorld(world);
		scheduler.setView(mainPanel);
		
		scheduler.start();



		
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
