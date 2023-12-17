package mouseMenu;

import java.awt.event.ItemEvent;

import java.awt.event.ItemListener;

import edu.uci.ics.jung.visualization.picking.PickedState;
import graph.AgentGraph;
import graph.UIAgent;
import parameters.Constants;
import parameters.Global;
import ui.UserInterface;

public class VertexListener implements ItemListener {
	
	private PickedState<UIAgent> pickedState;
	
	public VertexListener (PickedState<UIAgent> pickedState) {
		this.pickedState = pickedState;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		Object subject = e.getItem();
		if (subject instanceof UIAgent) {
			if (pickedState.getPicked().size() == 0) {
			}
			else {
				// enabled only if not in simulation mode
				if (!Global.simulationMode) {
				}
				UIAgent ag = (UIAgent) subject;
				if (pickedState.isPicked(ag)) {
					// first remove the selection of all other vertices
					for (UIAgent ag2 : Global.currentNetwork.getVertices()) {
						if (!ag2.equals(ag)) {
							pickedState.pick(ag2, false);
							ag2.getPanelOCF().setIsSelected(false);
							ag2.getPanelOCF().revalidate();
							ag2.getPanelOCF().repaint();
							//System.out.println("repaint non selected OCF Jpanel of agent " + ag2.getId());
						}
					}
					// then do something on this picked agent
					//System.out.println("agent " + ag.getId() + " is picked");
					//System.out.println("repaint selected OCF Jpanel of agent " + ag.getId());
					ag.getPanelOCF().setIsSelected(true);
					ag.getPanelOCF().revalidate();
					ag.getPanelOCF().repaint();
					//Constants.displayedOCFPanel.revalidate();
					//Constants.displayedOCFPanel.repaint();
				}
			}
		}
	}
}
