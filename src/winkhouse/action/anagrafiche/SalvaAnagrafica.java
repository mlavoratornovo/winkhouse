package winkhouse.action.anagrafiche;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.AnagraficheDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.helper.AnagraficheHelper;
import winkhouse.helper.EntityHelper;
import winkhouse.helper.OptimisticLockHelper;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.AttributeModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.anagrafica.DettaglioAnagraficaView;
import winkhouse.view.anagrafica.handler.DettaglioAnagraficaHandler;



public class SalvaAnagrafica extends Action {

	private AnagraficheModel anagrafica = null;
	private DettaglioAnagraficaView dav = null;
	
	public SalvaAnagrafica() {
		
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getImageDescriptor("icons/document-save.png");
	}

	@Override
	public String getText() {
		return "Salva anagrafica";
	}

	protected AnagraficheModel getAnagrafica(){
		
		if (anagrafica == null){
			try{
				dav = (DettaglioAnagraficaView)PlatformUI.getWorkbench()
					 									 .getActiveWorkbenchWindow()
					 									 .getActivePage()
					 									 .getActivePart();
				anagrafica = dav.getAnagrafica();
			}catch(Exception e){}
			
		}
		
		return anagrafica;
	}
	
	public void setAnagrafica(AnagraficheModel anagrafica){
		this.anagrafica = anagrafica;
	}
	
	@Override
	public void run() {
	try {
			
			boolean insnew = ((getAnagrafica().getCodAnagrafica() == null) || (getAnagrafica().getCodAnagrafica()==0));
			
			getAnagrafica().setDateUpdate(new Date());
			
			if (WinkhouseUtils.getInstance().getLoggedAgent() != null){
				getAnagrafica().setCodUserUpdate(WinkhouseUtils.getInstance().getLoggedAgent().getCodAgente());
			}

			String decision = new OptimisticLockHelper().checkOLAnagrafica(getAnagrafica());
			
			if (decision.equalsIgnoreCase(OptimisticLockHelper.SOVRASCRIVI)){
				
				if (getAnagrafica() != null){
					
					if (
							(((getAnagrafica().getCognome() != null) && (!getAnagrafica().getCognome().equalsIgnoreCase(""))) && 
							 ((getAnagrafica().getNome() != null) && (!getAnagrafica().getNome().equalsIgnoreCase("")))) ||
							 ((getAnagrafica().getRagioneSociale() != null) && (!getAnagrafica().getRagioneSociale().equalsIgnoreCase("")))
						){
						
						AnagraficheHelper ah = new AnagraficheHelper();
						EntityHelper eh = new EntityHelper();
						
						Connection con = ConnectionManager.getInstance().getConnection();
						
						if (ah.updateAnagrafiche(getAnagrafica(), true, con, false)){
							
							try {
								con.commit();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							for (AttributeModel attribute : getAnagrafica().getAttributes()){
								
								if (attribute.getValue(getAnagrafica().getCodAnagrafica()) != null){
									
									attribute.getValue(getAnagrafica().getCodAnagrafica()).setIdObject(getAnagrafica().getCodAnagrafica());
									if (!eh.saveAttributeValue(attribute.getValue(getAnagrafica().getCodAnagrafica()))){
										MessageDialog.openError(PlatformUI.getWorkbench()
												  						  .getActiveWorkbenchWindow()
												                          .getShell(), 
																"Errore salvataggio", 
																"Errore durante il salvataggio del campo " + attribute.getAttributeName());
									}
									
								}
								
							}

							if (insnew){
								if (dav != null){
									PlatformUI.getWorkbench()
									 		  .getActiveWorkbenchWindow()
									 		  .getActivePage().hideView(dav);
									dav = DettaglioAnagraficaHandler.getInstance().getDettaglioAnagrafica(getAnagrafica());
									
								}
								WinkhouseUtils.getInstance().setCodiciAnagrafiche(null);
							}
							
						}else{
							try {
								con.rollback();
								
							} catch (SQLException e) {
								e.printStackTrace();
							}							
						}
						
					}else{
						MessageBox mb = new MessageBox(dav.getSite().getShell(),SWT.ERROR);
						mb.setText("Errore salvataggio");
						mb.setMessage("Inserire cognome e nome o ragione sociale dell'anagrafica");			
						mb.open();
					}
					
				}
				
				RefreshAnagraficheAction raa = new RefreshAnagraficheAction();
				raa.run();
				
			}else if (decision.equalsIgnoreCase(OptimisticLockHelper.VISUALIZZA)){
				
				try {
					DettaglioAnagraficaView div_comp = (DettaglioAnagraficaView)PlatformUI.getWorkbench()
																				 	  	  .getActiveWorkbenchWindow()
																				 	  	  .getActivePage()
																				 	  	  .showView(DettaglioAnagraficaView.ID,
																				 	  			  	String.valueOf(getAnagrafica().getCodAnagrafica()) + "Comp",
																				 	  			  	IWorkbenchPage.VIEW_CREATE);
					AnagraficheDAO a_compDAO = new AnagraficheDAO();
					AnagraficheModel anagrafica_comp = (AnagraficheModel)a_compDAO.getAnagraficheById(AnagraficheModel.class.getName(), getAnagrafica().getCodAnagrafica());
					
					div_comp.setAnagrafica(anagrafica_comp);
					div_comp.setCompareView(false);
					
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
