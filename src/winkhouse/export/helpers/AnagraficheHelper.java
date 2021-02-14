package winkhouse.export.helpers;

import java.sql.SQLException;
import java.util.ArrayList;

import winkhouse.dao.AnagraficheDAO;
import winkhouse.engine.search.SearchEngineAnagrafiche;
import winkhouse.export.WrongCriteriaSequenceException;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ColloquiCriteriRicercaModel;
import winkhouse.vo.AnagraficheVO;

public class AnagraficheHelper {

	public AnagraficheHelper() {

	}

	/**
	 * Ritorna la lista di tutte le Anagrafiche presenti in archivio
	 * @return ArrayList<AnagraficheModel>
	 */
	
	public ArrayList<AnagraficheModel> getAnagrafiche(){
		
		ArrayList<AnagraficheModel> returnValue  = new ArrayList<AnagraficheModel>();
		
		AnagraficheDAO aDAO = new AnagraficheDAO();
		returnValue = aDAO.list(AnagraficheModel.class.getName());
		
		return returnValue;
		
	}
	
	/**
	 * Permette di eseguire ricerche nell'archivio degli immobili tramite il passaggio di una sequenza di criteri di ricerca,
	 * rappresentati da una lista di oggetti CriteriRicercaModel 
	 * @param properties, ArrayList<CriteriRicercaModel>
	 * @return ArrayList<ImmobiliModel>
	 * @throws WrongCriteriaSequenceException
	 */
	public ArrayList<AnagraficheModel> getAnagraficheByProperties(ArrayList<ColloquiCriteriRicercaModel> properties) throws WrongCriteriaSequenceException{
		
		ArrayList<AnagraficheModel> returnValue  = new ArrayList<AnagraficheModel>();
		
		SearchEngineAnagrafiche sea = new SearchEngineAnagrafiche(properties);
		
		if (sea.verifyQuery()){
			
			returnValue = sea.find();			
			
		}else{
			throw new WrongCriteriaSequenceException(properties, sea.getQueryText());
		}
		
		return returnValue;
		
	}
	
	/**
	 * Ritorna una lista di classi AnagraficheModel presenti in archivio che hanno come agente inseritore il codice agente passato in input  
	 * @param codAgenteInseritore
	 * @return ArrayList<AnagraficheModel>
	 */
	public ArrayList<AnagraficheModel> getAnagraficheByAgenteInseritore(Integer codAgenteInseritore){
		
		ArrayList<AnagraficheModel> returnValue  = new ArrayList<AnagraficheModel>();
		
		AnagraficheDAO aDAO = new AnagraficheDAO();
		returnValue = aDAO.getAnagraficheByAgenteInseritore(AnagraficheModel.class.getName(),codAgenteInseritore);
		
		return returnValue;

	}
	
	/**
	 * Ritorna una lista di classi AnagraficheModel presenti in archivio che hanno come classe di appartenenza il codice classe in input  
	 * @param codClasseCliente
	 * @return ArrayList<AnagraficheModel>
	 */
	public ArrayList<AnagraficheModel> getAnagraficheByClasseCliente(Integer codClasseCliente){
		
		ArrayList<AnagraficheModel> returnValue  = new ArrayList<AnagraficheModel>();
		
		AnagraficheDAO aDAO = new AnagraficheDAO();
		returnValue = aDAO.getAnagraficheByClasse(AnagraficheModel.class.getName(),codClasseCliente);
		
		return returnValue;

	}
	
	
	/**
	 * Ritorna una istanza AnagraficheModel presente in archivio che ha come chiave primaria il codice passato in input  
	 * @param codClasseCliente
	 * @return AnagraficheModel
	 */
	public AnagraficheModel getAnagraficheByID(Integer codAnagrafica){
		
		AnagraficheModel returnValue  = null;
		
		AnagraficheDAO aDAO = new AnagraficheDAO();
		returnValue = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(),codAnagrafica);
		
