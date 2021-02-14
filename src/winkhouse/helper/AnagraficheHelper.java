package winkhouse.helper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.AbbinamentiDAO;
import winkhouse.dao.AffittiAnagraficheDAO;
import winkhouse.dao.AffittiRateDAO;
import winkhouse.dao.AffittiSpeseDAO;
import winkhouse.dao.AnagraficheAppuntamentiDAO;
import winkhouse.dao.AnagraficheDAO;
import winkhouse.dao.AppuntamentiDAO;
import winkhouse.dao.AttributeValueDAO;
import winkhouse.dao.ColloquiAnagraficheDAO;
import winkhouse.dao.ColloquiDAO;
import winkhouse.dao.ContattiDAO;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.dao.ImmobiliPropietariDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.AppuntamentiModel;
import winkhouse.model.AttributeModel;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ContattiModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ContattiVO;



public class AnagraficheHelper {

	public boolean updateAnagrafiche(AnagraficheModel anagrafica,
									 Boolean saveContatti,
									 Connection connection,
									 Boolean doCommit){
		
		Boolean returnvalue = true;
		
		if (anagrafica != null){
			
			AnagraficheDAO anagraficheDAO = new AnagraficheDAO();
			Connection con = (connection == null)?ConnectionManager.getInstance().getConnection():connection;
			checkAnagraficaDB(anagrafica);
			
			if (anagrafica != null){
				
				anagrafica.setDateUpdate(new Date());
				if (WinkhouseUtils.getInstance().getLoggedAgent() != null){
					anagrafica.setCodUserUpdate(WinkhouseUtils.getInstance().getLoggedAgent().getCodAgente());
				}
				
				if (!anagraficheDAO.saveUpdate(anagrafica, con, doCommit)){
					returnvalue = false;
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore salvataggio anagrafica", 
											"Si è verificato un errore nel salvataggio dell'anagrafica : " + 
											anagrafica.getCognome() + " " + anagrafica.getNome());
					
				}else{
					ContattiHelper ch = new ContattiHelper();
					Iterator ite = anagrafica.getContatti().iterator();
					boolean update = true;
					while (ite.hasNext()){
						ContattiModel contatto = (ContattiModel)ite.next();
						checkContattoDB(contatto);
					}
						if (!ch.updateListaContatti(anagrafica, con, doCommit)){
							returnvalue = false;
						}
				}
			}
		
		}
		
