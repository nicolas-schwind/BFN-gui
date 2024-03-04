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

public class KleinbergGraphListener extends AbstractGraphListener {
	protected String getGraphTypeName() {
		return "Kleinberg small world Graph";
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
		container.add(Constants.nbRowsColsPanel);
		container.add(new JSeparator(JSeparator.HORIZONTAL));
		container.add(Constants.clusteringExpPanel);
		container.add(new JSeparator(JSeparator.HORIZONTAL));
		container.add(Constants.checkForSymmetricPanel);
	}
	
	@Override
	protected AgentGraph generateAgentGraph() {
		AgentGraph result;
		int nbRows;
		int nbCols;
		int clusteringExp;
		boolean checkIfSymmetric = Constants.symmetricCheckBox.isSelected();
		// default values for the remaining parameters for generation : nbRows = 3, nbCols = 5, clusteringExp = 2
		
		try {
			nbRows = Integer.parseInt(Constants.nbRowsTextField.getText());
		}
		catch (NumberFormatException e) {
			nbRows = 3;
		}
		try {
			nbCols = Integer.parseInt(Constants.nbColsTextField.getText());
		}
		catch (NumberFormatException e) {
			nbCols = 5;
		}
		try {
			clusteringExp = Integer.parseInt(Constants.clusteringExpTextField.getText());
		}
		catch (NumberFormatException e) {
			clusteringExp = 2;
		}
		
		long time = System.currentTimeMillis();
		do {
			result = Generator.getKleinbergGraph(nbRows, nbCols, clusteringExp);
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
	
	public KleinbergGraphListener () {}
}
