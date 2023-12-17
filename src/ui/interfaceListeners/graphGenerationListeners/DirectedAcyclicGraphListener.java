package ui.interfaceListeners.graphGenerationListeners;

import javax.swing.JPanel;

import graph.AgentGraph;
import graphTologyGeneration.Generator;
import parameters.Constants;

public class DirectedAcyclicGraphListener extends AbstractGraphListener {
	
	@Override
	protected String getGraphTypeName() {
		return "Directed Acyclic Graph";
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
		return 150;
	}
	
	@Override
	protected void setContainer (JPanel container) {
		container.add(Constants.nPanel);
		container.add(Constants.densityPanel);
	}
	
	@Override
	protected AgentGraph generateAgentGraph() {
		int nValue;
		double densityValue;
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
		return Generator.getDAGGraph(nValue,densityValue);
	}
	
	public DirectedAcyclicGraphListener () {}
}
