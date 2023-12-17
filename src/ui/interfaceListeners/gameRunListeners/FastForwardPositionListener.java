package ui.interfaceListeners.gameRunListeners;

import java.awt.event.ActionListener;
import parameters.Global;

//Fast forward 10 steps 
public class FastForwardPositionListener extends AbstractPositionListener implements ActionListener {
	
	@Override
	protected void updateNewPosition() {
		int nextPosition = Global.currentFullRun.getPosition() + 10;
		if (nextPosition > Global.currentFullRun.getTotalNbSteps()) {
			nextPosition = Global.currentFullRun.getTotalNbSteps();
		}
		Global.currentFullRun.setPosition(nextPosition);
	}	
}
