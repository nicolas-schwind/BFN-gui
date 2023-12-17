package gameRun;

import java.util.ArrayList;
import java.util.List;

import belief.Formula;
import belief.OCF;
import graph.UIAgent;
import graph.AbstractAgent;
import graph.MyEdge;
import graph.stronglyConnectedComponents.DAGSCCGraph;
import graph.stronglyConnectedComponents.StronglyConnectedComponent;
import parameters.Global;
import stochasticProcesses.AbstractStochasticProcess;

public abstract class AbstractRun {
	protected static int MAX_NB_STEPS = 100000;
	
	// saving the structure of the graph as a DAG of strongly connected components
	protected DAGSCCGraph dagSCCgraph;
	
	// saving all agents as runnable agents (we save the history of all OCFs)
	protected List<RunnableAgent> listAgents;
	
	// current lobal position in the game
	protected int currentPosition;
	
	// total number of steps triggered
	protected int totalNbSteps;
	
	// saving the ordered list of triggered edges
	// trigerredEdge[i] is the triggered edge at position i, the change is reflected at the next position
	protected List<MyEdge> triggeredEdgeList;
	
	// the stochastic process used to trigger edges
	protected AbstractStochasticProcess stochasticProcess;
	
	public AbstractRun () {
		this.currentPosition = 0;
		this.totalNbSteps = 0;
		this.dagSCCgraph = Global.currentNetwork.getSCCGraph();
		this.listAgents = new ArrayList<RunnableAgent>();
		// add to the list the exact RunnableAgent from the dagSCCgraph
		for (StronglyConnectedComponent scc : dagSCCgraph.vertexSet()) {
			for (RunnableAgent ag : scc.getAgentSet()) {
				this.listAgents.add(ag);
			}
		}
		this.triggeredEdgeList = new ArrayList<MyEdge>();
	}
	
	public abstract void triggerSingleChangeOperation (MyEdge triggeredEdge);
	
	public abstract boolean stabilityReached ();
	
	public abstract String runModeName ();
	
	public int getStableStatusFlag () {
		return this.dagSCCgraph.getStableStatusFlag();
	}
	
	public int getUnstableStatusFlag () {
		return this.dagSCCgraph.getUnstableStatusFlag();
	}
	
	public int getAllAgentsHaveReachedStabilityStepNumber () {
		return this.dagSCCgraph.getAllAgentsHaveReachedStabilityStepNumber();
	}
	
	public Formula getConsensus () {
		return this.dagSCCgraph.getConsensus();
	}
	
	public int getConsensusDetected () {
		return this.dagSCCgraph.getConsensusDetected();
	}
	
	public Formula getGlobalOutcomeUpperBound () {
		return this.dagSCCgraph.getGlobalOutcomeUpperBound();
	}
	
	public int getGlobalOutcomeUpperBoundLastChangeStep () {
		return this.dagSCCgraph.getGlobalOutcomeUpperBoundLastChangeStep();
	}
	
	public int getDisjunctionOfAllLastStepChange () {
		return this.dagSCCgraph.getDisjunctionOfAllLastChangeStep();
	}
	
	public int getPosition () {
		return currentPosition;
	}
	
	public void setPosition (int position) {
		this.currentPosition = position;
	}
	
	public int getTotalNbSteps () {
		return this.totalNbSteps;
	}
	
	public AbstractStochasticProcess getStochasticProcess () {
		return this.stochasticProcess;
	}
	
	public void setStochasticProcess (AbstractStochasticProcess stochasticProcess) {
		this.stochasticProcess = stochasticProcess;
	}
	
	public MyEdge getTriggeredEdgeAtPosition (int position) {
		return triggeredEdgeList.get(position);
	}
	
	// returns the agentOCFHistory from the list corresponding to the input agent
	public RunnableAgent getAgent (AbstractAgent agent) {
		for (RunnableAgent ag : listAgents) {
			if (ag.getId() == agent.getId()) {
				return ag;
			}
		}
		return null;
	}
	
	public List<RunnableAgent> getListAgents () {
		return this.listAgents;
	}
	
