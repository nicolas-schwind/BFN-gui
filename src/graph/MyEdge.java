package graph;

import parameters.Global;

public class MyEdge {
	private static int edgeCount = 0;
	private int id;
	
	// an edge can be triggerred during a run, this will modify the way it is displayed
	private boolean isCurrentlyTriggerred;

	public MyEdge() {
		id = edgeCount++;
		this.isCurrentlyTriggerred = false;
	}
	
	public void resetEdgeIds () {
		edgeCount = 0;
	}
	
	public boolean isCurrentlyTriggerred () {
		return this.isCurrentlyTriggerred;
	}
	
	public void setIsCurrentlyTriggerred (boolean triggerred) {
		this.isCurrentlyTriggerred = triggerred;
	}
	
	public String toString() {
		return "E" + id + " (" + Global.currentNetwork.getSource(this) + " -> " + Global.currentNetwork.getDest(this) + ")";
	}
}
