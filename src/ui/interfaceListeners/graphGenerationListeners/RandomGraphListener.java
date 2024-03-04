package ui.interfaceListeners.graphGenerationListeners;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;

import graph.AgentGraph;
import graph.stronglyConnectedComponents.DAGSCCGraph;
import graph.stronglyConnectedComponents.StronglyConnectedComponent;
import graphTologyGeneration.Generator;
import parameters.Constants;
import tools.EnterCheckBox;

//Generate a random graph with n edges and a density k of edges
public class RandomGraphListener extends AbstractGraphListener {
	@Override
	protected String getGraphTypeName() {
		return "Random Graph";
	}
	
	@Override
	protected int getGridLayoutX() {
		return 0;
	}

	@Override
	protected int getSubFrameSizeX() {
		return 260;
	}

	@Override
	protected int getSubFrameSizeY() {
		return 100;
	}
	
	@Override
	protected void setContainer (JPanel container) {
		container.setLayout(new GridLayout(0, 1));
		//checkboxSymmetric = new EnterCheckBox("symmetric graph");
		//checkboxSymmetric.setSelected(false);
		//JPanel checkboxSymmetricPanel = new JPanel();
		//checkboxSymmetricPanel.add(checkboxSymmetric);
		container.add(Constants.nPanel);
		container.add(Constants.densityPanel);
		container.add(new JSeparator(JSeparator.HORIZONTAL));
		container.add(Constants.nbStrongestSCCsNonSingletonPanel);
		container.add(new JSeparator(JSeparator.HORIZONTAL));
		container.add(Constants.nbStrongestSCCsSingletonPanel);
		container.add(new JSeparator(JSeparator.HORIZONTAL));
		container.add(Constants.nbSCCsPanel);
		container.add(new JSeparator(JSeparator.HORIZONTAL));
		container.add(Constants.checkForSymmetricPanel);
		//container.add(new JSeparator(JSeparator.HORIZONTAL));
		//container.add(Constants.optionalSymmetricPanel);
		//container.add(checkboxSymmetricPanel);
		//UIManager.put("CheckBox.focus",Color.BLACK);
	}
	
	@Override
	protected AgentGraph generateAgentGraph() {
		AgentGraph result;
		DAGSCCGraph dagSCCs;
		int nValue;
		double densityValue;
		int nbMinDesiredStrongestSCCsNonSingleton;
		int nbMaxDesiredStrongestSCCsNonSingleton;
		int nbMinDesiredStrongestSCCsSingleton;
		int nbMaxDesiredStrongestSCCsSingleton;
		int nbMinDesiredSCCs;
		int nbMaxDesiredSCCs;
		
		try {
			nValue = Integer.parseInt(Constants.nTextField.getText());
		}
		catch (NumberFormatException e) {
			nValue = 10;
		}
		try {
			densityValue = Double.parseDouble(Constants.densityTextField.getText()) / 100;
		}
		catch (NumberFormatException e) {
			densityValue = 10;
		}
		try {
			nbMinDesiredStrongestSCCsNonSingleton = Integer.parseInt(Constants.minNbStrongestSCCsNonSingletonTextField.getText());
		}
		catch (NumberFormatException e) {
			nbMinDesiredStrongestSCCsNonSingleton = 0;
		}
		try {
			nbMaxDesiredStrongestSCCsNonSingleton = Integer.parseInt(Constants.maxNbStrongestSCCsNonSingletonTextField.getText());
		}
		catch (NumberFormatException e) {
			nbMaxDesiredStrongestSCCsNonSingleton = nValue;
		}
		try {
			nbMinDesiredStrongestSCCsSingleton = Integer.parseInt(Constants.minNbStrongestSCCsSingletonTextField.getText());
		}
		catch (NumberFormatException e) {
			nbMinDesiredStrongestSCCsSingleton = 0;
		}
		try {
			nbMaxDesiredStrongestSCCsSingleton = Integer.parseInt(Constants.maxNbStrongestSCCsSingletonTextField.getText());
		}
		catch (NumberFormatException e) {
			nbMaxDesiredStrongestSCCsSingleton = nValue;
		}
		try {
			nbMinDesiredSCCs = Integer.parseInt(Constants.minNbSCCsTextField.getText());
		}
		catch (NumberFormatException e) {
			nbMinDesiredSCCs = 0;
		}
		try {
			nbMaxDesiredSCCs = Integer.parseInt(Constants.maxNbSCCsTextField.getText());
		}
		catch (NumberFormatException e) {
			nbMaxDesiredSCCs = nValue;
		}
		
		int nbStrongestSCCsSingleton;
		int nbStrongestSCCsNonSingleton;
		Set<StronglyConnectedComponent> strongestSCCs;
		int nbSCCs;
		boolean checkIfSymmetric = Constants.symmetricCheckBox.isSelected();
		
		long time = System.currentTimeMillis();
		do {
			if (checkIfSymmetric) {
				result = Generator.getSymmetricRandomGraph(nValue, densityValue);
			}
			else {
				result = Generator.getRandomGraph(nValue, densityValue);
			}
			dagSCCs = result.getSCCGraph(); 
			nbSCCs = dagSCCs.getStronglyConnectedComponentsAsTopologicalOrder().size();
			strongestSCCs = dagSCCs.getStrongestSCCs();
			nbStrongestSCCsSingleton = 0;
			nbStrongestSCCsNonSingleton = 0;
			for (StronglyConnectedComponent strongestSCC : strongestSCCs) {
				if (strongestSCC.getSize() == 1) {
					nbStrongestSCCsSingleton++;
				}
				else {
					nbStrongestSCCsNonSingleton++;
				}
			}
		}
		while ((System.currentTimeMillis() - time < Constants.GENERATION_TIME_OUT) &&
				(nbStrongestSCCsNonSingleton < nbMinDesiredStrongestSCCsNonSingleton ||
						nbStrongestSCCsNonSingleton > nbMaxDesiredStrongestSCCsNonSingleton ||
						nbStrongestSCCsSingleton < nbMinDesiredStrongestSCCsSingleton ||
						nbStrongestSCCsSingleton > nbMaxDesiredStrongestSCCsSingleton ||
						nbSCCs < nbMinDesiredSCCs ||
						nbSCCs > nbMaxDesiredSCCs ||
						(!result.isWeaklyConnected())));
		
		if (System.currentTimeMillis() - time >= Constants.GENERATION_TIME_OUT) {
			// generate a popup saying the constraints could not be matched
			JOptionPane.showMessageDialog(subframe, TEXT_MESSAGE_IF_GENERATION_NOT_SUCCESSFUL);
		}
		
		return result;
	}
	
	public RandomGraphListener () {}
}
