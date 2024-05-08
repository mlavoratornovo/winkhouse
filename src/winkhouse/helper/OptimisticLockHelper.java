package winkhouse.helper;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import winkhouse.dao.AffittiDAO;
import winkhouse.dao.AgentiDAO;
import winkhouse.dao.AnagraficheDAO;
import winkhouse.dao.AppuntamentiDAO;
import winkhouse.dao.ColloquiDAO;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.dao.ReportDAO;
import winkhouse.model.AffittiModel;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.AppuntamentiModel;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.model.ReportModel;
import winkhouse.orm.Affitti;
import winkhouse.orm.Agenti;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Appuntamenti;
import winkhouse.orm.Colloqui;
import winkhouse.orm.Immobili;
import winkhouse.vo.AgentiVO;


public class OptimisticLockHelper {
	
	public final static String SOVRASCRIVI = "Sovrascrivi";
	public final static String VISUALIZZA = "Visualizza";
	public final static String ANNULLA = "Annulla";
	
	public String checkOLImmobile(Immobili immobile){
		
		if (immobile.getCodImmobile() == 0){
			return SOVRASCRIVI;
		}else{
			
			ImmobiliDAO iDAO = new ImmobiliDAO();
			Immobili im_DB = iDAO.getImmobileById(immobile.getCodImmobile());
			
			if (im_DB.getDateupdate() != null){
				
				if (im_DB.getDateupdate().isAfter(immobile.getDateupdate())){
					
					String message = "ATTENZIONE : l'immobile " + immobile.toString() + "\n � stato modificato da un altro agente mentre lo stavi editando.\n";
					if (im_DB.getAgenti1() != null){
						AgentiDAO aDAO = new AgentiDAO();
						
						Agenti aVO = aDAO.getAgenteById(im_DB.getAgenti1().getCodAgente());
						if (aVO != null){
							message += "L'agente che ha effettuato le modifiche è " + aVO.toString();
						}
					}
					MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
															 "Winkhouse : l'immobile è stato modificato", null,
															 message,
															 MessageDialog.WARNING, new String[] {SOVRASCRIVI,VISUALIZZA,ANNULLA}, 0);
					switch (dialog.open()){
						case 0 :return SOVRASCRIVI;
						case 1 :return VISUALIZZA;
						case 2 :return ANNULLA;
						default : return ANNULLA;
					} 		
					
				}else{
					return SOVRASCRIVI;
				}
				
			}else{
				return SOVRASCRIVI;
			}
			
		}		
		
	}

	public String checkOLAnagrafica(Anagrafiche anagrafica){
		
		if (anagrafica.getCodAnagrafica() == 0){
			return SOVRASCRIVI;
		}else{
			
			AnagraficheDAO aDAO = new AnagraficheDAO();
			Anagrafiche am_DB = aDAO.getAnagraficheById(anagrafica.getCodAnagrafica());
			//AnagraficheModel am_DB = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(), anagrafica.getCodAnagrafica());
			
			if (am_DB.getDateupdate() != null){
				
				if (am_DB.getDateupdate().isAfter(anagrafica.getDateupdate())){
					
					String message = "ATTENZIONE : l'anagrafica " + anagrafica.toString() + "\n è stata modificata da un altro agente mentre lo stavi editando.\n";
					if (am_DB.getAgenti1() != null){
//						AgentiDAO agDAO = new AgentiDAO();
//						AgentiVO aVO = (AgentiVO)agDAO.getAgenteById(AgentiVO.class.getName(), am_DB.getCodUserUpdate());
//						if (aVO != null){
							message += "L'agente che ha effettuato le modifiche è " + am_DB.getAgenti1().toString();
//						}
					}
					MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
															 "Winkhouse : l'anagrafica è stata modificata", null,
															 message,
															 MessageDialog.WARNING, new String[] {SOVRASCRIVI,VISUALIZZA,ANNULLA}, 0);
					switch (dialog.open()){
						case 0 :return SOVRASCRIVI;
						case 1 :return VISUALIZZA;
						case 2 :return ANNULLA;
						default : return ANNULLA;
					} 		
					
				}else{
					return SOVRASCRIVI;
				}
				
			}else{
				return SOVRASCRIVI;
			}
			
		}		
		
	}

	public String checkOLAffitto(Affitti affitto){
		
		if (affitto.getCodAffitti() == 0){
			return SOVRASCRIVI;
		}else{
			
			AffittiDAO aDAO = new AffittiDAO();
			Affitti am_DB = aDAO.getAffittoByID(affitto.getCodAffitti());
			
			if (am_DB.getDateupdate() != null){
				
				if (am_DB.getDateupdate().isAfter(affitto.getDateupdate())){
					
					String message = "ATTENZIONE : l'affitto " + affitto.toString() + "\n è stato modificato da un altro agente mentre lo stavi editando.\n";
					if (am_DB.getAgenti1() != null){
						message += "L'agente che ha effettuato le modifiche è " + am_DB.getAgenti1().toString();
					}
					MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
															 "Winkhouse : l'affitto è stato modificato", null,
															 message,
															 MessageDialog.WARNING, new String[] {SOVRASCRIVI,VISUALIZZA,ANNULLA}, 0);
					switch (dialog.open()){
						case 0 :return SOVRASCRIVI;
						case 1 :return VISUALIZZA;
						case 2 :return ANNULLA;
						default : return ANNULLA;
					} 		
					
				}else{
					return SOVRASCRIVI;
				}
				
			}else{
				return SOVRASCRIVI;
			}
			
		}		
		
	}

	public String checkOLColloquio(Colloqui colloquio){
		
		if (colloquio.getCodColloquio() == 0){
			return SOVRASCRIVI;
		}else{
			ColloquiDAO cDAO = new ColloquiDAO();
			Colloqui cm_DB = cDAO.getColloquioById(colloquio.getCodColloquio());
			
			if (cm_DB.getDateupdate() != null){
				
				if (cm_DB.getDateupdate().isAfter(colloquio.getDateupdate())){
					
					String message = "ATTENZIONE : il colloquio " + colloquio.toString() + "\n è stato modificato da un altro agente mentre lo stavi editando.\n";
					if (cm_DB.getAgenti()!= null){
						message += "L'agente che ha effettuato le modifiche è " + cm_DB.getAgenti().toString();
					}
					MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
															 "Winkhouse : il colloquio è stato modificato", null,
															 message,
															 MessageDialog.WARNING, new String[] {SOVRASCRIVI,VISUALIZZA,ANNULLA}, 0);
					switch (dialog.open()){
						case 0 :return SOVRASCRIVI;
						case 1 :return VISUALIZZA;
						case 2 :return ANNULLA;
						default : return ANNULLA;
					} 		
					
				}else{
					return SOVRASCRIVI;
				}
		
			}else{
				return SOVRASCRIVI;
			}
		}		
		
	}

	public String checkOLAppuntamento(Appuntamenti appuntamento){
		
		if (appuntamento.getCodAppuntamento() == 0){
			return SOVRASCRIVI;
		}else{
			
			AppuntamentiDAO aDAO = new AppuntamentiDAO();
			Appuntamenti am_DB = aDAO.getAppuntamentoByID(appuntamento.getCodAppuntamento());
			
			if (am_DB.getDateupdate() != null){
				
				if (am_DB.getDateupdate().isAfter(appuntamento.getDateupdate())){
					
					String message = "ATTENZIONE : l'appuntamento " + appuntamento.toString() + "\n è stato modificato da un altro agente mentre lo stavi editando.\n";
					if (am_DB.getAgenti() != null){
						message += "L'agente che ha effettuato le modifiche è " + am_DB.getAgenti().toString();
					}
					MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
															 "Winkhouse : l'appuntamento è stato modificato", null,
															 message,
															 MessageDialog.WARNING, new String[] {SOVRASCRIVI,VISUALIZZA,ANNULLA}, 0);
					switch (dialog.open()){
						case 0 :return SOVRASCRIVI;
						case 1 :return VISUALIZZA;
						case 2 :return ANNULLA;
						default : return ANNULLA;
					} 		
					
				}else{
					return SOVRASCRIVI;
				}
				
			}else{
				return SOVRASCRIVI;
			}
			
		}		
		
	}

	public String checkOLReport(ReportModel report){
		
		if ((report.getCodReport() == null) || (report.getCodReport() == 0)){
			return SOVRASCRIVI;
		}else{
			
			ReportDAO rDAO = new ReportDAO();
			ReportModel rm_DB = (ReportModel)rDAO.getReportByID(report.getCodReport());
			
			if (rm_DB.getDateUpdate() != null){
				
				if (rm_DB.getDateUpdate().after(report.getDateUpdate())){
					
					String message = "ATTENZIONE : il report " + report.toString() + "\n � stato modificato da un altro agente mentre lo stavi editando.\n";
					if (rm_DB.getCodUserUpdate() != null){
						AgentiDAO agDAO = new AgentiDAO();
						AgentiVO aVO = (AgentiVO)agDAO.getAgenteById(AgentiVO.class.getName(), rm_DB.getCodUserUpdate());
						if (aVO != null){
							message += "L'agente che ha effettuato le modifiche � " + aVO.toString();
						}
					}
					MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
															 "Winkhouse : il report � stato modificato", null,
															 message,
															 MessageDialog.WARNING, new String[] {SOVRASCRIVI,VISUALIZZA,ANNULLA}, 0);
					switch (dialog.open()){
						case 0 :return SOVRASCRIVI;
						case 1 :return VISUALIZZA;
						case 2 :return ANNULLA;
						default : return ANNULLA;
					} 		
					
				}else{
					return SOVRASCRIVI;
				}
				
			}else{
				return SOVRASCRIVI;
			}
			
		}		
		
	}
	
}