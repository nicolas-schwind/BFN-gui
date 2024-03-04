package belief;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import parameters.Global;

public class Formula {
	// A formula is characterized by a sorted list of integers from 0 to Global.nbInterpretations - 1
	private SortedSet<Integer> setOfInterpretations;
	
	public static Formula getTautology () {
		Formula result = new Formula();
		for (int i = 0; i < Global.nbInterpretations; i++) {
			result.addSingleInterpretation(i);
		}
		return result;
	}
	
	public static Formula getInconsistentFormula () {
		return new Formula();
	}
	
	// return as a formula the conjunction of the set of formulae given as parameter
	public static Formula getConjunction (Collection<Formula> setOfFormulae) {
		Formula result = Formula.getTautology();
		
		for (Formula form : setOfFormulae) {
			result.conjunctionWith(form);
		}
		
		return result;
	}
	
	// return as a formula the disjunction of the set of formulae given as parameter
	public static Formula getDisjunction (Collection<Formula> setOfFormulae) {
		Formula result = new Formula();
		
		for (Formula form : setOfFormulae) {
			result.disjunctionWith(form);
		}
		
		return result;
	}
	
	// creates a formula with no model
	public Formula () {
		this.setOfInterpretations = new TreeSet<Integer>();
	}
	
	// creates a formula which is a copy of another formula
	public Formula (Formula form) {
		this();
		this.setFormula(form.getSetOfInterpretations());
	}
	
	// creates a formula given a list of interpretations
	public Formula (int[] listInterpretations) {
		this.setOfInterpretations = new TreeSet<Integer>();
		for (int i = 0; i < listInterpretations.length; i++) {
			this.setOfInterpretations.add(listInterpretations[i]);
		}
	}
	
	// return an array of size the total number of interpretations
	// for each entry (the interpretation identifier), returns the number of formulae in the set setOfFormulae given as parameter
	// which have this interpretation as a model
	public static int[] getFrequencyStats (Collection<Formula> setOfFormulae) {
		int[] result = new int[Global.nbInterpretations];
		
		for (int interpretationID = 0; interpretationID < result.length; interpretationID++) {
			result[interpretationID] = 0;
			for (Formula form : setOfFormulae) {
				if (form.isModel(interpretationID)) {
					result[interpretationID]++;
				}
			}
		}
		
		return result;
	}
	
	public void setFormula (SortedSet<Integer> setOfInterpretations) {
		this.setOfInterpretations.clear();
		for (Integer inter : setOfInterpretations) {
			this.setOfInterpretations.add(inter);
		}
	}
	
	public Formula getNegation () {
		Formula result = new Formula();
		for (int i = 0; i < Global.nbInterpretations; i++) {
			if (!this.isModel(i)) {
				result.addSingleInterpretation(i);
			}
		}
		return result;
	}
	
	public SortedSet<Integer> getSetOfInterpretations () {
		return setOfInterpretations;
	}
	
	public void addSingleInterpretation (int inter) {
		this.setOfInterpretations.add(inter);
	}
	
	public boolean isModel (int inter) {
		return this.setOfInterpretations.contains(inter);
	}
	
	public void conjunctionWith (Formula form) {
		this.setOfInterpretations.retainAll(form.getSetOfInterpretations());
	}
	
	public void disjunctionWith (Formula form) {
		this.setOfInterpretations.addAll(form.getSetOfInterpretations());
	}
	
	public boolean isConsistent () {
		return !this.setOfInterpretations.isEmpty();
	}
	
	public boolean infers (Formula form) {
		return form.getSetOfInterpretations().containsAll(this.setOfInterpretations);
	}
	
	public boolean equiv (Formula form) {
		return this.infers(form) && form.infers(this);
	}
	
	public SortedSet<String> toCompressedString () {
		SortedSet<String> result = new TreeSet<String>();
		StringBuffer consecutiveInter;
		boolean isAlone;
		
		for (int i = 0; i < Global.nbInterpretations; i++) {
			consecutiveInter = new StringBuffer();
			// find the first next model
			while (i < Global.nbInterpretations && !isModel(i)) {
				i++;
			}
			if (i >= Global.nbInterpretations) {
				break;
			}
			consecutiveInter.append(Global.intToHexa(i));
			isAlone = true;
			// while the next interpretations are models, continue
			i++;
			while (i < Global.nbInterpretations && isModel(i)) {
				isAlone = false;
				i++;
			}
			if (!isAlone) {
				consecutiveInter.append("/" + Global.intToHexa(i-1));
			}
			result.add(consecutiveInter.toString());
		}
		return result;
	}
	
	public String toString () {
		return toCompressedString().toString();
	}
	
	public static void main (String[] args) {
		Global.initParameters();
		Formula form = new Formula();
		Formula form2 = new Formula();
		form.addSingleInterpretation(3);
		form.addSingleInterpretation(4);
		form.addSingleInterpretation(0);
		System.out.println("formula after adding 3, 4, 0: " + form);
		form.addSingleInterpretation(0);
		System.out.println("formula after adding 0: " + form);
		
		form2.addSingleInterpretation(4);
		form2.addSingleInterpretation(3);
		form.conjunctionWith(form2);
		System.out.println("formula after intersecting with 3, 4: " + form);
		
		Formula form3 = new Formula();
		form3.addSingleInterpretation(0);
		form3.addSingleInterpretation(4);
		form3.addSingleInterpretation(6);
		form3.addSingleInterpretation(10);
		form3.addSingleInterpretation(14);
		form3.addSingleInterpretation(11);
		form3.addSingleInterpretation(9);
		form3.addSingleInterpretation(5);
		form3.addSingleInterpretation(3);
		System.out.println(form3);
		
		Formula form4 = new Formula();
		form4.addSingleInterpretation(0);
		form4.addSingleInterpretation(4);
		form4.addSingleInterpretation(6);
		form4.addSingleInterpretation(10);
		form4.addSingleInterpretation(14);
		form4.addSingleInterpretation(11);
		form4.addSingleInterpretation(9);
		form4.addSingleInterpretation(5);
		
		System.out.println(form3 + " entails " + form4 + ": " + form3.infers(form4));
		System.out.println(form4 + " entails " + form3 + ": " + form4.infers(form3));
		System.out.println(form3 + " equiv " + form4 + ": " + form3.equiv(form4));
		System.out.println(form4 + " equiv " + form3 + ": " + form4.equiv(form3));
		
		form4.addSingleInterpretation(3);
		
		System.out.println(form3 + " entails " + form4 + ": " + form3.infers(form4));
		System.out.println(form4 + " entails " + form3 + ": " + form4.infers(form3));
		System.out.println(form3 + " equiv " + form4 + ": " + form3.equiv(form4));
		System.out.println(form4 + " equiv " + form3 + ": " + form4.equiv(form3));
	}
}
