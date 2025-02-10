package winkhouse.helper;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.cayenne.DeleteDenyException;
import org.apache.cayenne.ObjectContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.AbbinamentiDAO;
import winkhouse.dao.AffittiAllegatiDAO;
import winkhouse.dao.AffittiAnagraficheDAO;
import winkhouse.dao.AffittiDAO;
import winkhouse.dao.AffittiRateDAO;
import winkhouse.dao.AffittiSpeseDAO;
import winkhouse.dao.AgentiAppuntamentiDAO;
import winkhouse.dao.AgentiDAO;
import winkhouse.dao.AllegatiColloquiDAO;
import winkhouse.dao.AllegatiImmobiliDAO;
import winkhouse.dao.AnagraficheAppuntamentiDAO;
import winkhouse.dao.AnagraficheDAO;
import winkhouse.dao.AppuntamentiDAO;
import winkhouse.dao.AttributeDAO;
import winkhouse.dao.AttributeValueDAO;
import winkhouse.dao.ClassiClientiDAO;
import winkhouse.dao.ClassiEnergeticheDAO;
import winkhouse.dao.ColloquiAgentiDAO;
import winkhouse.dao.ColloquiAnagraficheDAO;
import winkhouse.dao.ColloquiCriteriRicercaDAO;
import winkhouse.dao.ColloquiDAO;
import winkhouse.dao.ContattiDAO;
import winkhouse.dao.DatiCatastaliDAO;
import winkhouse.dao.EntityDAO;
import winkhouse.dao.GCalendarDAO;
import winkhouse.dao.GDataDAO;
import winkhouse.dao.ImmaginiDAO;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.dao.PermessiDAO;
import winkhouse.dao.PermessiUIDAO;
import winkhouse.dao.PromemoriaDAO;
import winkhouse.dao.ReportDAO;
import winkhouse.dao.ReportMarkersDAO;
import winkhouse.dao.RicercheDAO;
import winkhouse.dao.RiscaldamentiDAO;
import winkhouse.dao.StanzeDAO;
import winkhouse.dao.StatoConservativoDAO;
import winkhouse.dao.TipiAppuntamentiDAO;
import winkhouse.dao.TipologiaContattiDAO;
import winkhouse.dao.TipologiaStanzeDAO;
import winkhouse.dao.TipologieImmobiliDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.orm.Agenti;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.vo.AffittiVO;
import winkhouse.vo.AgentiAppuntamentiVO;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ColloquiAgentiVO;
import winkhouse.vo.ColloquiVO;
import winkhouse.vo.ImmobiliVO;



public class AgentiHelper {

	public AgentiHelper() {
		
	}
	
	private class DeleteUpdaterProgressDialog implements IRunnableWithProgress{
		
		private	Agenti agente = null;
		private	int anagrafiche = 0;
		private	int colloqui = 0;
		private	int commentiColloqui = 0;
		private	int immobili = 0;
		private	int affitti = 0;
		private	int agentiappuntamenti = 0;
		
		public DeleteUpdaterProgressDialog(Shell parent,
										   Agenti agente,
										   int anagrafiche,
										   int colloqui,
										   int commentiColloqui,
										   int immobili,
										   int affitti,
										   int agentiappuntamenti){
			this.agente = agente;
			this.anagrafiche = anagrafiche;
			this.colloqui = colloqui;
			this.commentiColloqui = commentiColloqui;
			this.immobili = immobili;
			this.affitti = affitti;
			this.agentiappuntamenti = agentiappuntamenti; 
		}

		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

			Connection con = ConnectionManager.getInstance().getConnection();
			
			EntityDAO entityDAO = new EntityDAO();
			AttributeDAO attributeDAO = new AttributeDAO();
			AttributeValueDAO attributeValueDAO = new AttributeValueDAO();
			GCalendarDAO gCalendarDAO = new GCalendarDAO();
			GDataDAO gDataDAO = new GDataDAO();
			PromemoriaDAO promemoriaDAO = new PromemoriaDAO();
			
			AgentiDAO aDAO = new AgentiDAO();
			AgentiAppuntamentiDAO aaDAO = new AgentiAppuntamentiDAO();
			PermessiDAO	permessiDAO = new PermessiDAO();
			PermessiUIDAO permessiUIDAO = new PermessiUIDAO();
			
			ReportDAO reportDAO = new ReportDAO();
			ReportMarkersDAO reportMarkersDAO = new ReportMarkersDAO();
			RicercheDAO ricercheDAO = new RicercheDAO();
			
