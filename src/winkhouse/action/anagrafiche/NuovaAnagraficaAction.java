package winkhouse.action.anagrafiche;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import winkhouse.model.AnagraficheModel;
import winkhouse.orm.Anagrafiche;
import winkhouse.util.WinkhouseUtils;


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
		Anagrafiche am = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Anagrafiche.class);		
		ApriDettaglioAnagraficaAction adaa = new ApriDettaglioAnagraficaAction(am, null);
		adaa.run();
	}
	
	
}
