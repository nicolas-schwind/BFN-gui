package ui.interfaceListeners;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import belief.OCF;
import edu.uci.ics.jung.visualization.picking.PickedState;
import graph.UIAgent;
import parameters.Constants;
import parameters.Global;
import tools.EnterButton;
import tools.EnterCheckBox;

public class ModifyAgentBeliefListener implements ActionListener {
	private JFrame subframe;
	private JButton okButton;
	private List<EnterCheckBox> worldCheckBoxList;
	
	private PickedState<UIAgent> picked;
	private Set<UIAgent> pickedAgentSet;
	
	public ModifyAgentBeliefListener (PickedState<UIAgent> picked) {
		this.picked = picked;
	}
	
	public void actionPerformed (ActionEvent arg0) {
		this.pickedAgentSet = picked.getPicked();
		
		if (pickedAgentSet.isEmpty()) {
			return;
		}
		
		JPanel container = new JPanel(new FlowLayout(FlowLayout.CENTER));
		//JPanel container = new JPanel();
		//container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		subframe = new JFrame("Modify agent's belief");
		subframe.setSize(260, 150);
		subframe.setLocationRelativeTo(Constants.frameMain);
		okButton = new EnterButton("Ok");
		JPanel worldPanel;
		worldPanel = new JPanel(new GridLayout(4, Global.nbInterpretations / 4));
		worldCheckBoxList = new ArrayList<EnterCheckBox>();
		
		EnterCheckBox currentCheckBox;
		for (int i = 0; i < Global.nbInterpretations; i++) {
			currentCheckBox = new EnterCheckBox(Global.intToHexa(i));
			worldCheckBoxList.add(currentCheckBox);
			worldPanel.add(currentCheckBox);
		}
		
		container.add(worldPanel);
		container.add(okButton);
		subframe.add(container);
		subframe.getRootPane().setDefaultButton(okButton);
		//subframe.pack();
		subframe.setVisible(true);
		
		okButton.addActionListener(new ButtonListener(this.pickedAgentSet));
	}
	
	private class ButtonListener implements ActionListener {
		
		private Set<UIAgent> pickedAgentSet;
		
		private ButtonListener(Set<UIAgent> pickedAgentSet) {
			this.pickedAgentSet = pickedAgentSet;
		}
		
		public void actionPerformed(ActionEvent arg0) {
			int[] ocf;
			int idWorld;
			
			// at least one check box must be checked
			boolean atLeastOneCheckBoxIsChecked = false;
			for (EnterCheckBox checkBox : worldCheckBoxList) {
				if (checkBox.isSelected()) {
					atLeastOneCheckBoxIsChecked = true;
					break;
				}
			}
			if (atLeastOneCheckBoxIsChecked) {
				// create an OCF according to the check boxes
				ocf = new int[Global.nbInterpretations];
				idWorld = 0;
				for (EnterCheckBox checkBox : worldCheckBoxList) {
					if (checkBox.isSelected()) {
						ocf[idWorld] = 0;
						//System.out.println("world " + idWorld + " YES");
					}
					else {
						ocf[idWorld] = OCF.ENTRENCHMENT_LEVEL;
						//System.out.println("world " + idWorld + " NO");
					}
					idWorld++;
				}
				// assign this OCF to all picked agents (typically, at most one agent only is picked)
				//System.out.println("picked agents:");
				for (UIAgent ag : this.pickedAgentSet) {
					System.out.println(ag.getId());
					ag.setOCF(new OCF(ocf));
					ag.getPanelOCF().setOCF(ag.getOCF());
					//System.out.println(ag.getOCF());
				}
			}
			
			Global.resetAllOCFPanels();
			
			subframe.setVisible(false);
			Constants.frameMain.repaint();
		}
	}
}
