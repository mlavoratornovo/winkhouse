package winkhouse.action.immobili;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import winkhouse.model.ImmobiliModel;

public class ApriDettaglioImmobilePerspectiveAction extends Action {

	private ImmobiliModel immobile = null;
	private boolean comparerView = false;
	
	public ApriDettaglioImmobilePerspectiveAction(ImmobiliModel immobile) {
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
