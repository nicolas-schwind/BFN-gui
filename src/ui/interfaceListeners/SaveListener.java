package ui.interfaceListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import graph.UIAgent;
import graph.IO;
import parameters.Constants;
import parameters.Global;

//Save to an XML file
public class SaveListener implements ActionListener {
	public void actionPerformed(ActionEvent arg0) {
		Global.fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "/examples/"));
		int ret = Global.fileChooser.showSaveDialog(Constants.frameMain);
		if(ret == JFileChooser.APPROVE_OPTION) {
    		File file = Global.fileChooser.getSelectedFile();
    		String filename = file.getPath();
    		int i = filename.lastIndexOf('.');
    		if(i < 1) {
    		    filename += ".xml";
    		}
    		IO.write(Global.graphLayout, filename);
    		// add a new Agent to the graph g corresponding to the revision policy, save it, then remove this Agent
    		// TODO
    		/*AgentNode tmpToAdd2 = new AgentNode(new Belief(corr.getId(getNameActiveJRadioButtonRevisionPolicy())));
    		tmpToAdd2.setRepresentsRevisionRule();
    		g.addVertex(tmpToAdd2);
    		IO.write(layout, filename);
    		g.removeVertex(tmpToAdd2);
    		g.removeVertex(tmpToAdd);
    		*/    		
		}
	}
}