	// returns the conjunction of all agents' last beliefs as a formula
	public Formula conjunctionOfLasts () {
		Formula conjunction = Formula.getTautology();
		for (RunnableAgent agOCFHistory : listAgents) {
			conjunction.conjunctionWith(agOCFHistory.getLastOCF().getBeliefs());
		}
		return conjunction;
	}
	
	// returns the disjunction of all agents' last beliefs as a formula
	public Formula disjunctionOfLasts () {
		Formula disjunction = new Formula();
		for (RunnableAgent agOCFHistory : listAgents) {
			disjunction.disjunctionWith(agOCFHistory.getLastOCF().getBeliefs());
		}
		return disjunction;
	}
	
	// runs the game until the stability condition is reached or until MAX_NB_STEPS steps
	// For a 'cheated run' the stability condition is no change in epistemic state for any possible trigerred edge
	// For a 'true run', there is no valid stability condition
	public void triggerRun () {
		/*DAGSCCGraph dagscc = Global.currentNetwork.getSCCGraph();
		System.out.println(dagscc);
		for (int i = 0; i < dagscc.vertexSet().size(); i++) {
			System.out.println("SCC : ");
			System.out.println(dagscc.getSCC(i));
			System.out.println("Ancestors : ");
			System.out.println(dagscc.getAncestors(dagscc.getSCC(i)));
			System.out.println("********");
		}*/
		Global.log.clear();
		Global.log.addToMessageln(this.dagSCCgraph.getStronglyConnectedComponentsAsTotalPreorder().toString());
		Global.log.blankLines(1);
		Global.log.addToMessageln("*** Step 0 ***");
		
		this.stochasticProcess.init();
		dagSCCgraph.checkAndUpdateStabilityStatusesAndOutcomes(0);
		dagSCCgraph.checkAndUpdateDAGStabilityStatusesAndOutcomes(0);
		for (int i = 1; i < MAX_NB_STEPS && !stabilityReached(); i++) {
			triggerSingleChangeOperation(stochasticProcess.getNextEdge());
			
			Global.log.blankLines(1);
			Global.log.addToMessageln("*** Step " + i + " ***");
			dagSCCgraph.checkAndUpdateStabilityStatusesAndOutcomes(i);
			dagSCCgraph.checkAndUpdateDAGStabilityStatusesAndOutcomes(i);
		}
		Global.log.writeInFile();
	}
	
	// Simple displayed version of the run printed into a String 
	public String toString () {
		MyEdge triggeredEdge;
		UIAgent influencer, agentToChange;
		OCF influencerOCF, agentToChangePreviousOCF, agentToChangeRevisedOCF;
		
		StringBuffer result = new StringBuffer();
		result.append("FULL RUN with " + getTotalNbSteps() + " steps\n");
		for (int i = 0; i < getTotalNbSteps(); i++) {
			triggeredEdge = getTriggeredEdgeAtPosition(i);
			influencer = Global.currentNetwork.getSource(getTriggeredEdgeAtPosition(i));
			influencerOCF = getAgent(influencer).getOCF(i);
			agentToChange = Global.currentNetwork.getDest(triggeredEdge);
			agentToChangePreviousOCF = getAgent(agentToChange).getOCF(i);
			agentToChangeRevisedOCF = getAgent(agentToChange).getOCF(i + 1);
			
			// edge triggered
			result.append("**********\nSTEP " + i + "\n**********\nTriggering edge " + triggeredEdge + "\n\n");
			// agent to change previous ocf
			result.append("Agent to change (" + agentToChange + ") initial OCF:\n" + agentToChangePreviousOCF + "\n");
			// agent influencer
			result.append("Influencer (" + influencer + ") OCF:\n" + influencerOCF + "\n");
			// agent to change new ocf
			result.append("Agent to change (" + agentToChange + ") revised OCF:\n" + agentToChangeRevisedOCF + "\n");
		}
		
		// display all agents' ocfs at the last step
		result.append("\n***** FINAL STATES *****\n");
		List<UIAgent> allAgents = Global.currentNetwork.getOrderedByIDListAgents();
		for (UIAgent ag : allAgents) {
			result.append(ag + ": " + getAgent(ag).getLastOCF() + "\n\n");
		}
		return result.toString();
	}
}
