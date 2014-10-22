package kernel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import blackbox.BlackBox;
import blackbox.BlackBoxAgent;
import blackbox.Input;

import agents.SystemAgent;
import agents.Variable;
import agents.Agent;
import agents.context.Context;
import agents.controler.Controller;
import agents.criterion.Criterion;
import agents.criterion.CriticityType;

public class World {

	Scheduler scheduler;
	BlackBox blackbox;
	
	private HashMap<String,SystemAgent> agents = new HashMap<String,SystemAgent>();

	
	public World (Scheduler scheduler, File systemFile, BlackBox blackbox) {
		System.out.println("---Initialize the world---");
		this.scheduler = scheduler;
		this.blackbox = blackbox;
		createStartingAgents(systemFile);
		
		
		System.out.println("---End initialize the world---");
	}

	public void createStartingAgents(File systemFile) {
	      SAXBuilder sxb = new SAXBuilder();
	      Document document;
		try {
			document = sxb.build(systemFile);
		    Element racine = document.getRootElement();
		    System.out.println(racine.getName());
		    
		    // Initialize the sensor agents
		    for (Element element : racine.getChild("StartingAgents").getChildren("Sensor")){
		    	Variable s = new Variable(this);
		    	s.setName(element.getAttributeValue("Name"));
		    	scheduler.registerAgent(s);	   
		    	agents.put(s.getName(), s);
		    	s.setSensor(blackbox.getBlackBoxAgents().get(element.getAttributeValue("Source")));
		    }
		    
		    // Initialize the criterion agents
		    for (Element element : racine.getChild("StartingAgents").getChildren("Criterion")){
		    	Criterion a = new Criterion(this);
		    	a.setName(element.getAttributeValue("Name"));
		    	a.setCriticityType(CriticityType.valueOf(element.getAttributeValue("CriticityType")));
		    	a.setReference(Double.parseDouble(element.getAttributeValue("Reference")));

		    	scheduler.registerAgent(a);	   
		    	agents.put(a.getName(), a);
		    	((Variable) agents.get(element.getChild("CriticalVariable").getAttributeValue("Name"))).getTargets().add(a);
		    }
		    
		    //Initialize the controller agents
	    for (Element element : racine.getChild("StartingAgents").getChildren("Controller")){
		    	Controller a = new Controller(this);
		    	a.setName(element.getAttributeValue("Name"));
		    	a.setBlackBoxInput((Input) blackbox.getBlackBoxAgents().get(element.getAttributeValue("InputTarget")));

		    	scheduler.registerAgent(a);	   
		    	agents.put(a.getName(), a);
			    for (Element crit : racine.getChild("StartingAgents").getChildren("Criterion")){
			    	((Criterion) agents.get(crit.getAttributeValue("Name"))).getControllers().add(a);
			    }
		    }
		    
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	//----Get/Set----
	
	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void setBlackBox(BlackBox blackbox) {
		this.blackbox = blackbox;
	}

	public BlackBox getBlackbox() {
		return blackbox;
	}

	public HashMap<String, SystemAgent> getAgents() {
		return agents;
	}

	public void setAgents(HashMap<String, SystemAgent> agents) {
		this.agents = agents;
	}

	public void setBlackbox(BlackBox blackbox) {
		this.blackbox = blackbox;
	}
	
	public ArrayList<? extends Agent> getAllAgentInstanceOf(Class<? extends Agent> cl) {
		ArrayList<Agent> agentsList = new ArrayList<Agent>();
		for(String key : agents.keySet()) {
			Agent a = agents.get(key);
			if (a.getClass().equals(cl)) {
				agentsList.add(a);
			}
		}
		return agentsList;
	}

	public void startAgent(SystemAgent a) {
		scheduler.registerAgent(a);	   
    	agents.put(a.getName(), a);
	}


	
	
	
}
