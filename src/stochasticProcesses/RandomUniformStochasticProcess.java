package stochasticProcesses;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import gameRun.RunnableAgent;
import graph.MyEdge;
import parameters.Global;

public class RandomUniformStochasticProcess extends AbstractStochasticProcess {

	public RandomUniformStochasticProcess() {
		super();
	}
	
	@Override
	public void init () {}
	
	@Override
	public MyEdge getNextEdge() {
		MyEdge result = null;
		Collection<MyEdge> edgeList = Global.currentNetwork.getEdges();
		Iterator<MyEdge> it = edgeList.iterator(); 
		int chosenEdgePositionInCollection = random.nextInt(edgeList.size()) + 1;
		
		for (int i = 0; i < chosenEdgePositionInCollection; i++) {
			result = it.next();
		}
		
		return result;
	}
}
