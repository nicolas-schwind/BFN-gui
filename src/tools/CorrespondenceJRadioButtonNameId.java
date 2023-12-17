package tools;

import javax.swing.JRadioButton;

// this class makes a one-to-one correspondence between a JRadioButton, a name and an id
public class CorrespondenceJRadioButtonNameId {
	
	private JRadioButton button;
	private String name;
	private int id;
	
	public CorrespondenceJRadioButtonNameId (JRadioButton button, String name, int id) {
		// TODO Auto-generated constructor stub
		this.button = button;
		this.name = name;
		this.id = id;
	}
	
	public JRadioButton getButton () {
		return button;
	}
	
	public String getName () {
		return name;
	}
	
	public int getId () {
		return id;
	}
}
