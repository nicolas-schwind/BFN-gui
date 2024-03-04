package graph;

import belief.OCF;
import ocfDisplay.MyPanelOCF;

public class UIAgent extends AbstractAgent {
	// The current ocf of this agent
	private OCF ocf;
	
	// For save to a file and load purpose
	public double x;
	public double y;
	
	// the associated MyPanelOCF
	private MyPanelOCF panelOCF;
	
	public UIAgent (int n, OCF ocf) {
		super(n);
		this.ocf = ocf;
		this.panelOCF = new MyPanelOCF(this.id, ocf);
	}
	
	private UIAgent (UIAgent ag) {
		super(ag);
		this.ocf = new OCF(ag.getOCF().getOCF());
		this.panelOCF = new MyPanelOCF(this.id, this.ocf);
	}
	
	public UIAgent (int n) {
		super(n);
		this.ocf = new OCF();
		this.panelOCF = new MyPanelOCF(id, ocf);
	}
	
	public void setId(int id) {
		this.id = id;
		this.panelOCF.setAgentID(id);
	}
	
	public void setPanelOCF (MyPanelOCF panelOCF) {
		this.panelOCF = panelOCF;
	}
	
	public MyPanelOCF getPanelOCF () {
		return this.panelOCF;
	}

	public void setOCF(OCF ocf) {
		this.ocf = ocf;
	}
	
	public OCF getOCF() {
		return ocf;
	}
	
	public UIAgent getCopy() {
		return new UIAgent(this);
	}
	
	public String toString() {
		return "UIAgent " + getId() + " (belief = " + this.getOCF().getBeliefs() + ")";
	}
}
