package ui.interfaceListeners.gameRunListeners;

import java.awt.event.ActionListener;
import parameters.Global;

//Fast backward 10 steps 
public class FastBackwardPositionListener extends AbstractPositionListener implements ActionListener {
	
	@Override
	protected void updateNewPosition() {
		int nextPosition = Global.currentFullRun.getPosition() - 10;
		if (nextPosition < 0) {
			nextPosition = 0;
		}
		Global.currentFullRun.setPosition(nextPosition);
	}	
}
