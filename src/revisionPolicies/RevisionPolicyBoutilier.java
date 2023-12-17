package revisionPolicies;

import belief.Formula;
import belief.OCF;
import parameters.Constants;

public class RevisionPolicyBoutilier extends AbstractRevisionPolicy {

	public RevisionPolicyBoutilier () {
		super();
	}
	
	public OCF revise(OCF initOCF, Formula neighborFormula) {
		OCF result = new OCF(initOCF);
		
		result.nayak(result.getMin(neighborFormula));
		result.flaten();
		
		return result;
	}
	
	public String toString () {
		return Constants.nameBoutilier;
	}

	@Override
	public boolean tpoEpistemicSpace() {
		return true;
	}
}