package belief;

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import parameters.Global;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

public class OCF {
	// each interpretation is an integer ranging from 0 to Global.nbInterpretations
	// an OCF is represented as an array with 16 entries, one for each interpretation integer,
	// mapping each interpretation with an integer
	public static int ENTRENCHMENT_LEVEL = 1;
	
	// this parameter should be between 0 and 100 inclusive
	private static final int PARAMETER_FOR_RANDOMIZE_BELIEF = 20;
	
	// all necessary data is given in the following table 
	private int[] ocf;
	
	public OCF () {
		this.ocf = new int[Global.nbInterpretations];
		// by default, the belief base is set to a randomized base, set by the parameter PARAMETER_FOR_RANDOMIZE_BELIEF
		initBeliefsRandomizeParam(PARAMETER_FOR_RANDOMIZE_BELIEF);
	}
	
	// creates a random 2-level ocf identified by the parameter (int between 0 and 2^nbInterpretations-1)
	public OCF (int[] ocf) {
		this();
		setOCF(ocf);
	}
	
	// creates a belief base which is equivalent as the belief base given as parameter
	public OCF (OCF ocf) {
		this();
		setOCF(ocf.getOCF());
	}
	
	// set the value of interpretation i in the OCF 
	public void setInterpretationValue (int i, int value) {
		assert value >= 0;
		ocf[i] = value;
	}
	
	// set the OCF
	public void setOCF (int[] ocf) {
		this.ocf = new int[ocf.length];
		for (int i = 0; i < ocf.length; i++) {
			this.ocf[i] = ocf[i];
		}
	}
	
	// get the value of interpretation i in the OCF
	public int getInterpretationValue (int i) {
		return ocf[i];
	}
	
	// returns the OCF
	public int[] getOCF () {
		return ocf;
	}
	
	// returns the set of interpretations (as a 'Formula') associated with the input value
	public Formula getInterpretations (int value) {
		Formula result = new Formula();
		for (int i = 0; i < Global.nbInterpretations; i++) {
			if (this.ocf[i] == value) {
				result.addSingleInterpretation(i);
			}
		}
		return result;
	}
	
	/*
	 * some initializations
	 */
	
	// initialize the (beliefs of the) OCF to the tautology
	public void initOCFTautology () {
		for (int i = 0; i < Global.nbInterpretations; i++) {
			ocf[i] = 0;
		}
	}
	
	// set the belief base to a randomized belief base
	// to do that, one just sets the value of each interpretation to 0 or ENTRENCHMNENT_LEVEL on a PARAMETER_FOR_RANDOMIZE_BELIEF% basis
	public void initBeliefsRandomize () {
		initBeliefsRandomizeParam(PARAMETER_FOR_RANDOMIZE_BELIEF);
	}
	
	// randomizes a belief with a parameter p between 1 and 100.
	// if p = 100 this is the tautology
	// if p is close to 0, then the belief will only have a few models.
	// if p is close to 100 then the belief will have many models.
	// there is a guarantee that there is at least one model to the belief
	// if p <= 99 there is a guarantee that the belief is not the tautology
	// Note: there are only two levels in the ocf, and the second level is set to the value ENTRENCHMENT_LEVEL
	public void initBeliefsRandomizeParam (int p) {
		Random rand = new Random();
		
		assert p > 0;
		
		if (p == 100) {
			initOCFTautology();
		}
		else {
			do {
				for (int i = 0; i < Global.nbInterpretations; i++) {
					if (rand.nextInt(100) + 1 <= p) {
						setInterpretationValue(i, 0);
					}
					else {
						setInterpretationValue(i, ENTRENCHMENT_LEVEL);
					}
				}
			}
			while (!isConsistent());
		}
	}
	
	// returns the beliefs as a Formula 
	public Formula getBeliefs () {
		Formula formula = new Formula();
		
		for (int i = 0; i < Global.nbInterpretations; i++) {
			if (getInterpretationValue(i) == 0) {
				formula.addSingleInterpretation(i);
			}
		}
		return formula;
	}
	
	// returns the (sorted) list of values for which at least one interpretation is associated with 
	public SortedSet<Integer> getImage () {
		SortedSet<Integer> result = new TreeSet<Integer>();
		for (int i = 0; i < Global.nbInterpretations; i++) {
			result.add(getInterpretationValue(i));
		}
		return result;
	}
	
	/* 
	 * The following methods are test concerning the beliefs of OCF, sometimes wrt another formula
	 * The other formula is always an OCF, although we only use its beliefs sometimes (e.g., infers, isConsistentWith, etc.)  
	 */
	
