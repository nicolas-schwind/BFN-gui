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

public class ExperimentsStability {
	
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
		ArrayList<Integer> data;
		ArrayList<Integer> maxdatas;
		
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
		int nbSCCMax = 1000;
		boolean symmetricClosure = true;
		
		// the number we are interested in: the step when all agents reach stability
		int stabilityReachedStep;
		
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
		StringBuffer str;
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
					str = new StringBuffer();
					str.append("\\begin{figure}\n\\begin{tikzpicture}\n\\begin{axis}[line width=1pt,\nboxplot/draw direction=y,\nxtick={1,2,3,4,5,6,7},\n"
							+ "xticklabels={R10, R20, Clique, BA, K, Line, Loop},\n]\n");
					Constants.nTextField.setText(Integer.toString(nbNodes));
					cpt = 0;
					maxdatas = new ArrayList<>();
					//for (int density : new int[]{10, 20, 100, 200, 300, 400, 500}) {
					for (int density : new int[]{7}) {
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
						data = new ArrayList<>();
						for (int i = 0; i < nbInstances; i++) {
							graphListener.actionPerformed(null);
							Constants.symmetricCheckBox.setSelected(symmetricClosure);
							graphListener.getOkButton().doClick();
							simulationListener.actionPerformed(null);
							stabilityReachedStep = Global.currentFullRun.getAllAgentsHaveReachedStabilityStepNumber();
							//System.out.println(Global.currentFullRun.getListAgents().get(5).getOCF(5));
							simulationListener.actionPerformed(null);
							/*if (i == 0) {
								System.out.print(stabilityReachedStep);
							}
							else {
								System.out.print(" " + stabilityReachedStep);
							}*/
							data.add(stabilityReachedStep);
							try {
								Thread.sleep(2);
							}
							catch (InterruptedException e) {
								Thread.currentThread().interrupt();
							}
						}
						//System.out.println("Raw data: " + data);
						Collections.sort(data);
						//System.out.println("Sorted data: " + data);
						maxdatas.add(data.get(data.size() - 1));
						
						median = calculateMedian(data);
						lowerQuartile = calculateQuartile(data, 25);
						upperQuartile = calculateQuartile(data, 75);
						iqr = upperQuartile - lowerQuartile;
						lowerWhisker = Math.max(lowerQuartile - 1.5 * iqr, data.get(0));
						upperWhisker = Math.min(upperQuartile + 1.5 * iqr, data.get(data.size() - 1));
						outliers = findOutliers(data);
						
						/*System.out.println("Median: " + median);
						System.out.println("Lower Quartile: " + lowerQuartile);
						System.out.println("Upper Quartile: " + upperQuartile);
						System.out.println("Lower Whisker: " + lowerWhisker);
						System.out.println("Upper Whisker: " + upperWhisker);
						System.out.println("Outliers: " + outliers);*/
						
						str.append("\\addplot+ [boxplot prepared={draw position=" + cpt + ",");
						str.append("lower whisker=");
						str.append(lowerWhisker);
						str.append(", lower quartile=");
						str.append(lowerQuartile);
						str.append(", median=");
						str.append(median);
						str.append(", upper quartile=");
						str.append(upperQuartile);
						str.append(", upper whisker=");
						str.append(upperWhisker);
						str.append("},\n");
						
						//let us not display outliers to improve readability
						str.append("] coordinates {};\n");
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
						System.out.println(str);
					}
					str.append("\\end{axis}\n\\end{tikzpicture}\n\\label{fig:nbnodes=" + nbNodes + ",revPolicy=" + revPolicyString
							+ ",nbVar=" + nbPropVariables + "}\n"
							+ "\\caption{nbnodes=" + nbNodes + ", revPolicy=" + revPolicyString
							+ ", nbVar=" + nbPropVariables + ", maxForEach=" + maxdatas + "}\n"
							+ "\\end{figure}");
					System.out.println("\nFINAL:");
					System.out.println(str);
				}
			}
		}
	}
}