package revisionPolicies;

import belief.Formula;
import belief.OCF;

public abstract class AbstractRevisionPolicy {
	
	public AbstractRevisionPolicy () {}
	
	public abstract OCF revise(OCF initOCF, Formula neighborFormula);
	
	public abstract String toString ();
	
	public abstract boolean tpoEpistemicSpace ();
}