	// check whether the beliefs of the OCF is a consistent formula
	public boolean isConsistent () {
		for (int i = 0; i < Global.nbInterpretations; i++) {
			if (getInterpretationValue(i) == 0) {
				return true;
			}
		}
		return false;
	}
	
	// check whether the beliefs of the OCF is a valid formula
	public boolean isValid () {
		for (int i = 0; i < Global.nbInterpretations; i++) {
			if (getInterpretationValue(i) > 0) {
				return false;
			}
		}
		return true;
	}
	
	// check whether the beliefs of the OCF infers phi
	public boolean infers (Formula form) {
		for (int i = 0; i < Global.nbInterpretations; i++) {
			if (getInterpretationValue(i) == 0 && !form.isModel(i)) {
				return false;
			}
		}
		return true;
	}
	
	// check whether the beliefs of the OCF is inferred by phi
	public boolean isInferredBy (Formula form) {
		for (int i = 0; i < Global.nbInterpretations; i++) {
			if (getInterpretationValue(i) > 0 && form.isModel(i)) {
				return false;
			}
		}
		return true;
	}
	
	// check whether the beliefs of the OCF is consistent with phi
	public boolean isConsistentWith (Formula form) {
		for (int i = 0; i < Global.nbInterpretations; i++) {
			if (getInterpretationValue(i) == 0 && form.isModel(i)) {
				return true;
			}
		}
		return false;
	}
	
	// check whether the beliefs of the OCF is equivalent to phi
	public boolean isEquivalentTo (Formula form) {
		boolean isModel;
		for (int i = 0; i < Global.nbInterpretations; i++) {
			isModel = form.isModel(i);
			if ((getInterpretationValue(i) == 0 && !isModel)
					|| (getInterpretationValue(i) != 0 && isModel)) {
				return false;
			}
		}
		return true;
	}
	
	// check whether the input OCF given as parameter is the same as the current instance
	public boolean isSameAs (OCF phi) {
		for (int i = 0; i < Global.nbInterpretations; i++) {
			if (getInterpretationValue(i) != phi.getInterpretationValue(i)) {
				return false;
			}
		}
		return true;
	}
	
	// check whether the input OCF given as parameter and the current instance have the same preorder
	public boolean hasSamePreorderAs (OCF phi) {
		OCF flatenedOCF = new OCF(this);
		flatenedOCF.flaten();
		OCF flatenedPhi = new OCF(phi);
		flatenedPhi.flaten();
				
		return flatenedOCF.isSameAs(flatenedPhi);
	}
	
	/*
	 * some information according to the OCF and an input formula 
	 */
	
	// return the OCF value of the minimal elements of the input formula in the current OCF
	private int getMinOCFValue (Formula formula) {
		int result = Integer.MAX_VALUE;
		int currentOCFValue;
		for (int inter : formula.getSetOfInterpretations()) {
			currentOCFValue = this.getInterpretationValue(inter);
			if (currentOCFValue < result) {
				result = currentOCFValue;
			}
		}
		return result;
	}
	
	// return the OCF value of the maximal elements of the input formula in the current OCF
	private int getMaxOCFValue (Formula formula) {
		int result = Integer.MIN_VALUE;
		int currentOCFValue;
		for (int inter : formula.getSetOfInterpretations()) {
			currentOCFValue = this.getInterpretationValue(inter);
			if (currentOCFValue > result) {
				result = currentOCFValue;
			}
		}
		return result;
	}
	
	// return the minimal elements of the input formula in the current OCF, as a new formula
	public Formula getMin (Formula formula) {
		Formula result = new Formula();
		// search for the minimal OCF value
		int minOCFValue = getMinOCFValue(formula);
		// search for the formula corresponding to this min value
		for (int inter : formula.getSetOfInterpretations()) {
			if (this.getInterpretationValue(inter) == minOCFValue) {
				result.addSingleInterpretation(inter);
			}
		}
		return result; 
	}
	
	// return all interpretations of formula except its minimal elements in the current OCF, as a new formula
	public Formula getMinComplementary (Formula formula) {
		Formula result = new Formula();
		// search for the minimal OCF value
		int minOCFValue = getMinOCFValue(formula);
		// compute the resulting formula
		for (int inter : formula.getSetOfInterpretations()) {
			if (this.getInterpretationValue(inter) != minOCFValue) {
				result.addSingleInterpretation(inter);
			}
		}
		return result;
	}
	
	/*
	 * some change operations
	 */
	
	// normalize the OCF, i.e., shift all values down or up equally so that the lowest value is 0
	public void normalize() {
		// search for the min value
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < Global.nbInterpretations; i++) {
			if (min > getInterpretationValue(i)) {
				min = getInterpretationValue(i);
			}
		}
		
