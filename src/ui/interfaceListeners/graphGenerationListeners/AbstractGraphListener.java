package ui.interfaceListeners.graphGenerationListeners;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import graph.UIAgent;
import graph.AbstractAgent;
import graph.AgentGraph;
import parameters.Constants;
import parameters.Global;
import tools.EnterButton;

public abstract class AbstractGraphListener implements ActionListener {
	// text message if the graph generation fails
	protected final String TEXT_MESSAGE_IF_GENERATION_NOT_SUCCESSFUL = "Some constraints "
			+ "could not be met, so those parameters were ignored in the graph generation\n(note: one of the default constraints "
			+ "is that the graph must be weakly connected)";
	
	protected JFrame subframe;
	protected JButton okButton;
	
	public AbstractGraphListener () {}
	
	protected abstract String getGraphTypeName();
	protected abstract int getGridLayoutX();
	protected abstract int getSubFrameSizeX();
	protected abstract int getSubFrameSizeY();
	protected abstract void setContainer (JPanel container);
	protected abstract AgentGraph generateAgentGraph();
	
	public void actionPerformed (ActionEvent arg0) {
		JPanel container = new JPanel(new GridLayout(getGridLayoutX(), 1));
		subframe = new JFrame(getGraphTypeName());
		
		subframe.setSize(getSubFrameSizeX(), getSubFrameSizeY());
		subframe.setLocationRelativeTo(Constants.frameMain);
		okButton = new EnterButton("Ok");
		setContainer(container);
		container.add(okButton);
		subframe.add(container);
		subframe.getRootPane().setDefaultButton(okButton);
		subframe.pack();
		subframe.setVisible(true);
		
		okButton.addActionListener(new ButtonListener());
	}
	
	public JButton getOkButton () {
		return this.okButton;
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			try {
				Global.currentNetwork = generateAgentGraph();
				Global.currentNetwork.setGlobalRevisionPolicyFromJRadioButton();
    			Global.vv.refresh();
    			Global.resetAllOCFPanels();
    			
    			subframe.setVisible(false);
    			Constants.frameMain.repaint();
			}
			catch(java.lang.NumberFormatException e) {}
		}
	}
}
