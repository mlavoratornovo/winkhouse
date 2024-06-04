package winkhouse.export.helpers;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.cayenne.ObjectContext;

import winkhouse.dao.AbbinamentiDAO;
import winkhouse.dao.AllegatiImmobiliDAO;
import winkhouse.dao.DatiCatastaliDAO;
import winkhouse.dao.ImmaginiDAO;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.dao.ImmobiliPropietariDAO;
import winkhouse.dao.StanzeDAO;
import winkhouse.engine.search.SearchEngineImmobili;
import winkhouse.export.WrongCriteriaSequenceException;
import winkhouse.model.ColloquiCriteriRicercaModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Colloquicriteriricerca;
import winkhouse.orm.Immobili;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.AbbinamentiVO;
import winkhouse.vo.AllegatiImmobiliVO;
import winkhouse.vo.DatiCatastaliVO;
import winkhouse.vo.ImmagineVO;
import winkhouse.vo.ImmobiliPropietariVO;
import winkhouse.vo.ImmobiliVO;
import winkhouse.vo.StanzeImmobiliVO;
import winkhouse.xmldeser.models.xml.ImmobiliXMLModel;

/**
 * Classe Helper di accesso ai dati relativi agli immobili
 * @author Michele Lavoratornovo
 */
public class ImmobiliHelper {

	
	public ImmobiliHelper() {
		
	}
	/**
	 * Ritorna la lista di tutti gli immobili presenti in archivio
	 * @return ArrayList<ImmobiliModel>
	 */
	
	public ArrayList<Immobili> getImmobili(){
		
		ArrayList<Immobili> returnValue  = new ArrayList<Immobili>();
		
		ImmobiliDAO iDAO = new ImmobiliDAO();
		returnValue = iDAO.list(null);
		
		return returnValue;
		
	}
	
	/**
	 * Permette di eseguire ricerche nell'archivio degli immobili tramite il passaggio di una sequenza di criteri di ricerca,
	 * rappresentati da una lista di oggetti CriteriRicercaModel 
	 * @param properties, ArrayList<CriteriRicercaModel>
	 * @return ArrayList<ImmobiliModel>
	 * @throws WrongCriteriaSequenceException
	 */
	public ArrayList<Immobili> getImmobiliByProperties(ArrayList<Colloquicriteriricerca> properties) throws WrongCriteriaSequenceException{
		
		ArrayList<Immobili> returnValue  = new ArrayList<Immobili>();
		
		SearchEngineImmobili sei = new SearchEngineImmobili(properties);
		
		if (sei.verifyQuery()){
			
			returnValue = sei.find();			
			
		}else{
			throw new WrongCriteriaSequenceException(properties, sei.getQueryText());
		}
		
		return returnValue;
		
	}
		
	/**
	 * Ritorna un immobile dal suo codice di chiave primaria
	 * @param codImmobile
	 * @return ImmobiliModel
	 */
	public ImmobiliModel getImmobileById(Integer codImmobile){
		
		ImmobiliModel returnValue = null;
		
		ImmobiliDAO iDAO = new ImmobiliDAO();
		returnValue = (ImmobiliModel)iDAO.getImmobileById(ImmobiliModel.class.getName(), codImmobile);
		
		return returnValue;
	}

	/**
	 * Ritorna un immobile dal suo codice di riferimento (il codice parlante inserito dall'utente)
	 * @param codImmobile
	 * @return ImmobiliModel
	 */	
	public ImmobiliModel getImmobileByRif(String rif){

		ImmobiliModel returnValue = null;
		
		ImmobiliDAO iDAO = new ImmobiliDAO();
		ArrayList al = (ArrayList)iDAO.getImmobileByRif(ImmobiliModel.class.getName(), rif);
		
		if ((al != null) && (al.size() == 1)){
			returnValue = (ImmobiliModel)al.get(0);
		}
		
		return returnValue;
		
	}
	