			AbbinamentiDAO abbinamentiDAO = new AbbinamentiDAO();
			AnagraficheDAO anagraficheDAO = new AnagraficheDAO();
			AnagraficheAppuntamentiDAO anagraficheAppuntamentiDAO = new AnagraficheAppuntamentiDAO();
			ClassiClientiDAO classiClientiDAO = new ClassiClientiDAO();
			AppuntamentiDAO appuntamentiDAO = new AppuntamentiDAO();
			TipiAppuntamentiDAO tipiAppuntamentiDAO = new TipiAppuntamentiDAO();
			TipologiaContattiDAO tipologiaContattiDAO = new TipologiaContattiDAO();
			
			ColloquiDAO colloquiDAO = new ColloquiDAO();
			AllegatiColloquiDAO allegatiColloquiDAO = new AllegatiColloquiDAO();			
			ColloquiAgentiDAO colloquiAgentiDAO = new ColloquiAgentiDAO();
			ColloquiAnagraficheDAO colloquiAnagraficheDAO = new ColloquiAnagraficheDAO(); 
			ColloquiCriteriRicercaDAO colloquiCriteriRicercaDAO = new ColloquiCriteriRicercaDAO();
			
			ImmobiliDAO immobiliDAO = new ImmobiliDAO();
			AllegatiImmobiliDAO allegatiImmobiliDAO = new AllegatiImmobiliDAO();
			ClassiEnergeticheDAO classiEnergeticheDAO = new ClassiEnergeticheDAO();
			DatiCatastaliDAO datiCatastaliDAO = new DatiCatastaliDAO();
			ImmaginiDAO immaginiDAO = new ImmaginiDAO();
			RiscaldamentiDAO riscaldamentiDAO = new RiscaldamentiDAO();
			StanzeDAO stanzeDAO = new StanzeDAO();
			StatoConservativoDAO statoConservativoDAO = new StatoConservativoDAO();
			TipologiaStanzeDAO tipologiaStanzeDAO = new TipologiaStanzeDAO();
			TipologieImmobiliDAO tipologieImmobiliDAO = new TipologieImmobiliDAO();
			
			AffittiDAO affittiDAO = new AffittiDAO(); 
			AffittiAllegatiDAO affittiAllegatiDAO = new AffittiAllegatiDAO();
			AffittiAnagraficheDAO affittiAnagraficheDAO = new AffittiAnagraficheDAO();
			AffittiRateDAO affittiRateDAO = new AffittiRateDAO();
			AffittiSpeseDAO affittiSpeseDAO = new AffittiSpeseDAO();
			
			
			
			ContattiDAO cDAO = new ContattiDAO();
			
			int countoperations = 1;
			
			if (this.anagrafiche != 0){
				countoperations ++;
			}
			if (this.colloqui != 0){
				countoperations ++;
			}
			if (this.commentiColloqui != 0){
				countoperations ++;
			}
			if (this.immobili != 0){
				countoperations ++;
			}
			if (this.affitti != 0){
				countoperations ++;
			}
			if (this.agentiappuntamenti != 0){
				countoperations ++;
			}
			
			monitor.beginTask("Cancellazione agente", (countoperations * 10));
			
