package kernel;

import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;


public class Config {
	
	static public HashMap<String,ImageIcon> icons = new HashMap<String,ImageIcon>();

	
	public static ImageIcon getIcon (String name) {
		if (!icons.containsKey(name)) {
			try {	
				icons.put(name, new ImageIcon(ImageIO.read(Config.class.getResourceAsStream("/icons/" + name))));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	

	return icons.get(name);
	
	}
}