	/**
	 * Ritorna la lista di classi ImmobiliModel che hanno come agente inseritore il codice agente passato in input
	 * @param codAgenteInseritore
	 * @return ArrayList<ImmobiliModel>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ImmobiliModel> getImmobiliByAgenteInseritore(Integer codAgenteInseritore){
		
		ArrayList<ImmobiliModel> returnValue = null;
		
		ImmobiliDAO iDAO = new ImmobiliDAO();
		returnValue = iDAO.getImmobiliByAgente(ImmobiliModel.class.getName(), codAgenteInseritore);
		
		return returnValue;
		
	}
	
	/**
	 * Ritorna la lista degli immobili di proprieta dell'anagrafica passata in input tramite il codice.
	 * @param codAnagrafica
	 * @return ArrayList<ImmobiliModel>
	 */
	public ArrayList<ImmobiliModel> getImmobiliByAnagrafica(Integer codAnagrafica){
		
		ArrayList<ImmobiliModel> returnValue = null;
		
		ImmobiliDAO iDAO = new ImmobiliDAO();
		returnValue = iDAO.getImmobiliByAnagrafica(ImmobiliModel.class.getName(), codAnagrafica);
		
		return returnValue;
		
	}
	
	/**
	 * Ritorna la lista degli immobili che hanno come impianto di riscaldamento quello passato in input tramite il codice.
	 * @param codRiscaldamento
	 * @return ArrayList<ImmobiliModel>
	 */
	public ArrayList<ImmobiliModel> getImmobiliByRiscaldamento(Integer codRiscaldamento){
		
		ArrayList<ImmobiliModel> returnValue = null;
		
		ImmobiliDAO iDAO = new ImmobiliDAO();
		returnValue = iDAO.getImmobiliByRiscaldamento(ImmobiliModel.class.getName(), codRiscaldamento);
		
		return returnValue;
		
	}
	
	/**
	 * Ritorna la lista degli immobili che hanno come stato conservativo quello passato in input tramite il codice.
	 * @param codStatoConservativo
	 * @return ArrayList<ImmobiliModel>
	 */
	public ArrayList<ImmobiliModel> getImmobiliByStatoConservativo(Integer codStatoConservativo){
		
		ArrayList<ImmobiliModel> returnValue = null;
		
		ImmobiliDAO iDAO = new ImmobiliDAO();
		returnValue = iDAO.getImmobiliByStatoConservativo(ImmobiliModel.class.getName(), codStatoConservativo);
		
		return returnValue;
		
	}

	/**
	 * Ritorna la lista degli immobili che hanno come tipologia (appartamento,villa,ecc...) quella passata in input tramite il codice.
	 * @param codStatoConservativo
	 * @return ArrayList<ImmobiliModel>
	 */
	public ArrayList<ImmobiliModel> getImmobiliByTipologia(Integer codTipologia){
		
		ArrayList<ImmobiliModel> returnValue = null;
		
		ImmobiliDAO iDAO = new ImmobiliDAO();
		returnValue = iDAO.getImmobiliByTipologia(ImmobiliModel.class.getName(), codTipologia);
		
		return returnValue;
		
	}
	
	/**
	 * Ritorna la lista degli immobili che hanno come tipologia (appartamento,villa,ecc...) quella passata in input tramite il codice e che hanno la propriet�
	 * affittabile == True.
	 * @param codStatoConservativo
	 * @return ArrayList<ImmobiliModel>
	 */
	public ArrayList<ImmobiliModel> getImmobiliByTipologiaAffitti(Integer codTipologiaAffitti){
		
		ArrayList<ImmobiliModel> returnValue = null;
		
		ImmobiliDAO iDAO = new ImmobiliDAO();
		returnValue = iDAO.getImmobiliByTipologiaIsAffitti(ImmobiliModel.class.getName(), codTipologiaAffitti);
		
		return returnValue;
		
	}

