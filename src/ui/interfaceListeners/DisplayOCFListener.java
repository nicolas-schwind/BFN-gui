package ui.interfaceListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import parameters.Global;

public class DisplayOCFListener implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
		if (Global.frameDisplayedOCF.isVisible()) {
			Global.frameDisplayedOCF.setVisible(false);
		}
		else {
			Global.frameDisplayedOCF.setVisible(true);
			Global.frameDisplayedOCF.toFront();
			Global.frameDisplayedOCF.requestFocus();;
		}
	}
}
