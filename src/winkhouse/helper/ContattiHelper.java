package winkhouse.helper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;

import winkhouse.Activator;
import winkhouse.dao.ContattiDAO;
import winkhouse.model.AgentiModel;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ContattiModel;
import winkhouse.vo.ContattiVO;



public class ContattiHelper {

	public ContattiHelper() {
	}
	private Comparator<ContattiVO> comparer = new Comparator<ContattiVO>(){

		@Override
		public int compare(ContattiVO arg0, ContattiVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodContatto()!=null) && (arg1.getCodContatto()!=null)){				
				if ((arg0.getCodContatto().intValue() == arg1.getCodContatto().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodContatto().intValue() < arg1.getCodContatto().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodContatto().intValue() > arg1.getCodContatto().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodContatto()!=null) && (arg1.getCodContatto()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};
	
	public Boolean updateListaContatti(AnagraficheModel anagrafica,
									   Connection con,
									   Boolean doCommit){
		
		Boolean returnValue = true;
		
		doCommit = (doCommit == null)?true:doCommit;
		
		ArrayList contattiDB = new ContattiDAO().listByAnagrafica(ContattiVO.class.getName(), 
																  anagrafica.getCodAnagrafica());
		
		Collections.sort(contattiDB,comparer);
		ArrayList listaContatti = anagrafica.getContatti();
		
		if (listaContatti != null){
			
			ContattiDAO cDAO = new ContattiDAO();			
			Iterator it  = listaContatti.iterator();
			
			while (it.hasNext()){
				Object o = it.next();
				if (o != null){
					ContattiModel contatto = (ContattiModel)o;
					int index = Collections.binarySearch(contattiDB, contatto,comparer);
					if (index >= 0){
						contattiDB.remove(index);
						Collections.sort(contattiDB,comparer);
					}
					contatto.setCodAnagrafica(anagrafica.getCodAnagrafica());				
					if (contatto != null){
						if (!cDAO.saveUpdate(contatto, con, doCommit)){					
								MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
														"Errore salvataggio contatto", 
														"Si è verificato un errore nel salvataggio del contatto : " + 
														contatto.getContatto());
								returnValue = false;
						}
					}
				}
					
			}
			
			Iterator ite = contattiDB.iterator();
			while (ite.hasNext()){
				ContattiVO cVO = (ContattiVO)ite.next();
				if (!cDAO.delete(cVO.getCodContatto(), con, doCommit)){
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
							"Errore cancellazione contatto", 
							"Si è verificato un errore nella cancellazione del contatto : " + 
							cVO.getContatto());
					returnValue = false;
					
				}
			}
		}
		
		return returnValue;
	}
	
	
	
	public Boolean updateListaContatti(AgentiModel agente,
									   Connection con,
									   Boolean doCommit){
		
		Boolean returnValue = true;
		
		doCommit = (doCommit == null)?true:doCommit;
		
		ArrayList contattiDB = new ContattiDAO().listByAgente(ContattiVO.class.getName(), 
															  agente.getCodAgente());
		
		Collections.sort(contattiDB,comparer);
		ArrayList listaContatti = agente.getContatti();
		if (listaContatti != null){
			
			ContattiDAO cDAO = new ContattiDAO();	
			GDataHelper gdH = new GDataHelper();
			
			Iterator it  = listaContatti.iterator();
			
			while (it.hasNext()){
				ContattiVO contatto = (ContattiVO)it.next();
				int index = Collections.binarySearch(contattiDB, contatto,comparer);
				if (index >= 0){
					contattiDB.remove(index);
					Collections.sort(contattiDB,comparer);
				}
				contatto.setCodAgente(agente.getCodAgente());
				if (!cDAO.saveUpdate(contatto, con, doCommit)){					
						MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
												"Errore salvataggio contatto", 
												"Si è verificato un errore nel salvataggio del contatto : " + 
												contatto.getContatto());
						returnValue = false;
				}					
				
			}
			
			Iterator ite = contattiDB.iterator();
			while (ite.hasNext()){
				ContattiVO cVO = (ContattiVO)ite.next();
				if (!cDAO.delete(cVO.getCodContatto(), con, doCommit)){
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
							"Errore cancellazione contatto", 
							"Si è verificato un errore nella cancellazione del contatto : " + 
							cVO.getContatto());
					returnValue = false;
					
				}else{
					returnValue = gdH.deleteGDataByContatto(cVO.getCodContatto(), con, doCommit);
				}
			}
		}
		
		return returnValue;
	}
}
