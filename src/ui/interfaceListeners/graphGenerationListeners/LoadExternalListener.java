package ui.interfaceListeners.graphGenerationListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import edu.uci.ics.jung.algorithms.layout.Layout;
import graph.AgentGraph;
import graph.UIAgent;
import graph.MyEdge;
import graph.IO;
import parameters.Constants;
import parameters.Global;

//Load from an XML file
public class LoadExternalListener implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
		Global.fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "/examples/"));
		int ret = Global.fileChooser.showOpenDialog(Constants.frameMain);
		if (ret == JFileChooser.APPROVE_OPTION) {
			File file = Global.fileChooser.getSelectedFile();
    		String filename = file.getPath();
			Global.graphLayout = IO.read(filename);
			Global.currentNetwork = (AgentGraph) Global.graphLayout.getGraph();
			Global.setJRadioButtonRevisionPolicy(Global.currentNetwork.getGlobalRevisionPolicy().toString());
			Global.setJRadioButtonNbVar(Global.nbInterpretations);
			Global.resetAllOCFPanels();
			Global.vv.refresh(Global.graphLayout);
		}
	}
}
