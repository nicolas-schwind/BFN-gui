package ui.interfaceListeners.graphGenerationListeners;

import java.awt.Color;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.UIManager;

import graph.AgentGraph;
import graphTologyGeneration.Generator;
import parameters.Constants;
import tools.EnterCheckBox;

public class LineGraphListener extends AbstractGraphListener {
	
	@Override
	protected String getGraphTypeName() {
		return "Line Graph";
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
		//checkboxSymmetric = new EnterCheckBox("symmetric graph");
		//checkboxSymmetric.setSelected(false);
		container.add(Constants.nPanel);
		container.add(Constants.checkForSymmetricPanel);
		UIManager.put("CheckBox.focus",Color.BLACK);
	}
	
	@Override
	protected AgentGraph generateAgentGraph() {
		AgentGraph result;
		int nValue;
		try {
			nValue = Integer.parseInt(Constants.nTextField.getText());
		}
		catch (NumberFormatException e) {
			nValue = 10;
		}
		if (Constants.symmetricCheckBox.isSelected()) {
			result = Generator.getSymmetricLineGraph(nValue);
		}
		else {
			result = Generator.getLineGraph(nValue);
		}
		return result;
	}
	
	public LineGraphListener () {}
}