	/**
	 * Controlla la presenza dell'immobile passato in input nell'archivio del programma.
	 * Per la ricerca viene controllata la corrispondenza con il seguente criterio :
	 * 
	 * (indirizzo AND citta) OR codice di riferimento 
	 * 
	 *   
	 * @param immobile, immobile da ricercare
	 * @return ArrayList<ImmobiliModel> lista degli immobili corrispondenti all'immobile
	 * passato in input
	 */
	public ArrayList<Immobili> getImmobiliExist(ImmobiliXMLModel immobile){
		
		ArrayList<Immobili> returnValue = new ArrayList<Immobili>();		
		ArrayList<Colloquicriteriricerca> criteri = new ArrayList<Colloquicriteriricerca>();
		
		ObjectContext oc = WinkhouseUtils.getInstance().getCayenneObjectContext();
		
		Colloquicriteriricerca criterioOpen = oc.newObject(Colloquicriteriricerca.class);
		criterioOpen.setGettermethodname("(");			
		criterioOpen.setLineNumber(1);		
		criteri.add(criterioOpen);
				
		Colloquicriteriricerca criterioIndirizzo = oc.newObject(Colloquicriteriricerca.class);
		criterioIndirizzo.setFromvalue(immobile.getIndirizzo());			
		criterioIndirizzo.setGettermethodname("getIndirizzo");
		criterioIndirizzo.setLogicaloperator("AND");	
		criterioIndirizzo.setLineNumber(2);		
		criteri.add(criterioIndirizzo);
		
		Colloquicriteriricerca criterioCitta = oc.newObject(Colloquicriteriricerca.class);
		criterioCitta.setFromvalue(immobile.getCitta());			
		criterioCitta.setGettermethodname("getCitta");
		criterioCitta.setLineNumber(3);		
		criteri.add(criterioCitta);				
						
		Colloquicriteriricerca criterioClose = oc.newObject(Colloquicriteriricerca.class);
		criterioClose.setGettermethodname(")");			
		criterioClose.setLineNumber(4);
		criterioClose.setLogicaloperator("OR");
		criteri.add(criterioClose);
		
		Colloquicriteriricerca criterioRif = oc.newObject(Colloquicriteriricerca.class);		
		criterioRif.setFromvalue(immobile.getRif());			
		criterioRif.setGettermethodname("getRif");		
		criterioRif.setLineNumber(5);		
		criteri.add(criterioRif);
				
		
		try {
			returnValue = getImmobiliByProperties(criteri);
		} catch (WrongCriteriaSequenceException e) {
			e.printStackTrace();
		}
		
		return returnValue;
	}

	/**
	 * Controlla la presenza in archivio di una stanza in base al codice immobile e al codice tipologia stanza passati in input.
	 *   
	 * @param codImmobile, codice immobile da ricercare
	 * @param codTipologia, codice della tipologia stanza da ricercare
	 * @return ArrayList di oggetti StanzeImmobiliVO
	 */	
	public ArrayList getStanzaImmobileByCodImmobileCodTipologia(Integer codImmobile,Integer codTipologia){
		
		return new StanzeDAO().listByImmobileTipologia(StanzeImmobiliVO.class.getName(), codImmobile, codTipologia);
		
		
	}

	/**
	 * Controlla la presenza in archivio di un allegato in base al codice immobile passato in input.
	 *   
	 * @param codImmobile, codice immobile da ricercare
	 * @return ArrayList di oggetti AllegatiImmobiliVO
	 */	
	public ArrayList getAllegatiImmobileByCodImmobile(Integer codImmobile){
		
		return new AllegatiImmobiliDAO().getAllegatiByImmobile(AllegatiImmobiliVO.class.getName(), codImmobile);
		
	}

	/**
	 * 
	 * Controlla la presenza in archivio di una stanza in base ai parametri passati in input.
	 * 
	 * @param codImmobile
	 * @param foglio
	 * @param particella
	 * @param subalterno
	 * @param categoria
	 * @param rendita
	 * @param redditoDomenicale
	 * @param redditoAgricolo
	 * @param dimensione
	 * @return DatiCatastaliVO
	 */
	public DatiCatastaliVO getDatiCatastli(Integer codImmobile,String foglio, String particella, String subalterno,
		    						 String categoria, Double rendita, Double redditoDomenicale,
		    						 Double redditoAgricolo, Double dimensione){
		
		return new DatiCatastaliDAO().getDatiCatastaliByProperties(codImmobile, foglio, particella, subalterno, categoria, rendita, redditoDomenicale, redditoAgricolo, dimensione);
		
		
	}
	
