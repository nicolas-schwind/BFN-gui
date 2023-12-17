package ui.interfaceListeners;

import parameters.Global;

public class RandomizeBeliefsForceConsistentListener extends RandomizeBeliefsListener {
	public boolean successCondition () {
		return Global.currentNetwork.conjDisjStrongestSCCs().isConsistent();
	}
}
