package ui.interfaceListeners.graphGenerationListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import edu.uci.ics.jung.algorithms.layout.Layout;
import graph.AgentGraph;
import graph.UIAgent;
import graph.MyEdge;
import graph.IO;
import parameters.Global;

//Load from an XML file existing in the library
public class LoadInternalListener implements ActionListener {
	
	private String instanceName;
	
	public LoadInternalListener (String instanceName) {
		// instanceName has to be an xml file name existing in the internal library
		this.instanceName = instanceName;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		//Global.fileChooser.setCurrentDirectory(new File(this.getClass().getClassLoader().getResource("").getPath() + "../XmlGraphs/"));
		//Global.fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "/examples/"));
		//Global.graphLayout = IO.read(new File(this.getClass().getClassLoader().getResource("").getPath() + "../XmlGraphs/" + instanceName));
		//Global.graphLayout = IO.read(this.getClass().getClassLoader().getResourceAsStream("/XmlGraphs/" + instanceName));
		//Global.graphLayout = IO.read(this.getClass().getClassLoader().getResourceAsStream("/XmlGraphs/" + instanceName));
		//System.out.println(this.getClass().getClassLoader().getResource("XmlGraphs/default.xml"));
		Global.graphLayout = IO.read(this.getClass().getClassLoader().getResourceAsStream(instanceName));
		//Global.graphLayout = IO.read(this.getClass().getClassLoader().getResource("").getPath() + "XmlGraphs/" + instanceName);
		//Global.graphLayout = IO.read(this.getClass().getClassLoader().getResourceAsStream("../internal-examples/" + instanceName));
		Global.currentNetwork = (AgentGraph) Global.graphLayout.getGraph();
		Global.setJRadioButtonRevisionPolicy(Global.currentNetwork.getGlobalRevisionPolicy().toString());
		Global.setJRadioButtonNbVar(Global.nbInterpretations);
		Global.resetAllOCFPanels();
		Global.vv.refresh(Global.graphLayout);
	}
}
