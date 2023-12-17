package gameRun;

import belief.OCF;
import graph.UIAgent;
import graph.MyEdge;
import parameters.Global;

// A true run is a run where 'actual changes' (trigerred edges), stability conditions, etc. are
// checked using only observed beliefs, i.e., without knowing the belief change operators
// used by each agent, their epistemic space / state, etc.
public class TrueRun extends AbstractRun {

	public TrueRun() {
		super();
	}
	
	public String runModeName () {
		return "true";
	}
	
	public void triggerSingleChangeOperation(MyEdge triggeredEdge) {
		UIAgent influencer = Global.currentNetwork.getSource(triggeredEdge); // the influencer
		UIAgent agentToChange = Global.currentNetwork.getDest(triggeredEdge); // the one to change the OCF
		
		// search for the ocf of the influencer
		OCF influencerOCF = getAgent(influencer).getLastOCF();
		RunnableAgent agentToChangeOCFHistory = getAgent(agentToChange);
		OCF agentToChangeOCF = agentToChangeOCFHistory.getLastOCF();
		
		// compute the change
		OCF newOCF = agentToChange.getRevisionPolicy().revise(agentToChangeOCF, influencerOCF.getBeliefs());
		
		triggeredEdgeList.add(triggeredEdge);
		currentPosition++;
		totalNbSteps = currentPosition;
		agentToChangeOCFHistory.addEntryToHistory(currentPosition, newOCF);
	}
	
	public boolean stabilityReached() {
		return getAllAgentsHaveReachedStabilityStepNumber() >= 0;
	}
}
