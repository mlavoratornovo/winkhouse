package winkhouse.action.recapiti;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;

import winkhouse.model.ContattiModel;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Contatti;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.common.RecapitiView;

public class NuovoRecapitoAction extends Action {

	public NuovoRecapitoAction() {
		// TODO Auto-generated constructor stub
	}

	public NuovoRecapitoAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public NuovoRecapitoAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public NuovoRecapitoAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void run() {
		
		RecapitiView rv = null;
		
		rv = (RecapitiView)PlatformUI.getWorkbench()
									 .getActiveWorkbenchWindow()
				  					 .getActivePage()
				  					 .getActivePart();
		
		if (rv.getAnagrafiche() != null && rv.getAnagrafiche().size() > 0){
			
			
			Contatti cm = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Contatti.class);
			rv.getAnagrafiche().get(0).addToContattis(cm);
//			cm.setCodAnagrafica(rv.getAnagrafiche().get(0).getCodAnagrafica());
//			cm.setAnagrafica(rv.getAnagrafiche().get(0));
			
//			rv.getAnagrafiche().get(0).getContatti().add(cm);
			
			rv.getContatti().add(cm);
			
			rv.getTvRecapiti().setInput(rv.getContatti());
			rv.getTvRecapiti().refresh();				
			
			TableItem ti = rv.getTvRecapiti().getTable().getItem(rv.getTvRecapiti().getTable().getItemCount()-1);
			Object[] sel = new Object[1];
			sel[0] = ti.getData();
	
			StructuredSelection ss = new StructuredSelection(sel);
			
			rv.getTvRecapiti().setSelection(ss, true);
	
			Event ev = new Event();
			ev.item = ti;
			ev.data = ti.getData();
			ev.widget = rv.getTvRecapiti().getTable();
			rv.getTvRecapiti().getTable().notifyListeners(SWT.Selection, ev);
			
		}else{
			MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
									  "Nuovo recapito",
									  "Nessuna anagrafica presente, \n selezionare un dettaglio anagrafica o aggiungere delle anagrafiche proprietarie all'immobile");
		}
		
//		ApriDettaglioRecapitiAction adra = new ApriDettaglioRecapitiAction(rv.getAnagrafiche(),true);
//		adra.run();	
	}

	
	
}
