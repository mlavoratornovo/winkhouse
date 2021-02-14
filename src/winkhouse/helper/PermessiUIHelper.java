package winkhouse.helper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;

import winkhouse.Activator;
import winkhouse.dao.PermessiDAO;
import winkhouse.dao.PermessiUIDAO;
import winkhouse.model.AgentiModel;
import winkhouse.model.PermessiModel;
import winkhouse.model.PermessiUIModel;
import winkhouse.util.WinkhouseUtils;

public class PermessiUIHelper {

	private Comparator<PermessiUIModel> comparer = new Comparator<PermessiUIModel>(){

		@Override
		public int compare(PermessiUIModel arg0, PermessiUIModel arg1) {
			int returnValue = 0;
			if ((arg0.getCodPermessoUi()!=null) && (arg1.getCodPermessoUi()!=null)){				
				if ((arg0.getCodPermessoUi().intValue() == arg1.getCodPermessoUi().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodPermessoUi().intValue() < arg1.getCodPermessoUi().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodPermessoUi().intValue() > arg1.getCodPermessoUi().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodPermessoUi()!=null) && (arg1.getCodPermessoUi()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};

	private Comparator<PermessiModel> comparerP = new Comparator<PermessiModel>(){

		@Override
		public int compare(PermessiModel arg0, PermessiModel arg1) {
			int returnValue = 0;
			if ((arg0.getCodPermesso()!=null) && (arg1.getCodPermesso()!=null)){				
				if ((arg0.getCodPermesso().intValue() == arg1.getCodPermesso().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodPermesso().intValue() < arg1.getCodPermesso().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodPermesso().intValue() > arg1.getCodPermesso().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodPermesso()!=null) && (arg1.getCodPermesso()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};
	
	public Boolean updateListaPermessiAgente(AgentiModel agente,Connection con,Boolean doCommit){
		
		Boolean returnValue = true;
		
		doCommit = (doCommit == null)?true:doCommit;
		
		
		ArrayList permessiUIDB = new PermessiUIDAO().getPermessiByAgente(PermessiUIModel.class.getName(), 
			  	 														 agente.getCodAgente());
		
		Collections.sort(permessiUIDB,comparer);
		ArrayList listaPermessiUI = agente.getPermessiUIPerspectiveModels();
		listaPermessiUI.addAll(agente.getPermessiUIViewModels());
		listaPermessiUI.addAll(agente.getPermessiUIDialogModels());
		
		if (listaPermessiUI != null){
			
			PermessiUIDAO pDAO = new PermessiUIDAO();			
			Iterator it  = listaPermessiUI.iterator();
			
			while (it.hasNext()){
				Object o = it.next();
				if (o != null){
					PermessiUIModel permesso = (PermessiUIModel)o;
					int index = Collections.binarySearch(permessiUIDB, permesso,comparer);
					if (index >= 0){
						if (permesso.isSelected()){
							permessiUIDB.remove(index);
						}
						Collections.sort(permessiUIDB,comparer);
					}
					permesso.setCodAgente(agente.getCodAgente());
					permesso.setDateUpdate(new Date());
					if (WinkhouseUtils.getInstance().getLoggedAgent() != null){
						permesso.setCodUserUpdate(WinkhouseUtils.getInstance().getLoggedAgent().getCodAgente());
					}
					if ((permesso != null) && (permesso.isSelected())){
						if (!pDAO.saveUpdate(permesso, con, doCommit)){					
								MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
														"Errore salvataggio permesso", 
														"Si è verificato un errore nel salvataggio del permesso : " + 
														permesso.getCodPermessoUi());
								returnValue = false;
						}
					}
				}
					
			}
			
			Iterator ite = permessiUIDB.iterator();
			while (ite.hasNext()){
				PermessiUIModel pVO = (PermessiUIModel)ite.next();
				if (!pDAO.deletePermesso(pVO.getCodPermessoUi(), con, doCommit)){
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore salvataggio permesso", 
											"Si è verificato un errore nel salvataggio del permesso : " + 
											pVO.getCodPermessoUi());
					returnValue = false;
					
				}
			}
		}

		ArrayList permessiDB = new PermessiDAO().getPermessiByAgente(PermessiModel.class.getName(), 
			  	 													 agente.getCodAgente());
		
		Collections.sort(permessiDB,comparerP);
		ArrayList listaPermessi = agente.getPermessiModels();
		
		if (listaPermessi != null){
			
			PermessiDAO pDAO = new PermessiDAO();			
			Iterator it  = listaPermessi.iterator();
			
			while (it.hasNext()){
				Object o = it.next();
				if (o != null){
					PermessiModel permesso = (PermessiModel)o;
					int index = Collections.binarySearch(permessiDB, permesso,comparerP);
					if (index >= 0){
						permessiDB.remove(index);
						Collections.sort(permessiDB,comparerP);
					}
					permesso.setCodAgente(agente.getCodAgente());
					permesso.setDateUpdate(new Date());
					if (WinkhouseUtils.getInstance().getLoggedAgent() != null){
						permesso.setCodUserUpdate(WinkhouseUtils.getInstance().getLoggedAgent().getCodAgente());
					}
				
					if (permesso != null){
						if (!pDAO.saveUpdate(permesso, con, doCommit)){					
								MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
														"Errore salvataggio permesso", 
														"Si è verificato un errore nel salvataggio del permesso : " + 
														permesso.getCodPermesso());
								returnValue = false;
						}
					}
				}
					
			}
			
			Iterator ite = permessiDB.iterator();
			while (ite.hasNext()){
				PermessiModel pVO = (PermessiModel)ite.next();
				if (!pDAO.deletePermesso(pVO.getCodPermesso(), con, doCommit)){
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore salvataggio permesso", 
											"Si è verificato un errore nel salvataggio del permesso : " + 
											pVO.getCodPermesso());
					returnValue = false;
					
				}
			}
		}
		
		return returnValue;
	}

	
}
