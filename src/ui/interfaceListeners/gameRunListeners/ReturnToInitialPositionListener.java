package ui.interfaceListeners.gameRunListeners;

import java.awt.event.ActionListener;
import parameters.Global;

//Returns to step 0
public class ReturnToInitialPositionListener extends AbstractPositionListener implements ActionListener {
	
	@Override
	protected void updateNewPosition() {
		Global.currentFullRun.setPosition(0);
	}
	
}
