package mouseMenu;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.plaf.basic.BasicIconFactory;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;

public class MyEditingModalGraphMouse<AgentNode, BasicEdge> extends
		EditingModalGraphMouse<AgentNode, BasicEdge> {
	
	public MyEditingModalGraphMouse(RenderContext<AgentNode, BasicEdge> rc,
			Factory<AgentNode> vertexFactory, Factory<BasicEdge> edgeFactory) {
		super(rc, vertexFactory, edgeFactory);
		// TODO Auto-generated constructor stub
	}

	public MyEditingModalGraphMouse(RenderContext<AgentNode, BasicEdge> rc,
			Factory<AgentNode> vertexFactory, Factory<BasicEdge> edgeFactory,
			float in, float out) {
		super(rc, vertexFactory, edgeFactory, in, out);
		// TODO Auto-generated constructor stub
	}
	
    /*public JComboBox getModeComboBox() {
		// remove the TRANSFORMING mode
        if(modeBox == null) {
            modeBox = new JComboBox(new Mode[]{Mode.PICKING, Mode.ANNOTATING});
            modeBox.addItemListener(getModeListener());
        }
        modeBox.setSelectedItem(mode);
        return modeBox;
    }?8
	
	/**
     * create (if necessary) and return a menu that will change
     * the mode
     * @return the menu
     */
    @Override
    public JMenu getModeMenu() {
		// remove the TRANSFORMING mode
        if(modeMenu == null) {
            modeMenu = new JMenu();// {
            Icon icon = BasicIconFactory.getMenuArrowIcon();
            modeMenu.setIcon(BasicIconFactory.getMenuArrowIcon());
            modeMenu.setPreferredSize(new Dimension(icon.getIconWidth()+10, 
                    icon.getIconHeight()+10));
            
            final JRadioButtonMenuItem pickingButton = new JRadioButtonMenuItem(Mode.PICKING.toString());
            pickingButton.addItemListener(new ItemListener() {
            	
            	@Override
                public void itemStateChanged(ItemEvent e) {
                    if(e.getStateChange() == ItemEvent.SELECTED) {
                        setMode(Mode.PICKING);
                    }
                }});
            
            final JRadioButtonMenuItem editingButton = new JRadioButtonMenuItem(Mode.EDITING.toString());
                editingButton.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        if(e.getStateChange() == ItemEvent.SELECTED) {
                            setMode(Mode.EDITING);
                        }
                    }});
 
            ButtonGroup radio = new ButtonGroup();
            radio.add(pickingButton);
            radio.add(editingButton);
            modeMenu.add(pickingButton);
            modeMenu.add(editingButton);
            modeMenu.setToolTipText("Menu for setting Mouse Mode");
            addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if(e.getStateChange() == ItemEvent.SELECTED) {
                    	if(e.getItem() == Mode.PICKING) {
                            pickingButton.setSelected(true);
                        }
                    	else if(e.getItem() == Mode.EDITING) {
                            editingButton.setSelected(true);
                        }
                    }
                }});
        }
        return modeMenu;
    }

}
