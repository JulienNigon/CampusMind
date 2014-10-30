package blackbox;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import agents.Agent;
import agents.Variable;

import kernel.Scheduler;

public class BlackBox {
	
	private Scheduler scheduler;
	private HashMap<String,BlackBoxAgent> blackBoxAgents = new HashMap<String,BlackBoxAgent>();
	
	public BlackBox(Scheduler scheduler, File systemFile) {
		System.out.println("---Initialize the blackbox---");
		this.scheduler = scheduler;
		buildBlackBoxFromFile(systemFile);
		System.out.println("---End initialize the blackbox---");
	}

	private void buildBlackBoxFromFile(File systemFile) {
	      SAXBuilder sxb = new SAXBuilder();
	      Document document;
		try {
			document = sxb.build(systemFile);
		    Element racine = document.getRootElement();
		    System.out.println(racine.getName());
		    
		    // Initialize the Input agents
		    for (Element element : racine.getChild("Inputs").getChildren("Input")){
		    	Input a = new Input();
		    	a.setName(element.getAttributeValue("Name"));
		    	a.setValue(Double.parseDouble(element.getAttributeValue("DefaultValue")));
		    	System.out.println("v"+a.getValue());
		    	registerBlackBoxAgent(a);
		    }
		    
		    
		    // Initialize the Functions agents
		    for (Element element : racine.getChild("Functions").getChildren("Function")){
		    	Function a = new Function();
		    	a.setName(element.getAttributeValue("Name"));
		    	a.setFunc(MathFunction.valueOf(element.getAttributeValue("Func")));
		    	registerBlackBoxAgent(a);
		    }
		    
		    // Initialize the Output agents
		    for (Element element : racine.getChild("Outputs").getChildren("Output")){
		    	Output a = new Output();
		    	a.setName(element.getAttributeValue("Name"));
		    	a.setValue(Double.parseDouble(element.getAttributeValue("DefaultValue")));
		    	registerBlackBoxAgent(a);
		    }

		    
			createLinks(racine);
		    
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}

	}
	
	private void createLinks(Element root) {

			
		    
		    // Initialize the Input agents
		    for (Element element : root.getChild("Inputs").getChildren("Input")){
			    for (Element target : element.getChildren("Target")){
			    	blackBoxAgents.get(element.getAttribute("Name").getValue()).getTargets().add(blackBoxAgents.get(target.getAttribute("Name").getValue()));
			    }
		    }
		    
		    // Initialize the Output agents
		    for (Element element : root.getChild("Outputs").getChildren("Output")){
			    for (Element target : element.getChildren("Target")){
			    	blackBoxAgents.get(element.getAttribute("Name").getValue()).getTargets().add(blackBoxAgents.get(target.getAttribute("Name").getValue()));
			    }
		    }
		    
		    // Initialize the Function agents
		    for (Element element : root.getChild("Functions").getChildren("Function")){
			    for (Element target : element.getChildren("Target")){
			    	blackBoxAgents.get(element.getAttribute("Name").getValue()).getTargets().add(blackBoxAgents.get(target.getAttribute("Name").getValue()));
			    }
			    ((Function) (blackBoxAgents.get(element.getAttribute("Name").getValue()))).setAgentA(blackBoxAgents.get(element.getChild("InputA").getAttributeValue("Name")));
			    ((Function) (blackBoxAgents.get(element.getAttribute("Name").getValue()))).setAgentB(blackBoxAgents.get(element.getChild("InputB").getAttributeValue("Name")));
		    }
		    
		    
	}
	
	
	private Agent getAgentByName(String name) {
		return blackBoxAgents.get(name);
	}
	
	/**
	 * Register a new agent in the black box and in the scheduler
	 */
	private void registerBlackBoxAgent(BlackBoxAgent a) {
    	scheduler.registerAgent(a);	    	
    	blackBoxAgents.put(a.getName(),a);	    	
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public HashMap<String, BlackBoxAgent> getBlackBoxAgents() {
		return blackBoxAgents;
	}

	public void setBlackBoxAgents(HashMap<String, BlackBoxAgent> blackBoxAgents) {
		this.blackBoxAgents = blackBoxAgents;
	}
	
	
	
}