		return returnValue;

	}

	/**
	 * Controlla la presenza dell'anagrafica passata in input nell'archivio del programma.
	 * Per la ricerca viene controllata la corrispondenza con il seguente criterio :
	 * 
	 * (nome AND cognome AND citta AND indirizzo) OR codiceFiscale OR partitaIVA 
	 * 
	 *   
	 * @param anagrafica, anagrafica da ricercare
	 * @return ArrayList<AnagraficheModel> lista delle anagrafiche corrispondenti all'anagrafica
	 * passata in input
	 */
	public ArrayList<AnagraficheModel> getAnagraficheExist(AnagraficheModel anagrafica){
		
		boolean nomeok = false,cognomeok = false,cittaok = false,indirizzook = false,codicefiscaleok = false,partitaivaok = false;
		
		ArrayList<AnagraficheModel> returnValue = new ArrayList<AnagraficheModel>();
		
		ArrayList<ColloquiCriteriRicercaModel> criteri = new ArrayList<ColloquiCriteriRicercaModel>();
		
		if (anagrafica.getNome() != null && !anagrafica.getNome().trim().equalsIgnoreCase("")){	
			nomeok = true;
		}

		if (anagrafica.getCognome() != null && !anagrafica.getCognome().trim().equalsIgnoreCase("")){
			cognomeok = true;
		}

		if (anagrafica.getCitta() != null && !anagrafica.getCitta().trim().equalsIgnoreCase("")){
			cittaok = true;
		}

		if (anagrafica.getIndirizzo() != null && !anagrafica.getIndirizzo().trim().equalsIgnoreCase("")){
			indirizzook = true;
		}
		
		
		if (anagrafica.getCodiceFiscale() != null && !anagrafica.getCodiceFiscale().trim().equalsIgnoreCase("")){
			codicefiscaleok = true;
		}
		
		if (anagrafica.getPartitaIva() != null && !anagrafica.getPartitaIva().trim().equalsIgnoreCase("")){
			partitaivaok = true;
		}
		
		ColloquiCriteriRicercaModel criterioOpen = new ColloquiCriteriRicercaModel();
		criterioOpen.setGetterMethodName("(");			
		criterioOpen.setLineNumber(1);		
		criteri.add(criterioOpen);
		
		if (nomeok == true){
			ColloquiCriteriRicercaModel criterioNome = new ColloquiCriteriRicercaModel();
			criterioNome.setFromValue(anagrafica.getNome());			
			criterioNome.setGetterMethodName("getNome");
			if(cognomeok == true || cittaok == true || indirizzook == true){
				criterioNome.setLogicalOperator("AND");
			}
			criterioNome.setLineNumber(2);		
			criteri.add(criterioNome);
		}
		
		if (cognomeok == true){
			ColloquiCriteriRicercaModel criterioCognome = new ColloquiCriteriRicercaModel();
			criterioCognome.setFromValue(anagrafica.getCognome());			
			criterioCognome.setGetterMethodName("getCognome");
			if(cittaok == true || indirizzook == true){
				criterioCognome.setLogicalOperator("AND");
			}
			criterioCognome.setLineNumber(3);		
			criteri.add(criterioCognome);
		}

		if (cittaok == true){
			ColloquiCriteriRicercaModel criterioCitta = new ColloquiCriteriRicercaModel();
			criterioCitta.setFromValue(anagrafica.getCitta());			
			criterioCitta.setGetterMethodName("getCitta");
			if(indirizzook == true){
				criterioCitta.setLogicalOperator("AND");
			}
			criterioCitta.setLineNumber(4);		
			criteri.add(criterioCitta);
		}
		
		if (indirizzook == true){
			ColloquiCriteriRicercaModel criterioIndirizzo = new ColloquiCriteriRicercaModel();
			criterioIndirizzo.setFromValue(anagrafica.getIndirizzo());
			criterioIndirizzo.setGetterMethodName("getIndirizzo");
			criterioIndirizzo.setLineNumber(5);		
			criteri.add(criterioIndirizzo);			
		}
		
		ColloquiCriteriRicercaModel criterioClose = new ColloquiCriteriRicercaModel();
		criterioClose.setGetterMethodName(")");			
		criterioClose.setLineNumber(6);
		criterioClose.setLogicalOperator("OR");
		criteri.add(criterioClose);

		if (codicefiscaleok == true){
			ColloquiCriteriRicercaModel criterioCF = new ColloquiCriteriRicercaModel();
			criterioCF.setFromValue(anagrafica.getCodiceFiscale());			
			criterioCF.setGetterMethodName("getCodiceFiscale");
			if (partitaivaok == true){
				criterioCF.setLogicalOperator("OR");
			}
			criterioCF.setLineNumber(7);		
			criteri.add(criterioCF);			
		}
		
		if (partitaivaok == true){
			ColloquiCriteriRicercaModel criterioPIVA = new ColloquiCriteriRicercaModel();
			criterioPIVA.setFromValue(anagrafica.getPartitaIva());			
			criterioPIVA.setGetterMethodName("getPartitaIva");			
			criterioPIVA.setLineNumber(8);		
			criteri.add(criterioPIVA);
		}

		try {
			returnValue = getAnagraficheByProperties(criteri);
		} catch (WrongCriteriaSequenceException e) {
			e.printStackTrace();
		}
		
		return returnValue;
	}

	/**
	 * Inserisce un record nella tabella anagrafiche ritornando true o false.
	 * Se il campo codAnagrafica, del parametro anagrafica, è zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param anagrafica
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateAnagrafica(AnagraficheVO anagrafica) throws SQLException{		
		
		AnagraficheDAO aDAO = new AnagraficheDAO();
		return aDAO.saveUpdateWithException(anagrafica, null, true);
		
	}

}
