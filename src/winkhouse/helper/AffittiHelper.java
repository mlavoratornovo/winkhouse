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
import winkhouse.dao.AffittiAllegatiDAO;
import winkhouse.dao.AffittiAnagraficheDAO;
import winkhouse.dao.AffittiDAO;
import winkhouse.dao.AffittiRateDAO;
import winkhouse.dao.AffittiSpeseDAO;
import winkhouse.dao.AnagraficheDAO;
import winkhouse.dao.AttributeValueDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.model.AffittiAnagraficheModel;
import winkhouse.model.AffittiModel;
import winkhouse.model.AffittiRateModel;
import winkhouse.model.AffittiSpeseModel;
import winkhouse.model.AttributeModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.AffittiAllegatiVO;
import winkhouse.vo.AffittiAnagraficheVO;
import winkhouse.vo.AffittiRateVO;
import winkhouse.vo.AffittiSpeseVO;
import winkhouse.vo.AffittiVO;



public class AffittiHelper {
	
	public final static String LIST_SAVE_ALLEGATI_FILE = "listasaveallegatiaffitti";
	public final static String LIST_DELETE_ALLEGATI_FILE = "listadeleteallegatiaffitti";
	public final static String RESULT_ALLEGATI_DB = "resultallegatiaffittidb";
	public final static String RESULT_AFFITTI_DB = "resultffittidb";
	
	private String pathAllegatiDestinazione = WinkhouseUtils.getInstance()
	  														  .getPreferenceStore()
	  														  .getString(WinkhouseUtils.ALLEGATIPATH) + 
	  														  File.separator + "affitti";


