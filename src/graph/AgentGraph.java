package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.AbstractButton;

import org.apache.commons.collections15.Factory;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import belief.Formula;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import gameRun.AbstractRun;
import gameRun.RunnableAgent;
import graph.stronglyConnectedComponents.DAGSCCGraph;
import graph.stronglyConnectedComponents.StronglyConnectedComponent;
import graphTologyGeneration.Generator;
import parameters.Constants;
import parameters.Global;
import revisionPolicies.AbstractRevisionPolicy;
import revisionPolicies.RevisionPolicyBoutilier;
import revisionPolicies.RevisionPolicyDrasticMerging;
import revisionPolicies.RevisionPolicyNayak;
import revisionPolicies.RevisionPolicyOneImprovement;
import revisionPolicies.RevisionPolicyRestrained;
import revisionPolicies.RevisionPolicySKPBulletPlus;

public class AgentGraph extends DirectedSparseGraph<UIAgent, MyEdge> {
	private static final long serialVersionUID = 1L;

	// The revision policy used for all agents in this network
	// This parameter is within the scope of a network since for now, all agents in a network use the same revision policy
	// This is used only for saving a network in a file
	private AbstractRevisionPolicy globalRevisionPolicy;

	public AgentGraph () {
		super();
		this.globalRevisionPolicy = Global.defaultRevisionPolicy;
	}
	
	public AbstractRevisionPolicy getGlobalRevisionPolicy() {
		return this.globalRevisionPolicy;
	}
	
	public void setGlobalRevisionPolicyFromName (String nameRevisionPolicy) {
		if (nameRevisionPolicy.equals(Constants.nameDrasticMerging)) {
    		this.globalRevisionPolicy = new RevisionPolicyDrasticMerging();
    	}
    	else if (nameRevisionPolicy.equals(Constants.nameOneImprovement)) {
    		this.globalRevisionPolicy = new RevisionPolicyOneImprovement();
    	}
    	else if (nameRevisionPolicy.equals(Constants.nameBoutilier)) {
    		this.globalRevisionPolicy = new RevisionPolicyBoutilier();
    	}
    	else if (nameRevisionPolicy.equals(Constants.nameNayak)) {
    		this.globalRevisionPolicy = new RevisionPolicyNayak();
    	}
    	else if (nameRevisionPolicy.equals(Constants.nameRestrained)) {
    		this.globalRevisionPolicy = new RevisionPolicyRestrained();
    	}
    	else if (nameRevisionPolicy.equals(Constants.nameSKPBulletPlus)) {
    		this.globalRevisionPolicy = new RevisionPolicySKPBulletPlus();
    	}
		
		for(UIAgent ag : this.getVertices()) {
			ag.setRevisionPolicy(this.globalRevisionPolicy);
		}
	}
	
	public void setGlobalRevisionPolicyFromJRadioButton () {
		for (Enumeration<AbstractButton> buttons = Global.buttonGroupRevisionPolicies.getElements(); buttons.hasMoreElements();) {
	        AbstractButton button = buttons.nextElement();
	
	        if (button.isSelected()) {
	            // update the revision policy of each agent depending on the selected radio button
	        	setGlobalRevisionPolicyFromName(button.getText());
	        }
	    }
	}
	
	/*public void loadFromXml(String filename) {
		// Adds extension to filename if it has not
		int i = filename.lastIndexOf('.');
		if (i < 1) {
		    filename += ".xml";
		}
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(filename, new Handler(this));
		}
		catch (SAXException ex)
			{ex.printStackTrace();}
		catch (IOException ex)
			{ex.printStackTrace();}
		catch (ParserConfigurationException ex)
			{ex.printStackTrace();}
	}*/
	
