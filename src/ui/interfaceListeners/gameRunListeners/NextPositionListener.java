package ui.interfaceListeners.gameRunListeners;

import java.awt.event.ActionListener;
import parameters.Global;

//Next step 
public class NextPositionListener extends AbstractPositionListener implements ActionListener {
	
	@Override
	protected void updateNewPosition() {
		int nextPosition = Global.currentFullRun.getPosition() + 1;
		if (nextPosition > Global.currentFullRun.getTotalNbSteps()) {
			nextPosition = Global.currentFullRun.getTotalNbSteps();
		}
		Global.currentFullRun.setPosition(nextPosition);
	}	
}