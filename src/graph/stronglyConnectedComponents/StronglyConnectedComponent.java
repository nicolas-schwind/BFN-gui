package graph.stronglyConnectedComponents;

import java.util.Collection;
import java.util.Set;
import java.util.Vector;

import belief.Formula;
import gameRun.RunnableAgent;
import graph.AbstractAgent;
import parameters.Global;

// This class is intended to be used in a dynamic setting (within a particular run),
// with attributes such as evolving stability status and global outcome
public class StronglyConnectedComponent {
	/**
	 * Recall that:
	 * - An agent is stable at step i =(def) in all future runs from i, its Bel will never change
	 * - An agent is unstable at step i =(def) in all future runs from any j>=i, its Bel will always change at some k>j
	 * - An agent is unknown-stability at step i =(def) the remaining cases, i.e., it is neither stable not unstable at step i
	 * Extension of these properties to SCCs:
	 * - An SCC is stable iff all its agents are stable
	 * - An SCC is unstable iff all its agents are unstable
	 * So, we will have 2 flags about the stability status of an SCC: stableStatusFlag and unstableStatusFlag.
	 * Both these flags start with a value of -1. When one of these flags become >=0, then they do not change anymore.
	 * The value remains both at -1 when stability is unknown,
	 * it becomes equal to i>=0 when stability or unstability is detected at step i.
	 * Note : when an SCC becomes stable (stableStatusFlag>=0), it does not mean that the agents from the SCC have reached
	 * a stable state, it only means that we *know* that all agents will eventually reach a stable state
	 */
	private int stableStatusFlag;
	private int unstableStatusFlag;
	
	/** 
	 * An identifier for the SCC
	 * This identifier is designed to be unique for the underlying agent graph
	 */
	private int id;
	
	/**
	 *  The set of agents in the SCC
	 */
	private Set<RunnableAgent> agentSet;
	
	/**
	 * The final global outcome of an SCC Si, denoted by GO(Si), is defined as
	 * the disjunction of all future beliefs of all agents in the SCC.
	 * More formally, GO(Si) is the disjunction of the GO(l) for all l in Si.
	 * And for each agent l, GO(l) is the disjunction of the beliefs of l at the limit, i.e., (to define precisely).
	 * In particular, GO(Si) is useful to detect stability for the children of Si
	 * Initially, one sets GO(Si) = \bot and we change it to some consistent formula if/as soon as GO(Si) is known
	 * GO(Si) will be known iff Si is detected stable. Indeed, we cannot know GO(Si) if Si is detected unstable
	 * or if we do not know whether Si is stable or unstable. So, if Si is detected stable,
	 * then GO(Si) can be determined as follows:
	 * - if |Sj| = 0 (Si has no parent), then GO(Si) = conjunction_l\inSi(l) as soon as consistent
	 * - if |Sj| >= 1, then (all Sj are stable and) GO(Si) = conjunction_l\inSi(l) \wedge conjunction_j(GO(Sj)) as soon as consistent
	 */
	private Formula globalOutcomeFinal;
	
	/**
	 * This stores the (only) step where the GO of the SCC has been decided
	 */
	private int globalOutcomeFinalChangeStep;
	
	/**
	 * The upper bound for the global outcome of an SCC, denoted by GOUP(Si), is an upper bound for the actual GO(Si).
	 * In particular, GOUP(Si) is useful to propagate GOUP(Sk) for children Sk of Si and detect unstability of Sk.
	 * GOUP(Si) is set to the disjunction of GOUP(Sj) for all parents Sj if |Sj| >= 1, otherwise it is the disjunction of
	 * all agents l in Si.
	 * And if/when GO(Si) is known, then GOUP(Si) should be set to GOUP(Si) = GO(Si)
	 */
	private Formula globalOutcomeUpperBound;
	
	/**
	 * This stores the last step where the GO of the SCC has changed
	 */
	private int globalOutcomeUpperBoundLastChangeStep;
	
	/**
	 * This additional variable is useful to have more information to detect if an agent / SCC is unstable.
	 * It will be used by its children, so that children may be detected even if this agent's stability stats is unknown
	 * Indeed, if an agent / SCC stability status is unknown, then if it will become stable at some point, its beliefs will
	 * necessarily entail the conjunction of GOUPIfStable of all its parents; otherwise this means it will be unstable
	 * It is defined as:
	 * - GO(Si) is consistent
	 * - otherwise, if |Sj| = 0, GOUP(Si)
	 * - otherwise, conjunction(GOUPIfStable(Sj))
	 */
	private Formula globalOutcomeUpperBoundIfStable;
	
