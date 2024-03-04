package ui;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import graph.UIAgent;
import graph.MyEdge;
import parameters.Global;

public class MyVisualizationViewer extends VisualizationViewer<UIAgent, MyEdge> {
	
	private static final long serialVersionUID = 1L;
	
	public MyVisualizationViewer (Layout<UIAgent, MyEdge> layout) {
		super(layout);
		refresh();
	}
	
	// Refresh the graph view
	public void refresh() {
		Global.graphLayout = new FRLayout<UIAgent,MyEdge>(Global.currentNetwork);
		this.setGraphLayout(Global.graphLayout);
	}
	
	public void refresh(Layout<UIAgent,MyEdge> layout) {
		this.setGraphLayout(layout);
	}
}
