package parameters;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import tools.EnterCheckBox;

public class Constants {
	// default value for the number of interpretations (best is 16)
	public static int DEFAULT_NB_INTER = 16;
	
	public static String nameDrasticMerging = "Drastic-Merging";
	public static String nameOneImprovement = "One-Improvement";
	public static String nameBoutilier = "Boutilier";
	public static String nameNayak = "Nayak";
	public static String nameRestrained = "Restrained";
	public static String nameSKPBulletPlus = "SKP-BulletPlus";
	public static JPanel nPanel = new JPanel();
	public static JPanel nbStrongestSCCsNonSingletonPanel = new JPanel();
	public static JPanel nbStrongestSCCsSingletonPanel = new JPanel();
	public static JPanel nbSCCsPanel = new JPanel();
	public static JPanel checkForSymmetricPanel = new JPanel();
	
	// Generic interface and input elements
	public static JFrame frameMain;
	public static JTextField nTextField = new JTextField(3);
	public static JTextField densityTextField = new JTextField(3);
	public static JTextField kTextField = new JTextField(3);
	public static JTextField pTextField = new JTextField(3);
	public static JTextField minNbStrongestSCCsNonSingletonTextField = new JTextField(3);
	public static JTextField maxNbStrongestSCCsNonSingletonTextField = new JTextField(3);
	public static JTextField minNbStrongestSCCsSingletonTextField = new JTextField(3);
	public static JTextField maxNbStrongestSCCsSingletonTextField = new JTextField(3);
	public static JTextField minNbSCCsTextField = new JTextField(3);
	public static JTextField maxNbSCCsTextField = new JTextField(3);
	public static JPanel densityPanel = new JPanel();
	public static JLabel r1Label = new JLabel("k = n-1 : Clique");
	public static JLabel nLabel = new JLabel("Number of vertices : n = ");
	public static JLabel densityLabel = new JLabel("Density of links [0 - 100] = ");
	// for Barabasi-Albert graph generation
	public static JPanel initialSeedSizePanel = new JPanel();
	public static JLabel initialSeedSizeLabel = new JLabel("Initial seed size (default value = 3) = ");
	public static JTextField initialSeedSizeTextField = new JTextField(3);
	public static JPanel numLinksAddedPanel = new JPanel();
	public static JLabel numLinksAddedLabel = new JLabel("Number of links to add at each step (default value = (n/50)+2) = ");
	public static JTextField numLinksAddedTextField = new JTextField(3);
	// for Kleinberg graph generation
	public static JLabel nbRowsColLabel = new JLabel("Nb of agents (nb rows / nb cols): ");
	public static JPanel nbRowsColsPanel = new JPanel();
	public static JTextField nbRowsTextField = new JTextField(3);
	public static JTextField nbColsTextField = new JTextField(3);
	public static JPanel clusteringExpPanel = new JPanel();
	public static JLabel clusteringExpLabel = new JLabel("Clustering exponent (default value = 2) = ");
	public static JTextField clusteringExpTextField = new JTextField(3);
	
	public static JLabel kLabel = new JLabel("Number of links : k = ");
	public static JLabel pLabel = new JLabel("Random links : p = ");
	public static JLabel r2Label = new JLabel("k = 1   : Circuit");
	public static JLabel t1Label = new JLabel("k = 1 : Line");
	public static JLabel t2Label = new JLabel("k = 2 : Binary tree");
	public static JLabel t3Label = new JLabel("k = n : Star");
	public static JPanel kPanel = new JPanel();
	public static JPanel pPanel = new JPanel();
	
	// labels in the menus for generating graphs
	//public static JLabel optionalSCCsLabel = new JLabel("(Optional: constraints on number of strongest strongly connected components (SSCCs))");
	public static JLabel nbStrongestSCCsNonSingletonLabel = new JLabel("Nb of strongest non-singletons SCCs (min / max):");
	public static JLabel nbStrongestSCCsSingletonLabel = new JLabel("Nb of strongest singleton SCCs (min / max):");
	public static JLabel nbSCCsLabel = new JLabel("Nb of SCCs (min / max):");
	public static JLabel checkForSymmetricLabel = new JLabel("Check for symmetric closure:");
	// checkboxes
	public static EnterCheckBox symmetricCheckBox = new EnterCheckBox("Symmetric closure");
	
