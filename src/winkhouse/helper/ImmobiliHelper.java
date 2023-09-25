package winkhouse.helper;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.AbbinamentiDAO;
import winkhouse.dao.AffittiDAO;
import winkhouse.dao.AllegatiImmobiliDAO;
import winkhouse.dao.AttributeValueDAO;
import winkhouse.dao.ColloquiDAO;
import winkhouse.dao.DatiCatastaliDAO;
import winkhouse.dao.ImmaginiDAO;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.dao.ImmobiliPropietariDAO;
import winkhouse.dao.StanzeDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.model.AffittiModel;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.model.StanzeImmobiliModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.AffittiAllegatiVO;
import winkhouse.vo.AllegatiImmobiliVO;
import winkhouse.vo.DatiCatastaliVO;
import winkhouse.vo.ImmobiliPropietariVO;
import winkhouse.vo.ImmobiliVO;



public class ImmobiliHelper {

	public final static String RESULT_DELETE_IMMOBILE_DATA_DB = "datidbimmobile";
	public final static String LIST_IMAGES_IMMOBILE_DELETE = "listaimmaginiimmobile";
	public final static String LIST_ATTACHMENTS_COLLOQUI_IMMOBILE_DELETE = "listaallegaticolloquiimmobile";
	
	public final static String LIST_SAVE_ALLEGATI_FILE = "listasaveallegatiimmobili";
	public final static String LIST_DELETE_ALLEGATI_FILE = "listadeleteallegatiimmobili";
	public final static String RESULT_ALLEGATI_DB = "resultallegatiimmobilidb";
	
	public final static String SAVE_IMMOBILE_DB = "saveimmobiledb";
	
	private String pathAllegatiDestinazione = WinkhouseUtils.getInstance()
															  .getPreferenceStore()
															  .getString(WinkhouseUtils.ALLEGATIPATH) + 
															  File.separator + "immobili";

	public ImmobiliHelper() {

	}
	
