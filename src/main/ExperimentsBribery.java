package main;

import parameters.Constants;
import parameters.Global;
import ui.UserInterface;
import ui.interfaceListeners.gameRunListeners.SwitchSimulationModeListener;
import ui.interfaceListeners.graphGenerationListeners.AbstractGraphListener;
import ui.interfaceListeners.graphGenerationListeners.BarabasiAlbertGraphListener;
import ui.interfaceListeners.graphGenerationListeners.CyclicGraphListener;
import ui.interfaceListeners.graphGenerationListeners.KleinbergGraphListener;
import ui.interfaceListeners.graphGenerationListeners.LineGraphListener;
import ui.interfaceListeners.graphGenerationListeners.RandomGraphListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import gameRun.RunnableAgent;
import graph.MyEdge;
import graph.UIAgent;
import graph.stronglyConnectedComponents.StronglyConnectedComponent;

public class ExperimentsBribery {
	
	public static double calculateMedian(ArrayList<Integer> data) {
        int size = data.size();
        
        if (size % 2 == 0) {
            int middle1 = data.get(size / 2 - 1);
            int middle2 = data.get(size / 2);
            return (middle1 + middle2) / 2.0;
        } else {
            return data.get(size / 2);
        }
    }
	
	public static double calculateQuartile(ArrayList<Integer> data, double percent) {
        int size = data.size();
        double index = percent / 100 * (size - 1);
        int lowerIndex = (int) Math.floor(index);
        int upperIndex = (int) Math.ceil(index);
        double lowerValue = data.get(lowerIndex);
        double upperValue = data.get(upperIndex);
        return lowerValue + (upperValue - lowerValue) * (index - lowerIndex);
    }
	
	public static ArrayList<Integer> findOutliers(ArrayList<Integer> data) {
        double lowerQuartile = calculateQuartile(data, 25);
        double upperQuartile = calculateQuartile(data, 75);
        double iqr = upperQuartile - lowerQuartile;
        double lowerWhisker = lowerQuartile - 1.5 * iqr;
        double upperWhisker = upperQuartile + 1.5 * iqr;

        ArrayList<Integer> outliers = new ArrayList<>();
        for (int value : data) {
            if (value < lowerWhisker || value > upperWhisker) {
                outliers.add(value);
            }
        }
        return outliers;
    }
	
