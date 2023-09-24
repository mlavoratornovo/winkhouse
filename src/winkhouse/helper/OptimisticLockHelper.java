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
import winkhouse.vo.AgentiVO;


public class OptimisticLockHelper {
	
	public final static String SOVRASCRIVI = "Sovrascrivi";
	public final static String VISUALIZZA = "Visualizza";
	public final static String ANNULLA = "Annulla";
	
	public String checkOLImmobile(ImmobiliModel immobile){
		
		if ((immobile.getCodImmobile() == null) || (immobile.getCodImmobile() == 0)){
			return SOVRASCRIVI;
		}else{
			
			ImmobiliDAO iDAO = new ImmobiliDAO();
			ImmobiliModel im_DB = (ImmobiliModel)iDAO.getImmobileById(ImmobiliModel.class.getName(), immobile.getCodImmobile());
			
			if (im_DB.getDateUpdate() != null){
				
				if (im_DB.getDateUpdate().after(immobile.getDateUpdate())){
					
					String message = "ATTENZIONE : l'immobile " + immobile.toString() + "\n è stato modificato da un altro agente mentre lo stavi editando.\n";
					if (im_DB.getCodUserUpdate() != null){
						AgentiDAO aDAO = new AgentiDAO();
						AgentiVO aVO = (AgentiVO)aDAO.getAgenteById(AgentiVO.class.getName(), im_DB.getCodUserUpdate());
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

	public String checkOLAnagrafica(AnagraficheModel anagrafica){
		
		if ((anagrafica.getCodAnagrafica() == null) || (anagrafica.getCodAnagrafica() == 0)){
			return SOVRASCRIVI;
		}else{
			
			AnagraficheDAO aDAO = new AnagraficheDAO();
			AnagraficheModel am_DB = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(), anagrafica.getCodAnagrafica());
			
			if (am_DB.getDateUpdate() != null){
				
				if (am_DB.getDateUpdate().after(anagrafica.getDateUpdate())){
					
					String message = "ATTENZIONE : l'anagrafica " + anagrafica.toString() + "\n è stata modificata da un altro agente mentre lo stavi editando.\n";
					if (am_DB.getCodUserUpdate() != null){
						AgentiDAO agDAO = new AgentiDAO();
						AgentiVO aVO = (AgentiVO)agDAO.getAgenteById(AgentiVO.class.getName(), am_DB.getCodUserUpdate());
						if (aVO != null){
							message += "L'agente che ha effettuato le modifiche è " + aVO.toString();
						}
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

	public String checkOLAffitto(AffittiModel affitto){
		
		if ((affitto.getCodAffitti() == null) || (affitto.getCodAffitti() == 0)){
			return SOVRASCRIVI;
		}else{
			
			AffittiDAO aDAO = new AffittiDAO();
			AffittiModel am_DB = (AffittiModel)aDAO.getAffittoByID(AffittiModel.class.getName(), affitto.getCodAffitti());
			
			if (am_DB.getDateUpdate() != null){
				
				if (am_DB.getDateUpdate().after(affitto.getDateUpdate())){
					
					String message = "ATTENZIONE : l'affitto " + affitto.toString() + "\n è stato modificato da un altro agente mentre lo stavi editando.\n";
					if (am_DB.getCodUserUpdate() != null){
						AgentiDAO agDAO = new AgentiDAO();
						AgentiVO aVO = (AgentiVO)agDAO.getAgenteById(AgentiVO.class.getName(), am_DB.getCodUserUpdate());
						if (aVO != null){
							message += "L'agente che ha effettuato le modifiche è " + aVO.toString();
						}
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

	public String checkOLColloquio(ColloquiModel colloquio){
		
		if ((colloquio.getCodColloquio() == null) || (colloquio.getCodColloquio() == 0)){
			return SOVRASCRIVI;
		}else{
			
		
			if (colloquio.getCodColloquio() != null){
							
				ColloquiDAO cDAO = new ColloquiDAO();
				ColloquiModel cm_DB = (ColloquiModel)cDAO.getColloquioById(ColloquiModel.class.getName(), colloquio.getCodColloquio());
				
				if (cm_DB.getDateUpdate() != null){
					
					if (cm_DB.getDateUpdate().after(colloquio.getDateUpdate())){
						
						String message = "ATTENZIONE : il colloquio " + colloquio.toString() + "\n è stato modificato da un altro agente mentre lo stavi editando.\n";
						if (cm_DB.getCodUserUpdate() != null){
							AgentiDAO agDAO = new AgentiDAO();
							AgentiVO aVO = (AgentiVO)agDAO.getAgenteById(AgentiVO.class.getName(), cm_DB.getCodUserUpdate());
							if (aVO != null){
								message += "L'agente che ha effettuato le modifiche è " + aVO.toString();
							}
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
			}else{
				return SOVRASCRIVI;
			}
		}		
		
	}

	public String checkOLAppuntamento(AppuntamentiModel appuntamento){
		
		if ((appuntamento.getCodAppuntamento() == null) || (appuntamento.getCodAppuntamento() == 0)){
			return SOVRASCRIVI;
		}else{
			
			AppuntamentiDAO aDAO = new AppuntamentiDAO();
			AppuntamentiModel am_DB = (AppuntamentiModel)aDAO.getAppuntamentoByID(AppuntamentiModel.class.getName(), appuntamento.getCodAppuntamento());
			
			if (am_DB.getDateUpdate() != null){
				
				if (am_DB.getDateUpdate().after(appuntamento.getDateUpdate())){
					
					String message = "ATTENZIONE : l'appuntamento " + appuntamento.toString() + "\n è stato modificato da un altro agente mentre lo stavi editando.\n";
					if (am_DB.getCodUserUpdate() != null){
						AgentiDAO agDAO = new AgentiDAO();
						AgentiVO aVO = (AgentiVO)agDAO.getAgenteById(AgentiVO.class.getName(), am_DB.getCodUserUpdate());
						if (aVO != null){
							message += "L'agente che ha effettuato le modifiche è " + aVO.toString();
						}
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
					
					String message = "ATTENZIONE : il report " + report.toString() + "\n è stato modificato da un altro agente mentre lo stavi editando.\n";
					if (rm_DB.getCodUserUpdate() != null){
						AgentiDAO agDAO = new AgentiDAO();
						AgentiVO aVO = (AgentiVO)agDAO.getAgenteById(AgentiVO.class.getName(), rm_DB.getCodUserUpdate());
						if (aVO != null){
							message += "L'agente che ha effettuato le modifiche è " + aVO.toString();
						}
					}
					MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
															 "Winkhouse : il report è stato modificato", null,
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
