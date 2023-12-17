package vertexEdgeDisplay;

import java.awt.Paint;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.collections15.Transformer;

import graph.UIAgent;
import graph.stronglyConnectedComponents.StronglyConnectedComponent;
import parameters.Constants;
import parameters.Global;


public class VertexDrawPaint implements Transformer<UIAgent, Paint> {	
	// if the agent is in a strongest SCC and alone (i.e., it is a root node), then draw it white
	// if the agent is in a strongest SCC but not alone, then draw it in a color following Constants.vertexDrawColors
	// otherwise, the agent is not in a strongest SCC: set the default draw style
	public Paint transform(UIAgent ag) {
		Vector<StronglyConnectedComponent> strongestSCCs = Global.currentNetwork.getSCCGraph().getStrongestSCCsStaticOrder();
		int nbAvailableColors = Constants.vertexDrawColors.length;
		int idColor = 0;
		
		for (StronglyConnectedComponent component : strongestSCCs) {
			if (component.containsAgent(ag)) {
				// if this is a singleton, the particular pattern of dashed line will be used
				// otherwise, the straight thick line will be used 
				if (component.getSize() == 1) {
					return Constants.whiteColorForRoot;
				}
				else {
					return Constants.vertexDrawColors[idColor % nbAvailableColors];
				}
			}
			else {
				idColor++;
			}
		}
		
		// here, the agent is not contained in any strongest SCC
		return Global.defaultVertexDrawPaintTransformer.transform(ag);
	}
}