	public static void main(String[] args) {
		UserInterface graphView = new UserInterface();
		graphView.init();
		
		//int nbPropVariables = 4;
		int nbInstances = 1000;
		ArrayList<Integer> dataStableStep;
		ArrayList<Integer> maxdatasStability;
		ArrayList<Integer> dataUnanimousReached;
		
		double median;
		double lowerQuartile;
		double upperQuartile;
		double iqr;
		double lowerWhisker;
		double upperWhisker;
		ArrayList<Integer> outliers;
		int maxOutlier;
		
		// parameters for random graph
		//int nbNodes = 20;
		//int density = 15;
		int nbSourceNonSingletonsSCCMin = 0;
		int nbSourceNonSingletonsSCCMax = 1000;
		int nbSourceSingletonsSCCMin = 0;
		int nbSourceSingletonsSCCMax = 1000;
		int nbSCCMin = 0;
		int nbSCCMax = 10000;
		boolean symmetricClosure = true;
		
		// the number we are interested in: the step when all agents reach stability
		int stabilityReachedStep;
		// the number we are interested in: the step when all agents have accepted the control agent's beliefs
		int unanimousAcceptedReachedStep;
		
		AbstractGraphListener graphListener;
		graphListener = new RandomGraphListener();
		SwitchSimulationModeListener simulationListener;
		simulationListener = new SwitchSimulationModeListener();
		
		//Constants.nTextField.setText(Integer.toString(nbNodes));
		//Constants.densityTextField.setText(Integer.toString(density));
		Constants.minNbStrongestSCCsNonSingletonTextField.setText(Integer.toString(nbSourceNonSingletonsSCCMin));
		Constants.maxNbStrongestSCCsNonSingletonTextField.setText(Integer.toString(nbSourceNonSingletonsSCCMax));
		Constants.minNbStrongestSCCsSingletonTextField.setText(Integer.toString(nbSourceSingletonsSCCMin));
		Constants.maxNbStrongestSCCsSingletonTextField.setText(Integer.toString(nbSourceSingletonsSCCMax));
		Constants.minNbSCCsTextField.setText(Integer.toString(nbSCCMin));
		Constants.maxNbSCCsTextField.setText(Integer.toString(nbSCCMax));
		
		
		int cpt;
		String revPolicyString;
		StringBuffer strStability;
		StringBuffer strUnanimous;
		for (int nbPropVariables: new int[]{4}) {
			if (nbPropVariables == 4) {
				Global.nbInterpretations = 16;
			}
			else {
				Global.nbInterpretations = 256;
			}
			for (int revPolicy = 1; revPolicy < 2; revPolicy++) {
				if (revPolicy == 0) {
					revPolicyString = "restrained";
					Global.buttonRestrained.doClick();
				}
				else {
					revPolicyString = "one-improvement";
					Global.buttonOneImprovement.doClick();
				}
				for (int nbNodes : new int[]{20}) {
					strStability = new StringBuffer();
					strUnanimous = new StringBuffer();
					strStability.append("\\begin{figure}\n\\begin{tikzpicture}\n\\begin{axis}[line width=1pt,\nboxplot/draw direction=y,\nxtick={1,2,3,4,5,6,7},\n"
							+ "xticklabels={R7, R10, R20, Clique, BA, K, Line, Loop, Line+, Loop+},\n]\n");
					strUnanimous.append("\\begin{figure}\n\\begin{tikzpicture}\n\\begin{axis}[line width=1pt,\nboxplot/draw direction=y,\nxtick={1,2,3,4,5,6,7},\n"
							+ "xticklabels={R7, R10, R20, Clique, BA, K, Line, Loop, Line+, Loop+},\n]\n");
					Constants.nTextField.setText(Integer.toString(nbNodes));
					cpt = 0;
					maxdatasStability = new ArrayList<>();
					//for (int density : new int[]{10, 20, 100, 200, 300, 400, 500}) {
					for (int density : new int[]{7, 10, 20, 100, 200, 300, 400, 500, 400, 500}) {
						cpt++;
						// select the type of instance
						if (density <= 100) {
							Constants.densityTextField.setText(Integer.toString(density));
							graphListener = new RandomGraphListener();
						}
						else if (density == 200) {
							// Barabasi-Albert
							graphListener = new BarabasiAlbertGraphListener();
						}
						else if (density == 300) {
							// Kleinberg
							Constants.nbRowsTextField.setText("4");
							Constants.nbColsTextField.setText("5");
							graphListener = new KleinbergGraphListener();
						}
						else if (density == 400) {
							// Line
							graphListener = new LineGraphListener();
						}
						else if (density == 500) {
							// Loop
							graphListener = new CyclicGraphListener();
						}
						simulationListener = new SwitchSimulationModeListener();
						dataStableStep = new ArrayList<>();
						dataUnanimousReached = new ArrayList<>();
						for (int i = 0; i < nbInstances; i++) {
							graphListener.actionPerformed(null);
							if (cpt <= 8) {
								Constants.symmetricCheckBox.setSelected(true);
							}
							else {
								Constants.symmetricCheckBox.setSelected(false);
							}
							graphListener.getOkButton().doClick();
							
							/*try {
								Thread.sleep(3000);
							}
							catch (InterruptedException e) {
								Thread.currentThread().interrupt();
							}*/
							
							// HERE WE WANT TO BRIBE IT
							// search for the source SCCs
							Set<StronglyConnectedComponent> allSourceSCCs = Global.currentNetwork.getSCCGraph().getRootSet();
							int controlAgId = Global.currentNetwork.getVertexCount();
							int bribedAgId;
							Global.currentNetwork.addVertex(new UIAgent(controlAgId));
							for (StronglyConnectedComponent sourceSCC : allSourceSCCs) {
								for (RunnableAgent ag : sourceSCC.getAgentSet()) {
									bribedAgId = ag.getId();
									Global.currentNetwork.addEdge(new MyEdge(), Global.currentNetwork.getAgent(controlAgId),
											Global.currentNetwork.getAgent(bribedAgId));
									break;
								}
							}
							// set the beliefs of the bribed agent to a formula with half interpretations as models 
							Global.currentNetwork.getAgent(controlAgId).getOCF().initBeliefsRandomizeParam(50);
							//System.out.println(Global.currentNetwork.getAgent(controlAgId).getOCF().getBeliefs());
							
							Global.vv.refresh();
			    			Global.resetAllOCFPanels();
			    			Constants.frameMain.repaint();
			    			
			    			/*try {
								Thread.sleep(10000);
							}
							catch (InterruptedException e) {
								Thread.currentThread().interrupt();
							}*/
							
							
							simulationListener.actionPerformed(null);
							stabilityReachedStep = Global.currentFullRun.getAllAgentsHaveReachedStabilityStepNumber();
							unanimousAcceptedReachedStep = Global.currentFullRun.getDisjunctionOfAllLastStepChange();
							if (unanimousAcceptedReachedStep < 0) {
								unanimousAcceptedReachedStep = 0;
							}
							
							//System.out.println(Global.currentFullRun.getListAgents().get(5).getOCF(5));
							simulationListener.actionPerformed(null);
							/*if (i == 0) {
								System.out.print(stabilityReachedStep);
							}
							else {
								System.out.print(" " + stabilityReachedStep);
							}*/
							dataStableStep.add(stabilityReachedStep);
							dataUnanimousReached.add(unanimousAcceptedReachedStep);
							try {
								Thread.sleep(2);
							}
							catch (InterruptedException e) {
								Thread.currentThread().interrupt();
							}
						}
						
						// STABILITY
						//System.out.println("Raw data: " + data);
						Collections.sort(dataStableStep);
						//System.out.println("Sorted data: " + data);
						maxdatasStability.add(dataStableStep.get(dataStableStep.size() - 1));
						
						median = calculateMedian(dataStableStep);
						lowerQuartile = calculateQuartile(dataStableStep, 25);
						upperQuartile = calculateQuartile(dataStableStep, 75);
						iqr = upperQuartile - lowerQuartile;
						lowerWhisker = Math.max(lowerQuartile - 1.5 * iqr, dataStableStep.get(0));
						upperWhisker = Math.min(upperQuartile + 1.5 * iqr, dataStableStep.get(dataStableStep.size() - 1));
						outliers = findOutliers(dataStableStep);
						
						/*System.out.println("Median: " + median);
						System.out.println("Lower Quartile: " + lowerQuartile);
						System.out.println("Upper Quartile: " + upperQuartile);
						System.out.println("Lower Whisker: " + lowerWhisker);
						System.out.println("Upper Whisker: " + upperWhisker);
						System.out.println("Outliers: " + outliers);*/
						
						strStability.append("\\addplot+ [boxplot prepared={draw position=" + cpt + ",");
						strStability.append("lower whisker=");
						strStability.append(lowerWhisker);
						strStability.append(", lower quartile=");
						strStability.append(lowerQuartile);
						strStability.append(", median=");
						strStability.append(median);
						strStability.append(", upper quartile=");
						strStability.append(upperQuartile);
						strStability.append(", upper whisker=");
						strStability.append(upperWhisker);
						strStability.append("},\n");
						
						//let us not display outliers to improve readability
						strStability.append("] coordinates {};\n");
						//if (outliers.isEmpty()) {
						//	str.append("] coordinates {};\n");
						//}
						//else {
						//	str.append("] table [row sep=\\\\,y index=0] { ");
						//	for (int j = 0; j < outliers.size(); j++) {
						//		str.append(outliers.get(j));
						//		str.append("\\\\ ");
						//	}
						//	str.append("};\n");
						//}
						System.out.println("STABILITY\n" + strStability);
						
						
						
						
						
						
						// UNANIMOUS
						//System.out.println("Raw data: " + data);
						Collections.sort(dataUnanimousReached);
						//System.out.println("Sorted data: " + data);
						
						median = calculateMedian(dataUnanimousReached);
						lowerQuartile = calculateQuartile(dataUnanimousReached, 25);
						upperQuartile = calculateQuartile(dataUnanimousReached, 75);
						iqr = upperQuartile - lowerQuartile;
						lowerWhisker = Math.max(lowerQuartile - 1.5 * iqr, dataUnanimousReached.get(0));
						upperWhisker = Math.min(upperQuartile + 1.5 * iqr, dataUnanimousReached.get(dataUnanimousReached.size() - 1));
						outliers = findOutliers(dataUnanimousReached);
						
						/*System.out.println("Median: " + median);
						System.out.println("Lower Quartile: " + lowerQuartile);
						System.out.println("Upper Quartile: " + upperQuartile);
						System.out.println("Lower Whisker: " + lowerWhisker);
						System.out.println("Upper Whisker: " + upperWhisker);
						System.out.println("Outliers: " + outliers);*/
						
						strUnanimous.append("\\addplot+ [boxplot prepared={draw position=" + cpt + ",");
						strUnanimous.append("lower whisker=");
						strUnanimous.append(lowerWhisker);
						strUnanimous.append(", lower quartile=");
						strUnanimous.append(lowerQuartile);
						strUnanimous.append(", median=");
						strUnanimous.append(median);
						strUnanimous.append(", upper quartile=");
						strUnanimous.append(upperQuartile);
						strUnanimous.append(", upper whisker=");
						strUnanimous.append(upperWhisker);
						strUnanimous.append("},\n");
						
						//let us not display outliers to improve readability
						strUnanimous.append("] coordinates {};\n");
						//if (outliers.isEmpty()) {
						//	str.append("] coordinates {};\n");
						//}
						//else {
						//	str.append("] table [row sep=\\\\,y index=0] { ");
						//	for (int j = 0; j < outliers.size(); j++) {
						//		str.append(outliers.get(j));
						//		str.append("\\\\ ");
						//	}
						//	str.append("};\n");
						//}
						System.out.println("UNANIMOUS\n" + strUnanimous);
					}
					strStability.append("\\end{axis}\n\\end{tikzpicture}\n\\label{fig:stability after bribery,nbnodes=" + nbNodes + ",revPolicy=" + revPolicyString
							+ ",nbVar=" + nbPropVariables + "}\n"
							+ "\\caption{stability after bribery, nbnodes=" + nbNodes + ", revPolicy=" + revPolicyString
							+ ", nbVar=" + nbPropVariables + ", maxForEach=" + maxdatasStability + "}\n"
							+ "\\end{figure}");
					System.out.println();
					strUnanimous.append("\\end{axis}\n\\end{tikzpicture}\n\\label{fig:nb states to unanimous, nbnodes=" + nbNodes + ",revPolicy=" + revPolicyString
							+ ",nbVar=" + nbPropVariables + "}\n"
							+ "\\caption{nb states to unanimous, nbnodes=" + nbNodes + ", revPolicy=" + revPolicyString
							+ ", nbVar=" + nbPropVariables + ", maxForEach=" + maxdatasStability + "}\n"
							+ "\\end{figure}");
					System.out.println("\nFINAL:");
					System.out.println("STABILITY:");
					System.out.println(strStability);
					System.out.println("UNANIMOUS:");
					System.out.println(strUnanimous);
				}
			}
		}
	}
}