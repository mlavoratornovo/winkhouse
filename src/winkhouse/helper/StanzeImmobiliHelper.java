package winkhouse.helper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.StanzeDAO;
import winkhouse.model.ImmobiliModel;
import winkhouse.model.StanzeImmobiliModel;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.vo.StanzeImmobiliVO;



public class StanzeImmobiliHelper {

	public StanzeImmobiliHelper() {
	}

	private Comparator<StanzeImmobiliVO> comparer = new Comparator<StanzeImmobiliVO>(){

		@Override
		public int compare(StanzeImmobiliVO arg0, StanzeImmobiliVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodStanzeImmobili()!=null) && (arg1.getCodStanzeImmobili()!=null)){				
				if ((arg0.getCodStanzeImmobili().intValue() == arg1.getCodStanzeImmobili().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodStanzeImmobili().intValue() < arg1.getCodStanzeImmobili().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodStanzeImmobili().intValue() > arg1.getCodStanzeImmobili().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodStanzeImmobili()!=null) && (arg1.getCodStanzeImmobili()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};	
	
	public StanzeImmobiliVO checkStanzeImmobiliDB(StanzeImmobiliModel stanzeImmobiliVO){
		if (stanzeImmobiliVO.getCodStanzeImmobili() != null){
			if (new StanzeDAO().getStanzaById(StanzeImmobiliVO.class.getName(), 
					  						  stanzeImmobiliVO.getCodStanzeImmobili()) == null){
				MessageDialog.openError(PlatformUI.getWorkbench()
						     					  .getActiveWorkbenchWindow()
						     					  .getShell(),
						     			"ERRORE", 
										"La stanza che si sta cercando di aggiornare è stata cancellata da un altro utente");
				stanzeImmobiliVO = null;
			
/*			if (new StanzeDAO().getStanzaById(StanzeImmobiliVO.class.getName(), 
											  stanzeImmobiliVO.getCodStanzeImmobili()) == null){
				if (MessageDialog.openConfirm(PlatformUI.getWorkbench()
														.getActiveWorkbenchWindow()
														.getShell(), 
											  "ATTENZIONE", 
											  "Si sta cercando di aggiornare la stanza " + stanzeImmobiliVO.getTipologia().getDescrizione() + " già cancellato da un altro utente. \n" +
											  "Se si prosegue con l'operazione la stanza verrà inserito nella base dati." +
			      							  "Procedere con l'operazione ?")){
					stanzeImmobiliVO.setCodStanzeImmobili(0);
				}else{
					stanzeImmobiliVO = null;
				}	
			*/	
			}
		}
		return stanzeImmobiliVO;
	}

	
	public Boolean updateListaStanze(ImmobiliModel immobile,
									 Connection con,
									 Boolean doCommit){
		
		Boolean returnValue = true;
		
		doCommit = (doCommit == null)?true:doCommit;
		StanzeDAO sDAO = new StanzeDAO();
		ArrayList stanzeDB = sDAO.listByImmobile(StanzeImmobiliModel.class.getName(), immobile.getCodImmobile());
		
		Collections.sort(stanzeDB,comparer);
		ArrayList listaStanze = immobile.getStanze();
		if (listaStanze != null){
						
			Iterator it  = listaStanze.iterator();
			
			while (it.hasNext()){
				
				Object o = it.next();
				if (o != null){
					StanzeImmobiliModel stanza = (StanzeImmobiliModel)o;
					int index = Collections.binarySearch(stanzeDB, stanza,comparer);
					if (index >= 0){
						stanzeDB.remove(index);
						Collections.sort(stanzeDB,comparer);
					}
					stanza.setCodImmobile(immobile.getCodImmobile());
	//				checkStanzeImmobiliDB(stanza);
					if (stanza != null){
						if (!sDAO.saveUpdate(stanza, con, doCommit)){
								
								MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
														"Errore salvataggio stanza", 
														"Si è verificato un errore nel salvataggio della stanza : " + 
														((stanza.getTipologia()!= null)?"":stanza.getTipologia().getDescrizione()));
								returnValue = false;
						}					
					}
				}
			}
			
			Iterator ite = stanzeDB.iterator();			
			while (ite.hasNext()){
				StanzeImmobiliModel siVO = (StanzeImmobiliModel)ite.next();				
				if (!sDAO.delete(siVO.getCodStanzeImmobili(), con, doCommit)){					
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
							"Errore cancellazione stanza", 
							"Si è verificato un errore nella cancellazione della stanza: " + 
							((siVO.getTipologia()!= null)?"":siVO.getTipologia().getDescrizione()));
					returnValue = false;
					
				}
			}
		}
		MobiliaDatiBaseCache.getInstance().setTipologieStanze(null);
		return returnValue;
	}	
}
