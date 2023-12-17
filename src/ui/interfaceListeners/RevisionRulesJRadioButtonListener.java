package ui.interfaceListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import parameters.Global;

public class RevisionRulesJRadioButtonListener implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
		Global.currentNetwork.setGlobalRevisionPolicyFromJRadioButton();
	}
}
