package winkhouse.action.anagrafiche;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import winkhouse.model.AnagraficheModel;


public class NuovaAnagraficaAction extends Action {

	public final static String ID = "winkhouse.NuovaAnagraficaAction";
	
	public NuovaAnagraficaAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public NuovaAnagraficaAction() {
		setId(ID);
	}

	@Override
	public void run() {
		AnagraficheModel am = new AnagraficheModel();
		ApriDettaglioAnagraficaAction adaa = new ApriDettaglioAnagraficaAction(new AnagraficheModel(am), null);
		adaa.run();
	}
	
	
}
