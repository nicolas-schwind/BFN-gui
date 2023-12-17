package ui.interfaceListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;

import belief.OCF;
import graph.UIAgent;
import ocfDisplay.MyPanelOCF;
import parameters.Global;

public class NbVarJRadioButtonListener implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
		for (Enumeration<AbstractButton> buttons = Global.buttonGroupNbVar.getElements(); buttons.hasMoreElements();) {
	        AbstractButton button = buttons.nextElement();
	        
	        if (button.isSelected()) {
	        	// do something only if the button selected makes a change
	        	int newNbInterpretations = (int)Math.pow(2, Integer.valueOf(button.getText()));
	        	if (newNbInterpretations != Global.nbInterpretations) {
	        		// update the nb var depending on the selected radio button
	        		// also randomize all beliefs according to this new nb of var
	        		Global.nbInterpretations = newNbInterpretations;
	        		for (UIAgent ag : Global.currentNetwork.getVertices()) {
	        			ag.setOCF(new OCF());
	        			ag.setPanelOCF(new MyPanelOCF(ag.getId(), ag.getOCF()));
	        		}
	        		Global.resetAllOCFPanels();
	        	}
	        }
	    }
	}
}