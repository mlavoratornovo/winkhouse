package winkhouse.action.anagrafiche;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.anagrafica.AnagraficaTreeView;
import winkhouse.view.anagrafica.handler.AnagraficheFilter;



public class FiltraAnagraficheAction extends Action {

	public FiltraAnagraficheAction() {

	}

	public FiltraAnagraficheAction(String text) {
		super(text);

	}

	public FiltraAnagraficheAction(String text, ImageDescriptor image) {
		super(text, image);

	}

	public FiltraAnagraficheAction(String text, int style) {
		super(text, style);
		setImageDescriptor(Activator.getImageDescriptor("icons/filter.png"));
	}

	@Override
	public void run() {
		
		AnagraficaTreeView atv = null;
		
		atv = (AnagraficaTreeView)PlatformUI.getWorkbench()
										  .getActiveWorkbenchWindow()
					  					  .getActivePage()
					  					  .findView(AnagraficaTreeView.ID);		
		
		if (isChecked()){
			WinkhouseUtils.getInstance().setAnagraficheFiltered(true);
			atv.getViewer().addFilter(AnagraficheFilter.getInstance());
			atv.getViewer().setInput(new Object());
			setToolTipText("Rimuovi filtro anagrafiche");
		}else{
			WinkhouseUtils.getInstance().setAnagraficheFiltered(false);
			atv.getViewer().removeFilter(AnagraficheFilter.getInstance());
			setToolTipText("Attiva filtro anagrafiche");
		}
		

		/*
		
		RefreshAnagraficheAction raa = new RefreshAnagraficheAction();
		raa.run();*/
	}

}