	public List<UIAgent> getOrderedByIDListAgents () {
        Collection<UIAgent> allVerticesNotOrdered = this.getVertices();
        int previousMin = Integer.MIN_VALUE;
        int currentMin;
        List<UIAgent> result = new ArrayList<UIAgent>();
        
        // do while we still have agents
        while (result.size() < allVerticesNotOrdered.size()) {
	    	currentMin = Integer.MAX_VALUE;
	        for (UIAgent ag : allVerticesNotOrdered) {
	        	// search for the new min id (will be stored in the variable currentMin)
	        	if (ag.getId() > previousMin && ag.getId() < currentMin) {
	        		currentMin = ag.getId();
	        	}
	        }
	        for (UIAgent ag : allVerticesNotOrdered) {
	        	// add all agents with that min id (currentMin) to the new list
	        	if (ag.getId() == currentMin) {
	        		result.add(ag);
	        	}
	        }
	    	previousMin = currentMin;
        }
        
        return result;
	}
	
	public UIAgent getAgent(int index) throws IndexOutOfBoundsException {
		// return (Agent) getVertices().toArray()[index];
		Iterator<UIAgent> it = this.getVertices().iterator();
		UIAgent ag;
		while (it.hasNext()) {
			ag = it.next();
			if (ag.getId() == index) {
				return ag;
			}
		}
		throw new IndexOutOfBoundsException("This agent id does not exist in the graph");
	}
	
	public void randomizeBeliefs() {
		for (UIAgent ag: this.getVertices()) {
			ag.getOCF().initBeliefsRandomize();
		}
	}
	
	// return the conjunction for all strongest SCCs of the disjunction of initial beliefs from the agents for each SCC
	public Formula conjDisjStrongestSCCs () {
		Formula result = Formula.getTautology();
		Set<StronglyConnectedComponent> strongestSCCs = this.getSCCGraph().getStrongestSCCs();
		
		for (StronglyConnectedComponent scc : strongestSCCs) {
			result.conjunctionWith(scc.getDisjunction());
		}
		
		return result;
	}
	
	// computes the closure of set
	/*public Collection<Agent> closure (Collection<Agent> set) {
		boolean changed = false;
		Collection<Agent> setUnionNeighbors = new HashSet<Agent>(set);
		// add the neighbors of each element from setUnionNeighbors
		for (Agent ag:set) {
			if (setUnionNeighbors.addAll(this.getSuccessors((ag)))) {
				changed = true;
			}
		}
		
		if (!changed) {
			return set;
		}
		return closure(setUnionNeighbors);
	}*/
	
	//returns the number of agents who infer phi 
	public int getNbAgentsInfer (Formula form) {
		int result = 0;
		int nbAgents = this.getVertexCount();
		
		for (int i = 0; i < nbAgents; i++) {
			if (this.getAgent(i).getOCF().infers(form)) {
				result++;
			}
		}
		
		return result;
	}
	
	// Return the graph structure without cloning beliefs
	// It seems impossible to clone vertices, thanks jung 2.0 !
	/*public AgentGraph getGraphStructure() {
		AgentGraph clone = new AgentGraph();
		for (MyEdge edge:this.getEdges()) {
			clone.addEdge(edge, this.getEndpoints(edge));
		}
		return clone;
	}*/
	
	//XMLHandler
	/*public class Handler extends DefaultHandler {
		AgentGraph g;
		public Handler(AgentGraph graph) {
			g = graph;
		}
		public void startElement(String uri, String name, String qname, Attributes attr) {
			int n, i, j;
			if (name.equals("NbAgent")) {
				n = Integer.parseInt(attr.getValue("n"));
				for(int k = 0 ; k < n ; k++)
				{
					g.addVertex(new Agent(k));			
				}
			}
			
			if (name.equals("Arc")) {
				i = Integer.parseInt(attr.getValue("i"));
				j = Integer.parseInt(attr.getValue("j"));
				g.addEdge(new MyEdge(), g.getAgent(i), g.getAgent(j));	
			}
		}
	}*/
	