		// normalize
		for (int i = 0; i < Global.nbInterpretations; i++) {
			setInterpretationValue(i, getInterpretationValue(i) - min);
		}
	}
	
	// 'flaten' the OCF, i.e., change the OCF to the one having the same preorder and such that it is a surjection to [1..k] for some k 
	public void flaten() {
		SortedSet<Integer> image = this.getImage();
		int newLevel = 0;
		for (int value: image) {
			for (int i = 0; i < Global.nbInterpretations; i++) {
				if (getInterpretationValue(i) == value) {
					setInterpretationValue(i, newLevel);
				}
			}
			newLevel++;
		}
	}
	
	// nayak operation: moves down all models of the input formula just enough until nayak
	// note that boutilier is nayak on minimal models
	public void nayak (Formula formula) {
		int maxOCFValue = this.getMaxOCFValue(formula);
		this.shiftRelative(formula, - (maxOCFValue + 1));
	}
	
	// refinement operation: separates all models and counter-models of the input formula
	// to do that, multiply by 2 all values, then minus 1 to all models of the formula (then normalize)
	public void refinement (Formula formula) {
		this.multiplyBy(Formula.getTautology(), 2);
		this.shiftRelative(formula, -1);
	}
	
	// MAYBE THIS METHOD IS NOT NECESSARY?
	// move all min elements of the input formula in the OCF to -1 (then normalize, but it is done already in the inner method)  
	// this operation can be seen as a Boutilier revision operation on OCFs
	//private void boutilier (Formula formula) {
	//	shiftAbsolute(getMin(formula), -1);
	//}
	
	// shift up or down by a value some interpretations and then normalize
	public void shiftRelative (Formula formula, int value) {
		SortedSet<Integer> setOfInterpretations = formula.getSetOfInterpretations();
		for (Integer inter : setOfInterpretations) {
			setInterpretationValue(inter, getInterpretationValue(inter) + value);
		}
		normalize();
	}
	
	// move all interpretations to a certain value and then normalize
	public void shiftAbsolute (Formula formula, int value) {
		SortedSet<Integer> setOfInterpretations = formula.getSetOfInterpretations();
		for (Integer inter : setOfInterpretations) {
			setInterpretationValue(inter, value);
		}
		normalize();
	}
	
	// multiply all values of all models of a formula to a certain value (no need to normalize)
	public void multiplyBy (Formula formula, int value) {
		SortedSet<Integer> setOfInterpretations = formula.getSetOfInterpretations();
		for (Integer inter : setOfInterpretations) {
			setInterpretationValue(inter, getInterpretationValue(inter) * value);
		}
		normalize();
	}
	
	// move some interpretations to the lower level in the preorder (used for one improvement for example)
	// when there is no lower level, i.e., when the interpretation is already at level 0, create a new level -1
	// after all that, normalize, so all values are shifted to +1 if a new level was created
	// !!! this only works for flatened ocfs !!!
	public void moveToLowerLayer (Formula formula) {
		this.flaten();
		shiftRelative(formula, -1);
		/*
		SortedSet<Integer> setOfInterpretations = formula.getSetOfInterpretations();
		
		Iterator<Integer> iter = setOfInterpretations.iterator();
		Iterator<Integer> iterImage;
		int currentLevel, nextLevel;
		int interpretation;
		while (iter.hasNext()) {
			interpretation = iter.next();
			if (getInterpretationValue(interpretation) == 0) {
				setInterpretationValue(interpretation, -1);
			}
			else {
				// search for how much value we need to remove for this interpretation (i.e., how far is the lower level)
				iterImage = getImage().iterator();
				currentLevel = iterImage.next();
				nextLevel = iterImage.next();
				while (nextLevel != getInterpretationValue(interpretation)) {
					currentLevel = nextLevel;
					nextLevel = iterImage.next();
				}
				setInterpretationValue(interpretation, currentLevel);
			}
		}
		normalize();
		*/
	}
	
	// move some interpretations to a new, lower level (used for half improvement for example)
	public void moveAndCreateLowerLayerIfNecessary () {
		// TODO
	}
	
	/*
	 * generate some random OCFs
	 */
	
	public static OCF getPerfectFlatOCF () {
		OCF result = new OCF();
		result.initOCFTautology();
				
		return result;
	}
	
	public static OCF getPerfectSparseOCF () {
		OCF result = new OCF();
		result.initOCFTautology();
		Random ran = new Random();
		int shiftValue = ran.nextInt(3) + 1;
		boolean posOrNeg = ran.nextBoolean();
		Formula formula;   
		
		for (int i = 0; i < Global.nbInterpretations; i++) {
			formula = new Formula();
			formula.addSingleInterpretation(i);
			if (posOrNeg) {
				result.shiftRelative(formula, shiftValue);
			}
			else {
				result.shiftRelative(formula, -shiftValue);
			}
			shiftValue += ran.nextInt(3) + 1;
			posOrNeg = ran.nextBoolean();
		}
		
		return result;
	}
	
	// prints the OCF
	public String toString() {
		StringBuffer result = new StringBuffer();
		ListIterator<Integer> iter = new ArrayList<Integer>(getImage()).listIterator(getImage().size());
		int imageValue;
		while (iter.hasPrevious()) {
			imageValue = iter.previous();
			result = result.append(imageValue + " :");
			for (int i = 0; i < Global.nbInterpretations; i++) {
				if (getInterpretationValue(i) == imageValue) {
					result = result.append(" " + i);
				}
			}
			result.append("\n");
		}
		
		return new String(result.toString());
	}
	
	// gets a string for this OCF when saving into an xml file
	public String toStringForXMLSave() {
		StringBuffer result = new StringBuffer();
		for (int idWorld = 0; idWorld < Global.nbInterpretations - 1; idWorld++) {
			result.append(this.getInterpretationValue(idWorld) + " ");
		}
		result.append(this.getInterpretationValue(Global.nbInterpretations - 1));
		return result.toString();
	}
	
	// parse a string that is supposed to be in an XML file and convert if to an OCF
	public void setOCFFromStringInXMLFile (String ocfString) {
		String[] splittedOCFString = ocfString.split(" ");
		//int[] ocf = new int[Global.nbInterpretations];
		int[] ocf = new int[splittedOCFString.length];
		
		for (int i = 0; i < splittedOCFString.length; i++) {
			ocf[i] = Integer.valueOf(splittedOCFString[i]);
		}
		this.setOCF(ocf);
	}
	
	public static void main(String[] args) throws IOException {
		Global.nbInterpretations = 16;
		OCF ocf = new OCF();
		System.out.println(ocf.getBeliefs());
		System.out.println(ocf);
		int[] formulaAsInts = {0, 1, 2, 3};
		Formula form = new Formula(formulaAsInts);
		System.out.println("move down lower layer");
		ocf.moveToLowerLayer(form);
		System.out.println(ocf);
		System.out.println("shift + 1");
		ocf.shiftAbsolute(form, 1);
		System.out.println(ocf);
		System.out.println("shift - 5");
		ocf.shiftAbsolute(form, -5);
		System.out.println(ocf);
		System.out.println("flaten");
		OCF newOCF = new OCF(ocf);
		newOCF.flaten();
		System.out.println(newOCF);
		System.out.println(newOCF.toStringForXMLSave());
		
		System.out.println(ocf);
		System.out.println(newOCF);
		System.out.println("have same preorders: " + ocf.hasSamePreorderAs(newOCF));
		
		System.out.println("\n**************");
		ocf = OCF.getPerfectSparseOCF();
		System.out.println(ocf);
		System.out.println(ocf.toStringForXMLSave());
		
		OCF testOCF = new OCF();
		testOCF.setOCFFromStringInXMLFile("0 0 0 0 2 2 2 2 1 2 1 2 2 2 1 2");
		System.out.println(testOCF.toStringForXMLSave());
		System.out.println(testOCF);
		testOCF.setOCFFromStringInXMLFile(ocf.toStringForXMLSave());
		System.out.println(testOCF.toStringForXMLSave());
		System.out.println(testOCF);
		testOCF.setOCFFromStringInXMLFile("3 3 1 5 1 0 0 1 1 2 6 2 2 2 1 2");
		System.out.println(testOCF);
		
		/*
		SortedSet<Integer> set1 = new TreeSet<Integer>();
		SortedSet<Integer> set2 = new TreeSet<Integer>();
		set1.add(1);
		set1.add(3);
		set1.add(2);
		set2.add(4);
		set2.add(2);
		set2.add(3);
		System.out.println("set1 = " + set1);
		System.out.println("set2 = " + set2);
		SortedSet<Integer> set3 = new TreeSet<Integer>(set1);
		System.out.println("set3 = " + set3);
		set3.retainAll(set2);
		System.out.println("new set1 = " + set1);
		System.out.println("new set2 = " + set2);
		System.out.println("new set3 = " + set3);
		set3.removeAll(set1);
		System.out.println("new set1 = " + set1);
		System.out.println("new set2 = " + set2);
		System.out.println("new set3 = " + set3);
		
		System.out.println(set3);
		*/
	}
}
