package ui.interfaceListeners.graphGenerationListeners;

import javax.swing.JPanel;

import graph.AgentGraph;
import graphTologyGeneration.Generator;
import parameters.Constants;

public class SymmetricCliqueGraphListener extends AbstractGraphListener {
	
	@Override
	protected String getGraphTypeName() {
		return "Symmetric Clique";
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
	}
	
	@Override
	protected AgentGraph generateAgentGraph() {
		int nValue = Integer.parseInt(Constants.nTextField.getText());
		return Generator.getSymmetricClique(nValue);
	}
	
	public SymmetricCliqueGraphListener () {}
}
