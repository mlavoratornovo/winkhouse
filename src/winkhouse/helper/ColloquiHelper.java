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
import winkhouse.dao.AllegatiColloquiDAO;
import winkhouse.dao.AttributeValueDAO;
import winkhouse.dao.ColloquiAgentiDAO;
import winkhouse.dao.ColloquiAnagraficheDAO;
import winkhouse.dao.ColloquiCriteriRicercaDAO;
import winkhouse.dao.ColloquiDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.engine.search.SearchEngineImmobili;
import winkhouse.model.AttributeModel;
import winkhouse.model.ColloquiAgentiModel_Age;
import winkhouse.model.ColloquiAnagraficheModel_Ang;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ColloquiModelVisiteCollection;
import winkhouse.model.ImmobiliModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.AllegatiColloquiVO;
import winkhouse.vo.ColloquiAgentiVO;
import winkhouse.vo.ColloquiAnagraficheVO;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.vo.ColloquiVO;



public class ColloquiHelper {

	public final static String RESULT_DELETE_ALLEGATI_DB = "rda_db";
	public final static String LIST_DELETE_ALLEGATI_FILE = "lda_file";
	public final static String LIST_SAVE_ALLEGATI_FILE = "lsa_file";
	public final static String RESULT_DELETE_COLLOQUIO_DB = "rdc_db";
	public final static String NUM_ERROR_DEL_COLLOQUIO_DB = "nrdc_db";
	
	private String pathAllegatiDestinazione = WinkhouseUtils.getInstance()
															  .getPreferenceStore()
															  .getString(WinkhouseUtils.ALLEGATIPATH) + 
											  File.separator + "colloqui";

	
	public ColloquiHelper() {
	}
	