	// Vertex factory
    public static Factory<UIAgent> vertexFactory = new Factory<UIAgent>() {
    	// create a new agent with id the maxId of the current network + 1
        public UIAgent create() {
        	// compute first the max id of the current graph
        	int maxAgentId = -1;
        	for (UIAgent ag : Global.currentNetwork.getVertices()) {
        		if (maxAgentId < ag.getId()) {
        			maxAgentId = ag.getId();
        		}
        	}
            return new UIAgent(maxAgentId + 1);
        }
    };
    
    // Edge factory
    public static Factory<MyEdge> edgeFactory = new Factory<MyEdge>() {
        public MyEdge create() {
            return new MyEdge();
        }
    };
    
    // Graph factory
    public static Factory<Graph<UIAgent, MyEdge>> agentGraphFactory = new Factory<Graph<UIAgent, MyEdge>>() {
    	public AgentGraph create() {
            return new AgentGraph();
        }
    };
    
    /**
     * The following methods are tests about graph structure (closure, etc.)  
     * 
     */
    
    // returns the set of agent ids in the transitive closure of the agent given as input
    public Collection<Integer> getTransitiveClosureAgentIds (UIAgent ag) {
    	Collection<Integer> transitiveClosureAgentIds = new TreeSet<Integer>();
 		transitiveClosureAgentIds.add(ag.getId());
 		Collection<Integer> lastAddedAgentIds = new TreeSet<Integer>();
 		Collection<Integer> newlyAddedAgentIds = new TreeSet<Integer>();
 		newlyAddedAgentIds.add(ag.getId());
 		
 		Collection<UIAgent> successorAgents;
 		int successorAgentId;
 		while (!newlyAddedAgentIds.isEmpty()) {
 			//System.out.println("newly added : " + newlyAddedAgents);
 			lastAddedAgentIds = new TreeSet<Integer>(newlyAddedAgentIds);
 			newlyAddedAgentIds.clear();
 			for (int lastAddedAg : lastAddedAgentIds) {
 				//System.out.println("  --> checking the successors of " + lastAddedAg);
 				successorAgents = this.getSuccessors(this.getAgent(lastAddedAg));
 				for (UIAgent successorAgent : successorAgents) {
 					successorAgentId = successorAgent.getId();
 					// check if it is already in the transitive closure
 					if (transitiveClosureAgentIds.contains(successorAgentId)) {
 						// the agent is already in the transitive closre, so we ignore it
 					}
 					else {
 						// the agent has not been seen before, so we add it to the transitive closure
 						newlyAddedAgentIds.add(successorAgentId);
 						transitiveClosureAgentIds.add(successorAgentId);
 						//System.out.println("  ----> adding " + successorAgentId);
 					}
 				}
 			}
 			
 		}
 		
 		//System.out.println("nb of agents in graph: " + this.getVertexCount());
 		//System.out.println("nb of agents in the transitive closure: " + transitiveClosure.size());
 		
 		return transitiveClosureAgentIds;
    }
    
