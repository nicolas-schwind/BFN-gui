package ui.interfaceListeners;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;

import parameters.Global;

public class DisplayLogInfoListener implements ActionListener {
	
	public DisplayLogInfoListener () {
		Global.logtext.setEditable(false);
		Global.frameLog.setLayout(new FlowLayout());
		Global.frameLog.getContentPane().add(new JScrollPane(Global.logtext));
		Global.frameLog.pack();
		Global.frameLog.setVisible(false);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// OLD: generate stats for this graph
		//Stats stats = new Stats(Global.currentNetwork);
		//stats.computeAllStats();
		//stats.displayAllStats();
		if (Global.frameLog.isVisible()) {
			Global.frameLog.setVisible(false);
		}
		else {
			Global.frameLog.setVisible(true);
			Global.frameLog.toFront();
			Global.frameLog.requestFocus();;
		}
	}
}