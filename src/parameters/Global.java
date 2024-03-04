package parameters;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.Enumeration;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import belief.Formula;
import belief.OCF;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.picking.PickedState;
import gameRun.AbstractRun;
import gameRun.CheatedRun;
import gameRun.TrueRun;
import graph.AgentGraph;
import graph.UIAgent;
import graph.stronglyConnectedComponents.StronglyConnectedComponent;
import log.Log;
import graph.MyEdge;
import mouseMenu.MousePlugin;
import revisionPolicies.AbstractRevisionPolicy;
import revisionPolicies.RevisionPolicyBoutilier;
import revisionPolicies.RevisionPolicyDrasticMerging;
import revisionPolicies.RevisionPolicyNayak;
import revisionPolicies.RevisionPolicyOneImprovement;
import revisionPolicies.RevisionPolicyRestrained;
import revisionPolicies.RevisionPolicySKPBulletPlus;
import stochasticProcesses.AbstractStochasticProcess;
import stochasticProcesses.RandomUniformStochasticProcess;
import ui.MyVisualizationViewer;
import org.apache.commons.collections15.Transformer;

public class Global {
	
	// The currently considered number of interpretations, should be the same for all agents in a graph
	public static int nbInterpretations;
	public static JPanel displayedOCFPanel;
	public static JFrame frameDisplayedOCF = new JFrame("ep. state list");
	// A frame and text to store some stats for the game
	public static JFrame frameLog = new JFrame("Log");
	public static JTextArea logtext = new JTextArea(30, 30);
	// The following has no link with frameLog and logText above, it is just to store some log message in a file
	public static Log log = new Log();
	public static PickedState<UIAgent> pickedAgentInterface;
	// Graph's specifics elements
	public static AgentGraph currentNetwork;
	// radio buttons for nbVar
	public static ButtonGroup buttonGroupNbVar = new ButtonGroup();
	public static JRadioButton buttonNbVar1 = new JRadioButton("1");
	public static JRadioButton buttonNbVar2 = new JRadioButton("2");
	public static JRadioButton buttonNbVar3 = new JRadioButton("3");
	public static JRadioButton buttonNbVar4 = new JRadioButton("4");
	public static JRadioButton buttonNbVar5 = new JRadioButton("5");
	public static JRadioButton buttonNbVar6 = new JRadioButton("6");
	public static JRadioButton buttonNbVar7 = new JRadioButton("7");
	public static JRadioButton buttonNbVar8 = new JRadioButton("8");
	
	// radio buttons for revision policies
	public static ButtonGroup buttonGroupRevisionPolicies = new ButtonGroup();
	public static JRadioButton buttonDrasticMerging = new JRadioButton(Constants.nameDrasticMerging);
	public static JRadioButton buttonOneImprovement = new JRadioButton(Constants.nameOneImprovement);
	public static JRadioButton buttonBoutilier = new JRadioButton(Constants.nameBoutilier);
	public static JRadioButton buttonNayak = new JRadioButton(Constants.nameNayak);
	public static JRadioButton buttonRestrained = new JRadioButton(Constants.nameRestrained);
	public static JRadioButton buttonSKPBulletPlus = new JRadioButton(Constants.nameSKPBulletPlus);
	
	public static JFileChooser fileChooser;
	public static JPanel panelRight = new JPanel();
	public static JLabel messageInfoEditionOrRunMode = new JLabel();
	public static JLabel messageInfoRunPosition = new JLabel();
	public static JLabel messageInfoStabilityReached = new JLabel();
	public static JLabel messageInfoConsensusReached = new JLabel();
	public static JLabel messageInfoUnanimous = new JLabel();
	
	// default revision policy when launching the software
	public static AbstractRevisionPolicy defaultRevisionPolicy;
	
	// The stochastic process under consideration
	public static AbstractStochasticProcess stochasticProcess;
	
	// The belief that will determine the color of each agent, useful during a run
	// Currently, the tracked belief is the final global outcome
	public static Formula trackedBelief;
	
	public static AbstractLayout<UIAgent, MyEdge> graphLayout;
	public static EditingModalGraphMouse<UIAgent, MyEdge> editingModelGraphMouse;
	public static MousePlugin<UIAgent, MyEdge> mousePlugin;
	