	private Comparator<AffittiAnagraficheVO> comparerAffittiAnagrafiche = new Comparator<AffittiAnagraficheVO>(){

		@Override
		public int compare(AffittiAnagraficheVO arg0, AffittiAnagraficheVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodAffittiAnagrafiche()!=null) && (arg1.getCodAffittiAnagrafiche()!=null)){				
				if ((arg0.getCodAffittiAnagrafiche().intValue() == arg1.getCodAffittiAnagrafiche().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodAffittiAnagrafiche().intValue() < arg1.getCodAffittiAnagrafiche().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodAffittiAnagrafiche().intValue() > arg1.getCodAffittiAnagrafiche().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodAffittiAnagrafiche()!=null) && (arg1.getCodAffittiAnagrafiche()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};		

	private Comparator<AffittiRateVO> comparerAffittiRate = new Comparator<AffittiRateVO>(){

		@Override
		public int compare(AffittiRateVO arg0, AffittiRateVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodAffittiRate()!=null) && (arg1.getCodAffittiRate()!=null)){				
				if ((arg0.getCodAffittiRate().intValue() == arg1.getCodAffittiRate().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodAffittiRate().intValue() < arg1.getCodAffittiRate().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodAffittiRate().intValue() > arg1.getCodAffittiRate().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodAffittiRate()!=null) && (arg1.getCodAffittiRate()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};		

	private Comparator<AffittiSpeseVO> comparerAffittiSpese = new Comparator<AffittiSpeseVO>(){

		@Override
		public int compare(AffittiSpeseVO arg0, AffittiSpeseVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodAffittiSpese()!=null) && (arg1.getCodAffittiSpese()!=null)){				
				if ((arg0.getCodAffittiSpese().intValue() == arg1.getCodAffittiSpese().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodAffittiSpese().intValue() < arg1.getCodAffittiSpese().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodAffittiSpese().intValue() > arg1.getCodAffittiSpese().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodAffittiSpese()!=null) && (arg1.getCodAffittiSpese()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};		
	
	private Comparator<AffittiAllegatiVO> comparerAffittiAllegati = new Comparator<AffittiAllegatiVO>(){

		@Override
		public int compare(AffittiAllegatiVO arg0, AffittiAllegatiVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodAffittiAllegati()!=null) && (arg1.getCodAffittiAllegati()!=null)){				
				if ((arg0.getCodAffittiAllegati().intValue() == arg1.getCodAffittiAllegati().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodAffittiAllegati().intValue() < arg1.getCodAffittiAllegati().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodAffittiAllegati().intValue() > arg1.getCodAffittiAllegati().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodAffittiAllegati()!=null) && (arg1.getCodAffittiAllegati()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};
	
	public boolean saveAllegatiAffitti(ArrayList<AffittiAllegatiVO> allegati){
		Boolean returnValue = true;
		try {
			Iterator it = allegati.iterator();
			while (it.hasNext()){
				AffittiAllegatiVO acVO = (AffittiAllegatiVO)it.next();
				if ((acVO.getFromPath() != null) && 
					(!acVO.getFromPath().equalsIgnoreCase(""))){
					WinkhouseUtils.getInstance()
									.copiaFile(acVO.getFromPath(),
											   pathAllegatiDestinazione + File.separator + 
											   acVO.getCodAffitto() + File.separator + acVO.getNome());
				}
			}
		} catch (Exception e) {
			returnValue = false;
			e.printStackTrace();
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
									"Si è verificato un errore nella cancellazione dell' allegato da : " + 									 
									pathAllegatiDestinazione+allegato.getNome());
			returnValue = false;
		}
		return returnValue;
		
	}
	
	public HashMap updateListaAllegati(AffittiModel affitto,
			   						   Connection con){

		HashMap returnValue = new HashMap();
		returnValue.put(RESULT_ALLEGATI_DB, true);
		returnValue.put(LIST_SAVE_ALLEGATI_FILE, new ArrayList());
		returnValue.put(LIST_DELETE_ALLEGATI_FILE, new ArrayList());
	
		ArrayList affittiAllegati = new AffittiAllegatiDAO().getAffittiAllegatiByCodAffitto(AffittiAllegatiVO.class.getName(),
																							affitto.getCodAffitti()); 
		Collections.sort(affittiAllegati,comparerAffittiAllegati);
		ArrayList listaAffittiAllegati = affitto.getAllegati();
		if (listaAffittiAllegati != null){
	
			AffittiAllegatiDAO aaDAO = new AffittiAllegatiDAO();			
			Iterator it  = listaAffittiAllegati.iterator();
	
			while (it.hasNext()){
				Object o = it.next();
				if (o != null){
					AffittiAllegatiVO affittiAllegato = (AffittiAllegatiVO)o;
					int index = Collections.binarySearch(affittiAllegati, affittiAllegato, comparerAffittiAllegati);
					if (index >= 0){
						affittiAllegati.remove(index);
						Collections.sort(affittiAllegati,comparerAffittiAllegati);
					}
					affittiAllegato.setCodAffitto(affitto.getCodAffitti());			
	//checkAllegatoImmobileDB(immobileAllegati);
					if (affittiAllegato != null){
						if (!aaDAO.saveUpdate(affittiAllegato, con, false)){					
							MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
													"Errore salvataggio allegato", 
													"Si è verificato un errore nel salvataggio dell' allegato : " + 
													affittiAllegato.getCodAffittiAllegati());
							returnValue.put(RESULT_ALLEGATI_DB, false);
							return returnValue;
	
						}else{
							((ArrayList)returnValue.get(LIST_SAVE_ALLEGATI_FILE)).add(affittiAllegato);
						}					
					}
				}	
			}
	
			Iterator ite = affittiAllegati.iterator();
			while (ite.hasNext()){
				AffittiAllegatiVO affittoAllegati = (AffittiAllegatiVO)ite.next();
				if (!aaDAO.deleteAffittiAllegatiByID(affittoAllegati.getCodAffittiAllegati(), con, false)){
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore cancellazione allegato", 
											"Si è verificato un errore nella cancellazione dell' allegato : " + 
											affittoAllegati.getCodAffittiAllegati());
					returnValue.put(RESULT_ALLEGATI_DB, false);
					return returnValue;
		
				}else{
					((ArrayList)returnValue.get(LIST_DELETE_ALLEGATI_FILE)).add(affittoAllegati);
				}
			}
		}
	
		return returnValue;
	}
	

	public Boolean updateListaAnagrafiche(AffittiModel affitto,
									 	  Connection con){

		Boolean returnValue = true;

		ArrayList<AffittiAnagraficheModel> affittiAnagraficheDB = new AffittiAnagraficheDAO().getAffittiAnagraficheByAffitto(AffittiAnagraficheModel.class.getName(),
																						 		   	 						 affitto.getCodAffitti()); 
		Collections.sort(affittiAnagraficheDB,comparerAffittiAnagrafiche);
		ArrayList<AffittiAnagraficheModel> listaAffittiAnagrafiche = affitto.getAnagrafiche();
		
		if (listaAffittiAnagrafiche != null){
			
			if (checkAnagraficheData(listaAffittiAnagrafiche)){
				
				AffittiAnagraficheDAO caDAO = new AffittiAnagraficheDAO();			
				Iterator it  = listaAffittiAnagrafiche.iterator();
	
				while (it.hasNext()){
					Object o = it.next();
					if (o != null){
						AffittiAnagraficheModel affittiAnagrafica = (AffittiAnagraficheModel)o;
						if (affittiAnagrafica.getCodAnagrafica() != null){
							int index = Collections.binarySearch(affittiAnagraficheDB, affittiAnagrafica,comparerAffittiAnagrafiche);
							if (index >= 0){
								affittiAnagraficheDB.remove(index);
								Collections.sort(affittiAnagraficheDB,comparerAffittiAnagrafiche);
							}
						}else{
							if (affittiAnagrafica.getAnagrafica() != null){
								AnagraficheDAO aDAO = new AnagraficheDAO();
								//controllo nome cognome presenti ????
								aDAO.saveUpdate(affittiAnagrafica.getAnagrafica(), con, false);
								affittiAnagrafica.setCodAnagrafica(affittiAnagrafica.getAnagrafica()
																					.getCodAnagrafica());
							}
						}
						affittiAnagrafica.setCodAffitto(affitto.getCodAffitti());
						if (!caDAO.saveUpdate(affittiAnagrafica, con, false)){					
							MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
													"Errore salvataggio colloqui anagrafica", 
													"Si è verificato un errore nel salvataggio dell'associazione affitto anagrafica : " + 
													affittiAnagrafica.getCodAffittiAnagrafiche());
							returnValue = false;
						}					
					}
				}
	
				Iterator ite = affittiAnagraficheDB.iterator();
				while (ite.hasNext()){
					AffittiAnagraficheModel affittoAnagrafiche = (AffittiAnagraficheModel)ite.next();
					if (!caDAO.deleteAffittiAnagraficheByID(affittoAnagrafiche.getCodAffittiAnagrafiche(), con, false)){
						MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
												"Errore cancellazione anagrafiche affitto", 
												"Si è verificato un errore nella cancellazione dell'associazione affitto anagrafica : " + 
												affittoAnagrafiche.getCodAffittiAnagrafiche());
						returnValue = false;
	
					}
				}
			}else{
				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
										"Errore salvataggio anagrafiche affitto", 
										"Le anagrafiche inserite devono avere valorizzati entrambi i campi nome e cognome"); 						
				returnValue = false;
			}
		}

		return returnValue;
	}
	
	private boolean checkAnagraficheData(ArrayList<AffittiAnagraficheModel> listaAnagrafiche){
		
		boolean returnValue = true;
		
		Iterator<AffittiAnagraficheModel> it = listaAnagrafiche.iterator();
		
		while(it.hasNext()){
			AffittiAnagraficheModel aaModel = it.next();
			if ((aaModel.getAnagrafica() != null) && 
				(
				  (((aaModel.getAnagrafica().getCognome() != null) && (!aaModel.getAnagrafica().getCognome().equalsIgnoreCase(""))) && 
				  ((aaModel.getAnagrafica().getNome() != null) && (!aaModel.getAnagrafica().getNome().equalsIgnoreCase("")))) ||
				  ((aaModel.getAnagrafica().getRagioneSociale() != null) && (!aaModel.getAnagrafica().getRagioneSociale().equalsIgnoreCase("")))
				 )
			){
				returnValue = true;
			}else{
				returnValue = false;
			}
		}
		
		return returnValue;
		
	}
	
	public Boolean updateListaRate(AffittiModel affitto,
		 	  					   Connection con){

		Boolean returnValue = true;

		ArrayList affittiRateDB = new AffittiRateDAO().getAffittiRateByCodAffitto(AffittiRateModel.class.getName(),
																				  affitto.getCodAffitti()); 
		Collections.sort(affittiRateDB,comparerAffittiRate);
		ArrayList listaAffittiRate = affitto.getRate();
		if (listaAffittiRate != null){

			AffittiRateDAO arDAO = new AffittiRateDAO();			
			Iterator it  = listaAffittiRate.iterator();

			while (it.hasNext()){
				Object o = it.next();
				if (o != null){
					AffittiRateVO affittiRata = (AffittiRateVO)o;
					int index = Collections.binarySearch(affittiRateDB, affittiRata, comparerAffittiRate);
					if (index >= 0){
						affittiRateDB.remove(index);
						Collections.sort(affittiRateDB,comparerAffittiRate);
					}
					affittiRata.setCodAffitto(affitto.getCodAffitti());
					if (!arDAO.saveUpdate(affittiRata, con, false)){					
						MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
								"Errore salvataggio colloqui rata", 
								"Si è verificato un errore nel salvataggio dell'associazione affitto rata : " + 
								affittiRata.getCodAffittiRate());
						returnValue = false;
					}					
				}
			}

			Iterator ite = affittiRateDB.iterator();
			while (ite.hasNext()){
				AffittiRateVO affittoRata = (AffittiRateVO)ite.next();
				if (!arDAO.deleteAffittiRateByID(affittoRata.getCodAffittiRate(), con, false)){
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore cancellazione colloqui rate", 
											"Si è verificato un errore nella cancellazione dell'associazione affitto rata : " + 
											affittoRata.getCodAffittiRate());
					returnValue = false;

				}
			}
		}

		return returnValue;
	}
	
	
	public Boolean updateListaSpese(AffittiModel affitto,
		 	  					   Connection con){

		Boolean returnValue = true;

		ArrayList affittiSpeseDB = new AffittiSpeseDAO().getAffittiSpeseByCodAffitto(AffittiSpeseModel.class.getName(),
																				  	affitto.getCodAffitti()); 
		Collections.sort(affittiSpeseDB,comparerAffittiSpese);
		ArrayList listaAffittiSpese = affitto.getSpese();
		if (listaAffittiSpese != null){

			AffittiSpeseDAO asDAO = new AffittiSpeseDAO();			
			Iterator it  = listaAffittiSpese.iterator();

			while (it.hasNext()){
				Object o = it.next();
				if (o != null){
					AffittiSpeseModel affittiSpesa = (AffittiSpeseModel)o;
					int index = Collections.binarySearch(affittiSpeseDB, affittiSpesa, comparerAffittiSpese);
					if (index >= 0){
						affittiSpeseDB.remove(index);
						Collections.sort(affittiSpeseDB,comparerAffittiSpese);
					}
					affittiSpesa.setCodAffitto(affitto.getCodAffitti());
					if (!asDAO.saveUpdate(affittiSpesa, con, false)){					
						MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
								"Errore salvataggio colloqui rata", 
								"Si è verificato un errore nel salvataggio dell'associazione affitto spesa : " + 
								affittiSpesa.getCodAffittiSpese());
						returnValue = false;
					}					
				}
			}

			Iterator ite = affittiSpeseDB.iterator();
			while (ite.hasNext()){
				AffittiSpeseModel affittoSpesa = (AffittiSpeseModel)ite.next();
				if (!asDAO.deleteAffittiSpeseByID(affittoSpesa.getCodAffittiSpese(), con, false)){
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore cancellazione colloqui spese", 
											"Si è verificato un errore nella cancellazione dell'associazione affitto spesa : " + 
											affittoSpesa.getCodAffittiSpese());
					returnValue = false;

				}
			}
		}

		return returnValue;
	}
	
	public HashMap saveAffitto(AffittiModel affitto, Connection con){
		
		HashMap returnValue = new HashMap();
		HashMap allegatiResult = null;
		boolean isNew = (affitto.getCodAffitti() == null)
						? true
						: false;
		
		returnValue.put(RESULT_AFFITTI_DB, true);
		returnValue.put(RESULT_ALLEGATI_DB, false);
		returnValue.put(LIST_DELETE_ALLEGATI_FILE, new ArrayList());
		returnValue.put(LIST_SAVE_ALLEGATI_FILE, new ArrayList());
		
		AffittiDAO aDAO = new AffittiDAO();
		
		Connection connection = (con == null)
								? ConnectionManager.getInstance().getConnection()
								: con;
		
		if (checkPeriodoAffitto(affitto)){
								
			if (aDAO.saveUpdate(affitto, connection, false)){
				if (updateListaAnagrafiche(affitto, connection)){
					if (updateListaRate(affitto, connection)){
						if (updateListaSpese(affitto, connection)){
							allegatiResult = updateListaAllegati(affitto, connection);
							if ((Boolean)allegatiResult.get(RESULT_ALLEGATI_DB)){
								returnValue.put(RESULT_ALLEGATI_DB, allegatiResult.get(RESULT_ALLEGATI_DB));
								if (con != null){
									returnValue.put(LIST_SAVE_ALLEGATI_FILE, allegatiResult.get(LIST_SAVE_ALLEGATI_FILE));
									returnValue.put(LIST_DELETE_ALLEGATI_FILE, allegatiResult.get(LIST_DELETE_ALLEGATI_FILE));
								}else{
								}
								
							}else{
								returnValue.put(RESULT_AFFITTI_DB, false);
							}
						}else{
							returnValue.put(RESULT_AFFITTI_DB, false);
						}
					}else{
						returnValue.put(RESULT_AFFITTI_DB, false);
					}
				}else{
					returnValue.put(RESULT_AFFITTI_DB, false);
				}
			}else{
				returnValue.put(RESULT_AFFITTI_DB, false);
			}
			
			if (con == null){
				if (
						((Boolean)returnValue.get(RESULT_ALLEGATI_DB) &&
						 (Boolean)returnValue.get(RESULT_AFFITTI_DB))
				){
					try {
						connection.commit();
						saveAllegatiAffitti((ArrayList)allegatiResult.get(LIST_SAVE_ALLEGATI_FILE));
						deleteAllegatiAffitti((ArrayList)allegatiResult.get(LIST_DELETE_ALLEGATI_FILE));
						MessageDialog.openInformation(PlatformUI.getWorkbench()
	     														.getActiveWorkbenchWindow()
	     														.getShell(), 
	     														"Salvataggio completato", 
																"Salvataggio affitto eseguito correttamente");
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}else{
					try {
						connection.rollback();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
	
			}
		}else{
			MessageDialog.openError(PlatformUI.getWorkbench()
											  .getActiveWorkbenchWindow()
											  .getShell(),
									"Errore salvataggio affitto", 
									"L'immobile risulta già affittato nel periodo selezionato");
			returnValue.put(RESULT_AFFITTI_DB, false);
		}
		return returnValue;
		
	}
	
	public HashMap deleteAffitto(AffittiModel affitto, Connection con){
	
		HashMap returnValue = new HashMap();		
		
		returnValue.put(RESULT_AFFITTI_DB, true);
		returnValue.put(LIST_DELETE_ALLEGATI_FILE, new ArrayList());		
		
		AffittiDAO aDAO = new AffittiDAO();
		AffittiAnagraficheDAO aaDAO = new AffittiAnagraficheDAO();
		AffittiAllegatiDAO aalDAO = new AffittiAllegatiDAO();
		AffittiRateDAO arDAO = new AffittiRateDAO();
		AffittiSpeseDAO asDAO = new AffittiSpeseDAO();
		
		Connection connection = (con == null)
								? ConnectionManager.getInstance().getConnection()
								: con;

		
			if (aaDAO.deleteAffittiAnagraficheByCodAffitto(affitto.getCodAffitti(), connection, false)){
				if (arDAO.deleteAffittiRateByCodAffitto(affitto.getCodAffitti(), connection, false)){
					if (asDAO.deleteAffittiSpeseByCodAffitto(affitto.getCodAffitti(), con, false)){
						ArrayList alAllegati = aalDAO.getAffittiAllegatiByCodAffitto(AffittiAllegatiVO.class.getName(), 
															  						 affitto.getCodAffitti());
						returnValue.put(LIST_DELETE_ALLEGATI_FILE, alAllegati);
						if (aalDAO.deleteAffittiAllegatiByCodAffitto(affitto.getCodAffitti(), connection, false)){							
														
							AttributeValueDAO avDAO = new AttributeValueDAO();
							for (Iterator<AttributeModel> iterator = affitto.getEntity().getAttributes().iterator(); iterator.hasNext();) {
								AttributeModel attribute = iterator.next();
								if (!avDAO.deleteByAttributeIdObjectId(attribute.getIdAttribute(), affitto.getCodAffitti(), con, false)){
									returnValue.put(RESULT_AFFITTI_DB, false);
									break;
								}
							}
							if (((Boolean)returnValue.get(RESULT_AFFITTI_DB)) == true){
								if (!aDAO.deleteAffittoByID(affitto.getCodAffitti(), connection, false)){
									returnValue.put(RESULT_AFFITTI_DB, false);
								}
							}
							
						}else{
							returnValue.put(RESULT_AFFITTI_DB, false);
						}
					}else{
						returnValue.put(RESULT_AFFITTI_DB, false);
					}						
				}else{
					returnValue.put(RESULT_AFFITTI_DB, false);
				}
			}else{
				returnValue.put(RESULT_AFFITTI_DB, false);
			}
		
		if (con == null){
			if ((Boolean)returnValue.get(RESULT_AFFITTI_DB)){
				try {
					connection.commit();
					deleteAllegatiAffitti((ArrayList)returnValue.get(LIST_DELETE_ALLEGATI_FILE));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else{
				try {
					connection.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

		return returnValue;
	}
	
	public boolean checkPeriodoAffitto(AffittiModel am){
		
		boolean returnValue = true;
		ArrayList al = new ArrayList();
		ArrayList next = new ArrayList();
		ArrayList previous = new ArrayList();
		ArrayList contain = new ArrayList();
		ArrayList middle = new ArrayList();
		
		AffittiDAO aDAO = new AffittiDAO();
		if (am.getDataFine() != null){
			next = aDAO.getAffittiNextBetWeen(AffittiVO.class.getName(), am.getDataInizio(), am.getDataFine(), am.getCodImmobile(), am.getCodAffitti());
			previous = aDAO.getAffittiPreviousBetWeen(AffittiVO.class.getName(), am.getDataInizio(), am.getDataFine(), am.getCodImmobile(), am.getCodAffitti());
			contain = aDAO.getAffittiContainBetWeen(AffittiVO.class.getName(), am.getDataInizio(), am.getDataFine(), am.getCodImmobile(), am.getCodAffitti());
			middle = aDAO.getAffittiMiddleBetWeen(AffittiVO.class.getName(), am.getDataInizio(), am.getDataFine(), am.getCodImmobile(), am.getCodAffitti());
			
		}else{
			next = aDAO.getAffittiByDataInizio(AffittiVO.class.getName(), am.getDataInizio(), am.getCodImmobile(), am.getCodAffitti());
			
		}
		
		if ((next.size() > 0) || (previous.size() > 0) || (contain.size() > 0) || (middle.size() > 0)){
			returnValue = false;
		}
		
		return returnValue;
		
	}

}
