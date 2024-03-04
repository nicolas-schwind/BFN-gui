package revisionPolicies;

import belief.Formula;
import belief.OCF;
import parameters.Constants;

public class RevisionPolicySKPBulletPlus extends AbstractRevisionPolicy {

	public RevisionPolicySKPBulletPlus () {
		super();
	}
	
	public OCF revise(OCF initOCF, Formula neighborFormula) {
		OCF result = new OCF(initOCF);
		
		result.shiftAbsolute(result.getMin(neighborFormula), 0);
		result.shiftRelative(neighborFormula.getNegation(), 1);
		
		return result;
	}
	
	public String toString () {
		return Constants.nameSKPBulletPlus;
	}

	@Override
	public boolean tpoEpistemicSpace() {
		return false;
	}
}
