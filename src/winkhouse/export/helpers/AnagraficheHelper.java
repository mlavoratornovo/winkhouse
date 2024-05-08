package winkhouse.export.helpers;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.cayenne.ObjectContext;

import winkhouse.dao.AnagraficheDAO;
import winkhouse.engine.search.SearchEngineAnagrafiche;
import winkhouse.export.WrongCriteriaSequenceException;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ColloquiCriteriRicercaModel;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Colloquicriteriricerca;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.AnagraficheVO;

public class AnagraficheHelper {

	public AnagraficheHelper() {

	}

	/**
	 * Ritorna la lista di tutte le Anagrafiche presenti in archivio
	 * @return ArrayList<AnagraficheModel>
	 */
	
	public ArrayList<Anagrafiche> getAnagrafiche(){
		
		ArrayList<Anagrafiche> returnValue  = new ArrayList<Anagrafiche>();
		
		AnagraficheDAO aDAO = new AnagraficheDAO();
		returnValue = aDAO.list(null);
		
		return returnValue;
		
	}
	
	/**
	 * Permette di eseguire ricerche nell'archivio degli immobili tramite il passaggio di una sequenza di criteri di ricerca,
	 * rappresentati da una lista di oggetti CriteriRicercaModel 
	 * @param properties, ArrayList<CriteriRicercaModel>
	 * @return ArrayList<ImmobiliModel>
	 * @throws WrongCriteriaSequenceException
	 */
	public ArrayList<Anagrafiche> getAnagraficheByProperties(ArrayList<Colloquicriteriricerca> properties) throws WrongCriteriaSequenceException{
		
		ArrayList<Anagrafiche> returnValue  = new ArrayList<Anagrafiche>();
		
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
	public ArrayList<Anagrafiche> getAnagraficheExist(Anagrafiche anagrafica){
		
		boolean nomeok = false,cognomeok = false,cittaok = false,indirizzook = false,codicefiscaleok = false,partitaivaok = false;
		
		ArrayList<Anagrafiche> returnValue = new ArrayList<Anagrafiche>();
		
		ArrayList<Colloquicriteriricerca> criteri = new ArrayList<Colloquicriteriricerca>();
		
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
		
		
		if (anagrafica.getCodicefiscale() != null && !anagrafica.getCodicefiscale().trim().equalsIgnoreCase("")){
			codicefiscaleok = true;
		}
		
		if (anagrafica.getPiva() != null && !anagrafica.getPiva().trim().equalsIgnoreCase("")){
			partitaivaok = true;
		}
		
		ObjectContext oc = WinkhouseUtils.getInstance().getCayenneObjectContext();
		
		Colloquicriteriricerca criterioOpen = oc.newObject(Colloquicriteriricerca.class);		
		criterioOpen.setGettermethodname("(");			
		criterioOpen.setLineNumber(1);		
		criteri.add(criterioOpen);
		
		if (nomeok == true){			
			Colloquicriteriricerca criterioNome = oc.newObject(Colloquicriteriricerca.class);
			criterioNome.setFromvalue(anagrafica.getNome());			
			criterioNome.setGettermethodname("getNome");
			if(cognomeok == true || cittaok == true || indirizzook == true){
				criterioNome.setLogicaloperator("AND");
			}
			criterioNome.setLineNumber(2);		
			criteri.add(criterioNome);
		}
		
		if (cognomeok == true){			
			Colloquicriteriricerca criterioCognome = oc.newObject(Colloquicriteriricerca.class);
			criterioCognome.setFromvalue(anagrafica.getCognome());			
			criterioCognome.setGettermethodname("getCognome");
			if(cittaok == true || indirizzook == true){
				criterioCognome.setLogicaloperator("AND");
			}
			criterioCognome.setLineNumber(3);		
			criteri.add(criterioCognome);
		}

		if (cittaok == true){			
			Colloquicriteriricerca criterioCitta = oc.newObject(Colloquicriteriricerca.class);
			criterioCitta.setFromvalue(anagrafica.getCitta());			
			criterioCitta.setGettermethodname("getCitta");
			if(indirizzook == true){
				criterioCitta.setLogicaloperator("AND");
			}
			criterioCitta.setLineNumber(4);		
			criteri.add(criterioCitta);
		}
		
		if (indirizzook == true){			
			Colloquicriteriricerca criterioIndirizzo = oc.newObject(Colloquicriteriricerca.class);
			criterioIndirizzo.setFromvalue(anagrafica.getIndirizzo());
			criterioIndirizzo.setGettermethodname("getIndirizzo");
			criterioIndirizzo.setLineNumber(5);		
			criteri.add(criterioIndirizzo);			
		}
				
		Colloquicriteriricerca criterioClose = oc.newObject(Colloquicriteriricerca.class);
		criterioClose.setGettermethodname(")");			
		criterioClose.setLineNumber(6);
		criterioClose.setLogicaloperator("OR");
		criteri.add(criterioClose);

		if (codicefiscaleok == true){			
			Colloquicriteriricerca criterioCF = oc.newObject(Colloquicriteriricerca.class);
			criterioCF.setFromvalue(anagrafica.getCodicefiscale());			
			criterioCF.setGettermethodname("getCodiceFiscale");
			if (partitaivaok == true){
				criterioCF.setLogicaloperator("OR");
			}
			criterioCF.setLineNumber(7);		
			criteri.add(criterioCF);			
		}
		
		if (partitaivaok == true){			
			Colloquicriteriricerca criterioPIVA = oc.newObject(Colloquicriteriricerca.class);
			criterioPIVA.setFromvalue(anagrafica.getPiva());			
			criterioPIVA.setGettermethodname("getPartitaIva");			
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
	 * Se il campo codAnagrafica, del parametro anagrafica, � zero viene inserito un nuovo record, altrimenti esegue una operazione
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