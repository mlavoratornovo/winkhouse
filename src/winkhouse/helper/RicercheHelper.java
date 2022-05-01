package winkhouse.helper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;

import winkhouse.Activator;
import winkhouse.dao.ColloquiCriteriRicercaDAO;
import winkhouse.dao.PermessiDAO;
import winkhouse.dao.RicercheDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.model.RicercheModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.vo.RicercaVO;
import winkhouse.vo.RicercheVO;
import winkhouse.wizard.RicercaWizard;



public class RicercheHelper {

	public RicercheHelper() {
		
	}
	
	public Boolean saveUpdateRicerca(RicercheModel ricerca){
		Boolean returnValue = true;
		RicercheDAO rDAO = new RicercheDAO();
		Connection con = ConnectionManager.getInstance()
										  .getConnection();
		if (rDAO.saveUpdate(ricerca, con, false)){
			if (updateListaCriteriRicerca(ricerca, con)){
				try {
					con.commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else{
				try {
					returnValue = false;
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}else{
			try {
				returnValue = false;
				con.rollback();				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return returnValue;
	}

	public Boolean deleteRicerca(RicercheModel ricerca,int wiztype){
		
		Boolean returnValue = true;
		
		ColloquiCriteriRicercaDAO ccrDAO = new ColloquiCriteriRicercaDAO();
		RicercheDAO rDAO = new RicercheDAO();
		PermessiDAO pDAO = new PermessiDAO();
		
		Connection con = ConnectionManager.getInstance()
										  .getConnection();
		
		boolean criteriResult = false;
		criteriResult = ccrDAO.deleteByRicerca(ricerca.getCodRicerca(), con, false);
		
		boolean ricercaResult = false;
		if (wiztype != RicercaWizard.PERMESSI){
			ricercaResult = rDAO.delete(ricerca.getCodRicerca(), con, false);
		}
		if (wiztype == RicercaWizard.PERMESSI){
				ricercaResult = rDAO.delete(ricerca.getCodRicerca(), con, false);
				ricercaResult = pDAO.deletePermessoByCodRicerca(ricerca.getCodRicerca(), con, false);
			}
		
		if (criteriResult){
			if (ricercaResult){
				try {
					con.commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else{
				try {
					returnValue = false;
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}else{
			try {
				returnValue = false;
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return returnValue;
	}
	
	public Boolean updateListaCriteriRicerca(RicercheModel ricerche,
									 	     Connection con){

		Boolean returnValue = true;

		ArrayList colloquiCriteriRicerca = new ColloquiCriteriRicercaDAO().getColloquiCriteriRicercaByRicerca(ColloquiCriteriRicercaVO.class.getName(),
																											  ricerche.getCodRicerca()); 
		Collections.sort(colloquiCriteriRicerca,comparerColloquiCriteriRicerca);
		ArrayList listaColloquiCriteriRicerca = ricerche.getCriteri();
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
					if (colloquioCriteriRicerca.getCodColloquio() == 0){
						colloquioCriteriRicerca.setCodColloquio(null);
					}
					colloquioCriteriRicerca.setCodRicerca(ricerche.getCodRicerca());
					if (!ccrDAO.saveUpdate(colloquioCriteriRicerca, con, false)){					
						MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
												"Errore salvataggio criterio ricerca", 
												"Si è verificato un errore nel salvataggio del criterio ricerca : " + 
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
											"Si è verificato un errore nella cancellazione del criterio ricerca : " + 
											colloquioCriteriRicerca.getCodCriterioRicerca());
					returnValue = false;

				}
			}
		}

		return returnValue;
	}

	public RicercheModel saveNewRicercaFromWizardRicerca(RicercaVO ricercaObjWizard){
		
		RicercheModel rm = new RicercheModel();
		
		if (WinkhouseUtils.getInstance().getLoggedAgent() != null){
			rm.setCodUserUpdate(WinkhouseUtils.getInstance().getLoggedAgent().getCodAgente());	
		}
		
		rm.setDateUpdate(new Date());
		
		String tipo = "";
		
		if (ricercaObjWizard.getType() == RicercheVO.RICERCHE_IMMOBILI){
			rm.setTipo(RicercheVO.PERMESSI_IMMOBILI);
			tipo = "immobili";
			rm.setCriteri(ricercaObjWizard.getCriteriImmobili());
		}
		if (ricercaObjWizard.getType() == RicercheVO.RICERCHE_ANAGRAFICHE){
			rm.setTipo(RicercheVO.PERMESSI_ANAGRAFICHE);
			tipo = "anagrafiche";
			rm.setCriteri(ricercaObjWizard.getCriteriAnagrafiche());			
		}
		if (ricercaObjWizard.getType() == RicercheVO.RICERCHE_IMMOBILI_AFFITTI){
			rm.setTipo(RicercheVO.PERMESSI_IMMOBILI_AFFITTI);
			tipo = "affitti";
			rm.setCriteri(ricercaObjWizard.getCriteriImmobiliAffitti());			
		}
				
		rm.setNome("Ricerca permessi " + tipo);
		if (saveUpdateRicerca(rm)){
			return rm;
		}else{
			return null;
		}
		 

	} 
	
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
	

}