	// Simulation mode, lock all the editing features
	// and unlock the previous step action
	public static boolean simulationMode;
	public static AbstractRun currentFullRun;
	
	public static MyVisualizationViewer vv;
	
	// This is to save the default shape and drawpaint of vertices and stroke of edges (by default in JUNG)
	public static Transformer<UIAgent, Shape> defaultVertexShapeTransformer;
	public static Transformer<UIAgent, Paint> defaultVertexDrawPaintTransformer;
	public static Transformer<MyEdge, Paint> defaultEdgeDrawPaintTransformer;
	
	public static void initParameters () {
		nbInterpretations = Constants.DEFAULT_NB_INTER;
		defaultRevisionPolicy = new RevisionPolicyBoutilier();
		// For now, the stochastic process is only the random uniform (Bernouilli) stochastic process
		stochasticProcess = new RandomUniformStochasticProcess();
		trackedBelief = Formula.getTautology();
		simulationMode = false;
		currentFullRun = null;
		currentNetwork = new AgentGraph();
	}
	
	public static void initInfoMessageLabels () {
		Global.messageInfoEditionOrRunMode.setPreferredSize(new Dimension(800, 60));
        Global.messageInfoEditionOrRunMode.setHorizontalTextPosition(SwingConstants.CENTER);
        Global.messageInfoEditionOrRunMode.setHorizontalAlignment(SwingConstants.CENTER);
        Global.messageInfoEditionOrRunMode.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        Global.messageInfoEditionOrRunMode.setForeground(Color.BLACK);
        Global.messageInfoEditionOrRunMode.setFont(new Font("Serif",Font.BOLD,30));
        
        Global.messageInfoRunPosition.setPreferredSize(new Dimension(800, 40));
        Global.messageInfoRunPosition.setHorizontalTextPosition(SwingConstants.CENTER);
        Global.messageInfoRunPosition.setHorizontalAlignment(SwingConstants.CENTER);
        Global.messageInfoRunPosition.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        Global.messageInfoRunPosition.setForeground(Color.GRAY);
        Global.messageInfoRunPosition.setFont(new Font("Serif",Font.BOLD,25));
        
        Global.messageInfoStabilityReached.setPreferredSize(new Dimension(800, 40));
        Global.messageInfoStabilityReached.setHorizontalTextPosition(SwingConstants.CENTER);
        Global.messageInfoStabilityReached.setHorizontalAlignment(SwingConstants.CENTER);
        Global.messageInfoStabilityReached.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        Global.messageInfoStabilityReached.setForeground(Color.GRAY);
        Global.messageInfoStabilityReached.setFont(new Font("Serif",Font.BOLD,25));
		
        Global.messageInfoConsensusReached.setPreferredSize(new Dimension(800, 40));
        Global.messageInfoConsensusReached.setHorizontalTextPosition(SwingConstants.CENTER);
        Global.messageInfoConsensusReached.setHorizontalAlignment(SwingConstants.CENTER);
        Global.messageInfoConsensusReached.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        Global.messageInfoConsensusReached.setForeground(Color.GRAY);
        Global.messageInfoConsensusReached.setFont(new Font("Serif",Font.BOLD,25));
        
        Global.messageInfoUnanimous.setPreferredSize(new Dimension(800, 40));
        Global.messageInfoUnanimous.setHorizontalTextPosition(SwingConstants.CENTER);
        Global.messageInfoUnanimous.setHorizontalAlignment(SwingConstants.CENTER);
        Global.messageInfoUnanimous.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        Global.messageInfoUnanimous.setForeground(Color.GRAY);
        Global.messageInfoUnanimous.setFont(new Font("Serif",Font.BOLD,25));
        
        Global.updateInfoMessageLabels();
	}
	