		monitor.setTaskName("Aggiornamento anagrafiche");
		if (anagraficheDAO.updateAgenteInseritore(this.agente.getCodAgente(), null, con, false) && 
			anagraficheDAO.updateAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
			anagraficheAppuntamentiDAO.updateAnagraficheAppuntamentiAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
			entityDAO.updateEntityAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
			attributeDAO.updateAttributeAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
			attributeValueDAO.updateAttributeValueAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
			classiClientiDAO.updateClasseClienteAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
			reportDAO.updateReportAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
			reportMarkersDAO.updateReportMarkersAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
			ricercheDAO.updateRichercheAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
			appuntamentiDAO.updateAppuntamentiAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
			tipiAppuntamentiDAO.updateTipiAppuntamentiAgenteUpdate(this.agente.getCodAgente(), null, con, false) && 
			promemoriaDAO.updatePromemoriaAgente(this.agente.getCodAgente(), null, con, false) &&
			promemoriaDAO.updatePromemoriaAgenteUpdate(this.agente.getCodAgente(), null, con, false)){
			
			monitor.worked(10);
			monitor.setTaskName("Aggiornamento colloqui");
			if (colloquiDAO.updateAgenteInseritore(this.agente.getCodAgente(), null, con, false) &&
				colloquiDAO.updateColloquiAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
				allegatiColloquiDAO.updateAllegatiColloquioAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
				colloquiCriteriRicercaDAO.updateCriteriRicercaAgenteUpdate(this.agente.getCodAgente(), null, con, false)){
				
				monitor.worked(10);
				monitor.setTaskName("Aggiornamento commenti colloqui");
				if (colloquiAgentiDAO.updateAgente(this.agente.getCodAgente(), null, con, false) &&
					colloquiAgentiDAO.updateAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
					colloquiAnagraficheDAO.updateColloquiAnagraficheAgenteUpdate(this.agente.getCodAgente(), null, con, false)){
					
					monitor.worked(10);
					monitor.setTaskName("Aggiornamento immobile");
					if (immobiliDAO.updateAgenteInseritore(this.agente.getCodAgente(), null, con, false) &&
						immobiliDAO.updateImmobiliAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
						abbinamentiDAO.updateAbbinamentiAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
						allegatiImmobiliDAO.updateAllegatiImmobiliAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
						classiEnergeticheDAO.updateClassiEnergeticheAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
						datiCatastaliDAO.updateDatiCatastaliAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
						immaginiDAO.updateImmaginiAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
						riscaldamentiDAO.updateRiscaldamentiAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
						stanzeDAO.updateStanzeAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
						statoConservativoDAO.updateStatoConservativoAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
						tipologiaStanzeDAO.updateTipologiaStanzeAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
						tipologieImmobiliDAO.updateTipologieImmobiliAgenteUpdate(this.agente.getCodAgente(), null, con, false)){
						
						monitor.worked(10);
						monitor.setTaskName("Aggiornamento affitti");
						if (affittiDAO.updateAgenteInseritore(this.agente.getCodAgente(), null, con, false) &&
							affittiDAO.updateAffittiAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
							affittiAllegatiDAO.updateAffittiAllegatiAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
							affittiAnagraficheDAO.updateAffittiAnagraficheAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
							affittiRateDAO.updateAffittiRateAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
							affittiSpeseDAO.updateAffittiSpeseAgenteUpdate(this.agente.getCodAgente(), null, con, false)){
							
							monitor.worked(10);
							monitor.setTaskName("Aggiornamento appuntamneti");
							if (aaDAO.deleteAgentiAppuntamentiByAgente(this.agente.getCodAgente(), con, false) && 
								aaDAO.updateAgentiAppuntamentiAgenteUpdate(this.agente.getCodAgente(), null, con, false)){
								
								monitor.worked(10);
								monitor.setTaskName("Cancellazione contatti");								
								if (cDAO.deleteByAgente(this.agente.getCodAgente(), con, false) &&
									cDAO.updateContattoAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
									tipologiaContattiDAO.updateTipologiaContattoAgenteUpdate(this.agente.getCodAgente(), null, con, false)){
									
									monitor.worked(10);
									monitor.setTaskName("Aggiornamento agenti");
									if (aDAO.updateAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
									    gCalendarDAO.updateGCalendarsAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
									    gDataDAO.updateGoogleDataAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
									    permessiDAO.deletePermessoByCodAgente(this.agente.getCodAgente(), con, false) &&
									    permessiDAO.updatePermessiAgenteUpdate(this.agente.getCodAgente(), null, con, false) &&
									    permessiUIDAO.deletePermessoByAgente(this.agente.getCodAgente(), con, false) &&
									    permessiUIDAO.updatePermessiAgenteUpdate(this.agente.getCodAgente(), null, con, false)){
										
										monitor.worked(10);
										if (aDAO.delete(this.agente.getCodAgente(), con, false)){
											monitor.worked(10);
										
											try{
												con.commit();
												MobiliaDatiBaseCache.getInstance().setAgenti(null);
											}catch (SQLException e) {
												con = null;
											}
											monitor.worked(10);
											monitor.setTaskName("Operazione completata con successo");
										}else{
											try {
												con.rollback();
											} catch (SQLException e) {
												e.printStackTrace();
											}
											MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
																	"Errore", 
																	"Errore aggiornamento affitti \n la cancellazione dell'agente � annullata");
										}
									}else{
										try {
											con.rollback();
										} catch (SQLException e) {
											e.printStackTrace();
										}
										MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
																"Errore", 
																"Errore aggiornamento affitti \n la cancellazione dell'agente � annullata");
									}
									
								}else{
									try {
										con.rollback();
									} catch (SQLException e) {
										e.printStackTrace();
									}
									MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
															"Errore", 
															"Errore aggiornamento affitti \n la cancellazione dell'agente � annullata");
								}
	
							}else{
								try {
									con.rollback();
								} catch (SQLException e) {
									e.printStackTrace();
								}
								MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
														"Errore", 
														"Errore aggiornamento affitti \n la cancellazione dell'agente � annullata");
							}
						
						}else{
							try {
								con.rollback();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
													"Errore", 
													"Errore aggiornamento immobili \n la cancellazione dell'agente � annullata");
						}
					
					}else{
						try {
							con.rollback();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
												"Errore", 
												"Errore aggiornamento commenti agenti \n la cancellazione dell'agente � annullata");
					}
				
				}else{
					try {
						con.rollback();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
											"Errore", 
											"Errore aggiornamento colloqui \n la cancellazione dell'agente � annullata");
				}
			
			}else{
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
										"Errore", 
										"Errore aggiornamento anagrafiche \n la cancellazione dell'agente � annullata");
			}
				
		}else{
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
									"Errore", 
									"Errore cancellazione agente \n la cancellazione dell'agente � annullata");
		}

		}
		
	}
	
	public Boolean deleteAgente(Agenti agentiVO, ObjectContext ocAgenti){
		
		Boolean result = true;
		ProgressMonitorDialog pmd = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		IRunnableWithProgress irwpDeleteAgente = new IRunnableWithProgress() {

			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				try {
					ocAgenti.deleteObjects(agentiVO.getContattis1());
					ocAgenti.deleteObject(agentiVO);			
					ocAgenti.commitChanges();
				} catch (DeleteDenyException e) {
					MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							"Errore", e.getMessage());
				}
				
			}
			
		};
		try {
			pmd.run(false, true, irwpDeleteAgente);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ERROR);
			mb.setText("impossibile cancellare tutti gli abbinamenti");
			mb.setMessage("impossibile cancellare tutti gli abbinamenti");			
			mb.open();							

		} catch (InterruptedException e) {
			e.printStackTrace();
			MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),SWT.ERROR);
			mb.setText("impossibile cancellare tutti gli abbinamenti");
			mb.setMessage("impossibile cancellare tutti gli abbinamenti");			
			mb.open();																	
		}
		return result;
	}
	
	private String buildDeleteMessage(Agenti agente, int anagraficheSize,int collquiSize, 
									  int commentiColloquiSize, int immobiliSize, int affittiSize,
									  int agentiappuntamentiSize){
		String returnValue = "";
		
		returnValue += "Cancellazione agente : " + agente.getCognome() + " " + agente.getNome() + "\n";
		if (anagraficheSize != 0){
			returnValue += "Verranno aggiornate " + anagraficheSize + " anagrafiche" + "\n";
		}
		if (collquiSize != 0){
			returnValue += "Verranno aggiornati " + collquiSize + " colloqui " + "\n";
		}
		if (commentiColloquiSize != 0){
			returnValue += "Verranno aggiornati " + commentiColloquiSize + " commenti nei colloqui " + "\n";
		}
		if (immobiliSize != 0){
			returnValue += "Verranno aggiornati " + immobiliSize + " immobili \n";
		}
		if (affittiSize != 0){
			returnValue += "Verranno aggiornati " + affittiSize + " affitti \n";
		}
		if (agentiappuntamentiSize != 0){
			returnValue += "Verranno aggiornati " + agentiappuntamentiSize + " appuntamenti \n";
		}
		
		returnValue += " Procedere con l'operazione ?";
		
		return returnValue;
	}
	
	public boolean updateDatiBase(ArrayList<Agenti> agenti, ObjectContext oc){
				
		boolean returnValue = true;
		try {
			oc.commitChanges();
		} catch (Exception e) {
			returnValue = false;
		}
//			
//		if (agenti != null){
//			
//			AgentiDAO agentiDAO = new AgentiDAO();			
//			Iterator<Agenti> it  = agenti.iterator();
//			
//			while (it.hasNext()){
//				Agenti agente = it.next();
//				if (!agentiDAO.saveUpdate(agente, null, true)){
//					returnValue = false;
//					MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
//											"Errore salvataggio agente", 
//											"Si è verificato un errore nel salvataggio dell'agente : " + 
//											agente.getCognome() + " " + agente.getNome());					
//				}
//			}
//			MobiliaDatiBaseCache.getInstance().setAgenti(null);
//			
//		}
		
		return returnValue;
	}
	
	public boolean checkPws(ArrayList agenti){
		HashMap hm = new HashMap();
		for (Object object : agenti) {			
			hm.put(((AgentiVO)object).getUsername()+((AgentiVO)object).getPassword(),null);
		}
		
		if (hm.size() == agenti.size()){
			return true;
		}else{
			return false;
		}
	}
		
}
