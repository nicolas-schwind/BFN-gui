package ui.interfaceListeners.gameRunListeners;

import java.awt.event.ActionListener;
import parameters.Global;

//Previous step 
public class PreviousPositionListener extends AbstractPositionListener implements ActionListener {
	
	@Override
	protected void updateNewPosition() {
		int nextPosition = Global.currentFullRun.getPosition() - 1;
		if (nextPosition < 0) {
			nextPosition = 0;
		}
		Global.currentFullRun.setPosition(nextPosition);
	}	
}