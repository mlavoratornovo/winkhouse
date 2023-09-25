package winkhouse.helper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.DatiCatastaliDAO;
import winkhouse.model.ImmobiliModel;
import winkhouse.vo.DatiCatastaliVO;




public class DatiCatastaliImmobiliHelper {

	public DatiCatastaliImmobiliHelper() {

	}

	private Comparator<DatiCatastaliVO> comparer = new Comparator<DatiCatastaliVO>(){

		@Override
		public int compare(DatiCatastaliVO arg0, DatiCatastaliVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodDatiCatastali()!=null) && (arg1.getCodDatiCatastali()!=null)){				
				if ((arg0.getCodDatiCatastali().intValue() == arg1.getCodDatiCatastali().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodDatiCatastali().intValue() < arg1.getCodDatiCatastali().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodDatiCatastali().intValue() > arg1.getCodDatiCatastali().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodDatiCatastali()!=null) && (arg1.getCodDatiCatastali()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};	
	
	public DatiCatastaliVO checkDatiCatastaliImmobiliDB(DatiCatastaliVO datiCatastaliVO){
		if (datiCatastaliVO.getCodDatiCatastali() != null){
			if (new DatiCatastaliDAO().getDatiCatastaliById(DatiCatastaliVO.class.getName(), 
															 datiCatastaliVO.getCodDatiCatastali()) == null){
				MessageDialog.openError(PlatformUI.getWorkbench()
						     					  .getActiveWorkbenchWindow()
						     					  .getShell(),
						     			"ERRORE", 
										"La stanza che si sta cercando di aggiornare � stata cancellata da un altro utente");
				datiCatastaliVO = null;
			
/*			if (new StanzeDAO().getStanzaById(StanzeImmobiliVO.class.getName(), 
											  stanzeImmobiliVO.getCodStanzeImmobili()) == null){
				if (MessageDialog.openConfirm(PlatformUI.getWorkbench()
														.getActiveWorkbenchWindow()
														.getShell(), 
											  "ATTENZIONE", 
											  "Si sta cercando di aggiornare la stanza " + stanzeImmobiliVO.getTipologia().getDescrizione() + " gi� cancellato da un altro utente. \n" +
											  "Se si prosegue con l'operazione la stanza verr� inserito nella base dati." +
			      							  "Procedere con l'operazione ?")){
					stanzeImmobiliVO.setCodStanzeImmobili(0);
				}else{
					stanzeImmobiliVO = null;
				}	
			*/	
			}
		}
		return datiCatastaliVO;
	}
	
	
	public Boolean updateDatiCatastali(ImmobiliModel immobile,
									   Connection con,
									   Boolean doCommit){
		
		Boolean returnValue = true;
		
		doCommit = (doCommit == null)?true:doCommit;
		DatiCatastaliDAO dcDAO = new DatiCatastaliDAO();
		ArrayList daticatastaliDB = dcDAO.findADatiCatastaliByCodImmobile(DatiCatastaliVO.class.getName(), immobile.getCodImmobile());
		
		Collections.sort(daticatastaliDB,comparer);
		ArrayList listaDatiCatastali = immobile.getDatiCatastali();
		if (listaDatiCatastali != null){
						
			Iterator it  = listaDatiCatastali.iterator();
			
			while (it.hasNext()){
				
				Object o = it.next();
				if (o != null){
					DatiCatastaliVO datiCatastali = (DatiCatastaliVO)o;
					int index = Collections.binarySearch(daticatastaliDB, datiCatastali,comparer);
					if (index >= 0){
						daticatastaliDB.remove(index);
						Collections.sort(daticatastaliDB,comparer);
					}
					datiCatastali.setCodImmobile(immobile.getCodImmobile());
	//				checkStanzeImmobiliDB(stanza);
					if (datiCatastali != null){
						if (!dcDAO.saveUpdate(datiCatastali, con, doCommit)){
								
								MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
														"Errore salvataggio dato catastale", 
														"Si � verificato un errore nel salvataggio del dato catastale : " + 
														"Foglio : " + datiCatastali.getFoglio() + " , Particella : " + datiCatastali.getParticella() +
														"Subalterno : " + datiCatastali.getSubalterno() + " , Categoria : " + datiCatastali.getCategoria());
								returnValue = false;
						}					
					}
				}
			}
			
			Iterator ite = daticatastaliDB.iterator();			
			while (ite.hasNext()){
				DatiCatastaliVO dcVO = (DatiCatastaliVO)ite.next();				
				if (!dcDAO.deleteDatiCatastaliById(dcVO.getCodDatiCatastali(), con, doCommit)){					
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
							"Errore cancellazione dato catastale", 
							"Si � verificato un errore nella cancellazione del dato catastale: " + 
							"Foglio : " + dcVO.getFoglio() + " , Particella : " + dcVO.getParticella() +
							"Subalterno : " + dcVO.getSubalterno() + " , Categoria : " + dcVO.getCategoria());

					returnValue = false;
					
				}
			}
		}

		return returnValue;
	}	
	


}