	public static void updateInfoMessageLabels () {
		if (!simulationMode) {
			messageInfoEditionOrRunMode.setText("Edition mode");
			messageInfoRunPosition.setText("");
			messageInfoStabilityReached.setText("");
			messageInfoConsensusReached.setText("");
			messageInfoUnanimous.setText("");
		}
		else {
			//messageInfoEditionOrRunMode.setText("Run mode (" + currentFullRun.runModeName() + ")");
			messageInfoEditionOrRunMode.setText("Run mode");
			messageInfoRunPosition.setText("STEP " + currentFullRun.getPosition() + " / " + currentFullRun.getTotalNbSteps());
			if (currentFullRun.getStableStatusFlag() >= 0) {
				messageInfoStabilityReached.setText("stable (detected at step " + currentFullRun.getStableStatusFlag() + " / reached at step " +
						currentFullRun.getAllAgentsHaveReachedStabilityStepNumber() + ")");
			}
			else if (currentFullRun.getUnstableStatusFlag() >= 0) {
				messageInfoStabilityReached.setText("unstable (detected at step " + currentFullRun.getUnstableStatusFlag() + ")");
			}
			else {
				messageInfoStabilityReached.setText("unknown");
			}
			if (currentFullRun.getConsensusDetected() < 0) {
				messageInfoConsensusReached.setText("consensus undetected");
			}
			else {
				messageInfoConsensusReached.setText("consensus (" + currentFullRun.getConsensusDetected() + "): "
						+ currentFullRun.getConsensus());
			}
			if (currentFullRun.stabilityReached()) {
				messageInfoUnanimous.setText("global outcome (reached at step  " + currentFullRun.getGlobalOutcomeUpperBoundLastChangeStep() + "): "
						+ currentFullRun.getGlobalOutcomeUpperBound());
			}
			else {
				messageInfoUnanimous.setText("global outcome (reached at step " + currentFullRun.getGlobalOutcomeUpperBoundLastChangeStep() + "): "
						+ currentFullRun.getGlobalOutcomeUpperBound());
			}
		}
		
	}

	public static void setEnabledJRadioButtons (boolean b) {
		buttonDrasticMerging.setEnabled(b);
		buttonOneImprovement.setEnabled(b);
		buttonBoutilier.setEnabled(b);
		buttonNayak.setEnabled(b);
		buttonRestrained.setEnabled(b);
		buttonSKPBulletPlus.setEnabled(b);
		buttonNbVar1.setEnabled(b);
		buttonNbVar2.setEnabled(b);
		buttonNbVar3.setEnabled(b);
		buttonNbVar4.setEnabled(b);
		buttonNbVar5.setEnabled(b);
		buttonNbVar6.setEnabled(b);
		buttonNbVar7.setEnabled(b);
		buttonNbVar8.setEnabled(b);
	}

	public static String getNameActiveJRadioButtonRevisionPolicy () {
		for (Enumeration<AbstractButton> buttons = buttonGroupRevisionPolicies.getElements(); buttons.hasMoreElements();) {
	        AbstractButton button = buttons.nextElement();
	
	        if (button.isSelected()) {
	            return button.getText();
	        }
		}
		return "";
	}
	
	// set the radio button to the revision policy whose name is given in parameter
	public static void setJRadioButtonRevisionPolicy (String name) {
		if (name.equals(Constants.nameBoutilier)) {
			buttonBoutilier.setSelected(true);
		}
		else if (name.equals(Constants.nameDrasticMerging)) {
			buttonDrasticMerging.setSelected(true);
		}
		else if (name.equals(Constants.nameNayak)) {
			buttonNayak.setSelected(true);
		}
		else if (name.equals(Constants.nameOneImprovement)) {
			buttonOneImprovement.setSelected(true);
		}
		else if (name.equals(Constants.nameRestrained)) {
			buttonRestrained.setSelected(true);
		}
		else if (name.equals(Constants.nameSKPBulletPlus)) {
			buttonSKPBulletPlus.setSelected(true);
		}
	}
	
	// set the radio button to the nbVar given in parameter
	public static void setJRadioButtonNbVar (int nbInter) {
		if (nbInter == 2) {
			buttonNbVar1.setSelected(true);
		}
		else if (nbInter == 4) {
			buttonNbVar2.setSelected(true);
		}
		else if (nbInter == 8) {
			buttonNbVar3.setSelected(true);
		}
		else if (nbInter == 16) {
			buttonNbVar4.setSelected(true);
		}
		else if (nbInter == 32) {
			buttonNbVar5.setSelected(true);
		}
		else if (nbInter == 64) {
			buttonNbVar6.setSelected(true);
		}
		else if (nbInter == 128) {
			buttonNbVar7.setSelected(true);
		}
		else if (nbInter == 256) {
			buttonNbVar8.setSelected(true);
		}
	}