    private void strongConnectRecursive (UIAgent ag, Stack<Integer> stack, Map<Integer, Integer> index,
    		Map<Integer, Integer> lowlink, Map<Integer, Boolean> onStack, Stack<Collection<Integer>> result) {
    	int currentIndex = index.size();
    	int agId = ag.getId();
    	int agSuccessorId;
    	int agInSCCId;
    	Collection<Integer> newStronglyConnectedComponent;
    	index.put(agId, currentIndex);
    	lowlink.put(agId, currentIndex);
    	stack.push(agId);
    	onStack.put(agId, true);
    	
    	// considers successors of agId
    	Collection<UIAgent> agSuccessors = this.getSuccessors(ag);
    	for (UIAgent agSuccessor : agSuccessors) {
    		agSuccessorId = agSuccessor.getId();
    		if (!index.containsKey(agSuccessorId)) {
    			// agSuccessor has not yet been visited, recurse on it
    			this.strongConnectRecursive(agSuccessor, stack, index, lowlink, onStack, result);
    			lowlink.replace(agId, Math.min(lowlink.get(agId), lowlink.get(agSuccessorId)));
    		}
    		else if (onStack.get(agSuccessorId)) {
    			// agSuccessor is in stack end hence in the current strongly connected component
    			lowlink.replace(agId, Math.min(lowlink.get(agId), index.get(agSuccessorId)));
    		}
    	}
    	
    	// if ag is a root node, then pop the stack and generate a strongly connected component
    	if (lowlink.get(agId) == index.get(agId)) {
    		// start a new strongly connected component
    		newStronglyConnectedComponent = new HashSet<Integer>();
    		do {
    			agInSCCId = stack.pop();
    			onStack.replace(agInSCCId, false);
    			// add agInSCCId to the current strongly connected component
    			newStronglyConnectedComponent.add(agInSCCId);
    		}
    		while (agId != agInSCCId);
    		// here a strongly connected component has been generated
    		result.add(newStronglyConnectedComponent);
    	}
    	
    }
    
    // returns the set of strongly connected components, in topological reversed order
    // agents are identified by their ids
    // this method implements Tarjan's algorithm
	/*
	 * public Stack<Collection<Integer>> getStronglyConnectedComponents () {
	 * Stack<Collection<Integer>> result = new Stack<Collection<Integer>>();
	 * Stack<Integer> stack = new Stack<Integer>(); Map<Integer, Integer> index =
	 * new HashMap<Integer, Integer>(); Map<Integer, Integer> lowlink = new
	 * HashMap<Integer, Integer>(); Map<Integer, Boolean> onStack = new
	 * HashMap<Integer, Boolean>();
	 * 
	 * for (UIAgent ag : this.getVertices()) { if (!index.containsKey(ag.getId())) {
	 * strongConnectRecursive(ag, stack, index, lowlink, onStack, result); } }
	 * 
	 * return result; }
	 */
    
    // returns the set of the strongest strongly connected components from the stronglyConnectedComponents given as parameter
    // agents are identified by their ids
    // For example, if we want to get the strongest SCCs of the graph, the parameter stronglyConnectedComponents should be the result
    // of the method getStronglyConnectedComponents() which computes the set of all SCCs in the graph in a reverse topological order,
    // using Tarjan's algorithm
	/*
	 * public Vector<Collection<Integer>> getStrongestStronglyConnectedComponents
	 * (Stack<Collection<Integer>> stronglyConnectedComponents) {
	 * Vector<Collection<Integer>> result = new Vector<Collection<Integer>>();
	 * 
	 * // We create a clone of the stronglyConnectedComponents // The clone will be
	 * fully emptied // From the initial parameter stronglyConnectedComponents, we
	 * will only remove the result Stack<Collection<Integer>>
	 * cloneStronglyConnectedComponents = new Stack<Collection<Integer>>();
	 * cloneStronglyConnectedComponents.addAll(stronglyConnectedComponents);
	 * //System.out.println(cloneStronglyConnectedComponents);
	 * 
	 * // We pop the stack stronglyConnectedComponents iteratively (so they will be
	 * in the 'correct' topological order) // If the current set of components
	 * include the next component, we do not add it Collection<Integer>
	 * currentComponent = cloneStronglyConnectedComponents.pop();
	 * result.add(currentComponent); Collection<Integer> transitiveClosure =
	 * this.getTransitiveClosureAgentIds(this.getAgent(currentComponent.iterator().
	 * next()));
	 * 
	 * while (!cloneStronglyConnectedComponents.isEmpty()) { //while
	 * (transitiveClosure.size() != this.getVertexCount()) { // there are still some
	 * remaining strongest components currentComponent =
	 * cloneStronglyConnectedComponents.pop(); if
	 * (!transitiveClosure.containsAll(currentComponent)) { // then this
	 * currentComponent is one of the strongest and thus should be added to the
	 * result result.add(currentComponent); // update the transitive closure
	 * transitiveClosure.addAll(this.getTransitiveClosureAgentIds(this.getAgent(
	 * currentComponent.iterator().next()))); } }
	 * 
	 * return result; }
	 */
    
