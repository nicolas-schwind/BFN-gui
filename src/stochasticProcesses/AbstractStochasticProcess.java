package stochasticProcesses;

import java.util.List;
import java.util.Random;
import gameRun.RunnableAgent;
import graph.MyEdge;

public abstract class AbstractStochasticProcess {
	
	protected Random random;
	protected List<RunnableAgent> listAgentOCFHistory;
	
	public AbstractStochasticProcess () {
		this.listAgentOCFHistory = null;
		this.random = new Random();
		this.init();
	}
	
	public void setListAgentOCFHistory (List<RunnableAgent> listAgentOCFHistory) {
		this.listAgentOCFHistory = listAgentOCFHistory;
	}
	
	public abstract void init ();
	
	public abstract MyEdge getNextEdge ();
}
