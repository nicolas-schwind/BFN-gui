package vertexEdgeDisplay;

import java.awt.Stroke;
import java.util.Set;

import org.apache.commons.collections15.Transformer;

import graph.UIAgent;
import graph.stronglyConnectedComponents.StronglyConnectedComponent;
import parameters.Constants;
import parameters.Global;


public class VertexStroke implements Transformer<UIAgent, Stroke> {
	// if the agent is in a strongest component, emphasize it with a thick stroke
	// in particular, if the agent is alone in its component (then it is a root node), put it dashed line
	// the color of the stroke depends on in which strongest component it is, but this is dealt with by the class VertexDrawPaint
	public Stroke transform(UIAgent ag) {
		Set<StronglyConnectedComponent> strongestSCCs = Global.currentNetwork.getSCCGraph().getStrongestSCCs();
		
		for (StronglyConnectedComponent component : strongestSCCs) {
			if (component.containsAgent(ag)) {
				// if this is a singleton, the particular pattern of dashed line will be used
				// otherwise, the straight thick line will be used 
				if (component.getSize() == 1) {
					return Constants.vertexStrokeThickDashed;
				}
				else {
					return Constants.vertexStrokeThickFilled;
				}
			}
		}
		
		// here, the agent is not contained in any strongest SCC
		return Constants.vertexStrokeThin;
	}
}
