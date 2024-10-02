package winkhouse.helper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.AgentiAppuntamentiDAO;
import winkhouse.dao.AnagraficheAppuntamentiDAO;
import winkhouse.dao.AppuntamentiDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.model.AgentiAppuntamentiModel;
import winkhouse.model.AnagraficheAppuntamentiModel;
import winkhouse.model.AppuntamentiModel;
import winkhouse.orm.Appuntamenti;
import winkhouse.vo.AgentiAppuntamentiVO;
import winkhouse.vo.AnagraficheAppuntamentiVO;



public class AppuntamentiHelper {

	public AppuntamentiHelper() {}
	
	public boolean saveAppuntamento(AppuntamentiModel appuntamento){
		
		boolean returnValue = true;
		
		AppuntamentiDAO appuntamentiDAO = new AppuntamentiDAO();
		AgentiAppuntamentiDAO agaDAO = new AgentiAppuntamentiDAO();
		AnagraficheAppuntamentiDAO anaDAO = new AnagraficheAppuntamentiDAO();
		
		if (appuntamento != null){			
			Connection con = ConnectionManager.getInstance().getConnection();
			
			if (appuntamentiDAO.saveUpdate(appuntamento, con, false)){
				if (updateListaAgenti(appuntamento, con)){
					if (updateListaAnagrafiche(appuntamento, con)){
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
				
			}else{
				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
										"Errore salvataggio appuntamento", 
										"Si � verificato un errore nel salvataggio dell'appuntamento ");
				returnValue = false;
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			
		}		
		
		return returnValue;
		
	}
	
	private Comparator<AgentiAppuntamentiVO> comparerAgentiAppuntamenti = new Comparator<AgentiAppuntamentiVO>(){

		@Override
		public int compare(AgentiAppuntamentiVO arg0, AgentiAppuntamentiVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodAgentiAppuntamenti()!=null) && (arg1.getCodAgentiAppuntamenti()!=null)){				
				if ((arg0.getCodAgentiAppuntamenti().intValue() == arg1.getCodAgentiAppuntamenti().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodAgentiAppuntamenti().intValue() < arg1.getCodAgentiAppuntamenti().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodAgentiAppuntamenti().intValue() > arg1.getCodAgentiAppuntamenti().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodAgentiAppuntamenti()!=null) && (arg1.getCodAgentiAppuntamenti()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};		

	public Boolean updateListaAgenti(AppuntamentiModel appuntamento,
			 						 Connection con){

		Boolean returnValue = true;

		ArrayList agentiAppuntamentiDB = new AgentiAppuntamentiDAO().listAgentiAppuntamentiByAppuntamento(AgentiAppuntamentiModel.class.getName(),
																									      appuntamento.getCodAppuntamento()); 
		Collections.sort(agentiAppuntamentiDB,comparerAgentiAppuntamenti);
		ArrayList listaAgentiAppuntamento = appuntamento.getAgenti();
		if (listaAgentiAppuntamento != null){
	
			AgentiAppuntamentiDAO aaDAO = new AgentiAppuntamentiDAO();			
			Iterator it  = listaAgentiAppuntamento.iterator();
	
			while (it.hasNext()){
				AgentiAppuntamentiVO agentiAppuntamentiVO = (AgentiAppuntamentiVO)it.next();
				int index = Collections.binarySearch(agentiAppuntamentiDB, agentiAppuntamentiVO,comparerAgentiAppuntamenti);
				if (index >= 0){
					agentiAppuntamentiDB.remove(index);
					Collections.sort(agentiAppuntamentiDB,comparerAgentiAppuntamenti);
				}
				agentiAppuntamentiVO.setCodAppuntamento(appuntamento.getCodAppuntamento());
				if (!aaDAO.saveUpdate(agentiAppuntamentiVO, con, false)){					
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore salvataggio appuntamento agenti", 
											"Si � verificato un errore nel salvataggio dell'associazione appuntamento agente : " + 
											agentiAppuntamentiVO.getCodAgentiAppuntamenti());
					returnValue = false;
				}					
	
			}
	
			Iterator ite = agentiAppuntamentiDB.iterator();
			while (ite.hasNext()){
				AgentiAppuntamentiVO agentiAppuntamentiVO = (AgentiAppuntamentiVO)ite.next();
				if (!aaDAO.deleteAgentiAppuntamenti(agentiAppuntamentiVO.getCodAgentiAppuntamenti(), con, false)){
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore cancellazione appuntamento agenti", 
											"Si � verificato un errore nella cancellazione dell'associazione appuntamento agente : " + 
											agentiAppuntamentiVO.getCodAgentiAppuntamenti());
					returnValue = false;
	
				}
			}
		}
	
		return returnValue;
	}

	private Comparator<AnagraficheAppuntamentiVO> comparerAnagraficheAppuntamenti = new Comparator<AnagraficheAppuntamentiVO>(){

		@Override
		public int compare(AnagraficheAppuntamentiVO arg0, AnagraficheAppuntamentiVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodAnagraficheAppuntamenti()!=null) && (arg1.getCodAnagraficheAppuntamenti()!=null)){				
				if ((arg0.getCodAnagraficheAppuntamenti().intValue() == arg1.getCodAnagraficheAppuntamenti().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodAnagraficheAppuntamenti().intValue() < arg1.getCodAnagraficheAppuntamenti().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodAnagraficheAppuntamenti().intValue() > arg1.getCodAnagraficheAppuntamenti().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodAnagraficheAppuntamenti()!=null) && (arg1.getCodAnagraficheAppuntamenti()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};		
	
	public Boolean updateListaAnagrafiche(AppuntamentiModel appuntamento,
			 						 	  Connection con){

		Boolean returnValue = true;

		ArrayList anagraficheAppuntamentiDB = new AnagraficheAppuntamentiDAO().listAnagraficheAppuntamentiByAppuntamento(AnagraficheAppuntamentiModel.class.getName(),
																									      				 appuntamento.getCodAppuntamento()); 
		Collections.sort(anagraficheAppuntamentiDB,comparerAnagraficheAppuntamenti);
		ArrayList listaAnagraficheAppuntamento = appuntamento.getAnagrafiche();
		if (listaAnagraficheAppuntamento != null){
	
			AnagraficheAppuntamentiDAO aaDAO = new AnagraficheAppuntamentiDAO();			
			Iterator it  = listaAnagraficheAppuntamento.iterator();
	
			while (it.hasNext()){
				AnagraficheAppuntamentiVO anagraficheAppuntamentiVO = (AnagraficheAppuntamentiVO)it.next();
				int index = Collections.binarySearch(anagraficheAppuntamentiDB, anagraficheAppuntamentiVO,comparerAnagraficheAppuntamenti);
				if (index >= 0){
					anagraficheAppuntamentiDB.remove(index);
					Collections.sort(anagraficheAppuntamentiDB,comparerAnagraficheAppuntamenti);
				}
				anagraficheAppuntamentiVO.setCodAppuntamento(appuntamento.getCodAppuntamento());
				if (!aaDAO.saveUpdate(anagraficheAppuntamentiVO, con, false)){					
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore salvataggio appuntamento anagrafica", 
											"Si � verificato un errore nel salvataggio dell'associazione appuntamento anagrafica : " + 
											anagraficheAppuntamentiVO.getCodAnagraficheAppuntamenti());
					returnValue = false;
				}					
	
			}
	
			Iterator ite = anagraficheAppuntamentiDB.iterator();
			while (ite.hasNext()){
				AnagraficheAppuntamentiVO anagraficheAppuntamentiVO = (AnagraficheAppuntamentiVO)ite.next();
				if (!aaDAO.deleteAnagraficheAppuntamenti(anagraficheAppuntamentiVO.getCodAnagraficheAppuntamenti(), con, false)){
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore cancellazione appuntamento anagrafica", 
											"Si � verificato un errore nella cancellazione dell'associazione appuntamento anagrafica : " + 
											anagraficheAppuntamentiVO.getCodAnagraficheAppuntamenti());
					returnValue = false;
	
				}
			}
		}
	
		return returnValue;
	}
	
	public boolean deleteAppuntamento(Appuntamenti appuntamento,Connection connection){
		
		boolean returnValue = true;
		Connection con = (connection == null)
						  ? ConnectionManager.getInstance().getConnection()
						  : connection;
		
		
		AnagraficheAppuntamentiDAO anaDAO = new AnagraficheAppuntamentiDAO();
		if (anaDAO.deleteAnagraficheAppuntamentiByAppuntamento(appuntamento.getCodAppuntamento(), con, false)){
			AgentiAppuntamentiDAO aaDAO = new AgentiAppuntamentiDAO();
			if (aaDAO.deleteAgentiAppuntamentiByAppuntamento(appuntamento.getCodAppuntamento(), con, false)){
				AppuntamentiDAO aDAO = new AppuntamentiDAO();
				if (aDAO.delete(appuntamento.getCodAppuntamento(), con, false)){
					try {
						if (connection == null){
							con.commit();
						}
					} catch (SQLException e) {						
						e.printStackTrace();
					}
				}else{
					returnValue = false;
					try {
						con.rollback();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}else{
				returnValue = false;
				try {
					if (connection == null){
						con.rollback();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			}
		}else{
			returnValue = false;
			if (connection == null){
				MessageDialog.openError(PlatformUI.getWorkbench()
						 						  .getActiveWorkbenchWindow().getShell(),
										"Errore cancellazione appuntamento", 
										"Si è verificato un errore nella cancellazione dell'appuntamento ");
			}
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
	
}