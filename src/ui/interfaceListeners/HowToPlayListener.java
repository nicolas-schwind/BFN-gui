package ui.interfaceListeners;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import parameters.Constants;
import tools.EnterButton;

public class HowToPlayListener implements ActionListener {

	private JTextPane textArea;
	private JScrollPane scrollPane;
	private JFrame subframe;
	private JButton okButton;

	public HowToPlayListener () {
		textArea = new JTextPane();
		textArea.setBounds(0, 0, 500, 350);
		textArea.setContentType("text/html");
		textArea.setEditable(false);
		scrollPane = new JScrollPane(textArea);
		
		StringBuilder builder = new StringBuilder();
		builder.append("<html>\n");
		builder.append("<h1>Welcome to the BRG software</h1>"
				+ "<b>This software allows one to play any BRG where:</b><ul style=\"list-style-type:disc\">"
				+ "<li><b>all agents apply the same revision policy, and</b></li>"
				+ "<li><b>the propositional language is based on four propositional variables {a, b, c, d}.</b></li</ul>");
		
		builder.append("<hr size=5 width=100% align=left>");
		
		builder.append("<h1>Quick start</h1><ul style=\"list-style-type:disc\">"
				+ "<li>The software lauches with a \"default\" BRG.</li>"
				+ "<li>The left panel displays the graph of the BRG.</li>"
				+ "<li>The right panel displays two tables:"
				+ "<ul style=\"list-style-type:circle\">"
				+ "<li>the table on the left is associated with the belief base associated with some agent;</li>"
				+ "<li>the table on the right is "
				+ "associated with some \"tracked formula.\"</li></ul></li>"
				//+ "For both tables the associated belief base / tracked formula is identified the set of its models, which corresponds to the set of the "
				//+ "selected lines in the table (each line represents one interpretation). There is only one tracked belief (associated with the table on the right) "
				//+ "at a time which is independent from the BRG. The models of this tracked belief can be modified at any time by selecting/deselecting lines"
				//+ "from the table. "
				//+ "The table on the left represents the belief base of some <i>selected</i> agent. An agent "
				//+ "can be selected by clicking on the corresponding node in the graph. Doing so, the models of her belief base will be displayed in"
				//+ "the table on the left. "
				//+ "As for the tracked belief, the models of the belief base of the selected agent can be modified by selecting/deselecting some lines from the table. "
				+ "<li>Below the tables the revision policy (which will be the one applied for all agents) can be chosen among 18 ones.</li>"
				+ "<li>The BRG can be run (thus switching to the \"run\" mode) with these settings in two ways:"
				+ "<ul style=\"list-style-type:circle\">"
				+ "<li>by pressing the space key (or menu \"Run\" + \"Play by hand\") "
				+ "then using the right key (or menu \"Run\" + \"Next step\") to go to the next step, and the "
				+ "left key (or menu \"Run\" + \"Previous step\") to go back to the previous step; \"stopping the game\" "
				+ "to go back to the edition mode can be done by pressing the space key (or menu \"Run\" + \"Play by hand\");</li>"
				+ "<li>by pressing the key \"Return\" (or menu \"Run\" + \"Play automatically\"); \"stopping the game\" "
				+ "to go back to the edition mode can be done by pressing the space \"Return\" (or menu \"Run\" + \"Stop\").</li></ul>"
				+ "<li>When the game is stopped, one switches to the \"edition\" mode.</li></ul>");
		
		builder.append("<hr size=5 width=100% align=left>");
		
		builder.append("<h1>On Selected Agent's Beliefs and Tracked Belief</h1><ul style=\"list-style-type:disc\">"
				+ "<li> For both tables the associated belief base / tracked formula is identified by the set of its models, represented through the "
				+ "selected lines in the table (each line represents one interpretation).</li>"
				+ "<li>The table on the right represents the tracked belief. This formula is independent from the BRG. "
				+ "The models of this tracked belief can be modified at any time by selecting/deselecting lines "
				+ "from the table. Then at each step, the color of each node is:"
				+ "<ul style=\"list-style-type:circle\">"
				+ "<li>blue if the corresponding agent's belief base <i>accepts</i> the tracked belief (i.e., all models of the agent's belief base are "
				+ "models of the tracked belief);</li>"
				+ "<li>red if the corresponding agent's belief <i>accepts the negation of</i> the tracked belief (i.e., no model of the agent's belief base is "
				+ "a model of the tracked belief);</li>"
				+ "<li>gray otherwise.</li></ul>"
				+ "<li>The table on the left represents the belief base of some <i>selected</i> agent. An agent "
				+ "can be selected by clicking on the corresponding node in the graph. Doing so, the models of her belief base will be displayed within "
				+ "the table on the left. "
				+ "As for the tracked belief, the models of the belief base of the selected agent can be modified by"
				+ "selecting/deselecting some lines from the table.</li>"
				+ "<li>Note that when the game is in \"run\" mode, only the tracked belief can be modified. The selected agent's belief base can be "
				+ "only checked in the run mode, but can be modified (as well as the revision policy) in the edition mode.</li></ul>");
		
		builder.append("<hr size=5 width=100% align=left>");
		
		builder.append("<h1>Edition mode</h1><ul style=\"list-style-type:disc\">");
		builder.append("<li>The current BRG can be modified:"
				+ "<ul style=\"list-style-type:circle\">"
				+ "<li>nodes and edges can be deleted by right-clicking on them + \"Delete vertex/edge\";</li>"
				+ "<li>nodes and edges can be added by changing the mouse mode (menu \"Mouse mode\" + \"EDITING\").</li></ul>");
		builder.append("<li>The current BRG can be saved (menu \"Edition\" + \"Save\"). Note that the graph structure, the belief base of each agent, "
				+ "the graph display, the tracked belief and the revision policy are all saved.</li>");
		builder.append("<li>A previously saved BRG can be loaded (menu \"Edition\" + \"Load \" + \"Load from file\").</li>");
		builder.append("<li>A new graph can be generated in two ways:");
		builder.append("<ul style=\"list-style-type:circle\">");
		builder.append("<li>through the menu \"Edition\" + \"New graph\";</li>");
		builder.append("<li>through the menu \"Edition\" + \"Load\" + \"Load from library\", which proposes a set of predefined BRG instances (including the one loaded "
				+ "when the software is launched).</li></ul></li>"
				+ "<li>By default, the belief base of each agent is assigned to a \"random\" belief base. More precisely, each interpretation among "
				+ "the 16 ones has a probability of 0.2 to be a model of each agent's belief base. A new belief base random assignment for all agents can be done "
				+ " through the menu \"Edition\" + \"Randomize Beliefs\".</li></ul>");
		
		builder.append("<hr size=5 width=100% align=left>");
		
		builder.append("<h1>Run mode</h1><ul style=\"list-style-type:disc\">");
		builder.append("<li>When one switches to the run mode (menu \"Run\" + \"Play by hand\" or \"Play automatically\"), "
				+ "only the tracked belief can be modified. The belief based of each agent can be checked by clicking on the corresponding node.</li>"
				+ "<li>The BRG can be reseted to step 0 by pressing the key \"R\" (or menu \"Run\" + \"Reset to step 0\").</li></ul>");
		
		textArea.setText(builder.toString());
	}
	
	public void actionPerformed(ActionEvent arg0) {
		JPanel container = new JPanel(new BorderLayout());
		subframe = new JFrame("How to play");
		subframe.setSize(750, 500);
		subframe.setLocationRelativeTo(Constants.frameMain);
		okButton = new EnterButton("Ok");
		container.add(scrollPane, BorderLayout.CENTER);
		container.add(okButton, BorderLayout.SOUTH);
		subframe.add(container);
		subframe.setVisible(true);
		okButton.addActionListener(new ButtonListener());
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			try {
    			subframe.setVisible(false);
			}
			catch(java.lang.NumberFormatException e){}
		}
	}
}