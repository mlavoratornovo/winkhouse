package winkhouse.action.anagrafiche;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import winkhouse.model.AnagraficheModel;

public class ApriDettaglioAnagraficaPerspectiveAction extends Action {
	
	private AnagraficheModel anagrafica = null;
	private boolean comparerView = false;
	
	public ApriDettaglioAnagraficaPerspectiveAction(AnagraficheModel anagrafica) {
		this.anagrafica = anagrafica;
	}

	public ApriDettaglioAnagraficaPerspectiveAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public ApriDettaglioAnagraficaPerspectiveAction(String text,
			ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public ApriDettaglioAnagraficaPerspectiveAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void run() {
		ApriDettaglioAnagraficaAction adaa = new ApriDettaglioAnagraficaAction(anagrafica,null);
		adaa.setComparerView(isComparerView());
		adaa.run();
	}

	public boolean isComparerView() {
		return comparerView;
	}

	public void setComparerView(boolean comparerView) {
		this.comparerView = comparerView;
	}

}
