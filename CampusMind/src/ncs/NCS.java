package ncs;

import kernel.World;

public enum NCS {

	CONTROLLER_INEPTITUDE, CONTROLLER_IMPRODUCTIVE, CONTROLLER_CONFLICT,
	CONTEXT_CONFLICT_FALSE, CONTEXT_CONFLICT_INEXACT, CONTEXT_INEPTITUDE, CONTEXT_USELESSNESS, CONTEXT_IMPRODUCTIVE_RANGE, CONTEXT_IMPRODUCTIVE_ACTION;
	
	
	public void raiseNCS(World world) {
//		System.out.println(this.toString());
		world.changeNCSNumber(1, this);
	}
	
}
