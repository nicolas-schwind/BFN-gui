package ui;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import graph.AgentGraph;
import graph.UIAgent;
import graph.MyEdge;
import graph.IO;
import graphTologyGeneration.Generator;
import mouseMenu.MousePlugin;
import mouseMenu.MyEditingModalGraphMouse;
import mouseMenu.VertexListener;
import parameters.Constants;
import parameters.Global;
import tools.CorrespondenceJRadioButtonNameId;
import tools.EnterButton;
import ui.interfaceListeners.DisplayOCFListener;
import ui.interfaceListeners.DisplayLogInfoListener;
import ui.interfaceListeners.HowToPlayListener;
import ui.interfaceListeners.ModifyAgentBeliefListener;
import ui.interfaceListeners.NbVarChoiceListener;
import ui.interfaceListeners.NbVarJRadioButtonListener;
import ui.interfaceListeners.RandomizeBeliefsForceConsistentListener;
import ui.interfaceListeners.RandomizeBeliefsForceInconsistentListener;
import ui.interfaceListeners.RandomizeBeliefsListener;
import ui.interfaceListeners.RefreshLayoutListener;
import ui.interfaceListeners.RefreshOCFPanelListener;
import ui.interfaceListeners.RevisionRulesJRadioButtonListener;
import ui.interfaceListeners.SaveListener;
import ui.interfaceListeners.SymmetricClosureListener;
import ui.interfaceListeners.gameRunListeners.FastBackwardPositionListener;
import ui.interfaceListeners.gameRunListeners.FastForwardPositionListener;
import ui.interfaceListeners.gameRunListeners.JumpLastPositionListener;
import ui.interfaceListeners.gameRunListeners.NextPositionListener;
import ui.interfaceListeners.gameRunListeners.PreviousPositionListener;
import ui.interfaceListeners.gameRunListeners.ReturnToInitialPositionListener;
import ui.interfaceListeners.gameRunListeners.SwitchSimulationModeListener;
import ui.interfaceListeners.graphGenerationListeners.BarabasiAlbertGraphListener;
import ui.interfaceListeners.graphGenerationListeners.CyclicGraphListener;
import ui.interfaceListeners.graphGenerationListeners.DirectedAcyclicGraphListener;
import ui.interfaceListeners.graphGenerationListeners.KleinbergGraphListener;
import ui.interfaceListeners.graphGenerationListeners.LineGraphListener;
import ui.interfaceListeners.graphGenerationListeners.LoadExternalListener;
import ui.interfaceListeners.graphGenerationListeners.LoadInternalListener;
import ui.interfaceListeners.graphGenerationListeners.RandomGraphListener;
import ui.interfaceListeners.graphGenerationListeners.SymmetricCliqueGraphListener;
import ui.interfaceListeners.graphGenerationListeners.SymmetricRandomGraphListener;
import vertexEdgeDisplay.EdgeDrawPaint;
import vertexEdgeDisplay.EdgeLabel;
import vertexEdgeDisplay.VertexDrawPaint;
import vertexEdgeDisplay.VertexFillPaint;
import vertexEdgeDisplay.VertexShape;
import vertexEdgeDisplay.VertexStroke;
import belief.Formula;
import belief.OCF;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;

//Provide a view of an agent graph enclosed in a frame
public class UserInterface {
	
	public UserInterface() {}
	
