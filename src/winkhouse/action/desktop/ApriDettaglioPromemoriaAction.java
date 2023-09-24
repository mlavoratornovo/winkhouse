package winkhouse.action.desktop;

import org.eclipse.jface.action.Action;

import winkhouse.model.PromemoriaModel;
import winkhouse.view.desktop.DesktopView;
import winkhouse.view.desktop.PopUpDettaglioPromemoria;

public class ApriDettaglioPromemoriaAction extends Action {

	private DesktopView desktop = null;
	private PromemoriaModel pm = null;
	
	public ApriDettaglioPromemoriaAction(DesktopView desktop,PromemoriaModel pm) {
		this.setToolTipText("Visualizza dettaglio promemoria");
		this.desktop = desktop;
		this.pm = pm;
	}

	@Override
	public void run() {
		PopUpDettaglioPromemoria pdp = new PopUpDettaglioPromemoria(desktop);
		pdp.setPromemoria(pm);		
	}

}
