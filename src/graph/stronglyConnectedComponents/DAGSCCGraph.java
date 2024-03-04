package graph.stronglyConnectedComponents;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import org.jgrapht.graph.DirectedAcyclicGraph;

import belief.Formula;
import gameRun.RunnableAgent;
import graph.AbstractAgent;
import graph.MyEdge;
import parameters.Global;

public class DAGSCCGraph extends DirectedAcyclicGraph<StronglyConnectedComponent, MyEdge> {
	
	/**
	 * An SCC DAG is stable iff all SCC are stable. stableStatusFlag is initialized to -1, >=0 (the detected step number)
	 * iff all SCCs are detected to have a stable status
	 */
	private int stableStatusFlag;
	
	/**
	 * An SCC DAG is unstable iff at least one of its SCC is unstable. unstableStatusFlag is initially set to -1, and gets to
	 * a non-negative number once unstability for some SCC has been detected		
	 */
	private int unstableStatusFlag;
	
	/**
	 * allAgentsHaveReachedStabilityStepNumber, initialized to -1, >=0 iff all SCCs have a stable status, and corresponds to
	 * the actual step number ahere all agents have reached a stable state
	 */
	private int allAgentsHaveReachedStabilityStepNumber;
	
	/**
	 * consensus, initialized to \bot, becomes conjunction_allSCC-Si(GO(Si)) if consistent, in which case that will be the final,
	 * consistent consensus. It may remain inconsistent but still at some point become 'known', i.e.,
	 * conjunction_allSCC-Si(GOUP(Si)) is inconsistent. In this case the consensus is guaranteed to be inconsistent.
	 * Anyway, in any case, consensus corresponds to the conjunction of all GO(Si) for all SCCs Si
	 */
	private Formula consensus;
	
	/**
	 * consensusDetected, initialized to -1, may become to >=0 if characterized at some step as described above
	 * (can be consistent or inconsistent)
	 */
	private int consensusDetected;
	
	/**
	 * This is denoted by GOUP and corresponds to the disjunction of all GOUP(Si) for all SCCs Si
	 * It is initialized to the tautology
	 */
	private Formula globalOutcomeUpperBound;
	
	/**
	 * This stores the last step where the GO of the DAG has changed
	 * This corresponds to the max of this variable for all SCCs of the DAG
	 */
	private int globalOutcomeUpperBoundLastChangeStep;
	
	/**
	 * This stores the current disjunction of all agents beliefs
	 */
	private Formula disjunctionOfAlls;
	
	/**
	 * This stores the last time where the disjunction of all agents beliefs has changed
	 */
	private int disjunctionOfAllLastChangeStep;
	
	public DAGSCCGraph(Class<? extends MyEdge> edgeClass) {
		super(edgeClass);
		this.stableStatusFlag = -1;
		this.unstableStatusFlag = -1;
		this.allAgentsHaveReachedStabilityStepNumber = -1;
		this.consensus = Formula.getInconsistentFormula();
		this.consensusDetected = -1;
		this.globalOutcomeUpperBound = Formula.getTautology();
		this.globalOutcomeUpperBoundLastChangeStep = -1;
		this.disjunctionOfAlls = Formula.getTautology();
		this.disjunctionOfAllLastChangeStep = -1;
	}
	
	private static final long serialVersionUID = 1L;
	
	public int getStableStatusFlag () {
		return this.stableStatusFlag;
	}
	
	public int getUnstableStatusFlag () {
		return this.unstableStatusFlag;
	}
	
	public int getAllAgentsHaveReachedStabilityStepNumber () {
		return this.allAgentsHaveReachedStabilityStepNumber;
	}
	
	public Formula getConsensus () {
		return this.consensus;
	}
	
	public int getConsensusDetected () {
		return this.consensusDetected;
	}
	
	public Formula getGlobalOutcomeUpperBound () {
		return this.globalOutcomeUpperBound;
	}
	
	public int getGlobalOutcomeUpperBoundLastChangeStep () {
		return this.globalOutcomeUpperBoundLastChangeStep;
	}
	
	public Formula getDisjunctionOfAll () {
		return this.disjunctionOfAlls;
	}
	
	public int getDisjunctionOfAllLastChangeStep () {
		return this.disjunctionOfAllLastChangeStep;
	}
	