	public void init() {
		Global.initParameters();
		Constants.frameMain = new JFrame("Belief Improvement Game");
		
		// The filechooser for saving and loading
		Global.fileChooser = new JFileChooser();
		Global.fileChooser.setFileFilter(new FileNameExtensionFilter("Extensible Markup Language", "xml"));
		
		// The Layout<V, E> is parameterized by the vertex and edge types
		Global.graphLayout = new FRLayout<UIAgent,MyEdge>(Global.currentNetwork);
		Global.graphLayout.setSize(new Dimension(700,500)); // sets the initial size of the space
		
		Global.frameDisplayedOCF.setPreferredSize(new Dimension(800,600));
		Global.displayedOCFPanel = new JPanel(new GridLayout(5, 10));
		
		// The VisualizationViewer<V,E> is parameterized by the edge types
		Global.vv = new MyVisualizationViewer(Global.graphLayout);
		Global.vv.setPreferredSize(new Dimension(750,550)); //Sets the viewing area size
		// set the belief to be tracked to the 
		Global.pickedAgentInterface = Global.vv.getPickedVertexState();
		
		// save the default shape and drawpaint transformers before setting custom ones
		Global.defaultVertexDrawPaintTransformer = Global.vv.getRenderContext().getVertexDrawPaintTransformer();
		Global.defaultVertexShapeTransformer = Global.vv.getRenderContext().getVertexShapeTransformer();
		Global.defaultEdgeDrawPaintTransformer = Global.vv.getRenderContext().getEdgeDrawPaintTransformer();
		// adding custom transformers to control the fill and draw colors of a node
		Global.vv.getRenderContext().setVertexFillPaintTransformer(new VertexFillPaint(Global.pickedAgentInterface));
		Global.vv.getRenderContext().setVertexDrawPaintTransformer(new VertexDrawPaint());
		// adding a custom transformer to control the stroke for the perimeter of a node
		Global.vv.getRenderContext().setVertexStrokeTransformer(new VertexStroke());
		// adding a custom transformer to control the shape of a node
		Global.vv.getRenderContext().setVertexShapeTransformer(new VertexShape());
		// adding a custom transformer to control the color of an edge
		Global.vv.getRenderContext().setEdgeDrawPaintTransformer(new EdgeDrawPaint());
		// adding a custom transformer to control the label of an edge
		Global.vv.getRenderContext().setEdgeLabelTransformer(new EdgeLabel());
		
		Global.vv.getRenderContext().setVertexLabelTransformer(new Transformer<UIAgent, String>() {
            public String transform(UIAgent a)
            {
                return String.valueOf(a.getId());
            }
        });
		//Global.vv.setBackground(Color.getHSBColor(255, 245, 155));
		Global.vv.setBackground(new Color(250, 250, 250));
		
		// Shows the edge's weight
		Global.vv.getRenderContext().setEdgeLabelTransformer(
				new Transformer<MyEdge, String>() {
					public String transform(MyEdge e) {
						return "";
						}
					}
		);
		
		// Create a graph mouse and add it to the visualization component
		Global.editingModelGraphMouse = new MyEditingModalGraphMouse<UIAgent, MyEdge>
			(Global.vv.getRenderContext(), AgentGraph.vertexFactory, AgentGraph.edgeFactory);
		Global.mousePlugin = new MousePlugin<UIAgent, MyEdge>();
		// Add some popup menus for the edges and vertices to our mouse plugin.
        JPopupMenu edgeMenu = new mouseMenu.Menus.EdgeMenu<MyEdge>();
        JPopupMenu vertexMenu = new mouseMenu.Menus.VertexMenu<UIAgent>();
        Global.mousePlugin.setEdgePopup(edgeMenu);
        Global.mousePlugin.setVertexPopup(vertexMenu);
        Global.mousePlugin.setEnabled(true);
        // Remove the existing popup editing plugin
        Global.editingModelGraphMouse.remove(Global.editingModelGraphMouse.getPopupEditingPlugin());
        // Add our new plugin to the mouse
        Global.editingModelGraphMouse.add(Global.mousePlugin);
        Global.vv.setGraphMouse(Global.editingModelGraphMouse);
		
		// Set menu bar & items
        Constants.nextMenuItem.setEnabled(false);
		Constants.previousMenuItem.setEnabled(false);
		Constants.fastForwardMenuItem.setEnabled(false);
		Constants.fastBackwardMenuItem.setEnabled(false);
		Constants.jumpLastMenuItem.setEnabled(false);
		Constants.playMenuItem.setEnabled(false);
		
		Constants.modeMenu = Global.editingModelGraphMouse.getModeMenu();
		
		Constants.newGraphMenu.add(Constants.randomGraphMenuItem);
		Constants.newGraphMenu.add(Constants.cyclicGraphMenuItem);
		Constants.newGraphMenu.add(Constants.lineGraphMenuItem);
		//Constants.newGraphMenu.add(Constants.directedAcyclicGraphMenuItem);
		Constants.newGraphMenu.add(Constants.barabasiAlbertMenuItem);
		Constants.newGraphMenu.add(Constants.kleinbergMenuItem);
		
		Constants.barabasiAlbertMenuItem.addActionListener(new BarabasiAlbertGraphListener());
		Constants.kleinbergMenuItem.addActionListener(new KleinbergGraphListener());
		Constants.symCliqueMenuItem.addActionListener(new SymmetricCliqueGraphListener());
		Constants.randomGraphMenuItem.addActionListener(new RandomGraphListener());
		Constants.directedAcyclicGraphMenuItem.addActionListener(new DirectedAcyclicGraphListener());
		Constants.symRandomGraphMenuItem.addActionListener(new SymmetricRandomGraphListener());
		Constants.currentGraphSymmetricClosureMenuItem.addActionListener(new SymmetricClosureListener());
		Constants.cyclicGraphMenuItem.addActionListener(new CyclicGraphListener());
		Constants.lineGraphMenuItem.addActionListener(new LineGraphListener());
		Constants.randomizeMenuItem.addActionListener(new RandomizeBeliefsListener());
		Constants.randomizeForceConsistentMenuItem.addActionListener(new RandomizeBeliefsForceConsistentListener());
		Constants.randomizeForceInconsistentMenuItem.addActionListener(new RandomizeBeliefsForceInconsistentListener());
		Constants.modifyAgentBeliefMenuItem.addActionListener(new ModifyAgentBeliefListener(Global.pickedAgentInterface));
		Constants.resetMenuItem.addActionListener(new ReturnToInitialPositionListener());
		Constants.resetMenuItem.setEnabled(false);
		Constants.refreshMenuItem.addActionListener(new RefreshLayoutListener());
		Constants.refreshOCFPanelItem.addActionListener(new RefreshOCFPanelListener());
		Constants.saveMenuItem.addActionListener(new SaveListener());
        
		Constants.examplesInternalLibraryMenuItemList = new Vector<JMenuItem>();
		JMenuItem example1, example2;
		example1 = new JMenuItem("default BFN when launching software");
		example1.addActionListener(new LoadInternalListener("default.xml"));
		Constants.examplesInternalLibraryMenuItemList.add(example1);
		example2 = new JMenuItem("BFN from running example");
		example2.addActionListener(new LoadInternalListener("running-example.xml"));
		Constants.examplesInternalLibraryMenuItemList.add(example2);
		// If other default examples need to be added, use the following three lines of code 
		// example = new JMenuItem("another BRG");
		// example.addActionListener(new LoadInternalListener("another_brg.xml"));
		// Constants.examplesInternalLibraryMenuItemList.add(example);
		
		// add all library examples as JMenuItem in loadInternal JMenu
		for (JMenuItem item : Constants.examplesInternalLibraryMenuItemList) {
			Constants.loadInternalMenu.add(item);
		}
		
		Constants.loadExternalMenuItem.addActionListener(new LoadExternalListener());
		Constants.loadMenu.add(Constants.loadInternalMenu);
		Constants.loadMenu.add(Constants.loadExternalMenuItem);
		
		Constants.nextMenuItem.addActionListener(new NextPositionListener());
		Constants.previousMenuItem.addActionListener(new PreviousPositionListener());
		Constants.simulateMenuItem.addActionListener(new SwitchSimulationModeListener());
		Constants.fastForwardMenuItem.addActionListener(new FastForwardPositionListener());
		Constants.fastBackwardMenuItem.addActionListener(new FastBackwardPositionListener());
		Constants.jumpLastMenuItem.addActionListener(new JumpLastPositionListener());
		Constants.displayOCFMenuItem.addActionListener(new DisplayOCFListener());
		Constants.displayLogFrameMenuItem.addActionListener(new DisplayLogInfoListener());
        
        Constants.editionMenu.add(Constants.newGraphMenu);
        Constants.editionMenu.add(Constants.currentGraphSymmetricClosureMenuItem);
        Constants.editionMenu.addSeparator();
        Constants.editionMenu.add(Constants.randomizeMenuItem);
        //Constants.editionMenu.add(Constants.randomizeForceConsistentMenuItem);
        //Constants.editionMenu.add(Constants.randomizeForceInconsistentMenuItem);
        Constants.editionMenu.add(Constants.modifyAgentBeliefMenuItem);
        Constants.editionMenu.addSeparator();
        Constants.editionMenu.add(Constants.refreshMenuItem);
        Constants.editionMenu.add(Constants.refreshOCFPanelItem);
        Constants.editionMenu.addSeparator();
        Constants.editionMenu.add(Constants.saveMenuItem);
        Constants.editionMenu.add(Constants.loadMenu);
        Constants.runMenu.add(Constants.simulateMenuItem);
        Constants.runMenu.addSeparator();
        Constants.runMenu.add(Constants.nextMenuItem);
        Constants.runMenu.add(Constants.previousMenuItem);
        Constants.runMenu.add(Constants.fastForwardMenuItem);
        Constants.runMenu.add(Constants.fastBackwardMenuItem);
        //Constants.runMenu.addSeparator();
        //Constants.runMenu.add(Constants.playMenuItem);
        //Constants.runMenu.addSeparator();
        Constants.runMenu.add(Constants.resetMenuItem);
        Constants.runMenu.add(Constants.jumpLastMenuItem);
        Constants.infoMenu.add(Constants.displayOCFMenuItem);
        //Constants.infoMenu.add(Constants.displayLogFrameMenuItem);
        Constants.modeMenu.setText("Mouse mode");
        Constants.modeMenu.setIcon(null);
        Constants.modeMenu.setPreferredSize(new Dimension(100,20));
        
        Constants.menuBar.add(Constants.editionMenu);
        Constants.menuBar.add(Constants.runMenu);
        Constants.menuBar.add(Constants.infoMenu);
        Constants.menuBar.add(Constants.modeMenu);
        
        // how to play part
        Constants.howToPlayMenuItem.addActionListener(new HowToPlayListener());
        Constants.helpMenu.add(Constants.howToPlayMenuItem);
        //Constants.menuBar.add(Constants.helpMenu);
        KeyStroke helpShortcut = KeyStroke.getKeyStroke(KeyEvent.VK_H, 0);
        Constants.howToPlayMenuItem.setAccelerator(helpShortcut);
        
        // assigning keyboard shortcuts to menu items
		Constants.simulateMenuItem.setAccelerator(Constants.simulateKeyStroke);
		Constants.nextMenuItem.setAccelerator(Constants.rightKeyStroke);
		Constants.previousMenuItem.setAccelerator(Constants.leftKeyStroke);
		Constants.fastForwardMenuItem.setAccelerator(Constants.fastRightKeyStroke);
		Constants.fastBackwardMenuItem.setAccelerator(Constants.fastLeftKeyStroke);
		Constants.jumpLastMenuItem.setAccelerator(Constants.jumpToLastKeyStroke);
		Constants.randomGraphMenuItem.setAccelerator(Constants.generateRandomGraphKeyStroke);
		Constants.cyclicGraphMenuItem.setAccelerator(Constants.generateCyclicGraphKeyStroke);
		Constants.lineGraphMenuItem.setAccelerator(Constants.generateLineGraphKeyStroke);
		Constants.directedAcyclicGraphMenuItem.setAccelerator(Constants.generateDAGGraphKeyStroke);
		Constants.barabasiAlbertMenuItem.setAccelerator(Constants.generateBarabasiAlbertGraphKeyStroke);
		Constants.kleinbergMenuItem.setAccelerator(Constants.generateKleinbergGraphKeyStroke);
		Constants.loadExternalMenuItem.setAccelerator(Constants.loadExternalKeyStroke);
		Constants.randomizeMenuItem.setAccelerator(Constants.randomizeBeliefsKeyStroke);
		Constants.randomizeForceConsistentMenuItem.setAccelerator(Constants.randomizeBeliefsForceConsistentKeyStroke);
		Constants.randomizeForceInconsistentMenuItem.setAccelerator(Constants.randomizeBeliefsForceInconsistentKeyStroke);
        Constants.resetMenuItem.setAccelerator(Constants.resetKeyStroke);
        Constants.refreshOCFPanelItem.setAccelerator(Constants.refreshOCFPanelKeyStroke);
        Constants.saveMenuItem.setAccelerator(Constants.saveKeyStroke);
        Constants.symClosureMenuItem.setAccelerator(Constants.symmetricClosureKeyStroke);
        Constants.displayOCFMenuItem.setAccelerator(Constants.displayOCFKeyStroke);
        Constants.displayLogFrameMenuItem.setAccelerator(Constants.displayLogFrameKeyStroke);
        Constants.modifyAgentBeliefMenuItem.setAccelerator(Constants.modifyAgentBeliefKeyStroke);
		
		// Subframes settings
        Constants.nTextField.setFont(Constants.userInterfaceFont);
        Constants.densityTextField.setFont(Constants.userInterfaceFont);
		Constants.kTextField.setFont(Constants.userInterfaceFont);
		Constants.pTextField.setFont(Constants.userInterfaceFont);
		Constants.nbStrongestSCCsNonSingletonLabel.setFont(Constants.userInterfaceFontItalic);
		Constants.nbStrongestSCCsSingletonLabel.setFont(Constants.userInterfaceFontItalic);
		Constants.nbSCCsLabel.setFont(Constants.userInterfaceFontItalic);
		Constants.checkForSymmetricLabel.setFont(Constants.userInterfaceFontItalic);
		Constants.nTextField.setPreferredSize(new Dimension(22, 20));
		Constants.densityTextField.setPreferredSize(new Dimension(22, 20));
		Constants.kTextField.setPreferredSize(new Dimension(22, 20));
		Constants.pTextField.setPreferredSize(new Dimension(22, 20));
		Constants.nPanel.add(Constants.nLabel);
		Constants.nPanel.add(Constants.nTextField);
		Constants.densityPanel.add(Constants.densityLabel);
		Constants.densityPanel.add(Constants.densityTextField);
		Constants.kPanel.add(Constants.kLabel);
		Constants.kPanel.add(Constants.kTextField);
		Constants.pPanel.add(Constants.pLabel);
		Constants.pPanel.add(Constants.pTextField);
		Constants.nbStrongestSCCsNonSingletonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		Constants.nbStrongestSCCsNonSingletonPanel.add(Constants.nbStrongestSCCsNonSingletonLabel);
		Constants.nbStrongestSCCsNonSingletonPanel.add(Constants.minNbStrongestSCCsNonSingletonTextField);
		Constants.nbStrongestSCCsNonSingletonPanel.add(Constants.maxNbStrongestSCCsNonSingletonTextField);
		Constants.nbStrongestSCCsSingletonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		Constants.nbStrongestSCCsSingletonPanel.add(Constants.nbStrongestSCCsSingletonLabel);
		Constants.nbStrongestSCCsSingletonPanel.add(Constants.minNbStrongestSCCsSingletonTextField);
		Constants.nbStrongestSCCsSingletonPanel.add(Constants.maxNbStrongestSCCsSingletonTextField);
		Constants.nbSCCsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		Constants.nbSCCsPanel.add(Constants.nbSCCsLabel);
		Constants.nbSCCsPanel.add(Constants.minNbSCCsTextField);
		Constants.nbSCCsPanel.add(Constants.maxNbSCCsTextField);
		Constants.checkForSymmetricPanel.add(Constants.checkForSymmetricLabel);
		Constants.checkForSymmetricPanel.add(Constants.symmetricCheckBox);
		Constants.initialSeedSizePanel.add(Constants.initialSeedSizeLabel);
		Constants.initialSeedSizePanel.add(Constants.initialSeedSizeTextField);
		Constants.numLinksAddedPanel.add(Constants.numLinksAddedLabel);
		Constants.numLinksAddedPanel.add(Constants.numLinksAddedTextField);
		Constants.nbRowsColsPanel.add(Constants.nbRowsColLabel);
		Constants.nbRowsColsPanel.add(Constants.nbRowsTextField);
		Constants.nbRowsColsPanel.add(Constants.nbColsTextField);
		Constants.clusteringExpPanel.add(Constants.clusteringExpLabel);
		Constants.clusteringExpPanel.add(Constants.clusteringExpTextField);
		
		JPanel radioButtonsPanel = new JPanel();
		radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel, BoxLayout.Y_AXIS));
		Box nbVarButtonsBox = Box.createVerticalBox();
        nbVarButtonsBox.add(Global.buttonNbVar2);
        nbVarButtonsBox.add(Global.buttonNbVar4);
        nbVarButtonsBox.add(Global.buttonNbVar8);
        NbVarJRadioButtonListener nbVarActionListener = new NbVarJRadioButtonListener();
        Global.buttonNbVar2.addActionListener(nbVarActionListener);
        Global.buttonNbVar4.addActionListener(nbVarActionListener);
        Global.buttonNbVar8.addActionListener(nbVarActionListener);
        radioButtonsPanel.add(new JLabel("Nb variables:"));
        radioButtonsPanel.add(nbVarButtonsBox);
		
        Box revPolicyButtonsBox = Box.createVerticalBox();
        //buttonsBox.add(Global.buttonDrasticMerging);
        revPolicyButtonsBox.add(Global.buttonOneImprovement);
        //buttonsBox.add(Global.buttonBoutilier);
        revPolicyButtonsBox.add(Global.buttonRestrained);
        revPolicyButtonsBox.add(Global.buttonNayak);
        //buttonsBox.add(Global.buttonSKPBulletPlus);
        Global.buttonDrasticMerging.addActionListener(new RevisionRulesJRadioButtonListener());
        Global.buttonOneImprovement.addActionListener(new RevisionRulesJRadioButtonListener());
        Global.buttonBoutilier.addActionListener(new RevisionRulesJRadioButtonListener());
        Global.buttonNayak.addActionListener(new RevisionRulesJRadioButtonListener());
        Global.buttonRestrained.addActionListener(new RevisionRulesJRadioButtonListener());
        Global.buttonSKPBulletPlus.addActionListener(new RevisionRulesJRadioButtonListener());
        radioButtonsPanel.add(new JLabel("Change policy:"));
        radioButtonsPanel.add(revPolicyButtonsBox);
        
        
        
        
        //String nbVarChoices[] = {"2", "3", "4", "5", "6", "7", "8"};
        //JComboBox<String> nbVarComboBox = new JComboBox<String>(nbVarChoices);
        //nbVarComboBox.setMaximumSize(nbVarComboBox.getPreferredSize());
        //nbVarComboBox.addItemListener(new NbVarChoiceListener(nbVarComboBox));
        
        Global.initInfoMessageLabels();
        Global.panelRight.setLayout(new BoxLayout(Global.panelRight, BoxLayout.Y_AXIS));
        Global.panelRight.add(Global.messageInfoEditionOrRunMode);
        Global.panelRight.add(Global.messageInfoStabilityReached);
        //Global.panelRight.add(Global.messageInfoConsensusReached);
        Global.panelRight.add(Global.messageInfoUnanimous);
        Global.panelRight.add(Global.messageInfoRunPosition);
        //Global.panelRight.add(nbVarButtonsBox);
        Global.panelRight.add(radioButtonsPanel);
        //Global.panelRight.add(buttonsBox);
        
        
		// Setting the frame parameters
        Constants.frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GridBagConstraints gridConstraints = new GridBagConstraints();
        Constants.frameMain.getContentPane().setLayout(new GridBagLayout());
        gridConstraints.fill = GridBagConstraints.BOTH;
        gridConstraints.weightx = 1.0;
        gridConstraints.weighty = 1.0;
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 0;
        gridConstraints.gridwidth = 10;
        gridConstraints.gridheight = 1;
        Constants.frameMain.getContentPane().add(Global.vv,gridConstraints);
        gridConstraints.fill = GridBagConstraints.ABOVE_BASELINE;
        gridConstraints.weightx = 0.0;
        gridConstraints.weighty = 1.0;
        gridConstraints.gridx = 10;
        gridConstraints.gridy = 0;
        gridConstraints.gridwidth = 1;
        gridConstraints.gridheight = 1;
        
        //Global.buttonGroupRevisionPolicies.add(Global.buttonDrasticMerging);
        Global.buttonGroupRevisionPolicies.add(Global.buttonOneImprovement);
        //Global.buttonGroupRevisionPolicies.add(Global.buttonBoutilier);
        Global.buttonGroupRevisionPolicies.add(Global.buttonRestrained);
        Global.buttonGroupRevisionPolicies.add(Global.buttonNayak);
        //Global.buttonGroupRevisionPolicies.add(Global.buttonSKPBulletPlus);
        Global.buttonOneImprovement.doClick();
        
        Global.buttonGroupNbVar.add(Global.buttonNbVar2);
        Global.buttonGroupNbVar.add(Global.buttonNbVar4);
        Global.buttonGroupNbVar.add(Global.buttonNbVar8);
        Global.buttonNbVar4.doClick();
        
        Constants.frameMain.getContentPane().add(Global.panelRight, gridConstraints);
        Constants.frameMain.setJMenuBar(Constants.menuBar);
        Constants.frameMain.pack();
        
		Constants.frameMain.setVisible(true);
		
		// Start in editing mode
		Global.editingModelGraphMouse.setMode(EditingModalGraphMouse.Mode.PICKING);
		
		Global.pickedAgentInterface.addItemListener(new VertexListener(Global.pickedAgentInterface));
		
        Global.frameDisplayedOCF.getContentPane().add(Global.displayedOCFPanel, BorderLayout.CENTER);
        Global.frameDisplayedOCF.setSize(1200, 800);
        Global.frameDisplayedOCF.setVisible(false);
        
        // put the focus on the main frame
        Constants.frameMain.toFront();
        Constants.frameMain.requestFocus();
		
		Constants.examplesInternalLibraryMenuItemList.get(0).doClick();
	}
}
