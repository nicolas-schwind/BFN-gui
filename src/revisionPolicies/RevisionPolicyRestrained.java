package revisionPolicies;

import belief.Formula;
import belief.OCF;
import parameters.Constants;

public class RevisionPolicyRestrained extends AbstractRevisionPolicy {

	public RevisionPolicyRestrained () {
		super();
	}
	
	public OCF revise(OCF initOCF, Formula neighborFormula) {
		OCF result = new OCF(initOCF);
		
		result.nayak(result.getMin(neighborFormula));
		result.refinement(result.getMinComplementary(neighborFormula));
		result.flaten();
		
		return result;
	}
	
	public String toString () {
		return Constants.nameRestrained;
	}

	@Override
	public boolean tpoEpistemicSpace() {
		return true;
	}
}