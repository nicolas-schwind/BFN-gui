package vertexEdgeDisplay;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
//import java.awt.geom.Point2D;
//import java.awt.geom.Path2D;
import java.awt.geom.Ellipse2D;
import java.util.Collection;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.util.VertexShapeFactory;
import graph.UIAgent;
import parameters.Constants;
import parameters.Global;


public class VertexShape implements Transformer<UIAgent, Shape> {	
	// if the agent is a root node, emphasis it
	public Shape transform(UIAgent ag) {
		return AffineTransform.getScaleInstance(1.2, 1.2).createTransformedShape(Global.defaultVertexShapeTransformer.transform(ag));
	}
}
