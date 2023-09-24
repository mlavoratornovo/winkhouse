package winkhouse.action.abbinamenti;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import winkhouse.engine.search.AnagraficheSearchEngine;
import winkhouse.engine.search.ImmobiliColloquiSearchEngine;
import winkhouse.engine.search.ImmobiliSearchEngine;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.common.AbbinamentiView;


public class FindAbbinamenti extends Action {
	
	private static final String ID = "winkhouse.action.FindAbbinamenti";
	
	public FindAbbinamenti() {
		setId(ID);
	}

	@Override
	public void run() {
		AbbinamentiView av = (AbbinamentiView)PlatformUI.getWorkbench()
														.getActiveWorkbenchWindow()
														.getActivePage()
														.findView(AbbinamentiView.ID);
		if (av.getImmobile() != null){
			if (WinkhouseUtils.getInstance().getLastCodImmobileSelected() != null){
								
				ArrayList anagrafiche = new ArrayList();
				AnagraficheSearchEngine ase = new AnagraficheSearchEngine(WinkhouseUtils.getInstance().getLastCodImmobileSelected(),anagrafiche);
				ProgressMonitorDialog pmd = new ProgressMonitorDialog(av.getSite().getShell());
				try {
					pmd.run(false, true, ase);
				} catch (InvocationTargetException e) {
					MessageBox mb = new MessageBox(winkhouse.Activator
															  .getDefault()
															  .getWorkbench()
															  .getActiveWorkbenchWindow()
															  .getShell(),
															  SWT.ICON_ERROR);
					mb.setText("Si è verificato un errore nella ricerca delle anagrafiche");
					mb.setMessage("Si è verificato un errore nella ricerca delle anagrafiche");			
					mb.open();
				} catch (InterruptedException e) {
					MessageBox mb = new MessageBox(winkhouse.Activator
							  								  .getDefault()
							  								  .getWorkbench()
															  .getActiveWorkbenchWindow()
															  .getShell(),
															  SWT.ICON_ERROR);
					mb.setText("Si è verificato un errore nella ricerca de anagrafiche");
					mb.setMessage("Si è verificato un errore nella ricerca delle anagrafiche");			
					mb.open();
				}
				av.setRicerca(anagrafiche);
			}else{
				MessageBox mb = new MessageBox(winkhouse.Activator
														  .getDefault()
														  .getWorkbench()
														  .getActiveWorkbenchWindow()
														  .getShell(),
											   SWT.ICON_WARNING);
				mb.setText("Avviso");
				mb.setMessage("Selezionare un dettaglio immobile");			
				mb.open();

			}

		}
		
		if (av.getAnagrafica() != null){
			if (WinkhouseUtils.getInstance().getLastCodAnagraficaSelected() != null){
								
				ArrayList immobili = new ArrayList();
				ImmobiliSearchEngine ase = new ImmobiliSearchEngine(WinkhouseUtils.getInstance().getLastCodAnagraficaSelected(),immobili);
				ProgressMonitorDialog pmd = new ProgressMonitorDialog(av.getSite().getShell());
				try {
					pmd.run(false, true, ase);
				} catch (InvocationTargetException e) {
					MessageBox mb = new MessageBox(winkhouse.Activator
							  								  .getDefault()
							  								  .getWorkbench()
							  								  .getActiveWorkbenchWindow()
							  								  .getShell(),
							  								  SWT.ICON_ERROR);
					mb.setText("Si è verificato un errore nella ricerca degli immobili");
					mb.setMessage("Si è verificato un errore nella ricerca degli immobili");			
					mb.open();
				} catch (InterruptedException e) {
					MessageBox mb = new MessageBox(winkhouse.Activator
							  				 			      .getDefault()
							  				 			      .getWorkbench()
							  				 			      .getActiveWorkbenchWindow()
							  				 			      .getShell(),
							  				 			      SWT.ICON_ERROR);
					mb.setText("Si è verificato un errore nella ricerca degli immobili");
					mb.setMessage("Si è verificato un errore nella ricerca degli immobili");			
					mb.open();
				}
				av.setRicerca(immobili);
			}else{
				MessageBox mb = new MessageBox(winkhouse.Activator
														  .getDefault()
														  .getWorkbench()
														  .getActiveWorkbenchWindow()
														  .getShell(),
											   SWT.ICON_WARNING);
				mb.setText("Avviso");
				mb.setMessage("Selezionare un dettaglio anagrafica");			
				mb.open();

			}
			
		}
		
		if ((av.getColloquio() != null) && (av.getColloquio().getTipologia().getCodTipologiaColloquio() == 1)){
			
			ArrayList immobili = new ArrayList();
			ImmobiliColloquiSearchEngine ase = new ImmobiliColloquiSearchEngine(av.getColloquio().getCodColloquio(),immobili);
			ProgressMonitorDialog pmd = new ProgressMonitorDialog(av.getSite().getShell());
			try {
				pmd.run(false, true, ase);
			} catch (InvocationTargetException e) {
				MessageBox mb = new MessageBox(winkhouse.Activator
						  								  .getDefault()
						  								  .getWorkbench()
						  								  .getActiveWorkbenchWindow()
						  								  .getShell(),
						  								  SWT.ICON_ERROR);
				mb.setText("Si è verificato un errore nella ricerca degli immobili");
				mb.setMessage("Si è verificato un errore nella ricerca degli immobili");			
				mb.open();
			} catch (InterruptedException e) {
				MessageBox mb = new MessageBox(winkhouse.Activator
						  				 			      .getDefault()
						  				 			      .getWorkbench()
						  				 			      .getActiveWorkbenchWindow()
						  				 			      .getShell(),
						  				 			      SWT.ICON_ERROR);
				mb.setText("Si è verificato un errore nella ricerca degli immobili");
				mb.setMessage("Si è verificato un errore nella ricerca degli immobili");			
				mb.open();
			}
			av.setRicerca(immobili);
			
		}
		
	}


}
