package revisionPolicies;

import belief.Formula;
import belief.OCF;
import parameters.Constants;

public class RevisionPolicyOneImprovement extends AbstractRevisionPolicy {

	public RevisionPolicyOneImprovement () {
		super();
	}
	
	public OCF revise(OCF initOCF, Formula neighborFormula) {
		OCF result = new OCF(initOCF);
		
		result.moveToLowerLayer(neighborFormula);
		result.flaten();
		
		return result;
	}
	
	public String toString () {
		return Constants.nameOneImprovement;
	}
	
	@Override
	public boolean tpoEpistemicSpace() {
		return true;
	}
}