		return returnvalue;
				
	}
	
	private ContattiModel checkContattoDB(ContattiModel cModel){
		if (cModel.getCodContatto() != 0){
			if (new ContattiDAO().getContattoById(ContattiVO.class.getName(), cModel.getCodContatto()) == null){
				MessageDialog.openError(PlatformUI.getWorkbench()
						     					  .getActiveWorkbenchWindow()
						     					  .getShell(),
						     			"ERRORE", 
										"Il contatto che si sta cercando di aggiornare è stata cancellata da un altro utente");
				cModel = null;

		/*		if (MessageDialog.openConfirm(PlatformUI.getWorkbench()
														.getActiveWorkbenchWindow()
														.getShell(), 
											  "ATTENZIONE", 
											  "Si sta cercando di aggiornare il contatto " + cModel.toString() + " già cancellato da un altro utente. \n" +
											  "Se si prosegue con l'operazione li contatto verrà inserito nella base dati." +
			      							  "Procedere con l'operazione ?")){
					cModel.setCodContatto(0);
				}else{
					cModel = null;
				}*/	
				
			}
		}
		return cModel;
	}

	
	private AnagraficheModel checkAnagraficaDB(AnagraficheModel aModel){
		
		AnagraficheDAO aDAO = new AnagraficheDAO();
		
		if ((aModel.getCodAnagrafica()!= null) && (aModel.getCodAnagrafica() != 0)){
			
			if (aDAO.getAnagraficheById(AnagraficheVO.class.getName(), aModel.getCodAnagrafica()) == null){
				MessageDialog.openError(PlatformUI.getWorkbench()
												  .getActiveWorkbenchWindow()
												  .getShell(),
										"ERRORE", 
										"L'anagrafica che si sta cercando di aggiornare è stata cancellata da un altro utente");
				aModel = null;
			}
			
		}
		
		return aModel;
	} 
	
	public boolean deleteAnagrafica(AnagraficheModel anagrafica){
		
		boolean returnValue = true;
				
		AnagraficheDAO aDAO = new AnagraficheDAO();
		ColloquiAnagraficheDAO colloquiAnagraficheDAO = new ColloquiAnagraficheDAO();
		AffittiRateDAO affittiRateDAO = new AffittiRateDAO();
		AffittiSpeseDAO affittiSpeseDAO = new AffittiSpeseDAO();
		AnagraficheAppuntamentiDAO anagraficheAppuntamentiDAO = new AnagraficheAppuntamentiDAO();
		ImmobiliDAO immobiliDAO = new ImmobiliDAO();
		AffittiAnagraficheDAO affittiAnagraficheDAO = new AffittiAnagraficheDAO();
		ContattiDAO contattiDAO = new ContattiDAO();
		AbbinamentiDAO abbinamentiDAO = new AbbinamentiDAO();
		ImmobiliPropietariDAO immobiliPropietariDAO = new ImmobiliPropietariDAO();
		AttributeValueDAO attributeValueDAO = new AttributeValueDAO();
		
		Connection con = ConnectionManager.getInstance().getConnection();
		
		if (colloquiAnagraficheDAO.deleteByAnagrafica(anagrafica.getCodAnagrafica(), con, false)){
			
			if (affittiRateDAO.deleteAffittiRateByCodAnagrafica(anagrafica.getCodAnagrafica(), con, false)){
				
				if (affittiSpeseDAO.deleteAffittiSpeseByCodAnagrafica(anagrafica.getCodAnagrafica(), con, false)){
				
					if (anagraficheAppuntamentiDAO.deleteAnagraficheAppuntamentiByAnagrafica(anagrafica.getCodAnagrafica(), con, false)){
						
						if (immobiliDAO.updateAnagrafica(anagrafica.getCodAnagrafica(), null, con, false)){
							
							if (affittiAnagraficheDAO.deleteAffittiAnagraficheByCodAnagrafica(anagrafica.getCodAnagrafica(), con, false)){
								
								if (contattiDAO.deleteByAnagrafica(anagrafica.getCodAnagrafica(), con, false)){
									
									if (abbinamentiDAO.deleteAbbinamentiByCodAnagrafica(anagrafica.getCodAnagrafica(), con, false)){
										
										if (immobiliPropietariDAO.deleteByCodAnagrafica(anagrafica.getCodAnagrafica(), con, false)){
											
											if (attributeValueDAO.deleteByClassNameObjectId(AnagraficheVO.class.getName(), anagrafica.getCodAnagrafica(), con, false)){
												
												if (!aDAO.delete(anagrafica.getCodAnagrafica(), con, false)){
													
													returnValue = false;
													
												}
																								
											}else {
												returnValue = false;
											}
											
											
										}else {
											returnValue = false;
										}
										
										
									}else {
										returnValue = false;
									}
									
									
								}else {
									returnValue = false;
								}
								
								
							}else {
								returnValue = false;
							}
							
							
						}else {
							returnValue = false;
						}
						
						
					}else {
						returnValue = false;
					}
					
					
				}else {
					returnValue = false;
				}
				
			}else {
				returnValue = false;
			}
			
		}else{
			returnValue = false;
		}
		
		try{
			if (returnValue){
				con.commit();
			}else{
				con.rollback();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
//		AbbinamentiDAO abDAO = new AbbinamentiDAO();
//		AppuntamentiHelper ah = new AppuntamentiHelper();
//		ColloquiHelper ch = new ColloquiHelper();
//		ContattiDAO cDAO = new ContattiDAO();
//		ImmobiliHelper ih = new ImmobiliHelper();
//		AnagraficheDAO aDAO = new AnagraficheDAO();
//		AppuntamentiDAO apDAO = new AppuntamentiDAO(); 
//		ColloquiDAO colDAO = new ColloquiDAO();
//		ImmobiliDAO iDAO = new ImmobiliDAO();
//		ImmaginiHelper imh = new ImmaginiHelper();
//		AttributeValueDAO avDAO = new AttributeValueDAO();
//		ImmobiliPropietariDAO ipDAO = new ImmobiliPropietariDAO();
//		
//		ArrayList delColloqui = new ArrayList();
//		ArrayList delImmagini = new ArrayList();
//		ArrayList delAllegatiImmobili = new ArrayList();
//		
//		boolean okDeleteAttribute = true;
//		
//		Connection con = ConnectionManager.getInstance()
//										  .getConnection();
//		
//		if (aDAO.delete(anagrafica.getCodAnagrafica(), con, false)){
//			
//			if (cDAO.deleteByAnagrafica(anagrafica.getCodAnagrafica(), con, false)){
//				
//				if (abDAO.deleteAbbinamentiByCodAnagrafica(anagrafica.getCodAnagrafica(), con, false)){
//					
//					for (Iterator<AttributeModel> iterator = anagrafica.getEntity().getAttributes().iterator(); iterator.hasNext();) {
//						AttributeModel attribute = iterator.next();
//						if (!avDAO.deleteByAttributeIdObjectId(attribute.getIdAttribute(), anagrafica.getCodAnagrafica(), con, false)){
//							okDeleteAttribute = false;
//							break;
//						}
//					}
//
//					if (okDeleteAttribute){
//						ArrayList alAppuntamenti = apDAO.listAppuntamentiByCodAnagrafica(AppuntamentiModel.class.getName(), 
//															  							 anagrafica.getCodAnagrafica());
//						
//						Iterator it = alAppuntamenti.iterator();
//						while (it.hasNext()){
//							AppuntamentiModel am = (AppuntamentiModel)it.next();
//							if (!ah.deleteAppuntamento(am, con)){
//								returnValue = false;
//								break;
//							}
//						}
//						
//						if (returnValue){
//							ArrayList colloqui = new ArrayList();
//							colloqui = colDAO.getColloquiByAnagrafica(ColloquiModel.class.getName(), 
//																	  anagrafica.getCodAnagrafica());
//							colloqui.addAll(colDAO.getColloquiByAnagraficaRicerca(ColloquiModel.class.getName(), 
//																	  			  anagrafica.getCodAnagrafica()));
//							Iterator itCollqui = colloqui.iterator();						
//							
//							while (it.hasNext()){
//								ColloquiModel cm  = (ColloquiModel)it.next();
//								HashMap resultDelColloquio = ch.deleteColloquio(cm, con, null);
//								if (!(Boolean)resultDelColloquio.get(ColloquiHelper.RESULT_DELETE_COLLOQUIO_DB)){
//									returnValue = false;
//									break;
//								}else{
//									delColloqui.addAll((ArrayList)resultDelColloquio.get(ColloquiHelper.LIST_DELETE_ALLEGATI_FILE));
//								}
//							}
//							
//						}
//						
////						if (returnValue){
////							ArrayList immobiliAnag = iDAO.getImmobiliByAnagrafica(ImmobiliModel.class.getName(), 
////																				  anagrafica.getCodAnagrafica());
////							Iterator itImmobili = immobiliAnag.iterator();
////							while (itImmobili.hasNext()){
////								ImmobiliModel immobile = (ImmobiliModel)itImmobili.next();
////								HashMap resultImmobile = ih.deleteImmobile(immobile, con);
////								if ((Boolean)resultImmobile.get(ImmobiliHelper.RESULT_DELETE_IMMOBILE_DATA_DB)){
////									delColloqui.addAll((ArrayList)resultImmobile.get(ImmobiliHelper.LIST_ATTACHMENTS_COLLOQUI_IMMOBILE_DELETE));
////									delImmagini.addAll((ArrayList)resultImmobile.get(ImmobiliHelper.LIST_IMAGES_IMMOBILE_DELETE));
////									delAllegatiImmobili.addAll((ArrayList)resultImmobile.get(ImmobiliHelper.LIST_DELETE_ALLEGATI_FILE));
////								}else{
////									returnValue = false;
////									break;
////								}
////							}
////						}
//						
//						if (ipDAO.deleteByCodAnagrafica(anagrafica.getCodAnagrafica(), con, false) == false){
//							returnValue = false;
//						}
//						
//					}else{
//						returnValue = false;
//					}
//				}else{
//					returnValue = false;
//				}
//			}else{
//				returnValue = false;
//			}
//		}else{
//			returnValue = false;
//		}				
//		
//		if (returnValue){
//			try {
//				con.commit();				
//				if (!ch.deleteAllegatiColloqui(delColloqui)){
//					MessageDialog.openWarning(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
//							  				  "Cancellazione file allegati colloqui",
//							  				  "Alcuni documenti allegati ai colloqui non sono stati cancellati");
//				}
//				if (!imh.deleteImmagini(delImmagini)){
//					MessageDialog.openWarning(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
//							  				  "Cancellazione file immagini immobile",
//							  				  "Alcune immagini dell'immobile non sono state cancellate");				
//				}
//				if (!ih.deleteAllegatiImmobili(delAllegatiImmobili)){
//					MessageDialog.openWarning(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
//							  				  "Cancellazione file allegati immobile",
//							  				  "Alcuni allegati dell'immobile non sono stati cancellati");				
//				}
//				
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}else{
//			try {
//				con.rollback();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		
		return returnValue;
	}

	
}
