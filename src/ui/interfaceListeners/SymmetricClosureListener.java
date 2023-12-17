package ui.interfaceListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import graphTologyGeneration.Generator;
import parameters.Global;

public class SymmetricClosureListener implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
		Global.currentNetwork = Generator.getSymmetricClosure(Global.currentNetwork);
		Global.vv.refresh();
	}
}