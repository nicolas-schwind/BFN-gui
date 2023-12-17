package revisionPolicies;

import belief.Formula;
import belief.OCF;
import parameters.Constants;

public class RevisionPolicyDrasticMerging extends AbstractRevisionPolicy {

	public RevisionPolicyDrasticMerging () {
		super();
	}
	
	public OCF revise(OCF initOCF, Formula neighborFormula) {
		OCF result = new OCF(initOCF);
		
		result.shiftRelative(neighborFormula, -1);
		
		return result;
	}
	
	public String toString () {
		return Constants.nameDrasticMerging;
	}

	@Override
	public boolean tpoEpistemicSpace() {
		return false;
	}
}