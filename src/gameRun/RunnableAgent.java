package gameRun;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import belief.Formula;
import belief.OCF;
import graph.AbstractAgent;
import graph.UIAgent;
import parameters.Global;

// We story the OCF history for each agent in a given run
// The history is stored as an list of pairs : <i, OCF_i> ordered by increasing i where i is the position in the game
// Hence, <0, OCF_0> must be in the list initially and OCF_0 is the OCF of the agent before running the game
// There is not necessary a pair <i, OCF_i> for each integer (there may be holes), this means that in those i the agent did not
// update her OCF
// Additionally
public class RunnableAgent extends AbstractAgent {
	
	private List<Entry<Integer, OCF>> history;
	
	/**
	 * Recall that:
	 * - An agent is stable at step i =(def) in all future runs from i, its Bel will never change
	 * - An agent is unstable at step i =(def) in all future runs from any j>=i, its Bel will always change at some k>j
	 * - An agent is unknown-stability at step i =(def) the remaining cases, i.e., it is neither stable not unstable at step i
	 * So, we will have 2 flags about the stability status of an agent: stableStatusFlag and unstableStatusFlag.
	 * Both these flags start with a value of -1. When one of these flags become >=0, then they do not change anymore.
	 * The value remains both at -1 when stability is unknown,
	 * it becomes equal to i>=0 when stability or unstability is detected at step i.
	 * Note : when an agent becomes stable (stableStatusFlag>=0), it does not mean that it has reached
	 * a stable state, it only means that we *know* that it will eventually reach a stable state
	 */
	private int stableStatusFlag;
	private int unstableStatusFlag;
	
	// >=0 if and only if the agent has reached an actual stable state
	// In particular, this is >=0 only if stableStatusFlag>=0 
	private int stabilityReachedStepNumber;
	
	/**
	 * This should always be the same as globalOutcomeFinal as the agent's SCC
	 */
	private Formula globalOutcomeFinal;
	
	/**
	 * This stores the (only) step where the GO of the agent has been decided
	 */
	private int globalOutcomeFinalChangeStep;
	
	/**
	 * This should always be the same as globalOutcomeUpperBound as the agent's SCC
	 */
	private Formula globalOutcomeUpperBound;
	
	/**
	 * This stores the last step where the GOUP of the agent has changed
	 */
	private int globalOutcomeUpperBoundLastChangeStep;
	
	/**
	 * An agent has reached an actual stable state iff
	 * - its SCC is stable, and
	 * - its belief implies conjunction_l\inSi(Bel(l)) \wedge conjunction_j(GO(Sj)).
	 * By design, it should be enough to check if its belief is equivalent to its GO(Si)
	 */
	private boolean hasReachedStableState;
	
	/**
	 * This additional variable is useful to have more information to detect if an agent / SCC is unstable.
	 * It will be used by its children, so that children may be detected even if this agent's stability stats is unknown
	 * Indeed, if an agent / SCC stability status is unknown, then if it will become stable at some point, its beliefs will
	 * necessarily entail the conjunction of GOUPIfStable of all its parents; otherwise this means it will be unstable
	 * It is defined as:
	 * - GO(Si) is consistent
	 * - otherwise, if |Sj| = 0, GOUP(Si)
	 * - otherwise, conjunction(GROUPIfStable(Sj))
	 */
	private Formula globalOutcomeUpperBoundIfStable;
	
	/**
	 * This stores the last step where the GOUPIfStable of the agent has changed
	 */
	private int globalOutcomeUpperBoundIfStableLastChangeStep;
	
	public RunnableAgent (UIAgent ag) {
		super(ag);
		this.history = new ArrayList<Entry<Integer, OCF>>();
		// set the initial step
		Entry<Integer, OCF> initialEntry = Map.entry(0, ag.getOCF());
		history.add(initialEntry);
		this.stableStatusFlag = -1;
		this.unstableStatusFlag = -1;
		this.stabilityReachedStepNumber = -1;
		this.hasReachedStableState = false;
		this.globalOutcomeFinal = Formula.getInconsistentFormula();
		this.globalOutcomeFinalChangeStep = -1;
		this.globalOutcomeUpperBound = Formula.getTautology();
		this.globalOutcomeUpperBoundLastChangeStep = -1;
		this.globalOutcomeUpperBoundIfStable = Formula.getTautology();
		this.globalOutcomeUpperBoundIfStableLastChangeStep = -1;
	}
	
	public void setToStable (int step) {
		assert step >= 0;
		assert stableStatusFlag == -1;
		assert unstableStatusFlag == -1;
		this.stableStatusFlag = step;
	}
	
	public void setToUnstable (int step) {
		assert step >= 0;
		assert stableStatusFlag == -1;
		assert unstableStatusFlag == -1;
		this.unstableStatusFlag = step;
	}
	
	public int getStabilityStatusFlag () {
		return this.stableStatusFlag;
	}
	