	/**
	 * Controlla la presenza in archivio di una abbinamento tra anagrafica ed immobile in base ai parametri passati in input.
	 * 
	 * @param codImmobile
	 * @param codAnagrafica
	 * @return AbbinamentiVO
	 */	
	public AbbinamentiVO getAbbinamenti(Integer codImmobile,Integer codAnagrafica){
		
		AbbinamentiVO returnValue = null;
		AbbinamentiDAO aDAO = new AbbinamentiDAO();
		
		Object o = aDAO.findAbbinamentiByCodImmobileCodAnagrafica(AbbinamentiVO.class.getName(), codAnagrafica, codImmobile);
		if (o != null){
			returnValue = (AbbinamentiVO)o;			
		}
				
		return returnValue;
		
		
	}
	
	/**
	 * Inserisce un record nella tabella immobili ritornando true o false.
	 * Se il campo codImmobile, del parametro immobile, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param immobile
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateImmobile(ImmobiliVO immobile) throws SQLException{		
		
		ImmobiliDAO iDAO = new ImmobiliDAO();
		return iDAO.saveUpdateWithException(immobile, null, true);
		
	}
	
	/**
	 * Inserisce un record nella tabella stanzeimmobili ritornando true o false.
	 * Se il campo codStanzaImmobile, del parametro stanzeimmobili, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param stanzeimmobili
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateStanzaImmobile(StanzeImmobiliVO stanzeimmobili) throws SQLException{		
		
		StanzeDAO sDAO = new StanzeDAO();
		return sDAO.saveUpdateWithException(stanzeimmobili, null, true);
		
	}	

	/**
	 * Inserisce un record nella tabella immagini ritornando true o false.
	 * Se il campo codImmagine, del parametro immagine, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param immagine
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateImmagine(ImmagineVO immagine) throws SQLException{		
		
		ImmaginiDAO iDAO = new ImmaginiDAO();
		return iDAO.saveUpdateWithException(immagine, null, true);
		
	}	

	/**
	 * Inserisce un record nella tabella allegatiimmobili ritornando true o false.
	 * Se il campo codAllegatiImmobili, del parametro allegatoimmobile, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param allegatoimmobile
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateAllegatiImmobili(AllegatiImmobiliVO allegatoimmobile) throws SQLException{		
		
		AllegatiImmobiliDAO aiDAO = new AllegatiImmobiliDAO();
		return aiDAO.saveUpdateWithException(allegatoimmobile, null, true);
		
	}	

	/**
	 * Inserisce un record nella tabella datiCatastali ritornando true o false.
	 * Se il campo codDatiCatastali, del parametro datiCatastali, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param datiCatastali
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateDatiCatastali(DatiCatastaliVO datiCatastali) throws SQLException{		
		
		DatiCatastaliDAO dcDAO = new DatiCatastaliDAO();
		return dcDAO.saveUpdateWithException(datiCatastali, null, true);
		
	}	

	/**
	 * Inserisce un record nella tabella abbinamento ritornando true o false.
	 * Se il campo codAbbinamento, del parametro abbinamento, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param abbinamento
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateAbbinamenti(AbbinamentiVO abbinamento) throws SQLException{		
		
		AbbinamentiDAO aDAO = new AbbinamentiDAO();
		return aDAO.saveUpdateWithException(abbinamento, null, true);
		
	}	

	/**
	 * Inserisce un record nella tabella immobilipropietari ritornando true o false.
	 * Se la combinazione di codimmobile e codanagrafica � gi� presente nel database non viene eseguito nessun inserimento,
	 * in questo caso viene ritornato true, in caso di inserimento viene ritornato il risultato dell'operazione di inserimento.
	 * SQL di inserimento.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param immobiliPropietari
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateImmobiliPropieta(ImmobiliPropietariVO immobiliPropietari) throws SQLException{		
		
		ImmobiliPropietariDAO ipDAO = new ImmobiliPropietariDAO();
		
		if (ipDAO.getImmobiliPropietariByCodImmobileCodAnagrafica(immobiliPropietari.getCodImmobile(), immobiliPropietari.getCodAnagrafica()) == null){
			return ipDAO.insert(immobiliPropietari, null, true);
		}
		return true;
		
	}	
	
}