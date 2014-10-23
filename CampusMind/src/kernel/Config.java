package kernel;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class Config {

	
	public static ImageIcon getIcon (String name) {
		try {
			return new ImageIcon(ImageIO.read(Config.class.getResourceAsStream("/icons/" + name)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}	
}
