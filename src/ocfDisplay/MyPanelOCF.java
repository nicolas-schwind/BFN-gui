package ocfDisplay;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import belief.OCF;
import parameters.Global;


//displays an OCF into an image, only works with 16 interpretations
public class MyPanelOCF extends JPanel {
	private static final long serialVersionUID = 1L;
	private static int WIDTH_BOX_AGENT_ID = 200;
	private static Color AGENT_ID_FONT_COLOR = Color.DARK_GRAY;
	private static Color AGENT_ID_BACKGROUND_COLOR = Color.LIGHT_GRAY;
	
	private static int TOTAL_WIDTH = 800;
	private static int LAYER_WIDTH = 700;
	private static Color BACKGROUND_COLOR = Color.WHITE;
	private static Color OCF_COLOR = Color.BLACK;
	private static Color DEFAULT_OCF_BORDER_COLOR = Color.BLACK;
	private static Color SELECTED_OCF_BORDER_COLOR = Color.BLACK;
	private static Color INFLUENT_OCF_BORDER_COLOR = Color.BLUE;
	private static Color CHANGED_OCF_BORDER_COLOR = Color.BLUE;
	//private static Color OCF_SOURCE_OF_THE_CHANGE_COLOR = Color.RED;
	private static Font FONT_OCF = new Font("Serif", Font.PLAIN, 50);
	private static Font FONT_AGENT_ID = new Font("Serif", Font.BOLD, 100);
	
	private static int LEFT_SPACE_OCF_VALUE_LABEL = 80;
	
