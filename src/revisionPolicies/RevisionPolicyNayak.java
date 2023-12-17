package revisionPolicies;

import belief.Formula;
import belief.OCF;
import parameters.Constants;

public class RevisionPolicyNayak extends AbstractRevisionPolicy {

	public RevisionPolicyNayak () {
		super();
	}
	
	public OCF revise(OCF initOCF, Formula neighborFormula) {
		OCF result = new OCF(initOCF);
		
		result.nayak(neighborFormula);
		result.flaten();
		
		return result;
	}
	
	public String toString () {
		return Constants.nameNayak;
	}

	@Override
	public boolean tpoEpistemicSpace() {
		return true;
	}
}