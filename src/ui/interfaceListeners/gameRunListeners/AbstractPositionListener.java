package ui.interfaceListeners.gameRunListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import parameters.Constants;
import parameters.Global;

public abstract class AbstractPositionListener implements ActionListener {
	
	protected abstract void updateNewPosition();
	
	@Override
	public void actionPerformed(ActionEvent e) {
		updateNewPosition();
		Global.currentNetwork.setGame(Global.currentFullRun);
		Global.displayedOCFPanel.repaint();
		Global.updateInfoMessageLabels();
		Constants.frameMain.repaint();
		//Global.resetAllOCFPanels();
	}

}
