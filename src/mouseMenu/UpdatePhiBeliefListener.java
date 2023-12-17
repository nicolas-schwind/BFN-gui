package mouseMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import belief.OCF;

public class UpdatePhiBeliefListener implements ActionListener {

	private OCF trackedOCF;
	private JFrame frame;
	
	public UpdatePhiBeliefListener (OCF trackedOCF, JFrame frame) {
		this.trackedOCF = trackedOCF;
		this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO
		// update beliefs of tracked Belief wrt selected rows in tableInterpretationsPhi
		
		//int[] selectedRows = tableInterpretationsPhi.getSelectedRows(); 
		//for (int i = 0; i < selectedRows.length; i++) {
			//System.out.println("selected Row " + selectedRows[i]);
			//trackedBelief.setInterpretation(Belief.NB_INTERPRETATIONS-1-selectedRows[i], Belief.B_TRUE);
		//}
		
		frame.repaint();
	}

}