	public int getUntabilityStatusFlag () {
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
	
	public int getstabilityReachedStepNumber () {
		return this.stabilityReachedStepNumber;
	}
	
	public void setStabilityReachedStepNumber (int stabilityReachedStepNumber) {
		assert this.stabilityReachedStepNumber == -1;
		this.stabilityReachedStepNumber = stabilityReachedStepNumber;
	}
	
	public Formula getGlobalOutcomeFinal () {
		return this.globalOutcomeFinal;
	}
	
	public void setGlobalOutcomeFinal (Formula form, int step) {
		assert !this.globalOutcomeFinal.isConsistent();
		this.globalOutcomeFinal = new Formula(form);
		this.globalOutcomeFinalChangeStep = step;
		this.setGlobalOutcomeUpperBound(form, step);
		this.setGlobalOutcomeUpperBoundIfStable(form, step);
	}
	
	public Formula getGlobalOutcomeUpperBound () {
		return this.globalOutcomeUpperBound;
	}
	
	/**
	 * returns true iff the agent's GOUP has changed
	 */
	public boolean setGlobalOutcomeUpperBound (Formula form, int step) {
		assert form.infers(this.globalOutcomeUpperBound);
		if (this.globalOutcomeUpperBound.infers(form)) {
			return false;
		}
		this.globalOutcomeUpperBound = new Formula(form);
		this.globalOutcomeUpperBoundLastChangeStep = step;
		return true;
	}
	
	/**
	 * returns true iff the agent's GOUPIfStable has changed
	 */
	public boolean setGlobalOutcomeUpperBoundIfStable (Formula form, int step) {
		assert form.infers(this.globalOutcomeUpperBoundIfStable);
		if (this.globalOutcomeUpperBoundIfStable.infers(form)) {
			return false;
		}
		this.globalOutcomeUpperBoundIfStable = new Formula(form);
		this.globalOutcomeUpperBoundIfStableLastChangeStep = step;
		return true;
	}
	
	public Formula getGlobalOutcomeUpperBoundIfStable () {
		return this.globalOutcomeUpperBoundIfStable;
	}
	
	public int getGlobalOutcomeUpperBoundLastChangeStep () {
		return this.globalOutcomeUpperBoundLastChangeStep;
	}
	
	public int getGlobalOutcomeFinalChangeStep () {
		return this.globalOutcomeFinalChangeStep;
	}
	
	public int getGlobalOutcomeUpperBoundIfStableLastChangeStep () {
		return this.globalOutcomeUpperBoundIfStableLastChangeStep;
	}
	
	public boolean getHasReachedStableState () {
		return this.hasReachedStableState;
	}
	
	public void setToHasReachedStableState (int step) {
		assert !this.hasReachedStableState;
		assert this.stabilityReachedStepNumber == -1;
		this.hasReachedStableState = true;
		this.stabilityReachedStepNumber = step;
	}
	
	/**
	 * check whether the agent has actually reached a stable state, i.e.,
	 * if its current beliefs is equivalent to its GO (there is no update here)
	 * Returns true if it has reached a stable state
	 */
	public boolean checkActuallyReachedStableState () {
		return this.getLastOCF().isEquivalentTo(this.globalOutcomeFinal);
	}
	
	public List<Entry<Integer, OCF>> getHistory () {
		return this.history;
	}
	
	public OCF getInitialOCF () {
		return history.get(0).getValue();
	}
	
	public OCF getLastOCF () {
		return history.get(history.size() - 1).getValue();
	}
	
	public OCF getOCF (int position) {
		Iterator<Entry<Integer, OCF>> it = history.iterator();
		Entry<Integer, OCF> currentResult = it.next();
		Entry<Integer, OCF> nextEntry;
		int nextStepEntry;
		
		while (it.hasNext()) {
			nextEntry = it.next();
			nextStepEntry = nextEntry.getKey();
			if (nextStepEntry > position) {
				return currentResult.getValue();
			}
			currentResult = nextEntry;
		}
		
		return currentResult.getValue();
	}
	
	public void addEntryToHistory (int position, OCF ocf) {
		history.add(Map.entry(position, ocf));
	}
	
	public String toString() {
		return "RunnableAgent " + getId() + " (initial belief = " + this.getOCF(0).getBeliefs() + ")";
	}
	
	public static void main(String[] args) {
		Global.initParameters();
		RunnableAgent agentHistory = new RunnableAgent(new UIAgent(1));
		agentHistory.addEntryToHistory(3, new OCF());
		agentHistory.addEntryToHistory(5, new OCF());
		agentHistory.addEntryToHistory(6, new OCF());
		System.out.println(agentHistory.getHistory());
		System.out.println(agentHistory.getInitialOCF());
		
		for (int position = 0; position < 10; position++) {
			System.out.println("Position " + position + ": " + agentHistory.getOCF(position));
		}
	}
}
