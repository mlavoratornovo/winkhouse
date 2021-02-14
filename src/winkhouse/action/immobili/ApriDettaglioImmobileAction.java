package winkhouse.action.immobili;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.action.recapiti.ApriDettaglioRecapitiAction;
import winkhouse.helper.ProfilerHelper;
import winkhouse.helper.ProfilerHelper.PermessoDetail;
import winkhouse.model.ImmobiliModel;
import winkhouse.util.IWinkSysProperties;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.immobili.AnagrafichePropietarieView;
import winkhouse.view.immobili.DettaglioImmobileView;
import winkhouse.view.immobili.ImmaginiImmobiliView;
import winkhouse.view.immobili.handler.DettaglioImmobiliHandler;
import winkhouse.vo.TipologieImmobiliVO;

public class ApriDettaglioImmobileAction extends Action {

	private ImmobiliModel iModel = null;
	private TipologieImmobiliVO tiVO = null;
	private boolean comparerView = false;
	
	public ApriDettaglioImmobileAction(ImmobiliModel iModel,TipologieImmobiliVO tiVO) {
		this.iModel = iModel;
		this.tiVO = tiVO;
	}

	public ApriDettaglioImmobileAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public ApriDettaglioImmobileAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public ApriDettaglioImmobileAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		if (((WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN) != null)
			    ? Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN))
				: false)){

			if (ProfilerHelper.getInstance().getPermessoUI(DettaglioImmobileView.ID)){
				
				PermessoDetail pd = ProfilerHelper.getInstance().getPermessoImmobile(this.iModel.getCodImmobile(), false);
				if (pd != null){
					
					try {
						DettaglioImmobileView div = DettaglioImmobiliHandler.getInstance()
																			.getDettaglioImmobile(iModel);		
						if (!pd.getCanwrite()){
							div.setCompareView(true);
						}else{
							div.setCompareView(isComparerView());
						}
						
						if (tiVO != null){
							iModel.setTipologiaImmobile(tiVO);
						}
			
						div.setImmobile(iModel);
						ApriDettaglioRecapitiAction adra = new ApriDettaglioRecapitiAction(iModel.getAnagrafichePropietarie(),false);
						adra.run();
			
						ImmaginiImmobiliView iiv = (ImmaginiImmobiliView)PlatformUI.getWorkbench()
										   										   .getActiveWorkbenchWindow()
										   										   .getActivePage()
										   										   .findView(ImmaginiImmobiliView.ID);
						
						AnagrafichePropietarieView apv = (AnagrafichePropietarieView)PlatformUI.getWorkbench()
								   															   .getActiveWorkbenchWindow()
								   															   .getActivePage()
								   															   .findView(AnagrafichePropietarieView.ID);
						
						if (iiv != null){
							iiv.setImmobile(iModel);
						}
						
						if (!pd.getCanwrite()){
							iiv.setCompareView(true);
						}else{
							iiv.setCompareView(isComparerView());
						}
						
						
						if (apv != null){
							apv.setAnagrafica(iModel);
							if (iModel.getCodImmobile() == null){
								apv.setFocus();
							}
						}
						
						if (!pd.getCanwrite()){
							apv.setCompareView(true);
						}else{
							apv.setCompareView(isComparerView());
						}

		
					} catch (Exception e) {
						e.printStackTrace();
						MessageBox mb = new MessageBox(Activator.getDefault()
																.getWorkbench().getActiveWorkbenchWindow().getShell(),
													   SWT.ERROR);
													   mb.setMessage("Errore nel trasferire i dati al dettaglio");
													   mb.open();										
					}
					
				}else{
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
		  					  					  "Controllo permessi accesso dati",
		  					  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
		  					  					  " non ha il permesso di accedere ai dati dell'immobile" + 
		  					  					  this.iModel.toString());
					
				}
					
			}else{
				MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						  					  "Controllo permessi accesso vista",
						  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
						  					  " non ha il permesso di accedere alla vista " + 
						  					  DettaglioImmobileView.ID);
			}
		}else{

			try {
				DettaglioImmobileView div = DettaglioImmobiliHandler.getInstance()
																	.getDettaglioImmobile(iModel);		
				
				if (tiVO != null){
					iModel.setTipologiaImmobile(tiVO);
				}
	
				div.setImmobile(iModel);
				
				div.setCompareView(isComparerView());
				
				ApriDettaglioRecapitiAction adra = new ApriDettaglioRecapitiAction(iModel.getAnagrafichePropietarie(),false);
				adra.run();
	
				ImmaginiImmobiliView iiv = (ImmaginiImmobiliView)PlatformUI.getWorkbench()
								   										   .getActiveWorkbenchWindow()
								   										   .getActivePage()
								   										   .findView(ImmaginiImmobiliView.ID);
				if (iiv != null){
					iiv.setImmobile(iModel);
					iiv.setCompareView(isComparerView());
				}
				
				
				
				AnagrafichePropietarieView apv = null; 
				if (iModel.getCodImmobile() != null){
					apv = (AnagrafichePropietarieView)PlatformUI.getWorkbench()
							   									.getActiveWorkbenchWindow()
							   									.getActivePage()
							   									.findView(AnagrafichePropietarieView.ID);
				}else{
					apv = (AnagrafichePropietarieView)PlatformUI.getWorkbench()
							   									.getActiveWorkbenchWindow()
							   									.getActivePage()
							   									.showView(AnagrafichePropietarieView.ID);
					
				}


				if (apv != null){
					apv.setAnagrafica(iModel);
					apv.setCompareView(isComparerView());
				}
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
				MessageBox mb = new MessageBox(Activator.getDefault()
														.getWorkbench().getActiveWorkbenchWindow().getShell(),
											   SWT.ERROR);
											   mb.setMessage("Errore nel trasferire i dati al dettaglio");
											   mb.open();										
			}
			
			
		}
		
	}

	public boolean isComparerView() {
		return comparerView;
	}

	public void setComparerView(boolean comparerView) {
		this.comparerView = comparerView;
	}

}
