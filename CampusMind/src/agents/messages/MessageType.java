package agents.messages;

public enum MessageType {

	VALUE, 
	AGENT, /*The message is an agent*/
	PROPOSAL, 
	REGISTER, /*Allow the agent to be memorized by another agent*/
	BAD_PROPOSITION,
	SELECTION,
	ABORT /*When a controller reject a Context*/;
	
}
