package winkhouse.action.anagrafiche;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.action.recapiti.ApriDettaglioRecapitiAction;
import winkhouse.helper.ProfilerHelper;
import winkhouse.helper.ProfilerHelper.PermessoDetail;
import winkhouse.model.AnagraficheModel;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Classicliente;
import winkhouse.util.IWinkSysProperties;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.anagrafica.DettaglioAnagraficaView;
import winkhouse.view.anagrafica.handler.DettaglioAnagraficaHandler;
import winkhouse.vo.ClassiClientiVO;

public class ApriDettaglioAnagraficaAction extends Action {

	private Anagrafiche anagrafica = null;
	private Classicliente classicliente = null;
	private boolean comparerView = false;
	
	public ApriDettaglioAnagraficaAction(Anagrafiche anagrafica,Classicliente classiCliente) {
		this.classicliente = classiCliente;
		this.anagrafica = anagrafica;
	}

	public ApriDettaglioAnagraficaAction(String text) {
		super(text);

	}

	public ApriDettaglioAnagraficaAction(String text, ImageDescriptor image) {
		super(text, image);

	}

	public ApriDettaglioAnagraficaAction(String text, int style) {
		super(text, style);

	}

	@Override
	public void run() {
		
	
		if (((WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN) != null)
			    ? Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN))
				: false)){
			
			if (ProfilerHelper.getInstance().getPermessoUI(DettaglioAnagraficaView.ID)){
				
				PermessoDetail pd = ProfilerHelper.getInstance().getPermessoAnagrafica(this.anagrafica.getCodAnagrafica(), false);
				if (pd != null){
					DettaglioAnagraficaView dav = DettaglioAnagraficaHandler.getInstance()
																		    .getDettaglioAnagrafica(this.anagrafica);
					if (!pd.getCanwrite()){
						dav.setCompareView(true);
					}else{
						dav.setCompareView(isComparerView());
					}
					if (this.classicliente != null){
						this.anagrafica.setClassicliente(this.classicliente);
					}
					
					PlatformUI.getWorkbench()
							 .getActiveWorkbenchWindow()
							 .getActivePage()
							 .bringToTop(dav);
													
					dav.setAnagrafica(this.anagrafica);
					ArrayList<Anagrafiche> anagrafiche = new ArrayList<Anagrafiche>();
					anagrafiche.add(anagrafica);
					ApriDettaglioRecapitiAction adra = new ApriDettaglioRecapitiAction(anagrafiche,false);
					adra.run();
					
				}else{
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
		  					  "Controllo permessi accesso dati",
		  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
		  					  " non ha il permesso di accedere ai dati dell'anagrafica" + 
		  					  this.anagrafica.toString());
	
				}
			}else{
				MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						  					  "Controllo permessi accesso vista",
						  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
						  					  " non ha il permesso di accedere alla vista " + 
						  					  DettaglioAnagraficaView.ID);
			}

		}else{
			
			DettaglioAnagraficaView dav = DettaglioAnagraficaHandler.getInstance()
				    												.getDettaglioAnagrafica(this.anagrafica);
			if (this.classicliente != null){
				this.anagrafica.setClassicliente(classicliente);
			}
			
			dav.setCompareView(isComparerView());

			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().bringToTop(dav);

			dav.setAnagrafica(this.anagrafica);
			ArrayList<Anagrafiche> anagrafiche = new ArrayList<Anagrafiche>();
			anagrafiche.add(anagrafica);			
			ApriDettaglioRecapitiAction adra = new ApriDettaglioRecapitiAction(anagrafiche,false);
			adra.run();
			
			
		}

	}

	
	public boolean isComparerView() {
		return comparerView;
	}

	
	public void setComparerView(boolean comparerView) {
		this.comparerView = comparerView;
	}
	
}
