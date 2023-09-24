package winkhouse.action.anagrafiche;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.view.anagrafica.AnagraficaTreeView;
import winkhouse.view.anagrafica.handler.AnagraficheSorter;

public class OrdinaAnagraficheAction extends Action {

	public OrdinaAnagraficheAction(String text, int style) {
		super(text, style);
		setImageDescriptor(Activator.getImageDescriptor("icons/alfa.png"));
		setToolTipText("Ordine alfabetico da A a Z");
	}

	@Override
	public void run() {
		
		AnagraficaTreeView atv = null;
		
		atv = (AnagraficaTreeView)PlatformUI.getWorkbench()
										  .getActiveWorkbenchWindow()
					  					  .getActivePage()
					  					  .findView(AnagraficaTreeView.ID);		
		
		if (isChecked()){			
			atv.getViewer().setSorter(AnagraficheSorter.getInstance()); 
			setToolTipText("Ordine naturale");
		}else{			
			atv.getViewer().setSorter(null);
			setToolTipText("Ordine alfabetico da A a Z");
		}
		

		
		/*
		RefreshAnagraficheAction raa = new RefreshAnagraficheAction();
		raa.run();
		*/
		
	}

	
}
