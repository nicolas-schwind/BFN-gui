package vertexEdgeDisplay;

import java.awt.Color;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections15.Transformer;

import belief.Formula;
import belief.OCF;
import edu.uci.ics.jung.visualization.picking.PickedState;
import graph.UIAgent;
import parameters.Constants;
import parameters.Global;


public class VertexFillPaint implements Transformer<UIAgent, Paint> {	
	private PickedState<UIAgent> picked;
	
	// if the beliefs base of the agent ag infers phi, then we choose the blue color
	// if the belief base of the agent ag infers not phi, then we choose the red color
	// otherwise (the belief of the agent ag infers neither phi or not phi) we choose light gray
	
	// moreover, if the agent is in the transitive closure of the picked agent (this includes the picked agent itself),
	// then we set the color darker
	public Paint transform(UIAgent ag) {
		OCF agOcf = ag.getOCF();
		Set<UIAgent> pickedAgentSet;
		UIAgent pickedAgent;
		Collection<Integer> transitiveClosureAgentIds;
		
		pickedAgentSet = picked.getPicked();
		if (pickedAgentSet.isEmpty()) {
			pickedAgent = null;
			transitiveClosureAgentIds = new TreeSet<Integer>();
		}
		else {
			pickedAgent = picked.getPicked().iterator().next();
			// the picked agent may be from a previous graph, so we catch the exception just in case
			try {
				transitiveClosureAgentIds = Global.currentNetwork.getTransitiveClosureAgentIds(pickedAgent);
			}
			catch (IndexOutOfBoundsException e) {
				transitiveClosureAgentIds = new TreeSet<Integer>();
			}
		}
		
		if (agOcf.infers(Global.trackedBelief)) {
			if (transitiveClosureAgentIds.contains(ag.getId())) {
				return Constants.normalBlueColor;
			}
			else {
				return Constants.lightBlueColor;
			}
		}
		else if (!agOcf.isConsistentWith(Global.trackedBelief)) {
			if (transitiveClosureAgentIds.contains(ag.getId())) {
				return Constants.normalRedColor;
			}
			else {
				return Constants.lightRedColor;
			}
		}
		else {
			if (transitiveClosureAgentIds.contains(ag.getId())) {
				return Constants.normalGrayColor;
			}
			else {
				return Constants.lightGrayColor;
			}
		}
	}
	
	public VertexFillPaint (PickedState<UIAgent> picked) {
		this.picked = picked;
	}
}
