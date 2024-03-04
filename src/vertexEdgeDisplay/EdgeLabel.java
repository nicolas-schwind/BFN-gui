package vertexEdgeDisplay;

import java.awt.Paint;

import org.apache.commons.collections15.Transformer;

import graph.MyEdge;
import parameters.Constants;
import parameters.Global;

public class EdgeLabel implements Transformer<MyEdge, String> {	
	public String transform(MyEdge edge) {
		if (edge.isCurrentlyTriggerred()) {
			return Constants.edgeTriggeredLabel;
		}
		return "";
		//return Global.defaultEdgeDrawPaintTransformer.transform(edge);
	}
}
