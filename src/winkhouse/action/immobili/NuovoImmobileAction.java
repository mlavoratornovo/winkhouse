package winkhouse.action.immobili;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Immobili;
import winkhouse.util.WinkhouseUtils;


public class NuovoImmobileAction extends Action {
	
	public final static String ID = "winkhouse.NuovoImmobileAction";
	
	public NuovoImmobileAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public NuovoImmobileAction() {
		setId(ID);
	}

	@Override
	public void run() {
		Immobili im = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Immobili.class);
		ApriDettaglioImmobileAction adia = new ApriDettaglioImmobileAction(im, null);
		adia.run();
	}


}
