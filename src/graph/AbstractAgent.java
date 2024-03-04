package graph;

import revisionPolicies.AbstractRevisionPolicy;

public abstract class AbstractAgent {
	protected int id;
	protected AbstractRevisionPolicy revisionPolicy;
	
	public AbstractAgent (int n) {
		this.id = n;
	}
	
	public AbstractAgent (AbstractAgent ag) {
		id = ag.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public AbstractRevisionPolicy getRevisionPolicy () {
		return this.revisionPolicy;
	}
	
	public void setRevisionPolicy (AbstractRevisionPolicy revisionPolicy) {
		this.revisionPolicy = revisionPolicy;
	}
	
	public abstract String toString();
}