	public void addSCC (int id, Set<RunnableAgent> agentSet) {
		this.addVertex(new StronglyConnectedComponent(id, agentSet));
	}
	
	// return an SCC given its id
	public StronglyConnectedComponent getSCC (int idSCC) {
		for (StronglyConnectedComponent scc : this.vertexSet()) {
			if (scc.getId() == idSCC) {
				return scc;
			}
		}
		return null;
	}
	
	// return an SCC given its agent set
	public StronglyConnectedComponent getSCC (Set<RunnableAgent> agentSet) {
		for (StronglyConnectedComponent scc : this.vertexSet()) {
			if (scc.getAgentSet().equals(agentSet)) {
				return scc;
			}
		}
		return null;
	}
	
	// return the SCC to which the agent given in parameter belongs
	public StronglyConnectedComponent getSCC (AbstractAgent agent) {
		for (StronglyConnectedComponent scc : this.vertexSet()) {
			for (AbstractAgent ag : scc.getAgentSet()) {
				if (ag.getId() == agent.getId()) {
					return scc;
				}
			}
		}
		return null;
	}
	
	/**
	 * returns the set of all agents of all SCCs in this DAG
	 */
	public Set<RunnableAgent> getAllAgents () {
		Set<RunnableAgent> result = new HashSet<RunnableAgent>();
		
		for (StronglyConnectedComponent scc : this.vertexSet()) {
			for (RunnableAgent ag : scc.getAgentSet()) {
				result.add(ag);
			}
		}
		
		return result;
	}
	
	/**
	 * returns the root vertices of this DAG
	 */
	public Set<StronglyConnectedComponent> getRootSet () {
		Set<StronglyConnectedComponent> result = new HashSet<StronglyConnectedComponent>();
		
		for (StronglyConnectedComponent scc : this.vertexSet()) {
			if (inDegreeOf(scc) == 0) {
				result.add(scc);
			}
		}
		
		return result;
	}
	
	/**
	 * returns the strongest SCCs of this DAG. It just corresponds to the roots
	 */
	public Set<StronglyConnectedComponent> getStrongestSCCs () {
		return getRootSet();
	}
	
	/**
	 * returns the strongest SCCs of this DAG, ordered in a fixed way depending on the agents' id.
	 * It is useful for consistency in displaying the vertices, i.e., when the DAG is computed for each agent independently...
	 * The first SCC in the vector will be the one with agent of min id
	 * The next one will be the one with agent of min id in the remaining pool of agents, etc.
	 */
	public Vector<StronglyConnectedComponent> getStrongestSCCsStaticOrder () {
		Vector<StronglyConnectedComponent> result = new Vector<StronglyConnectedComponent>();
		Set<StronglyConnectedComponent> setStrongestSCCs = getStrongestSCCs();
		StronglyConnectedComponent sccTmpToRemove;
		int minAgentId;
		
		while (!setStrongestSCCs.isEmpty()) {
			// search for the component containing the agent with min id
			minAgentId = Integer.MAX_VALUE;
			sccTmpToRemove = null;
			for (StronglyConnectedComponent scc : setStrongestSCCs) {
				for (RunnableAgent ag : scc.getAgentSet()) {
					if (ag.getId() < minAgentId) {
						minAgentId = ag.getId();
						sccTmpToRemove = scc;
					}
				}
			}
			// Here, minAgentId is the min id in all components from the remaining setStrongestSCCs
			// And sccTmpToRemove is the scc in which this agent belongs
			// We add this component to the result and remove it from setStrongestSCCs
			result.add(sccTmpToRemove);
			setStrongestSCCs.remove(sccTmpToRemove);
		}
		
		return result;
	}
	
	/**
	 * returns the set of all SCCs as a total preorder
	 * one copy the current DAG, first selects the root vertices, remove them from the copied DAG and iterate until no vertex remains
	 */ 
    public Vector<Set<StronglyConnectedComponent>> getStronglyConnectedComponentsAsTotalPreorder () {
    	Vector<Set<StronglyConnectedComponent>> result = new Vector<Set<StronglyConnectedComponent>>();
    	DAGSCCGraph dagCopy = (DAGSCCGraph)this.clone();
    	Set<StronglyConnectedComponent> levelSCCs;
    	
    	while (!dagCopy.vertexSet().isEmpty()) {
    		levelSCCs = dagCopy.getRootSet();
    		result.add(levelSCCs);
    		dagCopy.removeAllVertices(levelSCCs);
    	}
    	
    	return result;
    }
    
