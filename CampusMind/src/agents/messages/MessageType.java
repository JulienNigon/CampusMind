package agents.messages;

public enum MessageType {

	VALUE, 
	AGENT, /*The message is an agent*/
	PROPOSAL, 
	REGISTER, /*Allow the agent to be memorized by another agent*/
	BAD_PROPOSITION,
	SELECTION,
	KILL,
	UNREGISTER, /*The agent want to be forget. Before being destroyed for exemple...*/
	ABORT /*When a controller reject a Context*/;
	
}
