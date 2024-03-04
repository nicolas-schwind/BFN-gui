package ui.interfaceListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import parameters.Global;

//Refreshes the OCF Panel layout
public class RefreshOCFPanelListener implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
		Global.resetAllOCFPanels();
	}
}