	public static void switchSimulationMode () {
		if (!simulationMode) {
			simulationMode = true;
			currentFullRun = new TrueRun();
			stochasticProcess.setListAgentOCFHistory(currentFullRun.getListAgents());
			currentFullRun.setStochasticProcess(stochasticProcess);
			Constants.resetMenuItem.setEnabled(true);
			editingModelGraphMouse.setMode(EditingModalGraphMouse.Mode.PICKING);
			//Constants.infoMenu.setEnabled(false);
			Constants.helpMenu.setEnabled(false);
			Constants.modeMenu.setEnabled(false);
			Constants.editionMenu.setEnabled(false);
			Constants.nextMenuItem.setEnabled(true);
			Constants.previousMenuItem.setEnabled(true);
			Constants.fastForwardMenuItem.setEnabled(true);
			Constants.fastBackwardMenuItem.setEnabled(true);
			Constants.jumpLastMenuItem.setEnabled(true);
			mousePlugin.setEnabled(false);
			Global.currentNetwork.setGlobalRevisionPolicyFromJRadioButton();
			setEnabledJRadioButtons(false);
			Constants.playMenuItem.setEnabled(false);
			Constants.simulateMenuItem.setText("Stop");
			currentFullRun.triggerRun();
			//System.out.println("Number of actual changes: " + currentFullRun.getTotalNbStepsWithActualChanges());
			currentFullRun.setPosition(0);
			Global.currentNetwork.setGame(Global.currentFullRun);
			// set the tracked belief to the consensus if consistent, otherwise set the tracked belief to the current global outcome
			/*if (currentFullRun.getConsensus().isConsistent()) {
				trackedBelief = currentFullRun.getConsensus();
			}
			else {
				trackedBelief = currentFullRun.getGlobalOutcomeUpperBound();
			}*/
			// set the tracked belief to the current global outcome
			trackedBelief = currentFullRun.getGlobalOutcomeUpperBound();
			Global.updateInfoMessageLabels();
			Constants.frameMain.repaint();
		}
		else {
			simulationMode = false;
			Constants.resetMenuItem.setEnabled(false);
			Constants.modeMenu.setEnabled(true);
			//Constants.infoMenu.setEnabled(true);
			Constants.editionMenu.setEnabled(true);
			Constants.helpMenu.setEnabled(true);
			Constants.nextMenuItem.setEnabled(false);
			Constants.previousMenuItem.setEnabled(false);
			Constants.fastForwardMenuItem.setEnabled(false);
			Constants.fastBackwardMenuItem.setEnabled(false);
			Constants.jumpLastMenuItem.setEnabled(false);
			mousePlugin.setEnabled(true);
			// only if some agent is selected
			if (pickedAgentInterface.getPicked().size() > 0) {
			}
			// reset all panels to normal
			for (UIAgent ag : currentNetwork.getVertices()) {
				ag.getPanelOCF().setIsInfluentAgent(false);
				ag.getPanelOCF().setIsAgenttoBeChanged(false);
				ag.getPanelOCF().repaint();
			}
			// set all edges to the non triggerred status
			for (MyEdge edge : Global.currentNetwork.getEdges()) {
	    		edge.setIsCurrentlyTriggerred(false);
	    	}
			setEnabledJRadioButtons(true);
			Constants.playMenuItem.setEnabled(true);
			Constants.simulateMenuItem.setText("Start");
			trackedBelief = Formula.getTautology();
			Global.updateInfoMessageLabels();
			Constants.frameMain.repaint();
		}
	}
	
	public static void resetAllOCFPanels () {
		int nbDisplayedOCFs = 0;
		Global.displayedOCFPanel.removeAll();
        
		List<UIAgent> allVerticesOrdered = currentNetwork.getOrderedByIDListAgents();
        for (UIAgent ag : allVerticesOrdered) {
        	if (nbDisplayedOCFs++ >= Global.MAX_OCF_CAN_BE_DISPLAYED) {
        		break;
        	}
        	Global.displayedOCFPanel.add(ag.getPanelOCF());
        	ag.getPanelOCF().repaint();
        }
        
        Global.displayedOCFPanel.revalidate();
		Global.displayedOCFPanel.repaint();
	}

	public static int MAX_OCF_CAN_BE_DISPLAYED = 50;

	public static String intToHexa (int interpretation) {
		return Integer.toHexString(interpretation);
	}
}