    // this method removes the strongest SCCs from stronglyConnectedComponents
    // the parameter is assumed to be a set of SCCs in a reverse topological order (using Tarjan's algorithm)
    // the method returns the removed set of strongest SCCs
	/*
	 * public Collection<Collection<Integer>> removeStrongestConnectedComponentsFrom
	 * (Stack<Collection<Integer>> stronglyConnectedComponents) {
	 * Collection<Collection<Integer>> strongestSCCs =
	 * this.getStrongestStronglyConnectedComponents(stronglyConnectedComponents);
	 * 
	 * for (Collection<Integer> component : strongestSCCs) { for
	 * (Collection<Integer> candidateToBeRemoved : stronglyConnectedComponents) { if
	 * (candidateToBeRemoved.containsAll(component)) {
	 * stronglyConnectedComponents.remove(candidateToBeRemoved); break; } } }
	 * 
	 * return strongestSCCs; }
	 */
    
    // this method returns the set of all strongly connected components (SCCs) as a total preorder
    // one iteratively sets the next total preorder level as the strongest SCCs from the current set, and remove those strongest SCCs from that set
	/*
	 * public Vector<Collection<Collection<Integer>>>
	 * getStronglyConnectedComponentsAsTotalPreorder (Stack<Collection<Integer>>
	 * stronglyConnectedComponents) { Vector<Collection<Collection<Integer>>> result
	 * = new Vector<Collection<Collection<Integer>>>(); Stack<Collection<Integer>>
	 * stackAllStronglyConnectedComponents = this.getStronglyConnectedComponents();
	 * 
	 * while (!stackAllStronglyConnectedComponents.isEmpty()) {
	 * result.add(this.removeStrongestConnectedComponentsFrom(
	 * stackAllStronglyConnectedComponents)); }
	 * 
	 * return result; }
	 */
    
    // returns the set of agent ids that have a predecessor
	/*
	 * public Collection<Integer> havePredecessorsAgentIds () { Collection<Integer>
	 * result = new TreeSet<Integer>();
	 * 
	 * for (MyEdge edge : this.getEdges()) { result.add(getDest(edge).getId()); }
	 * 
	 * return result; }
	 */
    
    // returns the set of agent ids that 
    
    // Test if the graph is weakly connected / connex
 	// To test it, we do the symmetric closure of the graph, start from an arbitrary vertex and check if the
    // transitive closure covers the entire graph
 	public boolean isWeaklyConnected () {
 		// compute the symmetric closure of this graph
 		AgentGraph symmetricClosure = Generator.getSymmetricClosure(this);
 		// select an arbitrary agent from that symmetric closure
 		UIAgent ag = symmetricClosure.getVertices().iterator().next();
 		// perform the transitive closure starting from ag, and check whether that symmetric closure is the whole graph
 		return symmetricClosure.getTransitiveClosureAgentIds(ag).size() == symmetricClosure.getVertexCount();
 	}
 	
 	// Test if the graph is strongly connected / strongly connex
  	// It is true if it has exactly one SCC
	/*
	 * public boolean isStronglyConnected () { return
	 * getStronglyConnectedComponents().size() == 1; }
	 */
	
	// returns true if all revision rules are defined on the tpo epistemic space
	public boolean allRevisionRulesTpoEpistemicSpace () {
		for (UIAgent ag : this.getVertices()) {
			if (!ag.getRevisionPolicy().tpoEpistemicSpace()) {
				return false;
			}
		}
		return true;
	}
    
