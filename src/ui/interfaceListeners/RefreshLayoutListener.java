package ui.interfaceListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import parameters.Global;

//Refreshes the graph layout
public class RefreshLayoutListener implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
		Global.vv.refresh();
	}
}