	private Comparator<AllegatiImmobiliVO> comparerImmobiliAllegati = new Comparator<AllegatiImmobiliVO>(){

		@Override
		public int compare(AllegatiImmobiliVO arg0, AllegatiImmobiliVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodAllegatiImmobili()!=null) && (arg1.getCodAllegatiImmobili()!=null)){				
				if ((arg0.getCodAllegatiImmobili().intValue() == arg1.getCodAllegatiImmobili().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodAllegatiImmobili().intValue() < arg1.getCodAllegatiImmobili().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodAllegatiImmobili().intValue() > arg1.getCodAllegatiImmobili().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodAllegatiImmobili()!=null) && (arg1.getCodAllegatiImmobili()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};		

	private AllegatiImmobiliVO checkAllegatoImmobileDB(AllegatiImmobiliVO allegatiImmobiliVO){
		if (allegatiImmobiliVO.getCodAllegatiImmobili() != null){
			if (new AllegatiImmobiliDAO().getAllegatiById(AllegatiImmobiliVO.class.getName(), 
														  allegatiImmobiliVO.getCodAllegatiImmobili()) == null){
				if (MessageDialog.openConfirm(PlatformUI.getWorkbench()
														.getActiveWorkbenchWindow()
														.getShell(), 
											  "ATTENZIONE", 
											  "Si sta cercando di aggiornare l'allegato " + allegatiImmobiliVO.getNome() + " gi� cancellato da un altro utente. \n" +
											  "Se si prosegue con l'operazione l'allegato verr� inserito nella base dati." +
			      							  "Procedere con l'operazione ?")){
					allegatiImmobiliVO.setCodAllegatiImmobili(0);
				}else{
					allegatiImmobiliVO = null;
				}	
				
			}
		}
		return allegatiImmobiliVO;
	}

	public boolean saveImmobile(ImmobiliModel immobile){
		
		boolean returnValue = true;
		
		
//		MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "My Title", null,
//			    								"My message", MessageDialog.ERROR, new String[] { "First","Second", "Third" }, 0);
//			int result = dialog.open();
//			System.out.println(result); 		
//		
		
		
		
		StanzeImmobiliHelper sih = new StanzeImmobiliHelper();
		DatiCatastaliImmobiliHelper dcih = new DatiCatastaliImmobiliHelper();
		
		HashMap resultAllegati = null;
		
		Connection con = ConnectionManager.getInstance().getConnection();
		
		AnagraficheHelper ah = new AnagraficheHelper();
		if (ah.updateAnagrafiche(immobile.getAnagrafica(), true, con, false)){
			
			immobile.setAnagrafica(immobile.getAnagrafica());
			
			if (updateImmobile(immobile, con, false)){
				
				if (updateImmobiliPropietari(immobile,con,false)){
				
					ArrayList listaAllegati = immobile.getAllegati();
					Iterator itAllegati = listaAllegati.iterator();
					
					while (itAllegati.hasNext()){
						checkAllegatoImmobileDB((AllegatiImmobiliVO)itAllegati.next());
					}
					resultAllegati = updateListaAllegati(immobile, con);
					if ((Boolean)resultAllegati.get(ImmobiliHelper.RESULT_ALLEGATI_DB)){
						if (immobile.getStanze() != null){		
							
							ArrayList stanze = immobile.getStanze();
							Iterator itStanze = stanze.iterator();
							while (itStanze.hasNext()){
								sih.checkStanzeImmobiliDB((StanzeImmobiliModel)itStanze.next());
							}
							if (sih.updateListaStanze(immobile, con, false)){
								if (immobile.getDatiCatastali() != null){
									ArrayList datiCatastali = immobile.getDatiCatastali();
									Iterator itDatiCatastali = datiCatastali.iterator();
									while (itDatiCatastali.hasNext()){
										dcih.checkDatiCatastaliImmobiliDB((DatiCatastaliVO)itDatiCatastali.next());
									}
									if (dcih.updateDatiCatastali(immobile, con, false)){
										
									}else{
										returnValue = false;
									}
								}
							}else{
								returnValue = false;					
							}
							
						}else{
							returnValue = false;
						}
						
					}else{
						returnValue = false;
					}
				}else{
					returnValue = false;
				}
			}else{
				returnValue = false;
			}
		}else{
			returnValue = false;
		}
		
		if (returnValue){
			
			try {
				if (!con.isClosed()){
					try {
						con.commit();
						con.close();
						deleteAllegatiImmobili((ArrayList)resultAllegati.get(ImmobiliHelper.LIST_DELETE_ALLEGATI_FILE));
						saveAllegatiImmobili((ArrayList)resultAllegati.get(ImmobiliHelper.LIST_SAVE_ALLEGATI_FILE));
					} catch (SQLException e) {
						e.printStackTrace();
					}						
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}else{
			try {
				if (!con.isClosed()){
					con.rollback();
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
					
		return returnValue;
	}
	
	public HashMap updateListaAllegati(ImmobiliModel immobile,
			   						   Connection con){

		HashMap returnValue = new HashMap();
		returnValue.put(RESULT_ALLEGATI_DB, true);
		returnValue.put(LIST_SAVE_ALLEGATI_FILE, new ArrayList());
		returnValue.put(LIST_DELETE_ALLEGATI_FILE, new ArrayList());

		ArrayList immobiliAllegati = new AllegatiImmobiliDAO().getAllegatiByImmobile(AllegatiImmobiliVO.class.getName(),
																					 immobile.getCodImmobile()); 
		Collections.sort(immobiliAllegati,comparerImmobiliAllegati);
		ArrayList listaImmobiliAllegati = immobile.getAllegati();
		if (listaImmobiliAllegati != null){

			AllegatiImmobiliDAO aiDAO = new AllegatiImmobiliDAO();			
			Iterator it  = listaImmobiliAllegati.iterator();

			while (it.hasNext()){
				Object o = it.next();
				if (o != null){
					AllegatiImmobiliVO immobileAllegati = (AllegatiImmobiliVO)o;
					int index = Collections.binarySearch(immobiliAllegati, immobileAllegati,comparerImmobiliAllegati);
					if (index >= 0){
						immobiliAllegati.remove(index);
						Collections.sort(immobiliAllegati,comparerImmobiliAllegati);
					}
					immobileAllegati.setCodImmobile(immobile.getCodImmobile());			
					//checkAllegatoImmobileDB(immobileAllegati);
					if (immobileAllegati != null){
						if (!aiDAO.saveUpdate(immobileAllegati, con, false)){					
							MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
													"Errore salvataggio allegato", 
													"Si � verificato un errore nel salvataggio dell' allegato : " + 
													immobileAllegati.getCodAllegatiImmobili());
							returnValue.put(RESULT_ALLEGATI_DB, false);
							return returnValue;
		
						}else{
							((ArrayList)returnValue.get(LIST_SAVE_ALLEGATI_FILE)).add(immobileAllegati);
						}					
					}
				}	
			}

			Iterator ite = immobiliAllegati.iterator();
			while (ite.hasNext()){
				AllegatiImmobiliVO immobileAllegati = (AllegatiImmobiliVO)ite.next();
				if (!aiDAO.delete(immobileAllegati.getCodAllegatiImmobili(), con, false)){
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore cancellazione allegato", 
											"Si � verificato un errore nella cancellazione dell' allegato : " + 
											immobileAllegati.getCodAllegatiImmobili());
					returnValue.put(RESULT_ALLEGATI_DB, false);
					return returnValue;


				}else{
					((ArrayList)returnValue.get(LIST_DELETE_ALLEGATI_FILE)).add(immobileAllegati);
				}
			}
		}

		return returnValue;
	}
	
	private ImmobiliModel checkImmobileDB(ImmobiliModel iModel){
		if (iModel.getCodImmobile() != null){
			if (new ImmobiliDAO().getImmobileById(ImmobiliVO.class.getName(), iModel.getCodImmobile()) == null){
				MessageDialog.openError(PlatformUI.getWorkbench()
						  	 					  .getActiveWorkbenchWindow()
						  	 					  .getShell(),
						  	 		    "ERRORE", 
										"L'immobile che si sta cercando di aggiornare � stata cancellata da un altro utente");
				iModel = null;
				
				
/*				if (MessageDialog.openConfirm(PlatformUI.getWorkbench()
														.getActiveWorkbenchWindow()
														.getShell(), 
											  "ATTENZIONE", 
											  "Si sta cercando di aggiornare l'immobile " + iModel.toString() + " gi� cancellato da un altro utente. \n" +
											  "Se si prosegue con l'operazione l'immobile verr� inserito nella base dati." +
			      							  "Procedere con l'operazione ?")){
					iModel.setCodImmobile(0);
				}else{
					iModel = null;
				}	
				*/
			}
		}
		return iModel;
	}

	public boolean updateImmobile(ImmobiliModel immobile,Connection connection,Boolean doCommit){
		
		Boolean returnvalue = false;	
		Integer codImmobile = null;
		if (immobile != null){
			
			codImmobile = immobile.getCodImmobile();
			ImmobiliDAO immobiliDAO = new ImmobiliDAO();			
			Connection con = (connection == null)?ConnectionManager.getInstance().getConnection():connection;			
			checkImmobileDB(immobile);
			if (immobile != null){
				//Controllo riferimento
				if (!immobile.getRif().equalsIgnoreCase("")){
					Object o = immobiliDAO.getImmobileByRif(ImmobiliModel.class.getName(), immobile.getRif()); 
					if ((o != null) && (((ArrayList)o).size() > 0)){
						Integer codimm = ((ImmobiliModel)((ArrayList)o).get(0)).getCodImmobile();
						if ( codimm.intValue() != immobile.getCodImmobile().intValue()){
							MessageDialog.openInformation(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
														  "Codice riferimento presente nella base dati",
														  "Il codice di riferimento inserito � gia presente nella base dati \n viene sostituito con il codice assegnato dal programma");
							immobile.setRif(String.valueOf(immobiliDAO.getMaxCodImmobile() + 1));

						}
					}
				}else{
					if ((immobile.getCodImmobile() == null) ||
						(immobile.getCodImmobile().intValue() == 0)){
						immobile.setRif(String.valueOf(immobiliDAO.getMaxCodImmobile() + 1));
					}else{
						immobile.setRif(String.valueOf(immobile.getCodImmobile()));
					}
					
				}
				
				
				if (!immobiliDAO.saveUpdate(immobile, con, doCommit)){
					returnvalue = false;
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore salvataggio immobile", 
											"Si � verificato un errore nel salvataggio dell'immobile : " + 
											immobile.getCitta() + " " + immobile.getIndirizzo());
					try {
						if (doCommit){
							con.rollback();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}else{
					returnvalue = true;
					try {
						if (doCommit){
							con.commit();
/*							if (!immobile.getRif().equalsIgnoreCase("")){
						        if ((codImmobile == null) ||
						        	(codImmobile == 0)){
						        	immobile.setRif(String.valueOf(immobile.getCodImmobile()));
						        	con = ConnectionManager.getInstance().getConnection();
						        	immobiliDAO.saveUpdate(immobile, con, doCommit);
						        }
							}*/
	
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
						
				}
			}else{
				return false;
			}
			
		}
		
		return returnvalue;
				
	}
	

	public HashMap deleteImmobile(ImmobiliModel immobile,Connection connection){
		
		HashMap returnValue = new HashMap();
		returnValue.put(RESULT_DELETE_IMMOBILE_DATA_DB, true);
		returnValue.put(LIST_ATTACHMENTS_COLLOQUI_IMMOBILE_DELETE, new ArrayList());
		returnValue.put(LIST_IMAGES_IMMOBILE_DELETE, new ArrayList());
		
		ImmobiliDAO iDAO = new ImmobiliDAO();
		AbbinamentiDAO aDAO = new AbbinamentiDAO();
		ColloquiDAO cDAO = new ColloquiDAO();
		ColloquiHelper ch = new ColloquiHelper();
		ImmaginiDAO imDAO = new ImmaginiDAO(); 
		ImmaginiHelper ih = new ImmaginiHelper();
		AllegatiImmobiliDAO aiDAO = new AllegatiImmobiliDAO();
		StanzeDAO sDAO = new StanzeDAO();
		DatiCatastaliDAO dcDAO = new DatiCatastaliDAO();
		AttributeValueDAO avDAO = new AttributeValueDAO();
		ImmobiliPropietariDAO ipDAO = new ImmobiliPropietariDAO();
		AffittiDAO affittiDAO = new AffittiDAO(); 
		AffittiHelper affittiHelper = new AffittiHelper();
		
		Connection con = (connection == null)
						  ? ConnectionManager.getInstance()
						 	  			  	 .getConnection()
						  : connection;
						  							
        ArrayList allegatiToDelete = new ArrayList();
        ArrayList immaginiToDelete = new ArrayList();
        ArrayList allegatiImmobiliToDelete = new ArrayList();
        
        boolean okDeleteColloqui = true;        
        boolean okDeleteAttribute = true;
        boolean okDeleteAffitti = true;
        			
		if (ipDAO.deleteByCodImmobile(immobile.getCodImmobile(), con, false)){
			if (aDAO.deleteAbbinamentiByCodImmobile(immobile.getCodImmobile(), con, false)){
				if (sDAO.deleteByImmobile(immobile.getCodImmobile(), con, false)){
					if (dcDAO.deleteDatiCatastaliByCodImmobile(immobile.getCodImmobile(), con, false)){
						if (!avDAO.deleteByClassNameObjectId(ImmobiliVO.class.getName(), immobile.getCodImmobile(), con, false)){
							okDeleteAttribute = false;							
						}
//						for (Iterator<AttributeModel> iterator = immobile.getEntity().getAttributes().iterator(); iterator.hasNext();) {
//							AttributeModel attribute = iterator.next();
//							if (!avDAO.deleteByAttributeIdObjectId(attribute.getIdAttribute(), immobile.getCodImmobile(), con, false)){									
//								okDeleteAttribute = false;
//								break;
//							}
//						}
						
						if (okDeleteAttribute){
							ArrayList colloqui = cDAO.getColloquiByImmobile(ColloquiModel.class.getName(), immobile.getCodImmobile());
							Iterator itColloqui = colloqui.iterator();
							while(itColloqui.hasNext()){
								((ColloquiModel)itColloqui.next()).getAllegati();
							}
							itColloqui = colloqui.iterator();
							while(itColloqui.hasNext()){
								ColloquiModel cm = (ColloquiModel)itColloqui.next();
								HashMap ah = ch.deleteColloquio(cm, con, false);
								if (((Boolean)ah.get(ColloquiHelper.RESULT_DELETE_COLLOQUIO_DB))){
									if (((Boolean)ah.get(ColloquiHelper.RESULT_DELETE_ALLEGATI_DB))){
										allegatiToDelete.addAll((ArrayList)ah.get(ColloquiHelper.LIST_DELETE_ALLEGATI_FILE));							
									}else{
										okDeleteColloqui = false;
										break;							
									}
								}else{
									okDeleteColloqui = false;
									break;						
								}
							}
							if (okDeleteColloqui){
								ArrayList affittiModel = affittiDAO.getAffittiByCodImmobile(AffittiModel.class.getName(), immobile.getCodImmobile());
								for (Iterator iterator = affittiModel.iterator(); iterator.hasNext();) {
									AffittiModel affitto = (AffittiModel) iterator.next();
									HashMap hmresult = affittiHelper.deleteAffitto(affitto, con);
									if (((Boolean)hmresult.get(AffittiHelper.RESULT_AFFITTI_DB))){
										allegatiToDelete.addAll((ArrayList)hmresult.get(AffittiHelper.LIST_DELETE_ALLEGATI_FILE));
									}
								}
								
							}
							if (okDeleteColloqui){
								immaginiToDelete = immobile.getImmagini();
								if (!imDAO.deleteByImmobile(immobile.getCodImmobile(), con, false)){
									returnValue.put(RESULT_DELETE_IMMOBILE_DATA_DB, false);
								}
								allegatiImmobiliToDelete = immobile.getAllegati();
								aiDAO = new AllegatiImmobiliDAO();
								if (aiDAO.deleteByImmobile(immobile.getCodImmobile(), con, false)){
									
									if (!iDAO.delete(immobile.getCodImmobile(), con, false)){
										returnValue.put(RESULT_DELETE_IMMOBILE_DATA_DB, false);
									}
								}else{
									returnValue.put(RESULT_DELETE_IMMOBILE_DATA_DB, false);
								}
							}else{
								returnValue.put(RESULT_DELETE_IMMOBILE_DATA_DB, false);
							}
						}else{
							returnValue.put(RESULT_DELETE_IMMOBILE_DATA_DB, false);
						}
					}else{
						returnValue.put(RESULT_DELETE_IMMOBILE_DATA_DB, false);
					}
				}else{
					returnValue.put(RESULT_DELETE_IMMOBILE_DATA_DB, false);
				}
			}else{
				returnValue.put(RESULT_DELETE_IMMOBILE_DATA_DB, false);
			}
		}else{
			returnValue.put(RESULT_DELETE_IMMOBILE_DATA_DB, false);
		}				
		
		if (((Boolean)returnValue.get(RESULT_DELETE_IMMOBILE_DATA_DB))){
			returnValue.put(LIST_ATTACHMENTS_COLLOQUI_IMMOBILE_DELETE, allegatiToDelete);
			returnValue.put(LIST_IMAGES_IMMOBILE_DELETE, immaginiToDelete);
			returnValue.put(LIST_DELETE_ALLEGATI_FILE, allegatiImmobiliToDelete);
			try {
				if (connection == null){
					con.commit();
					if (!ch.deleteAllegatiColloqui(allegatiToDelete)){
						MessageDialog.openWarning(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
								  				  "Cancellazione file allegati colloqui",
								  				  "Alcuni documenti allegati ai colloqui non sono stati cancellati");
					}
					if (!ih.deleteImmagini(immaginiToDelete)){
						MessageDialog.openWarning(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
								  				  "Cancellazione file immagini immobile",
								  				  "Alcune immagini dell'immobile non sono state cancellate");				
					}
					if (!deleteAllegatiImmobili(allegatiImmobiliToDelete)){
						MessageDialog.openWarning(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
								  				  "Cancellazione file allegato immobile",
								  				  "Alcuni allegati dell'immobile non sono stati cancellati");				
					}
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			try {
				if (connection == null){
					con.rollback();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return returnValue;
		
	}
	
	public boolean deleteAllegatiImmobili(ArrayList<AllegatiImmobiliVO> allegati){ 
		boolean returnValue = true;
		AllegatiImmobiliVO allegato = null;
		try {
			Iterator<AllegatiImmobiliVO> it = allegati.iterator();
			while(it.hasNext()){
				allegato = it.next();
				File f = new File (pathAllegatiDestinazione+File.separator+allegato.getCodImmobile()+File.separator+allegato.getNome());
				f.delete();
			}
		} catch (Exception e) {
			MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
									"Errore cancellazione allegato", 
									"Si � verificato un errore nella cancellazione dell' allegato da : " + 									 
									pathAllegatiDestinazione+allegato.getNome());
			returnValue = false;
		}
		return returnValue;
		
	}

	public boolean deleteAllegatiAffitti(ArrayList<AffittiAllegatiVO> allegati){ 
		boolean returnValue = true;
		AffittiAllegatiVO allegato = null;
		try {
			Iterator<AffittiAllegatiVO> it = allegati.iterator();
			while(it.hasNext()){
				allegato = it.next();
				File f = new File (pathAllegatiDestinazione+File.separator+allegato.getCodAffitto()+File.separator+allegato.getNome());
				f.delete();
			}
		} catch (Exception e) {
			MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
									"Errore cancellazione allegato", 
									"Si � verificato un errore nella cancellazione dell' allegato da : " + 									 
									pathAllegatiDestinazione+allegato.getNome());
			returnValue = false;
		}
		return returnValue;
		
	}

	public boolean saveAllegatiImmobili(ArrayList<AllegatiImmobiliVO> allegati){
		Boolean returnValue = true;
		try {
			Iterator it = allegati.iterator();
			while (it.hasNext()){
				AllegatiImmobiliVO acVO = (AllegatiImmobiliVO)it.next();
				if ((acVO.getFromPath() != null) && 
					(!acVO.getFromPath().equalsIgnoreCase(""))){
					WinkhouseUtils.getInstance()
									.copiaFile(acVO.getFromPath(),
											   pathAllegatiDestinazione + File.separator + 
											   acVO.getCodImmobile() + File.separator + acVO.getNome());
				}
			}
		} catch (Exception e) {
			returnValue = false;
			e.printStackTrace();
		}
		
		return returnValue;

	}

	private Comparator<ImmobiliPropietariVO> comparerPropietari = new Comparator<ImmobiliPropietariVO>(){

		@Override
		public int compare(ImmobiliPropietariVO arg0, ImmobiliPropietariVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodAnagrafica()!=null) && (arg1.getCodAnagrafica()!=null) &&
				(arg0.getCodImmobile()!=null) && (arg1.getCodImmobile()!=null)){				
				if (
					(arg0.getCodAnagrafica().intValue() == arg1.getCodAnagrafica().intValue()) &&
					(arg0.getCodImmobile().intValue() == arg1.getCodImmobile().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodAnagrafica().intValue() == arg1.getCodAnagrafica().intValue())){
					if (arg0.getCodImmobile().intValue() < arg1.getCodImmobile().intValue()){
						returnValue = -1;
					}
					if (arg0.getCodImmobile().intValue() > arg1.getCodImmobile().intValue()){
						returnValue = 1;
					}
				}
				if ((arg0.getCodImmobile().intValue() == arg1.getCodImmobile().intValue())){
					if (arg0.getCodAnagrafica().intValue() < arg1.getCodAnagrafica().intValue()){
						returnValue = -1;
					}
					if (arg0.getCodAnagrafica().intValue() > arg1.getCodAnagrafica().intValue()){
						returnValue = 1;
					}
				}
													
			}
			
			returnValue = -1;
			
			return returnValue;
		}
		
	};
	
	public Boolean updateImmobiliPropietari(ImmobiliModel immobile,
									   		Connection con,
									   		Boolean doCommit){
		
		Boolean returnValue = true;
		
		doCommit = (doCommit == null)?true:doCommit;
				
		ImmobiliPropietariDAO ipdao = new ImmobiliPropietariDAO();
		if (ipdao.deleteByCodImmobile(immobile.getCodImmobile(), con, doCommit)){

			ArrayList<AnagraficheModel> anagrafiche = immobile.getAnagrafichePropietarie();
			Iterator<AnagraficheModel> it = anagrafiche.iterator();
			
			while(it.hasNext()){
				
				AnagraficheModel am = it.next();
				
				ImmobiliPropietariVO ipVO = new ImmobiliPropietariVO();
				ipVO.setCodAnagrafica(am.getCodAnagrafica());
				ipVO.setCodImmobile(immobile.getCodImmobile());
				
				ipdao.insert(ipVO, con, doCommit);
			}
			
		}else{
			
		}
		
		return returnValue;
	}
	

	
}