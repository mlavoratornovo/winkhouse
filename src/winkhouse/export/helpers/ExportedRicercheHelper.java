package winkhouse.export.helpers;

import java.util.ArrayList;

import winkhouse.dao.RicercheDAO;
import winkhouse.helper.RicercheHelper;
import winkhouse.model.RicercheModel;

public class ExportedRicercheHelper {

	/**
	 * Ritorna una lista di oggetti RicercheModel selezionate in base al tipo.
	 * Sono le ricerche salvate dagli utenti.
	 * @param type Integer tipo di ricerca:
	 * 		  possibili valori : 
	 * 				RicercheModel.RICERCHE_IMMOBILI;
	 * 				RicercheModel.RICERCHE_ANAGRAFICHE;
	 * 				RicercheModel.RICERCHE_IMMOBILI_AFFITTI;
	 *  						
	 * @return ArrayList<ERicercheModel>
	 */
	public ArrayList<RicercheModel> getRicercheByType(Integer type){
		
		ArrayList<RicercheModel> returnValue = new ArrayList<RicercheModel>();
		
		RicercheDAO rDAO = new RicercheDAO();
		returnValue = rDAO.getRichercheByTipo(RicercheModel.class.getName(), type);		
		
		return returnValue;
		
	}
	/**
	 * Esegue la cancellazione delle ricerca salvata nel database e dei suoi criteri.
	 * @param RicercheModel ricerca
	 * @return Boolean 
	 */
	public Boolean deleteRicerca(RicercheModel ricerca){
		
		RicercheHelper rh = new RicercheHelper();
		return rh.deleteRicerca(ricerca,0); 
		
	}
	/**
	 * Esegue il salvataggio delle ricerca nel database e dei suoi criteri.
	 * @param RicercheModel ricerca
	 * @return Boolean 
	 */	
	public Boolean saveRicerca(RicercheModel ricerca){
		RicercheHelper rh = new RicercheHelper();
		return rh.saveUpdateRicerca(ricerca);
	}
}
