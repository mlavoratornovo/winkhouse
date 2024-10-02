package winkhouse.action.agenda;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.db.ConnectionManager;
import winkhouse.helper.AppuntamentiHelper;
import winkhouse.helper.ColloquiHelper;
import winkhouse.orm.Appuntamenti;
import winkhouse.orm.Colloqui;
//import winkhouse.model.AppuntamentiModel;
//import winkhouse.model.ColloquiModel;
import winkhouse.view.agenda.CalendarioView;
import winkhouse.view.agenda.DettaglioAppuntamentoView;
import winkhouse.view.agenda.ListaAppuntamentiView;



public class CancellaAppuntamentoAction extends Action {

	private String returnView = null;
	
	public CancellaAppuntamentoAction(String returnView) {
		this.returnView = returnView;		
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getImageDescriptor("icons/edittrash.png");
	}

	@Override
	public String getText() {
		return "Cancella appuntamento";
	}
	

	@Override
	public void run() {
		IWorkbenchPart wp = PlatformUI.getWorkbench()
				  					  .getActiveWorkbenchWindow()
				  					  .getActivePage()
				  					  .getActivePart();
		
		
		AppuntamentiHelper ah = new AppuntamentiHelper();
		
		if (wp instanceof DettaglioAppuntamentoView){
			
			Appuntamenti am = ((DettaglioAppuntamentoView)wp).getAppuntamento();
			if (ah.deleteAppuntamento(am,null)){

				DettaglioAppuntamentoView dav = (DettaglioAppuntamentoView)wp;
	   
				PlatformUI.getWorkbench()
						  .getActiveWorkbenchWindow()
						  .getActivePage()
						  .hideView(dav);
	
				CercaAppuntamentiAction caa2 = new CercaAppuntamentiAction(CalendarioView.class.getName());
				caa2.run();					
				caa2 = new CercaAppuntamentiAction(ListaAppuntamentiView.class.getName());
				caa2.run();
			}

		}
		
		if (wp instanceof ListaAppuntamentiView){
			
			boolean okdel = true;
			HashMap hmResultdelColloquio = null;
			ColloquiHelper ch = new ColloquiHelper();
			
			Connection con = ConnectionManager.getInstance()
											  .getConnection();
			StructuredSelection ss = (StructuredSelection)((ListaAppuntamentiView)wp).getTvAgenda().getSelection();
			if (ss != null){
				Iterator it = ss.iterator();
				while (it.hasNext()){
					Object o = it.next();
					if (o instanceof Appuntamenti){
						if (!ah.deleteAppuntamento((Appuntamenti)o,con)){
							okdel = false;
							break;
						}
					}
					if (o instanceof Colloqui){
						hmResultdelColloquio = ch.deleteColloquio((Colloqui)o, con, false);
						if (!(Boolean)hmResultdelColloquio.get(ColloquiHelper.RESULT_DELETE_COLLOQUIO_DB)){
							okdel = false;
							break;
						}
					}
				}
				
				if (okdel){
					
						try {
							con.commit();				
							if (hmResultdelColloquio != null){
								if (ch.deleteAllegatiColloqui((ArrayList)hmResultdelColloquio.get(ColloquiHelper.LIST_DELETE_ALLEGATI_FILE))){
									MessageDialog.openWarning(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											  				  "Cancellazione file allegati colloqui",
											  				  "Alcuni documenti allegati ai colloqui non sono stati cancellati");
								}
							}
							
						} catch (SQLException e) {
							e.printStackTrace();
						}
						
						CercaAppuntamentiAction caa = new CercaAppuntamentiAction(returnView);
						caa.run();
						if (returnView.equalsIgnoreCase(ListaAppuntamentiView.class.getName())){
							CercaAppuntamentiAction caa2 = new CercaAppuntamentiAction(CalendarioView.class.getName());
							caa2.run();					
						}else{
							CercaAppuntamentiAction caa2 = new CercaAppuntamentiAction(ListaAppuntamentiView.class.getName());
							caa2.run();										
						}												

				}else{
					try {
						con.rollback();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
								
			}
		}
	}

}