    /**
     * returns the set of all SCCs in some topological order,
     * i.e., we first computes the SCCs as total preorder and for each level we browse the SCCs in any order
     */
    public Vector<StronglyConnectedComponent> getStronglyConnectedComponentsAsTopologicalOrder () {
    	Vector<StronglyConnectedComponent> result = new Vector<StronglyConnectedComponent>();
    	Vector<Set<StronglyConnectedComponent>> totalPreorderSCCs = getStronglyConnectedComponentsAsTotalPreorder();
    	
    	for (Set<StronglyConnectedComponent> level : totalPreorderSCCs) {
    		result.addAll(level);
    	}
    	
    	return result;
    }
    
    /**
     * returns the set of direct parents of a given scc
     */
    public Set<StronglyConnectedComponent> getParents (StronglyConnectedComponent scc) {
    	Set<StronglyConnectedComponent> result = new HashSet<StronglyConnectedComponent>();
    	
		for (MyEdge incomingEdge : this.incomingEdgesOf(scc)) {
			result.add(this.getEdgeSource(incomingEdge));
		}
		
		return result;
    }
    
    /**
     * returns conjunction_j(GO(Sj)), where Sj is the set of parents of a given scc.
     * If Sj is empty, returns the tautology
     */
    public Formula getConjunctionGOParents (StronglyConnectedComponent scc) {
    	Formula result = Formula.getTautology();
    	
		for (StronglyConnectedComponent parent : this.getParents(scc)) {
			result.conjunctionWith(parent.getGlobalOutcomeFinal());
		}
		
		return result;
    }
    
    /**
     * returns conjunction_j(GOUP(Sj)), where Sj is the set of parents of a given scc.
     * If Sj is empty, returns the tautology
     */
    public Formula getConjunctionGOUPParents (StronglyConnectedComponent scc) {
    	Formula result = Formula.getTautology();
    	
		for (StronglyConnectedComponent parent : this.getParents(scc)) {
			result.conjunctionWith(parent.getGlobalOutcomeUpperBound());
		}
		
		return result;
    }
    
    /**
     * returns disjunction_j(GOUP(Sj)), where Sj is the set of parents of a given scc.
     * If Sj is empty, returns \bot
     */
    public Formula getDisjunctionGOUPParents (StronglyConnectedComponent scc) {
    	Formula result = Formula.getInconsistentFormula();
    	
		for (StronglyConnectedComponent parent : this.getParents(scc)) {
			result.disjunctionWith(parent.getGlobalOutcomeUpperBound());
		}
		
		return result;
    }
    
    /**
     * returns conjunction_j(GOUPIfStable(Sj)), where Sj is the set of parents of a given scc.
     * If Sj is empty, returns the tautology
     */
    public Formula getConjunctionGOUPIfStableParents (StronglyConnectedComponent scc) {
    	Formula result = Formula.getTautology();
    	
		for (StronglyConnectedComponent parent : this.getParents(scc)) {
			result.conjunctionWith(parent.getGlobalOutcomeUpperBoundIfStable());
		}
		
		return result;
    }
	