	// Menubar & menu items
	public static JMenuItem clearMenuItem = new JMenuItem("New empty");
	public static JMenu newGraphMenu = new JMenu("New graph");
	public static JMenuItem cyclicGraphMenuItem = new JMenuItem("Loop graph");
	public static JMenuItem lineGraphMenuItem = new JMenuItem("Line graph");
	public static JMenuItem directedAcyclicGraphMenuItem = new JMenuItem("Directed acyclic graph");
	public static JMenuItem smallWorldMenuItem = new JMenuItem("Small World");
	public static JMenuItem barabasiAlbertMenuItem = new JMenuItem("Barabasi-Albert preferential influence graph");
	public static JMenuItem kleinbergMenuItem = new JMenuItem("Kleinberg small world graph");
	public static JMenuItem symCliqueMenuItem = new JMenuItem("Symmetric Clique");
	public static JMenuItem randomGraphMenuItem = new JMenuItem("Random Graph");
	public static JMenuItem randomGraphConfigLeadsToLoopMenuItem = new JMenuItem("Random Graph and Configuration Leading to a Loop");
	public static JMenuItem symRandomGraphMenuItem = new JMenuItem("Symmetric Random Graph");
	public static JMenuItem currentGraphSymmetricClosureMenuItem = new JMenuItem("Symmetric Closure of Current Graph");
	public static JMenuItem patternMenuItem = new JMenuItem("From XML pattern");
	public static JMenu loadMenu = new JMenu("Load");
	public static JMenuItem randomizeMenuItem = new JMenuItem("Randomize beliefs");
	public static JMenuItem randomizeUniformallyMenuItem = new JMenuItem("Randomize beliefs uniformally");
	public static JMenuItem randomizeForceConsistentMenuItem = new JMenuItem("Randomize beliefs (force consistent)");
	public static JMenuItem randomizeForceInconsistentMenuItem = new JMenuItem("Randomize beliefs (force inconsistent)");
	public static JMenuItem symClosureMenuItem = new JMenuItem("Symmetric Closure");
	public static JMenuItem resetMenuItem = new JMenuItem("Reset to step 0");
	public static JMenuItem jumpLastMenuItem = new JMenuItem("Jump to last step");
	public static JMenuItem refreshMenuItem = new JMenuItem("Refresh graph display");
	public static JMenuItem refreshOCFPanelItem = new JMenuItem("Refresh ep. state panel display");
	public static JMenuItem saveMenuItem = new JMenuItem("Save");
	public static JMenuItem loadExternalMenuItem = new JMenuItem("Load from file");
	public static JMenu loadInternalMenu = new JMenu("Load from library");
	public static JMenuItem playMenuItem = new JMenuItem("Play automatically");
	public static JMenuItem nextMenuItem = new JMenuItem("Next step");
	public static JMenuItem previousMenuItem = new JMenuItem("Previous step");
	public static JMenuItem fastForwardMenuItem = new JMenuItem("Jump forward 10 steps");
	public static JMenuItem fastBackwardMenuItem = new JMenuItem("Jump backward 10 steps");
	public static JMenuItem simulateMenuItem = new JMenuItem("Lauch a run");
	public static JMenuItem displayOCFMenuItem = new JMenuItem("Display all ep. states");
	public static JMenuItem displayLogFrameMenuItem = new JMenuItem("Display log frame");
	public static JMenuItem modifyAgentBeliefMenuItem = new JMenuItem("Modify agent's belief");
	public static JMenu helpMenu = new JMenu("Help");
	public static JMenuItem howToPlayMenuItem = new JMenuItem("How to play?");
	public static Font userInterfaceFont = new Font("Arial", Font.BOLD, 14);
	public static Font userInterfaceFontItalic = new Font("Arial", Font.ITALIC, 14);
	public static JMenu editionMenu = new JMenu("Edition");
	public static JMenu runMenu = new JMenu("Run");
	public static JMenu infoMenu = new JMenu("Info");
	public static JMenu modeMenu;
	public static JMenu analysisMenu = new JMenu("Analysis");
	public static JMenuBar menuBar = new JMenuBar();
	public static Vector<JMenuItem> examplesInternalLibraryMenuItemList;
	
