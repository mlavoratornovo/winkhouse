package winkhouse.export.helpers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import winkhouse.dao.AffittiAllegatiDAO;
import winkhouse.dao.AffittiAnagraficheDAO;
import winkhouse.dao.AffittiDAO;
import winkhouse.dao.AffittiRateDAO;
import winkhouse.dao.AffittiSpeseDAO;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.model.AffittiModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.vo.AffittiAllegatiVO;
import winkhouse.vo.AffittiAnagraficheVO;
import winkhouse.vo.AffittiRateVO;
import winkhouse.vo.AffittiSpeseVO;
import winkhouse.vo.AffittiVO;

/**
 * Classe Helper di accesso ai dati relativi agli affitti
 * @author Michele Lavoratornovo
 */

public class AffittiHelper {

	public AffittiHelper(){}
	
	/**
	 * Ritorna la lista degli affitti associati al codice immobile passato in input
	 * 
	 * @param codImmobile
	 * @return ArrayList<AffittiModel>
	 */
	public ArrayList<AffittiModel> getAffittiByImmobile(Integer codImmobile){
		
		ArrayList<AffittiModel> returnValue = new ArrayList<AffittiModel>();
		
		AffittiDAO affittiDAO = new AffittiDAO();
		returnValue = affittiDAO.getAffittiByCodImmobile(AffittiModel.class.getName(), codImmobile);
		
		return returnValue;
		
	}
	
	/**
	 * Ritorna una lista di affitti associati al codice di rifermento dell'immobile, con data di inizio e data fine
	 * uguali ai parametri passati in input 
	 * 
	 * @param rifImmobile
	 * @param dataInizio
	 * @param dataFine
	 * @return ArrayList<AffittiModel>
	 */	
	public ArrayList<AffittiModel> getAffittiByRifDate(String rifImmobile, Date dataInizio, Date dataFine){
		
		ArrayList<AffittiModel> returnValue = new ArrayList<AffittiModel>();
		
		ImmobiliDAO iDAO = new ImmobiliDAO();
		Object o = iDAO.getImmobileByRif(ImmobiliModel.class.getName(), rifImmobile);
		
		if (o != null){
			
			if (((ArrayList)o).size() > 0){
				
				ImmobiliModel im = ((ImmobiliModel)((ArrayList)o).get(0));
				AffittiDAO aDAO = new AffittiDAO();
				returnValue =  aDAO.getAffittiData(AffittiModel.class.getName(), 
											 				dataInizio, 
											 				dataFine, 
											 				im.getCodImmobile());
			}
			
		}
		
		
		return returnValue;
		
	}

	/**
	 * Ritorna un oggetto di tipo AffittiAllegatiVO cercato in base al codice affitto e al nome file passati in input.
	 * 
	 * @param codAffitto
	 * @param nomeFile
	 * @return ArrayList di oggetti AffittiAllegatiVO 
	 */
	public AffittiAllegatiVO getAffittiAllegatiExist(Integer codAffitto, String nomeFile){
		
		AffittiAllegatiVO returnValue = null;
		
		AffittiAllegatiDAO aadao = new AffittiAllegatiDAO();
		ArrayList al = aadao.getAffittiAllegatiByCodAffitto(AffittiAllegatiVO.class.getName(), codAffitto);
		Iterator it = al.iterator();
		
		while (it.hasNext()) {
			
			AffittiAllegatiVO affittiallegati = (AffittiAllegatiVO) it.next();
			
			if (affittiallegati.getNome().equalsIgnoreCase(nomeFile)){
				
				returnValue = affittiallegati;
				break;
				
			}
			
		}
		
		return returnValue;
	} 

