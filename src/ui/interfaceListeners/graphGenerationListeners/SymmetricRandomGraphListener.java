package ui.interfaceListeners.graphGenerationListeners;

import javax.swing.JPanel;

import graph.AgentGraph;
import graphTologyGeneration.Generator;
import parameters.Constants;

public class SymmetricRandomGraphListener extends AbstractGraphListener {
	
	@Override
	protected String getGraphTypeName() {
		return "Symmetric Random Graph";
	}
	
	@Override
	protected int getGridLayoutX() {
		return 7;
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
		container.add(Constants.nPanel);
		container.add(Constants.densityPanel);
	}
	
	@Override
	protected AgentGraph generateAgentGraph() {
		int nValue = Integer.parseInt(Constants.nTextField.getText());
		double densityValue = Double.parseDouble(Constants.densityTextField.getText()) / 100;
		return Generator.getSymmetricRandomGraph(nValue, densityValue);
	}
	
	public SymmetricRandomGraphListener () {}
}