    /*
     * This method checks whether an SCC given as parameter changes its stability state from unknown to stable.
     * Returns true iff there is a change to stable.
     * Note: this method should be called only when the stability status of the SCC in parameter is still unknown
     * 
	 * An SCC Si is detected stable at step k if:
	 * - all parent SCCs Sj are stable, and
	 * - |Sj| <= 1 or conjunction_l\inSi(Bel(l)) \wedge conjunction_j(GO(Sj)) is consistent
	 * So in particular, to detect stability in the case when all Sj are stable and |Sj| > 1, one needs to wait until GO(Sj) is known
	 * for all Sj (but since GO(Sk) is set to \bot before being known, we can just check the above condition).
	 */
	public boolean checkAndUpdateStabilityStatus (StronglyConnectedComponent scc, int currentStep) {
		assert !scc.stableStatusKnown();
		
		Formula form;
		Set<StronglyConnectedComponent> parentSet = this.getParents(scc);
		
		// First, we check if all parents of scc are stable, which is a necessary condition to know stability
		// parentSet is a set of SCCs that are the parents of the input scc (i.e., the set Sj)
		// parentSet may be empty, the following will still work
		for (StronglyConnectedComponent parent : parentSet) {
			if (!parent.isStable()) {
				return false;
			}
		}
		// At this stage, the set Sj of all parents of scc are stable.
		// If |Sj| <= 1, we have sufficient conditions for declaring scc to be stable
		if (parentSet.size() <= 1) {
			scc.setToStable(currentStep);
			return true;
		}
		// At this stage, the set Sj of all parents of scc are stable and |Sj| > 1.
		// We need to check if conjunction_l\inSi(Bel(l)) \wedge conjunction_j(GO(Sj)) is consistent,
		// which is a sufficient condition to declare scc to be stable
		form = getConjunctionGOParents(scc);
		form.conjunctionWith(scc.getConjunction());
		if (!form.isConsistent()) {
			return false;
		}
		
		// All sufficient conditions have been reached to declare scc stable
		scc.setToStable(currentStep);
		return true;
	}
	
	/*
     * This method checks whether an SCC given as parameter changes its stability state from unknown to unstable.
     * Returns true iff there is a change to unstable.
     * Note: this method should be called only when the stability status of the SCC in parameter is still unknown
     * 
	 * An SCC Si is detected unstable at step k if:
	 * - there exists an unstable parent SCC Sj, or
	 * - (|Sj| >= 1 and all parent Sj are not unstable) and conjunction_j(GOUP(Sj)) is inconsistent 
	 */
	public boolean checkAndUpdateUnstabilityStatus (StronglyConnectedComponent scc, int currentStep) {
		assert !scc.stableStatusKnown();
		
		Set<StronglyConnectedComponent> parentSet = this.getParents(scc);
		
		// if parentSet is empty, the scc should be stable, so it cannot be unstable
		if (parentSet.isEmpty()) {
			return false;
		}
		
		
		// Then, we check if there exists a parent of scc which is unstable, which is a sufficient condition to know unstability
		// parentSet is a set of SCCs that are the parents of the input scc (i.e., the set Sj)
		// parentSet may be empty, the following will still work
		for (StronglyConnectedComponent parent : parentSet) {
			if (parent.isUnstable()) {
				scc.setToUnstable(currentStep);
				return true;
			}
		}
		
		// At this stage, the parentSet is non-empty,
		// and we did not find a parent of scc that is unstable (they may be stable or unknown, it does not matter).
		// We need to check if conjunction_j(GOUPIfStable(Sj)) is inconsistent,
		// which is a sufficient condition to declare scc to be unstable,
		// where GOUPIfStable(Sj) is defined as
		// - GOUPIfStable(Sj) = GO(Sj) = GOUP(Sj) if Sj is stable, 
		// - otherwise Sj is unknown and GOUPIfStable(Sj) = conjunction(GOUPIfStable(Sk)) for all parents Sk of Sj
		if (!getConjunctionGOUPIfStableParents(scc).isConsistent()) {
			scc.setToUnstable(currentStep);
			return true;
		}
		
		// Here, getConjunctionGOUPParents is consistent, so we do not know if scc is unstable
		return false;
	}
    