	private Comparator<ColloquiVO> comparer = new Comparator<ColloquiVO>(){

		@Override
		public int compare(ColloquiVO arg0, ColloquiVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodColloquio()!=null) && (arg1.getCodColloquio()!=null)){				
				if ((arg0.getCodColloquio().intValue() == arg1.getCodColloquio().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodColloquio().intValue() < arg1.getCodColloquio().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodColloquio().intValue() > arg1.getCodColloquio().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodColloquio()!=null) && (arg1.getCodColloquio()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};		

	private Comparator<ColloquiAgentiVO> comparerColloquiAgenti = new Comparator<ColloquiAgentiVO>(){

		@Override
		public int compare(ColloquiAgentiVO arg0, ColloquiAgentiVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodColloquioAgenti()!=null) && (arg1.getCodColloquioAgenti()!=null)){				
				if ((arg0.getCodColloquioAgenti().intValue() == arg1.getCodColloquioAgenti().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodColloquioAgenti().intValue() < arg1.getCodColloquioAgenti().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodColloquioAgenti().intValue() > arg1.getCodColloquioAgenti().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodColloquioAgenti()!=null) && (arg1.getCodColloquioAgenti()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};		

	private Comparator<ColloquiAnagraficheVO> comparerColloquiAnagrafiche = new Comparator<ColloquiAnagraficheVO>(){

		@Override
		public int compare(ColloquiAnagraficheVO arg0, ColloquiAnagraficheVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodColloquioAnagrafiche()!=null) && (arg1.getCodColloquioAnagrafiche()!=null)){				
				if ((arg0.getCodColloquioAnagrafiche().intValue() == arg1.getCodColloquioAnagrafiche().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodColloquioAnagrafiche().intValue() < arg1.getCodColloquioAnagrafiche().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodColloquioAnagrafiche().intValue() > arg1.getCodColloquioAnagrafiche().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodColloquioAnagrafiche()!=null) && (arg1.getCodColloquioAnagrafiche()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};		

	private Comparator<AllegatiColloquiVO> comparerColloquiAllegati = new Comparator<AllegatiColloquiVO>(){

		@Override
		public int compare(AllegatiColloquiVO arg0, AllegatiColloquiVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodAllegatiColloquio()!=null) && (arg1.getCodAllegatiColloquio()!=null)){				
				if ((arg0.getCodAllegatiColloquio().intValue() == arg1.getCodAllegatiColloquio().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodAllegatiColloquio().intValue() < arg1.getCodAllegatiColloquio().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodAllegatiColloquio().intValue() > arg1.getCodAllegatiColloquio().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodAllegatiColloquio()!=null) && (arg1.getCodAllegatiColloquio()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};		

	private Comparator<ColloquiCriteriRicercaVO> comparerColloquiCriteriRicerca = new Comparator<ColloquiCriteriRicercaVO>(){

		@Override
		public int compare(ColloquiCriteriRicercaVO arg0, ColloquiCriteriRicercaVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodCriterioRicerca()!=null) && (arg1.getCodCriterioRicerca()!=null)){				
				if ((arg0.getCodCriterioRicerca().intValue() == arg1.getCodCriterioRicerca().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodCriterioRicerca().intValue() < arg1.getCodCriterioRicerca().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodCriterioRicerca().intValue() > arg1.getCodCriterioRicerca().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodCriterioRicerca()!=null) && (arg1.getCodCriterioRicerca()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};		
	
	public HashMap deleteColloquiImmobile(ImmobiliModel immobile,
			 							  Connection con,
			 							  Boolean doCommit){
		
		HashMap returnValue = new HashMap();
		returnValue.put(this.LIST_DELETE_ALLEGATI_FILE, new ArrayList());
		
		doCommit = (doCommit == null)?true:doCommit;
		ColloquiDAO cDAO = new ColloquiDAO();
		ArrayList colloquiDB = cDAO.getColloquiByImmobile(ColloquiModel.class.getName(), 
														  immobile.getCodImmobile());
		
		Collections.sort(colloquiDB,comparer);
		ArrayList listaColloqui = immobile.getColloqui();
		int numerror = 0;
		
		if (listaColloqui != null){
			
			if ((listaColloqui.get(0) != null) && 
				(((ColloquiModelVisiteCollection)listaColloqui.get(0)).getColloquiVisite() != null)
			    ){			
				
				Iterator it  = ((ColloquiModelVisiteCollection)listaColloqui.get(0)).getColloquiVisite()
																					.iterator();
				
				while (it.hasNext()){
					ColloquiVO colloquio = (ColloquiVO)it.next();
					int index = Collections.binarySearch(colloquiDB, colloquio,comparer);
					if (index >= 0){
						colloquiDB.remove(index);
						Collections.sort(colloquiDB,comparer);
					}
					
				}
				
				Iterator ite = colloquiDB.iterator();
				while (ite.hasNext()){
					ColloquiModel cModel = (ColloquiModel)ite.next();
					HashMap colloquioDelResult = deleteColloquio(cModel, con, doCommit);
					
					returnValue.put(this.LIST_DELETE_ALLEGATI_FILE, 
									((ArrayList)returnValue.get(this.LIST_DELETE_ALLEGATI_FILE))
														   .addAll((ArrayList)colloquioDelResult.get(this.LIST_DELETE_ALLEGATI_FILE)));
					
					if (!(Boolean)colloquioDelResult.get(this.RESULT_DELETE_COLLOQUIO_DB)){					
						MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
												"Errore cancellazione colloquio", 
												"Si � verificato un errore nella cancellazione del colloquio : " +							
												((cModel.getTipologia()!= null)?"":cModel.getTipologia().getDescrizione()) + "\n" +
												"descrizione : " + cModel.getDescrizione());
				
						numerror++;
					}
				}
			}
			
			if (numerror > 0){
				returnValue.put(this.RESULT_DELETE_COLLOQUIO_DB, false);
			}else{
				returnValue.put(this.RESULT_DELETE_COLLOQUIO_DB, true);
			}
			
		}
		
		return returnValue;
		
	}
	
	public HashMap deleteColloquio(ColloquiModel cModel, Connection con , Boolean doCommit){
		HashMap returnValue = new HashMap();
		boolean dbdelresult = true;
		HashMap resultAllegati = null;
		
		ColloquiAgentiDAO caDAO = new ColloquiAgentiDAO();
		if (caDAO.deleteByColloquio(cModel.getCodColloquio(), con, doCommit)){
					
			ColloquiAnagraficheDAO canagDAO = new ColloquiAnagraficheDAO();
			if (canagDAO.deleteByColloquio(cModel.getCodColloquio(), con, doCommit)){
					
				ColloquiCriteriRicercaDAO ccrDAO = new ColloquiCriteriRicercaDAO();
				if (ccrDAO.deleteByColloquio(cModel.getCodColloquio(), con, doCommit)){

					resultAllegati = deleteAllegatiColloquio(cModel, con, doCommit);
					if (((Boolean)resultAllegati.get(this.RESULT_DELETE_ALLEGATI_DB))){
						
						boolean okDeleteAttribute = true;
						AttributeValueDAO avDAO = new AttributeValueDAO();
						for (Iterator<AttributeModel> iterator = cModel.getEntity().getAttributes().iterator(); iterator.hasNext();) {
							AttributeModel attribute = iterator.next();
							if (!avDAO.deleteByAttributeIdObjectId(attribute.getIdAttribute(), cModel.getCodColloquio(), con, false)){
								okDeleteAttribute = false;
								break;
							}
						}

						if (okDeleteAttribute){
							ColloquiDAO cDAO = new ColloquiDAO();
							if (!cDAO.delete(cModel.getCodColloquio(), con, doCommit)){
								dbdelresult = false;
							}
						}					
					}
				}
			}
		}
				
		
		returnValue.put(this.LIST_DELETE_ALLEGATI_FILE, resultAllegati.get(this.LIST_DELETE_ALLEGATI_FILE));
		returnValue.put(this.RESULT_DELETE_ALLEGATI_DB, resultAllegati.get(this.RESULT_DELETE_ALLEGATI_DB));
		//returnValue.put(this.RESULT_DELETE_COLLOQUIO_DB, resultAllegati.get(this.RESULT_DELETE_COLLOQUIO_DB));
		returnValue.put(this.RESULT_DELETE_COLLOQUIO_DB, dbdelresult);
		return returnValue;
	}
	
	public HashMap deleteAllegatiColloquio(ColloquiModel cModel, Connection con , Boolean doCommit){
		HashMap returnValue = new HashMap();
		AllegatiColloquiDAO acDAO = new AllegatiColloquiDAO();
		
		returnValue.put(this.LIST_DELETE_ALLEGATI_FILE, cModel.getAllegati());
		if (!acDAO.deleteByColloquio(cModel.getCodColloquio(), con, doCommit)){
			returnValue.put(this.RESULT_DELETE_ALLEGATI_DB, false);
		}else{
			returnValue.put(this.RESULT_DELETE_ALLEGATI_DB, true);
		}
		return returnValue;
	}
	
	public boolean deleteAllegatiColloqui(ArrayList<AllegatiColloquiVO> allegati){ 
		boolean returnValue = true;
		AllegatiColloquiVO allegato = null;
		try {
			Iterator<AllegatiColloquiVO> it = allegati.iterator();
			while(it.hasNext()){
				allegato = it.next();
				File f = new File (pathAllegatiDestinazione+File.separator+allegato.getCodColloquio()+File.separator+allegato.getNome());
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
	
	private ColloquiModel checkColloquioDB(ColloquiModel cModel){
		if (cModel.getCodColloquio() != null){
			if (new ColloquiDAO().getColloquioById(ColloquiModel.class.getName(), cModel.getCodColloquio()) == null){
				MessageDialog.openError(PlatformUI.getWorkbench()
						  	 					  .getActiveWorkbenchWindow()
						  	 					  .getShell(),
						  	 		    "ERRORE", 
										"Il colloquio che si sta cercando di aggiornare � stato cancellato da un altro utente");
				cModel = null;
				
			}
		}
		return cModel;
	}

	private ColloquiAgentiModel_Age checkAgenteColloquioDB(ColloquiAgentiModel_Age cModel){
		if (cModel.getCodColloquioAgenti() != null && cModel.getCodColloquioAgenti() != 0){
			if (new ColloquiAgentiDAO().getColloquiAgentiById(ColloquiAgentiModel_Age.class.getName(), cModel.getCodColloquioAgenti()) == null){
				MessageDialog.openError(PlatformUI.getWorkbench()
						  	 					  .getActiveWorkbenchWindow()
						  	 					  .getShell(),
						  	 		    "ERRORE", 
										"Il colloquio agente che si sta cercando di aggiornare � stato cancellato da un altro utente");
				cModel = null;
				
			}
		}
		return cModel;
	}

	private ColloquiAnagraficheModel_Ang checkColloquioAnagraficaDB(ColloquiAnagraficheModel_Ang cModel){
		
		if (cModel.getCodColloquioAnagrafiche() != null && cModel.getCodColloquioAnagrafiche() != 0){
			if (new ColloquiAnagraficheDAO().getColloquiAnagraficheById(ColloquiAnagraficheModel_Ang.class.getName(), cModel.getCodColloquioAnagrafiche()) == null){
				MessageDialog.openError(PlatformUI.getWorkbench()
						  	 					  .getActiveWorkbenchWindow()
						  	 					  .getShell(),
						  	 		    "ERRORE", 
										"Il colloquio anagrafica che si sta cercando di aggiornare � stato cancellato da un altro utente");
				cModel = null;
				
			}
		}
		return cModel;
	}

	private ColloquiCriteriRicercaVO checkColloquioCriterioRicercaDB(ColloquiCriteriRicercaVO cModel){
		if (cModel.getCodCriterioRicerca() != null && cModel.getCodCriterioRicerca() != 0){
			if (new ColloquiCriteriRicercaDAO().getColloquiCriteriRicercaById(ColloquiCriteriRicercaVO.class.getName(), cModel.getCodCriterioRicerca()) == null){
				MessageDialog.openError(PlatformUI.getWorkbench()
						  	 					  .getActiveWorkbenchWindow()
						  	 					  .getShell(),
						  	 		    "ERRORE", 
										"Il colloquio criterio ricerca che si sta cercando di aggiornare � stato cancellato da un altro utente");
				cModel = null;
				
			}
		}
		return cModel;
	}

	private AllegatiColloquiVO checkAllegatiColloquiDB(AllegatiColloquiVO cModel){
		if (cModel.getCodAllegatiColloquio() != null && cModel.getCodAllegatiColloquio() != 0){
			if (new AllegatiColloquiDAO().getAllegatiById(AllegatiColloquiVO.class.getName(), cModel.getCodAllegatiColloquio()) == null){
				MessageDialog.openError(PlatformUI.getWorkbench()
						  	 					  .getActiveWorkbenchWindow()
						  	 					  .getShell(),
						  	 		    "ERRORE", 
										"Il colloquio allegato che si sta cercando di aggiornare � stato cancellato da un altro utente");
				cModel = null;
				
			}
		}
		return cModel;
	}
	
	public boolean saveColloquio(ColloquiModel cModel){
		
		Boolean returnValue = true;		
		
		if (cModel != null){
			
			Connection connection = ConnectionManager.getInstance().getConnection();
			ColloquiDAO cDAO = new ColloquiDAO();
			checkColloquioDB(cModel);
			if (cModel != null){
				if (cDAO.saveUpdate(cModel, connection, false)){
					ArrayList listaAgenti = cModel.getAgenti();
					Iterator itAgenti = listaAgenti.iterator();
					while(itAgenti.hasNext()){
						checkAgenteColloquioDB((ColloquiAgentiModel_Age)itAgenti.next());
					}
					if (updateListaAgenti(cModel,connection)){
						ArrayList listaAngrafiche = cModel.getAnagrafiche();
						Iterator itAnagrafiche = listaAngrafiche.iterator();
						while (itAnagrafiche.hasNext()){
							checkColloquioAnagraficaDB((ColloquiAnagraficheModel_Ang)itAnagrafiche.next());
						}
						if (updateListaAnagrafiche(cModel, connection)){
							
							ArrayList listaCriteri = cModel.getCriteriRicerca();
							
							boolean nextsave = true;
							if (cModel.getTipologia() != null){
								if ((cModel.getTipologia().getCodTipologiaColloquio() == 1) && (listaCriteri.size() > 0)){
									SearchEngineImmobili sei = new SearchEngineImmobili(listaCriteri);
									if (sei.verifyQuery() == false){
										nextsave = MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
																			  "Validit� criteri ricerca", 
																			  "I criteri di ricerca inseriti non producono una interrogazione valida.\n "
																			  + "Salvare comunque il colloquio ? (Non produrr� effetti nelle ricerche degli abbinamenti)");
											
										
									}
								}
								if (nextsave){
									Iterator itCriteri = listaCriteri.iterator();
									while(itCriteri.hasNext()){
										checkColloquioCriterioRicercaDB((ColloquiCriteriRicercaVO)itCriteri.next());
									}
									if (updateListaCriteriRicerca(cModel, connection)){
										try {
											ArrayList listaAllegatiColloquio = cModel.getAllegati();
											Iterator itAllegati = listaAllegatiColloquio.iterator();
											while(itAllegati.hasNext()){
												checkAllegatiColloquiDB((AllegatiColloquiVO)itAllegati.next());
											}
											HashMap hmAllegatiResult = updateListaAllegati(cModel, connection);
											connection.commit();																
											deleteAllegatiColloqui((ArrayList)hmAllegatiResult.get(LIST_DELETE_ALLEGATI_FILE));
											ArrayList alSaveAllegati = (ArrayList)hmAllegatiResult.get(LIST_SAVE_ALLEGATI_FILE);
											Iterator it = alSaveAllegati.iterator();
											while (it.hasNext()){
												AllegatiColloquiVO acVO = (AllegatiColloquiVO)it.next();
												if ((acVO.getFromPath() != null) && 
													(!acVO.getFromPath().equalsIgnoreCase(""))){
													WinkhouseUtils.getInstance()
																	.copiaFile(acVO.getFromPath(),
																			   pathAllegatiDestinazione + File.separator + 
																			   acVO.getCodColloquio() + File.separator + acVO.getNome());
												}
											}
										} catch (SQLException e) {
											try {
												connection.rollback();
											} catch (SQLException ex) {
												e.printStackTrace();
											}
											returnValue = false;
										}
																	
									}else{
										try {
											connection.rollback();
										} catch (SQLException e) {
											e.printStackTrace();
										}
										returnValue = false;
									}
								}
							}else{
								try {
									connection.commit();									
								} catch (SQLException e) {
									e.printStackTrace();
								}								
							}
						}else{
							try {
								connection.rollback();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							returnValue = false;
						}
					}else{
						try {
							connection.rollback();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						returnValue = false;
					}
				}else{
					try {
						connection.rollback();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					returnValue = false;
				}
			}else{
				returnValue = false;
			}
		}else{
			returnValue = false;
		}
		
		return returnValue;
		
	} 

	public Boolean updateListaAgenti(ColloquiModel colloquio,
									 Connection con){

		Boolean returnValue = true;

		ArrayList collquiAgentiDB = new ColloquiAgentiDAO().getColloquiAgentiByColloquio(ColloquiAgentiVO.class.getName(),
																						 colloquio.getCodColloquio()); 
		Collections.sort(collquiAgentiDB,comparerColloquiAgenti);
		ArrayList listaCollquiAgenti = colloquio.getAgenti();
		if (listaCollquiAgenti != null){

			ColloquiAgentiDAO caDAO = new ColloquiAgentiDAO();			
			Iterator it  = listaCollquiAgenti.iterator();

			while (it.hasNext()){
				Object o = it.next();
				if (o != null){
					ColloquiAgentiVO colloquiAgenti = (ColloquiAgentiVO)o;
					int index = Collections.binarySearch(collquiAgentiDB, colloquiAgenti,comparerColloquiAgenti);
					if (index >= 0){
						collquiAgentiDB.remove(index);
						Collections.sort(collquiAgentiDB,comparerColloquiAgenti);
					}
					colloquiAgenti.setCodColloquio(colloquio.getCodColloquio());
					if (!caDAO.saveUpdate(colloquiAgenti, con, false)){					
						MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
												"Errore salvataggio colloqui agenti", 
												"Si � verificato un errore nel salvataggio dell'associazione colloquio agente : " + 
												colloquiAgenti.getCodColloquioAgenti());
						returnValue = false;
					}					
				}
			}

			Iterator ite = collquiAgentiDB.iterator();
			while (ite.hasNext()){
				ColloquiAgentiVO colloquiAgenti = (ColloquiAgentiVO)ite.next();
				if (!caDAO.delete(colloquiAgenti.getCodColloquioAgenti(), con, false)){
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore cancellazione colloqui agenti", 
											"Si � verificato un errore nella cancellazione dell'associazione colloquio agente : " + 
											colloquiAgenti.getCodColloquioAgenti());
					returnValue = false;

				}
			}
		}

		return returnValue;
	}
	

	public Boolean updateListaAnagrafiche(ColloquiModel colloquio,
									 	  Connection con){

		Boolean returnValue = true;

		ArrayList colloquiAnagraficheDB = new ColloquiAnagraficheDAO().getColloquiAnagraficheByColloquio(ColloquiAnagraficheVO.class.getName(),
																						 		   colloquio.getCodColloquio()); 
		Collections.sort(colloquiAnagraficheDB,comparerColloquiAnagrafiche);
		ArrayList listaCollquiAnagrafiche = colloquio.getAnagrafiche();
		if (listaCollquiAnagrafiche != null){

			ColloquiAnagraficheDAO caDAO = new ColloquiAnagraficheDAO();			
			Iterator it  = listaCollquiAnagrafiche.iterator();

			while (it.hasNext()){
				Object o = it.next();
				if (o != null){
					ColloquiAnagraficheVO colloquiAnagrafiche = (ColloquiAnagraficheVO)o;
					int index = Collections.binarySearch(colloquiAnagraficheDB, colloquiAnagrafiche,comparerColloquiAnagrafiche);
					if (index >= 0){
						colloquiAnagraficheDB.remove(index);
						Collections.sort(colloquiAnagraficheDB,comparerColloquiAnagrafiche);
					}
					colloquiAnagrafiche.setCodColloquio(colloquio.getCodColloquio());
					if (!caDAO.saveUpdate(colloquiAnagrafiche, con, false)){					
						MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
												"Errore salvataggio colloqui anagrafica", 
												"Si � verificato un errore nel salvataggio dell'associazione colloquio anagrafica : " + 
												colloquiAnagrafiche.getCodColloquioAnagrafiche());
						returnValue = false;
					}					
				}
			}

			Iterator ite = colloquiAnagraficheDB.iterator();
			while (ite.hasNext()){
				ColloquiAnagraficheVO colloquiAnagrafiche = (ColloquiAnagraficheVO)ite.next();
				if (!caDAO.delete(colloquiAnagrafiche.getCodColloquioAnagrafiche(), con, false)){
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore cancellazione colloqui anagrafiche", 
											"Si � verificato un errore nella cancellazione dell'associazione colloquio anagrafica : " + 
											colloquiAnagrafiche.getCodColloquioAnagrafiche());
					returnValue = false;

				}
			}
		}

		return returnValue;
	}
	

	public Boolean updateListaCriteriRicerca(ColloquiModel colloquio,
									 	     Connection con){

		Boolean returnValue = true;

		ArrayList colloquiCriteriRicerca = new ColloquiCriteriRicercaDAO().getColloquiCriteriRicercaByColloquio(ColloquiCriteriRicercaVO.class.getName(),
																						 		   				colloquio.getCodColloquio()); 
		Collections.sort(colloquiCriteriRicerca,comparerColloquiCriteriRicerca);
		ArrayList listaColloquiCriteriRicerca = colloquio.getCriteriRicerca();
		if (listaColloquiCriteriRicerca != null){

			ColloquiCriteriRicercaDAO ccrDAO = new ColloquiCriteriRicercaDAO();			
			Iterator it  = listaColloquiCriteriRicerca.iterator();

			while (it.hasNext()){
				Object o = it.next();
				if (o != null){
					ColloquiCriteriRicercaVO colloquioCriteriRicerca = (ColloquiCriteriRicercaVO)o;
					
					int index = Collections.binarySearch(colloquiCriteriRicerca, colloquioCriteriRicerca,comparerColloquiCriteriRicerca);
					if (index >= 0){
						colloquiCriteriRicerca.remove(index);
						Collections.sort(colloquiCriteriRicerca,comparerColloquiCriteriRicerca);
					}
					colloquioCriteriRicerca.setCodColloquio(colloquio.getCodColloquio());
					if (!ccrDAO.saveUpdate(colloquioCriteriRicerca, con, false)){					
						MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
												"Errore salvataggio criterio ricerca", 
												"Si � verificato un errore nel salvataggio del criterio ricerca : " + 
												colloquioCriteriRicerca.getCodCriterioRicerca());
						returnValue = false;
					}
				}

			}

			Iterator ite = colloquiCriteriRicerca.iterator();
			while (ite.hasNext()){
				ColloquiCriteriRicercaVO colloquioCriteriRicerca = (ColloquiCriteriRicercaVO)ite.next();
				if (!ccrDAO.delete(colloquioCriteriRicerca.getCodCriterioRicerca(), con, false)){
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore cancellazione criterio ricerca", 
											"Si � verificato un errore nella cancellazione del criterio ricerca : " + 
											colloquioCriteriRicerca.getCodCriterioRicerca());
					returnValue = false;

				}
			}
		}

		return returnValue;
	}


	public HashMap updateListaAllegati(ColloquiModel colloquio,
									   Connection con){

		HashMap returnValue = new HashMap();
		returnValue.put(LIST_SAVE_ALLEGATI_FILE, new ArrayList());
		returnValue.put(LIST_DELETE_ALLEGATI_FILE, new ArrayList());

		ArrayList colloquiAllegati = new AllegatiColloquiDAO().getAllegatiByColloquio(AllegatiColloquiVO.class.getName(),
																					  colloquio.getCodColloquio()); 
		Collections.sort(colloquiAllegati,comparerColloquiAllegati);
		ArrayList listaColloquiAllegati = colloquio.getAllegati();
		if (listaColloquiAllegati != null){

			AllegatiColloquiDAO acDAO = new AllegatiColloquiDAO();			
			Iterator it  = listaColloquiAllegati.iterator();

			while (it.hasNext()){
				Object o = it.next();
				if (o != null){
					AllegatiColloquiVO colloquioAllegati = (AllegatiColloquiVO)o;
					int index = Collections.binarySearch(colloquiAllegati, colloquioAllegati,comparerColloquiAllegati);
					if (index >= 0){
						colloquiAllegati.remove(index);
						Collections.sort(colloquiAllegati,comparerColloquiAllegati);
					}
					colloquioAllegati.setCodColloquio(colloquio.getCodColloquio());				
					if (!acDAO.saveUpdate(colloquioAllegati, con, false)){					
						MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
												"Errore salvataggio allegato", 
												"Si � verificato un errore nel salvataggio dell' allegato : " + 
												colloquioAllegati.getCodAllegatiColloquio());
						
					}else{
						((ArrayList)returnValue.get(LIST_SAVE_ALLEGATI_FILE)).add(colloquioAllegati);
					}					
				}
			}

			Iterator ite = colloquiAllegati.iterator();
			while (ite.hasNext()){
				AllegatiColloquiVO colloquioAllegati = (AllegatiColloquiVO)ite.next();
				if (!acDAO.delete(colloquioAllegati.getCodAllegatiColloquio(), con, false)){
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore cancellazione allegato", 
											"Si � verificato un errore nella cancellazione dell' allegato : " + 
											colloquioAllegati.getCodAllegatiColloquio());
					
				}else{
					((ArrayList)returnValue.get(LIST_DELETE_ALLEGATI_FILE)).add(colloquioAllegati);
				}
			}
		}

		return returnValue;
	}

}