	private static int TOTAL_HEIGHT = 1000;
	private static int SMALL_SEP_WIDTH_BETWEEN_INTER = 15;
	//private static float[] dashingPattern = {4f, 2f};
	//private static Stroke stroke = new BasicStroke(4f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dashingPattern, 2.0f);
	private static Stroke DEFAULT_OCF_STROKE = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
	private static Stroke SELECTED_OCF_STROKE = new BasicStroke(20f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
	private static float[] dash = {50.0f, 25.0f, 15.0f};
	private static Stroke INFLUENT_OCF_STROKE = new BasicStroke(15f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
	private static Stroke CHANGED_OCF_STROKE = new BasicStroke(20f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
	
	private static double SCALING = 0.2;
	
	private boolean isSelected;
	private boolean isInfluentAgent;
	private boolean isAgenttoBeChanged;
	
	private OCF ocf;
	private int agentID;
	
	public MyPanelOCF (int agentID) {
		super(new BorderLayout());
		this.agentID = agentID;
		setBackground(BACKGROUND_COLOR);
		setPreferredSize(new Dimension((int)(TOTAL_WIDTH * SCALING), (int)(TOTAL_HEIGHT * SCALING)));
		//setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.isSelected = false;
		this.isInfluentAgent = false;
		this.isAgenttoBeChanged = false;
	}
	
	public MyPanelOCF (int agentID, OCF ocf) {
		this(agentID);
		this.ocf = ocf;
	}
	
	public OCF getOCF () {
		return this.ocf;
	}
	
	public void setOCF (OCF ocf) {
		this.ocf = ocf;
	}
	
	public int getAgentID () {
		return agentID;
	}
	
	public void setAgentID (int agentID) {
		this.agentID = agentID;
	}
	
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		RoundRectangle2D roundedRectangle = null;
		g2d.scale(SCALING, SCALING);
		drawOCF(g2d, this.ocf);
		if (isAgenttoBeChanged) {
			g2d.setColor(CHANGED_OCF_BORDER_COLOR);
			g2d.setStroke(CHANGED_OCF_STROKE);
			roundedRectangle = new RoundRectangle2D.Float(3, 3, TOTAL_WIDTH, TOTAL_HEIGHT, 50, 50);
		}
		else if (isInfluentAgent) {
			g2d.setColor(INFLUENT_OCF_BORDER_COLOR);
			g2d.setStroke(INFLUENT_OCF_STROKE);
			roundedRectangle = new RoundRectangle2D.Float(3, 3, TOTAL_WIDTH, TOTAL_HEIGHT, 50, 50);
		}
		else if (isSelected) {
			g2d.setColor(SELECTED_OCF_BORDER_COLOR);
			g2d.setStroke(SELECTED_OCF_STROKE);
			roundedRectangle = new RoundRectangle2D.Float(3, 3, TOTAL_WIDTH, TOTAL_HEIGHT, 50, 50);
		}
		else {
			g2d.setColor(DEFAULT_OCF_BORDER_COLOR);
			g2d.setStroke(DEFAULT_OCF_STROKE);
			roundedRectangle = new RoundRectangle2D.Float(3, 3, TOTAL_WIDTH, TOTAL_HEIGHT, 20, 20);
		}
		g2d.draw(roundedRectangle);
        g2d.setColor(OCF_COLOR);
		drawAgentID(g2d);
		g2d.dispose();
	}
	
	public void setIsSelected (boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public boolean getIsSelected () {
		return this.isSelected;
	}
	
	public void setIsInfluentAgent (boolean isInfluentAgent) {
		this.isInfluentAgent = isInfluentAgent;
	}
	
	public boolean getIsInfluentAgent () {
		return this.isInfluentAgent;
	}
	
	public void setIsAgenttoBeChanged (boolean isAgenttoBeChanged) {
		this.isAgenttoBeChanged = isAgenttoBeChanged;
	}
	
	public boolean getIsAgenttoBeChanged () {
		return this.isAgenttoBeChanged;
	}
	
	// draw the agent id on the top right of the OCF
	public void drawAgentID (Graphics2D g2d) {
		g2d.setFont(FONT_AGENT_ID);
		String labelAgentID = Integer.toString(this.agentID);
		int stringWidth = g2d.getFontMetrics().stringWidth(labelAgentID);
		int stringHeight = g2d.getFontMetrics().getHeight();
		
		g2d.setColor(AGENT_ID_BACKGROUND_COLOR);
		// draw oval
		g2d.fillOval(TOTAL_WIDTH - WIDTH_BOX_AGENT_ID - 10, 10,
				WIDTH_BOX_AGENT_ID, stringHeight);
		g2d.setColor(AGENT_ID_FONT_COLOR);
		// draw agent id
		g2d.drawString(Integer.toString(this.agentID), TOTAL_WIDTH - WIDTH_BOX_AGENT_ID + ((WIDTH_BOX_AGENT_ID - stringWidth) / 2) - 10,
				10 + stringHeight - (stringHeight / 4));
	}
	
	public void drawOCF (Graphics2D g2d, OCF ocf) {
		//g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(FONT_OCF);
        g2d.setStroke(DEFAULT_OCF_STROKE);
        
        // fill all the image with white
        //g2d.setColor(BACKGROUND_COLOR);
        //g2d.fillRect(0, 0, TOTAL_WIDTH, TOTAL_HEIGHT);
        g2d.setColor(OCF_COLOR);
		
		SortedSet<Integer> image = ocf.getImage();
		int nbSeparators = image.size() + 1;
		int oneLayerHeight = g2d.getFontMetrics().getHeight();
		int totalLayerHeight = oneLayerHeight * image.size();
		int layerSeparatorHeight = (TOTAL_HEIGHT - totalLayerHeight) / nbSeparators;
		int currentLayerY =  TOTAL_HEIGHT - layerSeparatorHeight - (oneLayerHeight / 2);
		int ocfValue;
		Vector<String> vecStr;
		StringBuffer tmpString;
		
		Iterator<Integer> iter = image.iterator();
		int cpt;
		
		while (iter.hasNext()) {
			ocfValue = iter.next();
			vecStr = new Vector<String>();
			cpt = 0;
			
			// create a new list of String for the current line (ocfValue)
			while (cpt < Global.nbInterpretations) {
				//System.out.println("new string, cpt = " + cpt);
				tmpString = new StringBuffer();
				// search for the first interpretation matching the value
				while (cpt < Global.nbInterpretations && ocf.getInterpretationValue(cpt) != ocfValue) {
					cpt++;
				}
				if (cpt < Global.nbInterpretations) {
					// here cpt is the first interpretation matching the value
					tmpString.append(Global.intToHexa(cpt));
					//System.out.println("cpt is the first interpretation matching the value, append " + cpt + " to tmpString");
				}
				else {
					// there is no more interpretation matching the value
					//System.out.println("break");
					break;
				}
				
				// test if previous cpt, which is the first interpretation matching the value, is also the last in consecutive
				if (cpt + 1 >= Global.nbInterpretations) {
					// cpt was the last possible interpretation
					// save tmpString in the vector list
					//System.out.println("cpt was the last possible interpretation, save tmpString = " + tmpString + " in the vector list");
					vecStr.add(tmpString.toString());
				}
				else if (ocf.getInterpretationValue(cpt + 1) != ocfValue) {
					// the next interpretation has a different value, so cpt was indeed the only value
					// save tmpString in the vector list
					//System.out.println("the next interpretation has a different value, so cpt = " + cpt + " was indeed the only value, "
					//		+ "save tmpString = " + tmpString + " in the vector list");
					vecStr.add(tmpString.toString());
				}
				else {
					// here the next interpretation has also the same value, so append "/" to the string
					//System.out.println("here the next interpretation has also the same value, so append / to the string");
					tmpString.append("/");
					// and continue the search, i.e., search for the last interpretation matching the value following the first one
					//System.out.println("and continue the search, i.e., search for the last interpretation matching the value following the first one");
					while (cpt < Global.nbInterpretations && ocf.getInterpretationValue(cpt) == ocfValue) {
						cpt++;
					}
					cpt--;
					// here cpt is the last interpretation with the same value
					tmpString.append(Global.intToHexa(cpt));
					vecStr.add(tmpString.toString());
				}
				//System.out.println("end of iteration, cpt = " + cpt);
				cpt++;
			}
			
			//System.out.println(vecStr);
			drawLine(g2d, ocfValue, vecStr, 10, currentLayerY);
			currentLayerY = currentLayerY - layerSeparatorHeight - oneLayerHeight;
		}
	}
	
	private static void drawLine (Graphics2D g2d, int ocfValue, Vector<String> vecStr, int startingX, int Y) {
		FontMetrics fm = g2d.getFontMetrics();
		String currentString;
		int yStringOffest = g2d.getFont().getSize() / 4;
		int currentStringWidth;
		int nbSeparators = vecStr.size() + 1;
		int totalStringWidth = getTotalStringWidth(g2d, vecStr);
		int separatorLength = (LAYER_WIDTH - totalStringWidth) / nbSeparators;
		int currentX = startingX + LEFT_SPACE_OCF_VALUE_LABEL;
		int newX = startingX + LEFT_SPACE_OCF_VALUE_LABEL + separatorLength; 
		//System.out.println("nbSeparators: " + nbSeparators);
		//System.out.println("totalStringWidth: " + totalStringWidth);
		//System.out.println("separatorLength: " + separatorLength);
		
		// first draw the ocf value
		g2d.drawString(Integer.toString(ocfValue) + ":", startingX, Y + yStringOffest);
		
		// draw the first separator
		g2d.drawLine(currentX, Y, newX - SMALL_SEP_WIDTH_BETWEEN_INTER, Y);
		//System.out.println("DRAWING line from " + currentX + " to " + (newX - SMALL_SEP_WIDTH_BETWEEN_INTER));
		
		// loop (string, separator)
		Iterator<String> it = vecStr.iterator();
		while (it.hasNext()) {
			// first draw the string
			currentX = newX;
			currentString = it.next();
			currentStringWidth = fm.stringWidth(currentString);
			//System.out.println("currentString: " + currentString);
			//System.out.println("currentStringWidth: " + currentStringWidth);
			newX = currentX + currentStringWidth;
			g2d.drawString(currentString, currentX, Y + yStringOffest);
			//System.out.println("DRAWING string " + currentString + " from " + currentX);
			
			// then draw the separator
			currentX = newX;
			newX = currentX + separatorLength;
			g2d.drawLine(currentX + SMALL_SEP_WIDTH_BETWEEN_INTER, Y, newX - SMALL_SEP_WIDTH_BETWEEN_INTER, Y);
			//System.out.println("DRAWING line from " + (currentX + SMALL_SEP_WIDTH_BETWEEN_INTER) + " to " + (newX - SMALL_SEP_WIDTH_BETWEEN_INTER));
		}
		// finish the line
		g2d.drawLine(newX - SMALL_SEP_WIDTH_BETWEEN_INTER, Y, LAYER_WIDTH, Y);
		//System.out.println("DRAWING final line from " + (newX - SMALL_SEP_WIDTH_BETWEEN_INTER) + " to " + (startingX + LEFT_SPACE_OCF_VALUE_LABEL + LAYER_WIDTH));
	}
	
	private static int getTotalStringWidth (Graphics2D g2d, Vector<String> vecStr) {
		FontMetrics fm = g2d.getFontMetrics();
		int result = 0;
		Iterator<String> it = vecStr.iterator();
		while (it.hasNext()) {
			result += fm.stringWidth(it.next());
		}
		return result;
	}
	
    public static void main(String[] args) throws IOException {
    	Global.initParameters();
    	OCF ocf = OCF.getPerfectFlatOCF();
    	//ocf = OCF.getPerfectSparseOCF();
    	//SortedSet<Integer> tmp = new TreeSet<Integer>();
    	//tmp.add(0); tmp.add(1); tmp.add(2);
    	//ocf.moveToLowerLayer(tmp);
    	System.out.println(ocf);
    	JPanel mainPanel = new JPanel(new GridLayout(0, 2));
    	MyPanelOCF myPanelOCF = new MyPanelOCF(11, ocf);
    	myPanelOCF.setIsSelected(true);
    	MyPanelOCF myPanelOCF2 = new MyPanelOCF(15, ocf);
    	myPanelOCF2.setIsSelected(true);
    	mainPanel.add(myPanelOCF);
    	mainPanel.add(myPanelOCF2);
        JFrame frame = new JFrame("Test Display OCF");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        
        //myPanelOCF.revalidate();
        //myPanelOCF.repaint();
        //myPanelOCF.setVisible(true);
        frame.setSize(850, 1050);
        frame.setVisible(true);
        //frame.pack();
    }
}
