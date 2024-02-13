package winkhouse.action.immobili;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Immobili;

public class ApriDettaglioImmobilePerspectiveAction extends Action {

	private Immobili immobile = null;
	private boolean comparerView = false;
	
	public ApriDettaglioImmobilePerspectiveAction(Immobili immobile) {
		this.immobile = immobile;
	}

	public ApriDettaglioImmobilePerspectiveAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public ApriDettaglioImmobilePerspectiveAction(String text,
			ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public ApriDettaglioImmobilePerspectiveAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void run() {
		ApriDettaglioImmobileAction adia = new ApriDettaglioImmobileAction(immobile,null);
		adia.setComparerView(isComparerView());
		adia.run();		
	}

	public boolean isComparerView() {
		return comparerView;
	}

	public void setComparerView(boolean comparerView) {
		this.comparerView = comparerView;
	}

	
}
