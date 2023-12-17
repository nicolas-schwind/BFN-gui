package gameRun;

import belief.OCF;
import graph.UIAgent;
import graph.MyEdge;
import parameters.Global;

// simulates a run without changing the graph and its beliefs
// This 'cheated run' means that one checks the epistemic state (preorder/OCF) to know if there is a change or not
// This is used to check stability conditions, for instance.
// The fact that is is 'cheated' is because one typically do not know the epistemic state of an agent but one only can observe its beliefs
public class CheatedRun extends AbstractRun {
	
	public CheatedRun () {
		super();
	}
	
	public String runModeName () {
		return "cheated";
	}
	
	// returns true if simulating the trigger of every edge results in no change of epistemic state;
	// in this case, there will never be any further change;
	// this should not be used in the simulation of a 'true' run
	public boolean stabilityReached () {
		UIAgent influencer; // the influencer
		UIAgent agentToChange; // the one to change the OCF
		// search for the ocf of the influencer
		OCF influencerOCF;
		RunnableAgent agentToChangeOCFHistory;
		OCF agentToChangeOCF;
		OCF newOCF;
		// we test every edge
		for (MyEdge edge : Global.currentNetwork.getEdges()) {
			// simulate a triggered revision that edge
			influencer = Global.currentNetwork.getSource(edge);
			agentToChange = Global.currentNetwork.getDest(edge);
			// search for the ocf of the influencer
			influencerOCF = getAgent(influencer).getLastOCF();
			agentToChangeOCFHistory = getAgent(agentToChange);
			agentToChangeOCF = agentToChangeOCFHistory.getLastOCF();
			// compute the change
			newOCF = agentToChange.getRevisionPolicy().revise(agentToChangeOCF, influencerOCF.getBeliefs());
			
			// check if that revision resulted in a change
			if (agentToChange.getRevisionPolicy().tpoEpistemicSpace()) {
				// no change if same tpo
				if (!newOCF.hasSamePreorderAs(agentToChangeOCF)) {
					return false;
				}
			}
			else {
				// no change if same ocf
				if (!newOCF.isSameAs(agentToChangeOCF)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	// Here, the trigger is only effectively counted when there is an actual change of epistemic state;
	// this should not be used in the case of a 'true' run
	public void triggerSingleChangeOperation (MyEdge triggeredEdge) {
		UIAgent influencer = Global.currentNetwork.getSource(triggeredEdge); // the influencer
		UIAgent agentToChange = Global.currentNetwork.getDest(triggeredEdge); // the one to change the OCF
		
		// search for the ocf of the influencer
		OCF influencerOCF = getAgent(influencer).getLastOCF();
		RunnableAgent agentToChangeOCFHistory = getAgent(agentToChange);
		OCF agentToChangeOCF = agentToChangeOCFHistory.getLastOCF();
		
		// compute the change
		OCF newOCF = agentToChange.getRevisionPolicy().revise(agentToChangeOCF, influencerOCF.getBeliefs());
		
		boolean actualChange = false;
		
		// add the triggered change to the history of agentToChange, but only if there was an actual change
		// so first check if there was an actual change
		if (agentToChange.getRevisionPolicy().tpoEpistemicSpace()) {
			// no change if same tpo
			if (!newOCF.hasSamePreorderAs(agentToChangeOCF)) {
				actualChange = true;
			}
		}
		else {
			// no change if same ocf
			if (!newOCF.isSameAs(agentToChangeOCF)) {
				actualChange = true;
			}
		}
		
		if (actualChange) {
			triggeredEdgeList.add(triggeredEdge);
			currentPosition++;
			totalNbSteps = currentPosition;
			agentToChangeOCFHistory.addEntryToHistory(currentPosition, newOCF);
		}
	}
}