    // this method is used when navigating through a run
    // it sets all beliefs (OCFs) of the agents in the graph as the beliefs at the current position of the run
    // in addition it will refresh the OCF panel, which will highlight the two agents related to a trigger
    public void setGame (AbstractRun run) {
    	int targetPosition = run.getPosition();
    	MyEdge triggeredEdge;
    	for (MyEdge edge : this.getEdges()) {
    		edge.setIsCurrentlyTriggerred(false);
    	}
    	for (UIAgent ag : this.getVertices()) {
    		ag.getPanelOCF().setIsInfluentAgent(false);
    		ag.getPanelOCF().setIsAgenttoBeChanged(false);
    		// update the agent OCF
    		ag.setOCF(run.getAgent(ag).getOCF(targetPosition));
    		// update the displayed OCF in its associated panel
    		ag.getPanelOCF().setOCF(ag.getOCF());
    		// update the status of the displayed (selected / change agent / influential agent)
    		// there is a change / influential agent only when the targetPosition is not the last
    		if (targetPosition < run.getTotalNbSteps()) {
    			triggeredEdge = run.getTriggeredEdgeAtPosition(targetPosition);
    			triggeredEdge.setIsCurrentlyTriggerred(true);
        		if (Global.currentNetwork.getSource(triggeredEdge).getId() == ag.getId()) {
        			ag.getPanelOCF().setIsInfluentAgent(true);
        		}
        		else if (Global.currentNetwork.getDest(triggeredEdge).getId() == ag.getId()) {
        			ag.getPanelOCF().setIsAgenttoBeChanged(true);
        		}
    		}
    		ag.getPanelOCF().repaint();
    	}
    }
    
    // return the condensation of the current agent graph (a DAG where each node is a strongly connected component)
    public DAGSCCGraph getSCCGraph () {
    	DAGSCCGraph result = new DAGSCCGraph(MyEdge.class);
    	// a counter for SCC identifiers
    	int idCpt = 0;
    	int idAgentSourceTmp;
    	int idAgentDestTmp;
    	RunnableAgent agentSourceTmp = null;
    	RunnableAgent agentDestTmp = null;
    	
    	// first convert the current agent graph to a simple directed graph (so that we can use the getCondensation method)
    	SimpleDirectedGraph<RunnableAgent, MyEdge> thisGraph = new SimpleDirectedGraph<RunnableAgent, MyEdge>(MyEdge.class);
    	for (UIAgent ag : this.getVertices()) {
    		thisGraph.addVertex(new RunnableAgent(ag));
    	}
    	for (MyEdge edge : this.getEdges()) {
    		
    		idAgentSourceTmp = this.getSource(edge).getId();
    		idAgentDestTmp = this.getDest(edge).getId();
    		// search for the two RunnableAgent with ids idAgentSourceTmp and idAgentDestTmp in thisGraph
    		for (RunnableAgent ag : thisGraph.vertexSet()) {
    			if (ag.getId() == idAgentSourceTmp) {
    				agentSourceTmp = ag;
    			}
    			if (ag.getId() == idAgentDestTmp) {
    				agentDestTmp = ag;
    			}
    		}
    		thisGraph.addEdge(agentSourceTmp, agentDestTmp);
    	}
    	
    	// get the condensation of this graph
    	org.jgrapht.Graph<org.jgrapht.Graph<RunnableAgent, MyEdge>, DefaultEdge> condensation = new KosarajuStrongConnectivityInspector<RunnableAgent, MyEdge>(thisGraph).getCondensation();
    	for (org.jgrapht.Graph<RunnableAgent, MyEdge> scc : condensation.vertexSet()) {
    		result.addSCC(idCpt++, scc.vertexSet());
    	}
    	for (DefaultEdge edge : condensation.edgeSet()) {
    		result.addEdge(result.getSCC(condensation.getEdgeSource(edge).vertexSet()),
    				result.getSCC(condensation.getEdgeTarget(edge).vertexSet()));
    	}
    	
    	return result;
    }
}