	/**
	 * The main algorithm that iterates over all SCC to check their stability / unstability conditions,
	 * and update their final global outcomes (GO(Si)) if found, and global outcomes upper bounds (GOUP(Si)) if necessary.
	 * We browse each SCC in topological order in the DAG. We repeat the process until no effective update has occurred.
	 * Return true iff all SCCs are stable and all agents in all SCCs have reached a stable state, otherwise returns false.
	 * 
	 * The algorithm works as follows.
	 * We browse all SCCs in a topological order and perform a check for each SCC. If each check results in a non-change then we stop,
	 * otherwise we will retirate again over all SCCs in topological order.
	 * A check for an SCC consists in what follows, in order:
	 * 1) check whether it changes to stable status
	 * 2) if still unknown, check whether it changes to unstable status
	 * 3) if scc is stable (whether it *changed* to stable status or not at this step), and if GO(scc) is \bot, we do the following:
	 *    GO(scc) should be updated to conjunction_l\inSi(Bel(l)) \wedge conjunction_j(GO(Sj)) if consistent.
	 *    And in this case, update GOUP(scc) to GOUP(scc) = GO(scc).
	 * 4) if GO(scc) is inconsistent, update GOUP(scc) as follows.
	 *    GOUP(Si) = disjunction_l\inscc(l) if |Sj| = 0, otherwise
	 *    GOUP(Si) = disjunction_j(GOUP(Sj))
	 * 5) check for each agent in the graph if the agent has actually reached a stable state, and update when necessary.
	 * 6) update the GOUPIfStable of the SCC: it is defined as:
	 *    - GO(Si) is consistent
	 *    - otherwise, if |Sj| = 0, GOUP(Si)
	 *    - otherwise, conjunction(GROUPIfStable(Sj))
	 * 
	 * After no change has been made and the method ends, one should call another method,
	 * checkAndUpdateDAGStabilityStatusesAndOutcomes, to update the stability / outcome variables of the DAG (see next method)
	 */
	public boolean checkAndUpdateStabilityStatusesAndOutcomes (int currentStep) {
		Vector<StronglyConnectedComponent> allSCCs = getStronglyConnectedComponentsAsTopologicalOrder();
		boolean hasChanged = true;
		Formula form;
		
		Global.log.addToMessageln("===== checking stable / outcomes for all SCCs =====");
		
		while (hasChanged) {
			Global.log.addToMessageln("looping...");
			for (StronglyConnectedComponent scc : allSCCs) {
				hasChanged = false;
				// step 1)
				if (!scc.stableStatusKnown()) {
					hasChanged = checkAndUpdateStabilityStatus(scc, currentStep);
					if (hasChanged) {
						Global.log.addToMessageln("--> " + scc + ": detected stable");
					}
				}
				// step 2)
				if (!scc.stableStatusKnown()) {
					hasChanged = checkAndUpdateUnstabilityStatus(scc, currentStep);
					if (hasChanged) {
						Global.log.addToMessageln("--> " + scc + ": detected UNstable");
					}
				}
				// step 3)
				if (scc.isStable() && !scc.getGlobalOutcomeFinal().isConsistent()) {
					form = getConjunctionGOParents(scc);
					form.conjunctionWith(scc.getConjunction());
					if (form.isConsistent()) {
						scc.setGlobalOutcomeFinal(form, currentStep);
						// in the method above, GOUP is updated as well, since scc.setGlobalOutcomeUpperBound(form) is called
						Global.log.addToMessageln("--> " + scc + ": GO updated to " + scc.getGlobalOutcomeFinal());
						Global.log.addToMessageln("--> " + scc + ": GOUP updated to " + scc.getGlobalOutcomeUpperBound());
						Global.log.addToMessageln("--> " + scc + ": GOUPIfStable updated to " + scc.getGlobalOutcomeUpperBoundIfStable());
						hasChanged = true;
					}
				}
				// step 4)
				if (!scc.getGlobalOutcomeFinal().isConsistent()) {
					if (this.getParents(scc).size() == 0) {
						if (scc.setGlobalOutcomeUpperBound(scc.getDisjunction(), currentStep)) {
							Global.log.addToMessageln("--> " + scc + ": GOUP updated to " + scc.getGlobalOutcomeUpperBound());
							hasChanged = true;
						}
					}
					else {
						if (scc.setGlobalOutcomeUpperBound(this.getDisjunctionGOUPParents(scc), currentStep)) {
							Global.log.addToMessageln("--> " + scc + ": GOUP updated to " + scc.getGlobalOutcomeUpperBound());
							hasChanged = true;
						}
					}
				}
				// step 5)
				if (scc.updateStableStateActuallyReached(currentStep)) {
					hasChanged = true;
					if (scc.allAgentsHaveReachedStability()) {
						Global.log.addToMessageln("--> " + scc + ": stable state actually reached");
					}
				}
				// step 6)
				if (scc.getGlobalOutcomeFinal().isConsistent()) {
					// Here, GOUPIfFinal should already be set to GO
					assert scc.getGlobalOutcomeUpperBoundIfStable().equiv(scc.getGlobalOutcomeFinal());
				}
				else {
					if (this.getParents(scc).size() == 0) {
						if (scc.setGlobalOutcomeUpperBoundIfStable(scc.getGlobalOutcomeUpperBound(), currentStep)) {							
							hasChanged = true;
						}
					}
					else {
						if (scc.setGlobalOutcomeUpperBoundIfStable(this.getConjunctionGOUPIfStableParents(scc), currentStep)) {
							hasChanged = true;
						}
					}
					if (hasChanged) {
						Global.log.addToMessageln("--> " + scc + ": GOUPIfStable updated to " + scc.getGlobalOutcomeUpperBoundIfStable());
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * A method called after checkAndUpdateStabilityStatusesAndOutcomes, that updates variables on the global level
	 * 1) stableStatusFlag, initialized to -1, >=0 (the detected step number) iff all SCCs have a stable status
	 * 2) unstableStatusFlag, initialized to -1, >=0 (the detected step number) iff there is an SCC having an unstable status
	 * 3) allAgentsHaveReachedStabilityStepNumber, initialized to -1, >=0 iff all SCCs have a stable status, and corresponds to
	 *    the actual step number ahere all agents have reached a stable state
	 * 4) consensus, initialized to \bot, becomes conjunction_allSCC-Si(GO(Si)) if consistent, in which case that will be the final,
	 *    consistent consensus. It may remain inconsistent but still at some point become 'known', i.e.,
	 *    conjunction_allSCC-Si(GOUP(Si)) is inconsistent. In this case the consensus is guaranteed to be inconsistent.
	 * 5) consensusDetected, initialized to -1, may become to >=0 if characterized at some step as described above
	 *    (can be consistent or inconsistent)
	 * 6) globalOutcomeUpperBound, initialized to \top, is updated very time to disjunction(GOUP(Si)) for all SCCs Si
	 * 7) globalOutcomeUpperBoundLastChangeStep, initialized to -1, is updated every time globalOutcomeUpperBound is updated
	 * 8) update disjunctionOfAll and step if necessary 
	 */
	public boolean checkAndUpdateDAGStabilityStatusesAndOutcomes (int currentStep) {
		int step;
		Formula form;
		boolean unstabilityDetected;
		boolean stableStateReached;
		
		Global.log.addToMessageln("===== checking stable / outcomes for the SCC DAG =====");
		
		// variable 1), stableStatusFlag
		// if this DAG has not been detected stable (yet), check if all SCC are stable and update stableStatusFlag if necessary.
		// In this case, stableStatusFlag is simply equal to min_Si(stableStatusFlag(Si))
		if (this.stableStatusFlag == -1) {
			step = Integer.MAX_VALUE;
			for (StronglyConnectedComponent scc : this.vertexSet()) {
				if (scc.getStabilityStatusFlag() < step) {
					step = scc.getStabilityStatusFlag();
				}
			}
			this.stableStatusFlag = step;
			if (this.stableStatusFlag >= 0) {
				Global.log.addToMessageln("--> DAG detected stable");
				assert this.stableStatusFlag == currentStep;
			}
		}
		
		// variable 2), unstableStatusFlag
		// if this DAG has not been detected unstable (yet), check there is an unstable SCC update unstableStatusFlag if necessary.
		// In this case, unstableStatusFlag is simply equal to min_Si(unstableStatusFlag(Si)) *for those unstable Si*
		if (this.unstableStatusFlag == -1) {
			unstabilityDetected = false;
			step = Integer.MAX_VALUE;
			for (StronglyConnectedComponent scc : this.vertexSet()) {
				if (scc.isUnstable() && (scc.getUnstabilityStatusFlag() < step)) {
					unstabilityDetected = true;
					step = scc.getUnstabilityStatusFlag();
				}
			}
			if (unstabilityDetected) {
				this.unstableStatusFlag = step;
				Global.log.addToMessageln("--> DAG detected unstable");
				assert this.unstableStatusFlag == currentStep;
			}
		}
		
		// variable 3) allAgentsHaveReachedStabilityStepNumber
		// an update may be necessary only when the DAG is detected stable and the actual stability state has not been reached yet
		if (this.stableStatusFlag >= 0 && this.allAgentsHaveReachedStabilityStepNumber == -1) {
			stableStateReached = true;
			step = -1;
			for (StronglyConnectedComponent scc : this.vertexSet()) {
				if (!scc.allAgentsHaveReachedStability()) {
					stableStateReached = false;
					break;
				}
				else {
					if (step < scc.getStabilityReachedStepNumber()) {
						step = scc.getStabilityReachedStepNumber();
					}
				}
			}
			if (stableStateReached) {
				this.allAgentsHaveReachedStabilityStepNumber = step;
				Global.log.addToMessageln("--> DAG stability reached");
				assert this.allAgentsHaveReachedStabilityStepNumber == currentStep;
			}
		}
		
		// variables 4) consensus and 5) consensusDetected, may update if not detected yet (in which case,
		// consensus should be \bot). Indeed, once it is detected and becomes consistent, we know it will be the final consistent consensus
		// So, consensus is changed to conjunction_allSCC-Si(GO(Si)) if consistent.
		// If not consistent, we also check if consensus is known to be inconsistent at last, which is the case when
		// conjunction_allSCC-Si(GOUP(Si)) is inconsistent
		if (consensusDetected == -1) {
			assert !consensus.isConsistent();
			// Here, we check if a consistent consensus is obtained
			form = Formula.getTautology();
			for (StronglyConnectedComponent scc : this.vertexSet()) {
				form.conjunctionWith(scc.getGlobalOutcomeFinal());
				if (!form.isConsistent()) {
					break;
				}
			}
			if (form.isConsistent()) {
				this.consensus = new Formula(form);
				Global.log.addToMessageln("--> consensus detected (consistent): " + this.consensus);
				this.consensusDetected = currentStep;
			}
			else {
				// Here, we check if an inconsistent consensus is guaranteed
				form = Formula.getTautology();
				for (StronglyConnectedComponent scc : this.vertexSet()) {
					form.conjunctionWith(scc.getGlobalOutcomeUpperBound());
					if (!form.isConsistent()) {
						break;
					}
				}
				if (!form.isConsistent()) {
					// no need to update the consensus since it is already \bot
					Global.log.addToMessageln("--> consensus detected (INconsistent)");
					this.consensusDetected = currentStep;
				} 
			}
		}
		
		// 6) globalOutcomeUpperBound, updated in any case to disjunction(GOUP(Si)) for all SCCs Si
		form = Formula.getInconsistentFormula();
		for (StronglyConnectedComponent scc : this.vertexSet()) {
			//System.out.println(scc.getGlobalOutcomeUpperBound());
			form.disjunctionWith(scc.getGlobalOutcomeUpperBound());
		}
		assert form.infers(this.globalOutcomeUpperBound);
		//System.out.println("disjunction of all = " + form.toString() + ", current GOUP = " + this.globalOutcomeUpperBound);
		if (!this.globalOutcomeUpperBound.infers(form)) {
			//System.out.println("disjunction of all = " + form.toString() + ", current GOUP = " + this.globalOutcomeUpperBound);
			//System.out.println("current GOUP does not infer disjunction of all, so it CHANGED at step " + currentStep + "!");
			this.globalOutcomeUpperBound = new Formula(form);
			this.globalOutcomeUpperBoundLastChangeStep = currentStep;
			//System.out.println("--> disjunction of all = " + form.toString() + ", current GOUP = " + this.globalOutcomeUpperBound);
			Global.log.addToMessageln("--> DAG GOUP updated to " + this.globalOutcomeUpperBound);
		}
		else {
			//System.out.println("current GOUP infers disjunction of all, so no change at step " + currentStep + "...");
		}
		
		// 8) disjunctionOfAll
		form = Formula.getInconsistentFormula();
		for (StronglyConnectedComponent scc : this.vertexSet()) {
			form.disjunctionWith(scc.getDisjunction());
		}
		if (!this.disjunctionOfAlls.infers(form)) {
			// update last step change
			disjunctionOfAlls = new Formula(form);
			disjunctionOfAllLastChangeStep = currentStep;
			//System.out.println("last step change: " + disjunctionOfAllLastChangeStep);
		}
		
		
		return this.allAgentsHaveReachedStabilityStepNumber >= 0;
	}
	
	public static void main (String[] args) {
		DAGSCCGraph dag = new DAGSCCGraph(MyEdge.class);
	}

}
