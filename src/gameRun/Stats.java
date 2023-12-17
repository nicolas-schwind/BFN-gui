package gameRun;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import belief.Formula;
import graph.AgentGraph;
import graph.stronglyConnectedComponents.DAGSCCGraph;
import graph.stronglyConnectedComponents.StronglyConnectedComponent;
import parameters.Global;

//generate various stats for a given graph
public class Stats {
	
	public static int NB_RUNS = 10000;
	public static int LENGTH_LONGEST_STRING = 100;
	
	private AgentGraph graph;
	private DAGSCCGraph dagSCCs;
	private Set<StronglyConnectedComponent> stronglyConnectedComponents;
	private Set<StronglyConnectedComponent> strongestSCCs;
	private Vector<Set<StronglyConnectedComponent>> stronglyConnectedComponentsAsTotalPreorder;
	
	public Stats (AgentGraph graph) {
		this.graph = graph;
		this.dagSCCs = graph.getSCCGraph();
	}
	
	public static String getStringStarOfLength (int length) {
		return "*".repeat(length);
	}
	
	public static String getHeader (String message) {
		int length = (Stats.LENGTH_LONGEST_STRING - message.length() - 1) / 2;
		return (Stats.getStringStarOfLength(length) + " " + message + " " + Stats.getStringStarOfLength(length) + "\n");
	}
	
	public static String getFooter (String message) {
		return getStringStarOfLength(Stats.getHeader(message).length() - 1) + "\n";
	}
	
	public void computeAllStats () {
		stronglyConnectedComponents = dagSCCs.vertexSet();
		strongestSCCs = dagSCCs.getStrongestSCCs();
		stronglyConnectedComponentsAsTotalPreorder = dagSCCs.getStronglyConnectedComponentsAsTotalPreorder();
		
		Global.currentNetwork.setGlobalRevisionPolicyFromJRadioButton();
	}
	
	public String componentToString (StronglyConnectedComponent component) {
		StringBuffer result = new StringBuffer();
		Set<RunnableAgent> agentSet = component.getAgentSet();
		
		// we'd better display a component according to agents' id ascending order
		// (but this should not be mandatory)
		// first compute the min agent id
		int minAgentId = Integer.MAX_VALUE;
		for (RunnableAgent ag : agentSet) {
			if (ag.getId() < minAgentId) {
				minAgentId = ag.getId();
			}
		}
		// browse all agent id from the component starting from minAgentId 
		for (int cptAgentId = minAgentId, nbAgentsAlreadyDisplayed = 0; nbAgentsAlreadyDisplayed < component.getSize(); cptAgentId++) {
			for (RunnableAgent ag : agentSet) {
				if (ag.getId() == cptAgentId) {
					// display that agent
					result.append("-----> +++++ Agent #" + this.graph.getAgent(ag.getId()) + "\n");
					nbAgentsAlreadyDisplayed++;
					break;
				}
			}			
		}
		
		
		// old (without ordering)
		//for (int agentId : component) {
		//	result.append("-----> +++++ Agent #" + this.graph.getAgent(agentId) + "\n");
		//}
		
		return result.toString();
	}
	
	public String setOfComponentsToString (Set<StronglyConnectedComponent> setOfComponents) {
		StringBuffer result = new StringBuffer();
		int componentNumber = 0;
		
		for (StronglyConnectedComponent component : setOfComponents) {
			componentNumber++;
			if (component.getSize() == 1) {
				result.append("-----> Component #" + componentNumber + " (" + component.getSize() + " agent) :\n");
			}
			else {
				result.append("-----> Component #" + componentNumber + " (" + component.getSize() + " agents) :\n");
			}
			result.append(this.componentToString(component));
		}
		
		return result.toString();
	}
	
