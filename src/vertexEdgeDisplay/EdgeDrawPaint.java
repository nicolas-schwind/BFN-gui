package vertexEdgeDisplay;

import java.awt.Paint;
import java.awt.Stroke;
import java.util.Collection;
import java.util.Stack;
import java.util.Vector;

import org.apache.commons.collections15.Transformer;

import graph.UIAgent;
import graph.MyEdge;
import parameters.Constants;
import parameters.Global;

public class EdgeDrawPaint implements Transformer<MyEdge, Paint> {	
	// if the agent is in a strongest component, emphasize it with a thick stroke
	// in particular, if the agent is alone in its component (then it is a root node), put it dashed line
	// the color of the stroke depends on in which strongest component it is, but this is dealt with by the class VertexDrawPaint
	public Paint transform(MyEdge edge) {
		if (edge.isCurrentlyTriggerred()) {
			return Constants.edgeTriggeredColor;
		}
		return Global.defaultEdgeDrawPaintTransformer.transform(edge);
	}
}
