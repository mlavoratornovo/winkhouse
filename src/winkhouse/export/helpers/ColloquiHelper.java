package winkhouse.export.helpers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import winkhouse.dao.AllegatiColloquiDAO;
import winkhouse.dao.ColloquiAgentiDAO;
import winkhouse.dao.ColloquiAnagraficheDAO;
import winkhouse.dao.ColloquiCriteriRicercaDAO;
import winkhouse.dao.ColloquiDAO;
import winkhouse.model.ColloquiModel;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AllegatiColloquiVO;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ColloquiAgentiVO;
import winkhouse.vo.ColloquiAnagraficheVO;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.vo.ColloquiVO;

public class ColloquiHelper {

	/**
	 * Controlla la presenza del colloquio passato in input nell'archivio del programma.
	 * Per la ricerca viene controllata la corrispondenza con il seguente criterio :
	 * 
	 * data colloquio AND scadenziere AND iCalUid AND luogo incontro
	 * 
	 * controllando tra colloqui in cui è presente l'anagrafica passata in input 
	 * Utilizzare il metodo getAnagraficheExist della classe winkhouse.export.helpers.AnagraficheHelper 
	 * per individuare una lista di anagrafiche da passare al metodo.
	 *  
	 * @param colloquio
	 * @param anagrafica
	 * @return ArrayList<ColloquiModel>
	 */
	public ArrayList<ColloquiModel> getColloquiExist(ColloquiVO colloquio,AnagraficheVO anagrafica){
		
		ArrayList<ColloquiModel> returnValue = new ArrayList<ColloquiModel>();
		ColloquiDAO cdao = new ColloquiDAO();
		
		ArrayList<ColloquiModel> tmp = cdao.getColloquiByAnagraficaRicerca(ColloquiModel.class.getName(), 
														  				   anagrafica.getCodAnagrafica());
		

		tmp.addAll(cdao.getColloquiByAnagrafica(ColloquiModel.class.getName(), 
				  								anagrafica.getCodAnagrafica()));
		
		Iterator<ColloquiModel> it = tmp.iterator();
		
		while (it.hasNext()){
			
			ColloquiModel cm = it.next();
			
			Calendar c1 = Calendar.getInstance(Locale.ITALIAN);
			c1.setTime(cm.getDataColloquio());
			
			Calendar c2 = Calendar.getInstance(Locale.ITALIAN);
			c2.setTime(colloquio.getDataColloquio());
			
			if (
				(
						(c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) &&
						(c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) &&
						(c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) &&
						(c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY)) &&
						(c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE)) &&
						(c1.get(Calendar.SECOND) == c2.get(Calendar.SECOND))						
				) && 
				(cm.getScadenziere() == colloquio.getScadenziere()) &&
				(cm.getiCalUid().equalsIgnoreCase(colloquio.getiCalUid())) &&
				(cm.getLuogoIncontro().equalsIgnoreCase(colloquio.getLuogoIncontro()))){
				
				returnValue.add(cm);
				
			}
			
		}

		return returnValue;
	}

	/**
	 * Controlla la presenza del colloquio passato in input nell'archivio del programma.
	 * Per la ricerca viene controllata la corrispondenza con il seguente criterio :
	 * 
	 * data colloquio AND data inserimento AND scadenziere AND luogo incontro AND commento agenzia AND
	 * commento cliente AND descrizione
	 * 
	 * Utilizzare il metodo getAnagraficheExist della classe winkhouse.export.helpers.AnagraficheHelper 
	 * per individuare una lista di anagrafiche da passare al metodo.
	 *  
	 * @param colloquio
	 * @return ArrayList
	 */
	public ArrayList<ColloquiModel> getColloquiExist(ColloquiVO colloquio){
		
		ArrayList<ColloquiModel> returnValue = new ArrayList<ColloquiModel>();
		ColloquiDAO cdao = new ColloquiDAO();
		
		returnValue = cdao.getColloquiByProperties(ColloquiModel.class.getName(), colloquio);
		return returnValue;
	}	
	
	/**
	 * Controlla la presenza del colloquio passato in input nell'archivio del programma.
	 * Per la ricerca viene controllata la corrispondenza con il seguente criterio :
	 * 
	 * data colloquio AND data inserimento AND scadenziere AND iCalUid AND luogo incontro
	 * 
	 * Se il colloquio viene trovato si procede alla ricerca di un oggetto di tipo ColloquiAgentiVO
	 * con il codice colloquio dell'oggetto trovato con il metodo descritto precedentemente e con codice
	 * agente uguale al codice agente del parametro di input agente. 
	 * Utilizzare il metodo findAgentiByDescription della classe winkhouse.export.helpers.UtilsHelper 
	 * per individuare una lista di anagrafiche da passare al metodo.
	 *  
	 * @param colloquio
	 * @param agente
	 * @return ArrayList<ColloquiAgentiVO>
	 */
	public ArrayList<ColloquiAgentiVO> getColloquioAgenteExist(ColloquiVO colloquio,AgentiVO agente){
		
		ArrayList<ColloquiAgentiVO> returnValue = new ArrayList<ColloquiAgentiVO>();
		ColloquiDAO cDAO = new ColloquiDAO();
		ColloquiAgentiDAO caDAO = new ColloquiAgentiDAO(); 
		
		ArrayList alColloqui = cDAO.listColloquiByAgenteDaAInNotScadenziere(ColloquiModel.class.getName(),
									 									  agente.getCodAgente(),
									 									  colloquio.getDataColloquio(),
									 									  colloquio.getDataColloquio());
		
		Iterator<ColloquiModel> it = alColloqui.iterator();
		
		while (it.hasNext()){
			
			ColloquiModel cm = it.next();
			Calendar c1 = Calendar.getInstance(Locale.ITALIAN);
			c1.setTime(cm.getDataColloquio());
			
			Calendar c2 = Calendar.getInstance(Locale.ITALIAN);
			c2.setTime(colloquio.getDataColloquio());
			
			if (
				(
						(c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) &&
						(c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) &&
						(c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) &&
						(c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY)) &&
						(c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE)) &&
						(c1.get(Calendar.SECOND) == c2.get(Calendar.SECOND))						
				) && 
				(cm.getScadenziere() == colloquio.getScadenziere()) &&
				(cm.getiCalUid().equalsIgnoreCase(colloquio.getiCalUid())) &&
				(cm.getLuogoIncontro().equalsIgnoreCase(colloquio.getLuogoIncontro()))){
				
				ColloquiAgentiVO caVO = (ColloquiAgentiVO)caDAO.getColloquiAgentiByAgenteColloquio(ColloquiAgentiVO.class.getName(), 
														 										   agente.getCodAgente(), 
														 										   cm.getCodColloquio());
				returnValue.add(caVO);
				
			}
			
		}

		return returnValue;
	}

	/**
	 * Controlla la presenza del colloquio passato in input nell'archivio del programma.
	 * Per la ricerca viene controllata la corrispondenza con il seguente criterio :
	 * 
	 * data colloquio AND data inserimento AND scadenziere AND iCalUid AND luogo incontro
	 * 
	 * Se il colloquio viene trovato si procede alla ricerca di un oggetto di tipo ColloquiAnagraficheVO
	 * con il codice colloquio dell'oggetto trovato con il metodo descritto precedentemente e con codice
	 * anagrafica uguale al codice anagrafica del parametro di input anagrafica. 
	 * Utilizzare il metodo AnagraficheExist della classe winkhouse.export.helpers.AnagraficheHelper 
	 * per individuare una lista di anagrafiche da passare al metodo.
	 *  
	 * @param colloquio
	 * @param anagrafica
	 * @return ArrayList<ColloquiAnagraficheVO>
	 */
	public ArrayList<ColloquiAnagraficheVO> getColloquioAnagraficheExist(ColloquiVO colloquio,AnagraficheVO anagrafica){
		
		ArrayList<ColloquiAnagraficheVO> returnValue = new ArrayList<ColloquiAnagraficheVO>();
		ColloquiDAO cDAO = new ColloquiDAO();
		ColloquiAnagraficheDAO caDAO = new ColloquiAnagraficheDAO(); 
		
		ArrayList alColloqui = cDAO.listColloquiByAnagraficheDaAInNotScadenziere(ColloquiModel.class.getName(),
																		  	   	 anagrafica.getCodAnagrafica(),
																		  	   	 colloquio.getDataColloquio(),
																		  	   	 colloquio.getDataColloquio());
		
		Iterator<ColloquiModel> it = alColloqui.iterator();
		
		while (it.hasNext()){
			
			ColloquiModel cm = it.next();
			Calendar c1 = Calendar.getInstance(Locale.ITALIAN);
			c1.setTime(cm.getDataColloquio());
			
			Calendar c2 = Calendar.getInstance(Locale.ITALIAN);
			c2.setTime(colloquio.getDataColloquio());
			
			if (
				(
						(c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) &&
						(c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) &&
						(c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) &&
						(c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY)) &&
						(c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE)) &&
						(c1.get(Calendar.SECOND) == c2.get(Calendar.SECOND))						
				) && 
				(cm.getScadenziere() == colloquio.getScadenziere()) &&
				(cm.getiCalUid().equalsIgnoreCase(colloquio.getiCalUid())) &&
				(cm.getLuogoIncontro().equalsIgnoreCase(colloquio.getLuogoIncontro()))){
				
				ColloquiAnagraficheVO caVO = (ColloquiAnagraficheVO)caDAO.getColloquiAnagraficheByAnagraficaColloquio(ColloquiAnagraficheVO.class.getName(), 
														 										   					  anagrafica.getCodAnagrafica(), 
														 										   					  cm.getCodColloquio());
				returnValue.add(caVO);
				
			}
			
		}

		return returnValue;
	}

	/**
	 * Controlla la presenza degli allegati di un colloquio in base al codice colloquio passato in input
	 * @param codColloquio
	 * @return ArrayList<AllegatiColloquiVO>
	 */
	public ArrayList<AllegatiColloquiVO> getColloquioAllegatiByCodColloquio(Integer codColloquio){
		
		AllegatiColloquiDAO acDAO = new AllegatiColloquiDAO();
		return acDAO.getAllegatiByColloquio(AllegatiColloquiVO.class.getName(), codColloquio);
		
	}
	 	
	/**
	 * Inserisce un record nella tabella colloqui ritornando true o false.
	 * Se il campo codColloquio, del parametro colloquio, è zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param colloquio
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateColloquio(ColloquiVO colloquio) throws SQLException{		
		
		ColloquiDAO cDAO = new ColloquiDAO();
		return cDAO.saveUpdateWithException(colloquio, null, true);
		
	}

	/**
	 * Inserisce un record nella tabella colloquiagenti ritornando true o false.
	 * Se il campo codColloquioAgenti, del parametro colloquiagenti, è zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param colloquiagenti
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateColloquiAgenti(ColloquiAgentiVO colloquiagenti) throws SQLException{		
		
		ColloquiAgentiDAO caDAO = new ColloquiAgentiDAO();
		return caDAO.saveUpdateWithException(colloquiagenti, null, true);
		
	}
		
	/**
	 * Inserisce un record nella tabella colloquiagenti ritornando true o false.
	 * Se il campo codColloquioAnagrafica, del parametro colloquianagrafiche, è zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * 
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param colloquianagrafiche
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateColloquiAnagrafiche(ColloquiAnagraficheVO colloquianagrafiche) throws SQLException{		
		
		ColloquiAnagraficheDAO caDAO = new ColloquiAnagraficheDAO();
		return caDAO.saveUpdateWithException(colloquianagrafiche, null, true);
		
	}

	/**
	 * Inserisce un record nella tabella allegaticolloqui ritornando true o false.
	 * Se il campo codAllegatiColloqui, del parametro allegatiColloqui, è zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * 
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param allegatiColloqui
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateColloquiAllegati(AllegatiColloquiVO allegatiColloqui) throws SQLException{		
		
		AllegatiColloquiDAO acDAO = new AllegatiColloquiDAO();
		return acDAO.saveUpdateWithException(allegatiColloqui, null, true);
		
	}
	
	
	/**
	 * Inserisce un record nella tabella colloquicriteriricerca ritornando true o false.
	 * Se il campo codColloquioCriterioRicerca, del parametro colloquiCriteriRicerca, è zero viene inserito un nuovo record, altrimenti esegue una operazione
	 * 
	 * SQL di update.
	 * Se durante l'operazione avviene un errore viene ritornata una eccezione di tipo java.sql.SQLException 
	 * 
	 * @param colloquicriteriricerca
	 * @return true|false o l'eccezione java.sql.SQLException
	*/
	public boolean saveUpdateColloquiCriteriRicerca(ColloquiCriteriRicercaVO colloquiCriteriRicerca) throws SQLException{		
		
		ColloquiCriteriRicercaDAO ccrDAO = new ColloquiCriteriRicercaDAO();
		return ccrDAO.saveUpdateWithException(colloquiCriteriRicerca, null, true);
		
	}

}