	/**
	 * This stores the last step where the GOUPIfStable of the SCC has changed
	 */
	private int globalOutcomeUpperBoundIfStableLastChangeStep;
	
	public StronglyConnectedComponent (int id, Set<RunnableAgent> agentSet) {
		this.id = id;
		this.agentSet = agentSet;
		this.stableStatusFlag = -1;
		this.unstableStatusFlag = -1;
		this.globalOutcomeFinal = Formula.getInconsistentFormula();
		this.globalOutcomeFinalChangeStep = -1;
		this.globalOutcomeUpperBound = Formula.getTautology();
		this.globalOutcomeUpperBoundLastChangeStep = -1;
		this.globalOutcomeUpperBoundIfStable = Formula.getTautology();
		this.globalOutcomeUpperBoundIfStableLastChangeStep = -1;
	}
	
	public int getId () {
		return this.id;
	}
	
	public Set<RunnableAgent> getAgentSet () {
		return this.agentSet;
	}
	
	public int getSize () {
		return this.agentSet.size();
	}
	
	public boolean containsAgent (AbstractAgent agent) {
		for (AbstractAgent ag : this.agentSet) {
			if (ag.getId() == agent.getId()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsAgentId (int agentId) {
		for (AbstractAgent ag : this.agentSet) {
			if (ag.getId() == agentId) {
				return true;
			}
		}
		return false;
	}
	
	public void setToStable (int step) {
		assert step >= 0;
		assert stableStatusFlag == -1;
		assert unstableStatusFlag == -1;
		this.stableStatusFlag = step;
		
		// all agents from this SCC become stable status
		for (RunnableAgent ag : this.agentSet) {
			ag.setToStable(step);
		}
	}
	
	public void setToUnstable (int step) {
		assert step >= 0;
		assert stableStatusFlag == -1;
		assert unstableStatusFlag == -1;
		this.unstableStatusFlag = step;
		
		// all agents from this SCC become unstable status
		for (RunnableAgent ag : this.agentSet) {
			ag.setToUnstable(step);
		}
	}
	
	public int getStabilityStatusFlag () {
		return this.stableStatusFlag;
	}
	
	public int getUnstabilityStatusFlag () {
		return this.unstableStatusFlag;
	}
	
	public boolean isStable () {
		return this.stableStatusFlag >= 0;
	}
	
	public boolean isUnstable () {
		return this.unstableStatusFlag >= 0;
	}
	
	public boolean stableStatusKnown () {
		return isStable() || isUnstable();
	}
	
	/**
	 * update the actual stability reached status of all agents in the SCC when relevant.
	 * An agent updates its 'actual stability reached status' from no to yes when it was
	 * previously not the case (!ag.getHasReachedStableState()) and it has become the case this step
	 * ag.checkActuallyReachedStableState(), which is true when its last OCF's Bel is equivalent to its final outcome
	 * (ag.globalOutcomeFinal)
	 * Returns true if there was a change
	 */
	public boolean updateStableStateActuallyReached (int step) {
		boolean hasChanged = false;
		
		for (RunnableAgent ag : this.agentSet) {
			if (!ag.getHasReachedStableState()) {
				if (ag.checkActuallyReachedStableState()) {
					hasChanged = true;
					ag.setToHasReachedStableState(step);
					Global.log.addToMessageln("----> agent " + ag + ": stable state actually reached");
				}
			}
		}
		
		return hasChanged;
	}
	
	/**
	 *  >=0 if and only if all agents from the SCC have reached an actual stable state, and corresponds to the max for all agents
	 *  of the step for which a stable step has been reached.
	 *  In particular, this is >=0 only if stableStatusFlag>=0
	 *  Note: an agent has reached an actual stable state iff
	 *  - its SCC is stable, and
	 *  - its belief implies conjunction_l\inSi(Bel(l)) \wedge conjunction_j(GO(Sj)). By design, it should be enough to
	 *    check if its belief is equivalent to its GO(Si)
	 */
	public int getStabilityReachedStepNumber () {
		int maxStep = -1;
		if (!this.allAgentsHaveReachedStability()) {
			return -1;
		}
		for (RunnableAgent ag : this.agentSet) {
			if (ag.getstabilityReachedStepNumber() > maxStep) {
				maxStep = ag.getstabilityReachedStepNumber();
			}
		}
		return maxStep;
	}
	
	public boolean allAgentsHaveReachedStability () {
		for (RunnableAgent ag : this.agentSet) {
			if (!ag.getHasReachedStableState()) {
				return false;
			}
		}
		
		return true;
	}
	
	public Formula getGlobalOutcomeFinal () {
		return this.globalOutcomeFinal;
	}
	
	public void setGlobalOutcomeFinal (Formula form, int step) {
		assert !this.globalOutcomeFinal.isConsistent();
		this.globalOutcomeFinal = new Formula(form);
		this.globalOutcomeFinalChangeStep = step;
		// also update (if necessary) the GOUP and GOUPIfStable
		this.setGlobalOutcomeUpperBound(form, step);
		this.setGlobalOutcomeUpperBoundIfStable(form, step);
		
		// all agents from this SCC get that GO too
		for (RunnableAgent ag : this.agentSet) {
			ag.setGlobalOutcomeFinal(form, step);
		}
	}
	
	public Formula getGlobalOutcomeUpperBound () {
		return this.globalOutcomeUpperBound;
	}
	
	/**
	 * returns true iff the GOUP has changed
	 */
	public boolean setGlobalOutcomeUpperBound (Formula form, int step) {
		assert form.infers(this.globalOutcomeUpperBound);
		if (this.globalOutcomeUpperBound.infers(form)) {
			return false;
		}
		this.globalOutcomeUpperBound = new Formula(form);
		this.globalOutcomeUpperBoundLastChangeStep = step;
		
		// all agents from this SCC get that GOUP too
		for (RunnableAgent ag : this.agentSet) {
			ag.setGlobalOutcomeUpperBound(form, step);
		}
		return true;
	}
	
	
	public int getGlobalOutcomeUpperBoundLastChangeStep () {
		return this.globalOutcomeUpperBoundLastChangeStep;
	}
	
	/**
	 * returns true iff the SCC's GOUPIfStable has changed
	 */
	public boolean setGlobalOutcomeUpperBoundIfStable (Formula form, int step) {
		assert form.infers(this.globalOutcomeUpperBoundIfStable);
		if (this.globalOutcomeUpperBoundIfStable.infers(form)) {
			return false;
		}
		this.globalOutcomeUpperBoundIfStable = new Formula(form);
		this.globalOutcomeUpperBoundIfStableLastChangeStep = step;
		
		// all agents from this SCC get that GOUPIfStable too
		for (RunnableAgent ag : this.agentSet) {
			ag.setGlobalOutcomeUpperBoundIfStable(form, step);
		}
		return true;
	}
	
	public Formula getGlobalOutcomeUpperBoundIfStable () {
		return this.globalOutcomeUpperBoundIfStable;
	}
	
	public int getGlobalOutcomeFinalChangeStep () {
		return this.globalOutcomeFinalChangeStep;
	}
	
	public int getGlobalOutcomeUpperBoundIfStableLastChangeStep () {
		return this.globalOutcomeUpperBoundIfStableLastChangeStep;
	}
	
	/**
	 * returns the list of all beliefs of agents (for each agent, its last OCF) in this SCC as a collection 
	 */
	public Collection<Formula> getAllBeliefs () {
		Vector<Formula> result = new Vector<Formula>();
		
		for (RunnableAgent ag : this.agentSet) {
			result.add(ag.getLastOCF().getBeliefs());
		}
		
		return result;
	}
	
	/**
	 * returns the conjunction of all agents' last OCFs in this SCC 
	 */
	public Formula getConjunction () {
		return Formula.getConjunction(getAllBeliefs());
	}
	
	/**
	 * returns the disjunction of all agents' last OCFs in this SCC 
	 */
	public Formula getDisjunction () {
		return Formula.getDisjunction(getAllBeliefs());
	}
	
	public String toString () {
		StringBuffer result = new StringBuffer();
		result.append("SCC#" + this.getId() + "(");
		for (RunnableAgent ag : this.agentSet) {
			result.append(ag.getId() + ",");
		}
		result.deleteCharAt(result.length() - 1);
		result.append(")");
		return result.toString();
	}
	
}
