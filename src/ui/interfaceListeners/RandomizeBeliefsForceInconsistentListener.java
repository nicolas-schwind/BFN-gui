package ui.interfaceListeners;

import parameters.Global;

public class RandomizeBeliefsForceInconsistentListener extends RandomizeBeliefsListener {
	public boolean successCondition () {
		return !Global.currentNetwork.conjDisjStrongestSCCs().isConsistent();
	}
}
