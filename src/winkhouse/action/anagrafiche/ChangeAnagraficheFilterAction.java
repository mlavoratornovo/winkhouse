package winkhouse.action.anagrafiche;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import winkhouse.Activator;
import winkhouse.util.WinkhouseUtils;



public class ChangeAnagraficheFilterAction extends Action {

	public ChangeAnagraficheFilterAction() {}

	public ChangeAnagraficheFilterAction(String text) {
		super(text);
	}

	public ChangeAnagraficheFilterAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public ChangeAnagraficheFilterAction(String text, int style) {
		super(text, style);
		setImageDescriptor(Activator.getImageDescriptor("icons/anagrafica_16.png"));
	}

	@Override
	public void run() {
		if (isChecked()){
			WinkhouseUtils.getInstance()
							.setAngraficheFilterType(WinkhouseUtils.PROPIETARI);
			
			setToolTipText("Visualizza solo i richiedenti");
			setImageDescriptor(Activator.getImageDescriptor("icons/anagraficaImmobile16.png"));
		}else{
			WinkhouseUtils.getInstance()
						  .setAngraficheFilterType(WinkhouseUtils.RICHIEDENTI);
			setToolTipText("Visualizza solo i proprietari");
			setImageDescriptor(Activator.getImageDescriptor("icons/anagrafica16.png"));
		}
		if (WinkhouseUtils.getInstance().isAnagraficheFiltered()){
			RefreshAnagraficheAction raa = new RefreshAnagraficheAction();
			raa.run();
		}
	}

}
