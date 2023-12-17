package ui.interfaceListeners.gameRunListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import parameters.Constants;
import parameters.Global;

//Switch simulation mode
public class SwitchSimulationModeListener implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
		Global.switchSimulationMode();
		Constants.frameMain.repaint();
	}
}
