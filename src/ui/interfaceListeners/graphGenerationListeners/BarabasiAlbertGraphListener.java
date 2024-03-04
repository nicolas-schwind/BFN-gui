package ui.interfaceListeners.graphGenerationListeners;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Collection;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;

import graph.AgentGraph;
import graphTologyGeneration.Generator;
import parameters.Constants;
import tools.EnterCheckBox;

public class BarabasiAlbertGraphListener extends AbstractGraphListener {
	protected String getGraphTypeName() {
		return "Barabasi-Albert preferential attachment Graph";
	}
	
	@Override
	protected int getGridLayoutX() {
		return 3;
	}

	@Override
	protected int getSubFrameSizeX() {
		return 260;
	}

	@Override
	protected int getSubFrameSizeY() {
		return 205;
	}
	
	@Override
	protected void setContainer (JPanel container) {
		container.setLayout(new GridLayout(0, 1));
		//checkboxSymmetric = new EnterCheckBox("symmetric graph");
		//checkboxSymmetric.setSelected(false);
		//JPanel checkboxSymmetricPanel = new JPanel();
		//checkboxSymmetricPanel.add(checkboxSymmetric);
		container.add(Constants.nPanel);
		container.add(new JSeparator(JSeparator.HORIZONTAL));
		container.add(Constants.initialSeedSizePanel);
		container.add(Constants.numLinksAddedPanel);
		container.add(new JSeparator(JSeparator.HORIZONTAL));
		container.add(Constants.checkForSymmetricPanel);
	}
	
	@Override
	protected AgentGraph generateAgentGraph() {
		AgentGraph result;
		int nValue;
		boolean checkIfSymmetric = Constants.symmetricCheckBox.isSelected();
		int initialSeedSize, numLinksAdded;
		// default values for the remaining parameters for generation : nbInitNodes = 3 and nbEdgesToAttach = (nValue//50)+2
		
		try {
			nValue = Integer.parseInt(Constants.nTextField.getText());
		}
		catch (NumberFormatException e) {
			nValue = 10;
		}
		try {
			initialSeedSize = Integer.parseInt(Constants.initialSeedSizeTextField.getText());
		}
		catch (NumberFormatException e) {
			initialSeedSize = 3;
		}
		try {
			numLinksAdded = Integer.parseInt(Constants.numLinksAddedTextField.getText());
		}
		catch (NumberFormatException e) {
			numLinksAdded = (nValue / 50) + 2;
		}
		
		long time = System.currentTimeMillis();
		do {
			result = Generator.getBarabasiAlbertGraph(initialSeedSize, nValue, numLinksAdded);
		}
		while ((System.currentTimeMillis() - time < Constants.GENERATION_TIME_OUT) && !result.isWeaklyConnected());
		
		if (System.currentTimeMillis() - time >= Constants.GENERATION_TIME_OUT) {
			// generate a popup saying the constraints could not be matched
			JOptionPane.showMessageDialog(subframe, TEXT_MESSAGE_IF_GENERATION_NOT_SUCCESSFUL);
		}
		
		if (checkIfSymmetric) {
			result = Generator.getSymmetricClosure(result);
		}
		
		return result;
	}
	
	public BarabasiAlbertGraphListener () {}
}
