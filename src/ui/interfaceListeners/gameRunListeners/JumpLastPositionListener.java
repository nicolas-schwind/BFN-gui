package ui.interfaceListeners.gameRunListeners;

import java.awt.event.ActionListener;
import parameters.Global;

//Jump to last step
public class JumpLastPositionListener extends AbstractPositionListener implements ActionListener {
	
	@Override
	protected void updateNewPosition() {
		Global.currentFullRun.setPosition(Global.currentFullRun.getTotalNbSteps());
	}	
}