	/**
	 * Ritorna un oggetto di tipo AffittiAnagraficheVO cercato in base al codice affitto e al codice anagrafica
	 * passati in input.
	 * 
	 * @param codAffitto
	 * @param codAnagrafica
	 * @return ArrayList di oggetti AffittiAnagraficheVO 
	 */
	public AffittiAnagraficheVO getAffittiAnagraficheExist(Integer codAffitto, Integer codAnagrafica){
		
		AffittiAnagraficheVO returnValue = null;
		
		AffittiAnagraficheDAO aadao = new AffittiAnagraficheDAO();
		returnValue = (AffittiAnagraficheVO)aadao.getAffittiAnagraficheByAffittoAnagrafica(AffittiAnagraficheVO.class.getName(),
																	  					   codAffitto, 
																	  					   codAnagrafica);
		return returnValue;
	} 

	/**
	 * Ritorna un oggetto di tipo AffittiSpeseVO cercato in base al parametro di tipo AffittiSpeseVO 
	 * passato in input.
	 * 
	 * @param affittiSpese
	 * @return AffittiSpeseVO 
	 */
	public ArrayList<AffittiSpeseVO> getAffittiSpeseExist(AffittiSpeseVO affittiSpese){
		
		ArrayList<AffittiSpeseVO> returnValue = null;
		
		AffittiSpeseDAO asdao = new AffittiSpeseDAO();
		returnValue = asdao.getAffittiSpeseByProperties(AffittiSpeseVO.class.getName(), affittiSpese);
		return returnValue;
	} 

	/**
	 * Ritorna un oggetto di tipo AffittiRateVO cercato in base al parametro di tipo AffittiRateVO 
	 * passato in input.
	 * 
	 * @param affittiRate
	 * @return AffittiRateVO 
	 */
	public ArrayList<AffittiRateVO> getAffittiRateExist(AffittiRateVO affittiRate){
		
		ArrayList<AffittiRateVO> returnValue = null;
		
		AffittiRateDAO ardao = new AffittiRateDAO();
		returnValue = ardao.getAffittiRateByProperties(AffittiRateVO.class.getName(), affittiRate);
		return returnValue;
	} 

	
	/**
	 * Inserisce un record nella tabella affitti ritornando true o false.
	 * Se il campo codAffitti, del parametro affitto, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param affitto
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateAffitti(AffittiVO affitto) throws SQLException{		
		
		AffittiDAO aDAO = new AffittiDAO();
		return aDAO.saveUpdateWithException(affitto, null, true);
		
	}

	/**
	 * Inserisce un record nella tabella affittiallegati ritornando true o false.
	 * Se il campo codAffittiAllegati, del parametro affittiallegati, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param affittiAllegati
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateAllegatiAffitti(AffittiAllegatiVO affittiAllegati) throws SQLException{		
		
		AffittiAllegatiDAO aaDAO = new AffittiAllegatiDAO();
		return aaDAO.saveUpdateWithException(affittiAllegati, null, true);
		
	}
	
	/**
	 * Inserisce un record nella tabella affittiRate ritornando true o false.
	 * Se il campo codAffittiRate, del parametro affittiRate, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param affittiRate
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateAffittiRate(AffittiRateVO affittiRate) throws SQLException{		
		
		AffittiRateDAO arDAO = new AffittiRateDAO();
		return arDAO.saveUpdateWithException(affittiRate, null, true);
		
	}
	
	/**
	 * Inserisce un record nella tabella affittiSpese ritornando true o false.
	 * Se il campo codAffittiSpese, del parametro affittiSpese, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param affittiSpese
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateAffittiSpese(AffittiSpeseVO affittiSpese) throws SQLException{		
		
		AffittiSpeseDAO asDAO = new AffittiSpeseDAO();
		return asDAO.saveUpdateWithException(affittiSpese, null, true);
		
	}

	/**
	 * Inserisce un record nella tabella affittianagrafiche ritornando true o false.
	 * Se il campo codAffittiAnagrafiche, del parametro affittianagrafiche, � zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param affittiAnagrafiche
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateAffittiAnagrafiche(AffittiAnagraficheVO affittiAnagrafiche) throws SQLException{		
		
		AffittiAnagraficheDAO aaDAO = new AffittiAnagraficheDAO();
		return aaDAO.saveUpdateWithException(affittiAnagrafiche, null, true);
		
	}

	
}