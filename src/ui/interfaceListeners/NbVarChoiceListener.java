package ui.interfaceListeners;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import graph.AgentGraph;
import parameters.Constants;
import parameters.Global;

public class NbVarChoiceListener implements ItemListener {
	
	private JComboBox<String> nbVarComboBox;
	
	public NbVarChoiceListener (JComboBox<String> nbVarComboBox) {
		this.nbVarComboBox = nbVarComboBox;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		int selectedNbInterpretations;
		if (e.getSource() == this.nbVarComboBox) {
			System.out.println(this.nbVarComboBox.getSelectedItem().toString());
			selectedNbInterpretations = 1 << Integer.parseInt(this.nbVarComboBox.getSelectedItem().toString());
			if (selectedNbInterpretations != Global.nbInterpretations) {
				// if the number of interpretations is different from the current one, we need to reset the graph to empty 
				Global.currentNetwork = new AgentGraph();
				Global.vv.refresh();
    			Global.resetAllOCFPanels();
    			Constants.frameMain.repaint();
			}
			Global.nbInterpretations = selectedNbInterpretations;
		}
	}
}