	public String stronglyConnectedComponentsToString () {
		StringBuffer result = new StringBuffer();
		String message = "All strongly connected components (SCCs)";
		int levelNumber = 0;
		
		result.append(Stats.getHeader(message));
		
		for (Set<StronglyConnectedComponent> currentStrongestSCCs : stronglyConnectedComponentsAsTotalPreorder) {
			levelNumber++;
			result.append("- Level " + levelNumber + " :\n");
			result.append(this.setOfComponentsToString(currentStrongestSCCs));
		}
		
		result.append(Stats.getFooter(message));
		
		return result.toString();
	}
	
	public String componentWorldFrequencyToString (StronglyConnectedComponent component) {
		StringBuffer result = new StringBuffer();
		Collection<Formula> setOfFormulae = new HashSet<Formula>();
		int[] frequencyStats;
		Formula conjunction;
		int currentMaxfrequency;
		int localMaxfrequency;
		Collection<Integer> interpretationSet;
		Iterator<Integer> iterator;
		boolean found;
		
		// check if the conjunction of all formulae from setOfFormulae is consistent
		// if the conjunction is consistent, we treat this case in a specific way
		conjunction = Formula.getConjunction(setOfFormulae);
		if (conjunction.isConsistent()) {
			return "-----> %%%%% The joint conjunction of this component is consistent, its joint conjunction is : " + conjunction + "\n";
		}
		
		// here, the conjunction of all formulae from setOfFormulae is inconsistent
		// in this case, we display the frequencyStats array
		// from the most frequent interpretations to the least ones
		// no need to display interpretations whose frequency is 0
		frequencyStats = Formula.getFrequencyStats(setOfFormulae);
		result.append("-----> %%%%% The joint component is inconsistent, the frequency of worlds is :\n");
		currentMaxfrequency = component.getSize();
		while (currentMaxfrequency > 0) {
			localMaxfrequency = 0;
			found = false;
			// compute the localMaxFrequency first
			for (int i = 0; i < frequencyStats.length; i++) {
				if ((frequencyStats[i] > localMaxfrequency) && (frequencyStats[i] < currentMaxfrequency)) {
					localMaxfrequency = frequencyStats[i];
					found = true;
				}
			}
			
			// if found is true, return the worlds appearing as the frequency localMaxFrequency in the set of formulae
			// otherwise do nothing
			if (found) {
				// compute these worlds 
				interpretationSet = new HashSet<Integer>();
				for (int i = 0; i < frequencyStats.length; i++) {
					if (frequencyStats[i] == localMaxfrequency) {
						interpretationSet.add(i);
					}
				}
				iterator = interpretationSet.iterator();
				result.append("-----> %%%%% world set (");
				// display these worlds in the string
				if (interpretationSet.size() == 1) {
					result.append(Global.intToHexa(iterator.next()) + ") : ");
				}
				else {
					result.append(Global.intToHexa(iterator.next()));
					while (iterator.hasNext()) {
						result.append(", " + Global.intToHexa(iterator.next()));
					}
					result.append(") : ");
				}
				result.append("frequency " + localMaxfrequency);
				if (localMaxfrequency > 1) {
					result.append(" agents\n");
				}
				else {
					result.append(" agent\n");
				}
			}
			
			currentMaxfrequency = localMaxfrequency;
		}
		
		return result.toString();
	}
	
	public String strongestSCCsToString () {
		StringBuffer result = new StringBuffer();
		String message = "Strongest SCCs";
		int componentNumber = 0;
		
		result.append(Stats.getHeader(message));
		
		for (StronglyConnectedComponent component : strongestSCCs) {
			componentNumber++;
			if (component.getSize() == 1) {
				result.append("-----> Component #" + componentNumber + " (" + component.getSize() + " agent) :\n");
			}
			else {
				result.append("-----> Component #" + componentNumber + " (" + component.getSize() + " agents) :\n");
			}
			result.append(this.componentToString(component));
			result.append(this.componentWorldFrequencyToString(component));
		}
		
		result.append(Stats.getFooter(message));
		
		return result.toString();
	}
	
	public void displayAllStats () {
		System.out.println(this.stronglyConnectedComponentsToString() + "\n");
		System.out.println(this.strongestSCCsToString());
	}
}
