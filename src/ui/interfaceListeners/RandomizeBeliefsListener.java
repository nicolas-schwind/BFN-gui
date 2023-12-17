package ui.interfaceListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import parameters.Constants;
import parameters.Global;

public class RandomizeBeliefsListener implements ActionListener {
	
	public RandomizeBeliefsListener () {
		super();
	}
	
	public boolean successCondition () {
		return true;
	}
	
	// randomize beliefs until some stopping condition is satisfied
	public void actionPerformed(ActionEvent arg0) {
		long time = System.currentTimeMillis();
		do {
			Global.currentNetwork.randomizeBeliefs();
		}
		while ((System.currentTimeMillis() - time < Constants.GENERATION_TIME_OUT) && !successCondition());
		
		if (System.currentTimeMillis() - time >= Constants.GENERATION_TIME_OUT) {
			// generate a popup saying the constraints could not be matched
			JOptionPane.showMessageDialog(Constants.frameMain, "Failed to generate random beliefs with the underlying constraints");
		}
		
		Constants.frameMain.repaint();
		Global.resetAllOCFPanels();
	}
}