	// a set of key strokes that are used as shortcuts for menu items
	public static KeyStroke rightKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
	public static KeyStroke leftKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
	public static KeyStroke simulateKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);
	public static KeyStroke fastRightKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_SEMICOLON, 0);
	public static KeyStroke fastLeftKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_K, 0);
	public static KeyStroke jumpToLastKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_L, 0);
	public static KeyStroke generateRandomGraphKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.SHIFT_DOWN_MASK);
	public static KeyStroke generateCyclicGraphKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.SHIFT_DOWN_MASK);
	public static KeyStroke generateLineGraphKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.SHIFT_DOWN_MASK);
	public static KeyStroke generateDAGGraphKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.SHIFT_DOWN_MASK);
	public static KeyStroke generateBarabasiAlbertGraphKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.SHIFT_DOWN_MASK);
	public static KeyStroke generateKleinbergGraphKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_K, KeyEvent.SHIFT_DOWN_MASK);
	public static KeyStroke generateScaleFreeGraphKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.SHIFT_DOWN_MASK);
	public static KeyStroke symmetricClosureKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, 0);
	public static KeyStroke loadExternalKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_L, 0);
	public static KeyStroke randomizeBeliefsKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_B, 0);
	public static KeyStroke randomizeBeliefsForceConsistentKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, 0);
	public static KeyStroke randomizeBeliefsForceInconsistentKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_I, 0);
	public static KeyStroke resetKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_R, 0);
	public static KeyStroke refreshOCFPanelKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_E, 0);
	public static KeyStroke saveKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, 0);
	public static KeyStroke displayOCFKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_O, 0);
	public static KeyStroke displayLogFrameKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0);
	public static KeyStroke modifyAgentBeliefKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_M, 0);
	
	// some custom colors and line strokes for the vertices
	// fill colors
	public static Color lightRedColor = new Color(255, 102, 102);
	public static Color normalRedColor =  new Color(204, 0, 0);
	public static Color lightBlueColor = new Color(102, 102, 255);
	public static Color normalBlueColor = new Color(0, 0, 204);
	public static Color lightGrayColor = new Color(192, 192, 192);
	public static Color normalGrayColor = new Color(96, 96, 96);
	// draw colors (for strongest components)
	public static Color whiteColorForRoot = new Color(255, 255, 255);
	public static Color yellowColor = new Color(255, 255, 0);
	public static Color greenColor = new Color(153, 153, 0);
	public static Color greenBlueColor = new Color(0, 204, 204);
	public static Color veryDarkGreenRedColor = new Color(51, 51, 0);
	public static Color lightGrayBisColor = new Color(192, 192, 192);
	public static Color pinkColor = new Color(255, 102, 255);
	// line strokes
	// a thin stroke is used for defaults vertices
	public static Stroke vertexStrokeThin = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
	// a thick stroke is used for vertices in some strongest component
	public static Stroke vertexStrokeThickFilled = new BasicStroke(6f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
	// in particular, a dashed thick stroke is used for lone root vertices (these vertices are also in some strongest singleton component)
	private static float[] dash = {5f};
	public static Stroke vertexStrokeThickDashed = new BasicStroke(6f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f, dash, 0.0f);
	// put the draw colors in an array
	public static Color[] vertexDrawColors = {greenBlueColor, veryDarkGreenRedColor, greenColor, yellowColor, lightGrayBisColor, pinkColor};
	// a different edge color and specific label is used for the edge that is currently triggerred during a run
	public static Color edgeTriggeredColor = new Color(255, 255, 0);
	public static String edgeTriggeredLabel = "!";

	// time out in ms to generate a graph before giving up the constraints
	public final static int GENERATION_TIME_OUT = 5000;
}
