package winkhouse.helper;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.ImmaginiDAO;
import winkhouse.model.ImmobiliModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.ImmagineVO;



public class ImmaginiHelper {
	
	public final static String RESULT_UPDATE_IMMAGINI = "rui";
	public final static String LIST_SAVE_IMMAGINI = "lsi";
	public final static String LIST_DELETE_IMMAGINI = "ldi";
	
	private String pathImmaginiOrigine = Activator.getDefault().getStateLocation().toFile() + 
										 File.separator + "tmpimmagini";

	private String pathImmaginiDestinazione = WinkhouseUtils.getInstance()
												  			  .getPreferenceStore()
												  			  .getString(WinkhouseUtils.IMAGEPATH);
	



	public ImmaginiHelper() {
	}


	private Comparator<ImmagineVO> comparer = new Comparator<ImmagineVO>(){

		@Override
		public int compare(ImmagineVO arg0, ImmagineVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodImmagine()!=null) && (arg1.getCodImmagine()!=null)){				
				if ((arg0.getCodImmagine().intValue() == arg1.getCodImmagine().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodImmagine().intValue() < arg1.getCodImmagine().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodImmagine().intValue() > arg1.getCodImmagine().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodImmagine()!=null) && (arg1.getCodImmagine()==null)){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};	
	
	public ImmagineVO checkImmaginiDB(ImmagineVO immagineVO){
		if (immagineVO.getCodImmagine() != 0){
			if (new ImmaginiDAO().getImmaginiById(ImmagineVO.class.getName(), 
											  	  immagineVO.getCodImmagine()) == null){
				MessageDialog.openError(PlatformUI.getWorkbench()
   					  							  .getActiveWorkbenchWindow()
   					  							  .getShell(),
   					  					"ERRORE", 
										"L'immagine che si sta cercando di aggiornare � stata cancellata da un altro utente");
				immagineVO = null;

/*				if (MessageDialog.openConfirm(PlatformUI.getWorkbench()
														.getActiveWorkbenchWindow()
														.getShell(), 
											  "ATTENZIONE", 
											  "Si sta cercando di aggiornare l'immagine " + immagineVO.getPathImmagine() + " gi� cancellata da un altro utente. \n" +
											  "Se si prosegue con l'operazione l'immagine verr� inserito nella base dati." +
			      							  "Procedere con l'operazione ?")){
					immagineVO.setCodImmagine(0);
				}else{
					immagineVO = null;
				}	
				*/
			}
		}
		return immagineVO;
	}
	
	public HashMap updateListaImmagini(ImmobiliModel immobile,
									   Connection con,
									   Boolean doCommit){
		
		//Boolean returnValue = true;
		ArrayList saveImmagini = new ArrayList<ImmagineVO>();
		ArrayList deleteImmagini = new ArrayList<ImmagineVO>();
		HashMap returnValue = new HashMap();
		
		returnValue.put(RESULT_UPDATE_IMMAGINI, true);
		returnValue.put(LIST_DELETE_IMMAGINI, deleteImmagini);
		returnValue.put(LIST_SAVE_IMMAGINI, saveImmagini);
		
		doCommit = (doCommit == null)?true:doCommit;
		
		ImmaginiDAO iDAO = new ImmaginiDAO();
		ArrayList immaginiDB = iDAO.getImmaginiByImmobile(ImmagineVO.class.getName(), immobile.getCodImmobile());
		
		Collections.sort(immaginiDB,comparer);
		ArrayList listaImmagini = immobile.getImmagini();
		int counterrors = 0;
		if (listaImmagini != null){
						
			Iterator it  = listaImmagini.iterator();
			
			while (it.hasNext()){
				Object o = it.next();
				if (o != null){
					ImmagineVO immagine = (ImmagineVO)o;
					boolean isnew = (immagine.getCodImmagine() == 0) ? true: false;
					int index = Collections.binarySearch(immaginiDB, immagine, comparer);
					if (index >= 0){
						immaginiDB.remove(index);
						Collections.sort(immaginiDB,comparer);
					}
					immagine.setCodImmobile(immobile.getCodImmobile());
			//		checkImmaginiDB(immagine);
					if (immagine != null){
						if (!iDAO.saveUpdate(immagine, con, doCommit)){						
								MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
														"Errore salvataggio immagine", 
														"Si � verificato un errore nel salvataggio dell' immagine : " + 
														immagine.getPathImmagine());
								counterrors++;
								
						}else{
							if (isnew){
								((ArrayList<ImmagineVO>)returnValue.get(LIST_SAVE_IMMAGINI)).add(immagine);
								/*WinkhouseUtils.getInstance().copiaFile(pathImmaginiOrigine+immagine.getPathImmagine(),
																		 pathImmaginiDestinazione+immagine.getPathImmagine());*/
							}
						}					
					}
				}
			}
			
			Iterator ite = immaginiDB.iterator();
			while (ite.hasNext()){
				ImmagineVO immagine = (ImmagineVO)ite.next();
				if (!iDAO.delete(immagine.getCodImmagine(), con, doCommit)){					
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore cancellazione immagine", 
											"Si � verificato un errore nella cancellazione dell' immagine : " + 
											immagine.getPathImmagine());
					counterrors++;
					
				}else{
					((ArrayList<ImmagineVO>)returnValue.get(LIST_DELETE_IMMAGINI)).add(immagine);
/*					File f = new File (pathImmaginiDestinazione+immagine.getPathImmagine());
					f.delete();*/
				}
			}
		}
		
		if (counterrors > 0){
			returnValue.put(RESULT_UPDATE_IMMAGINI, false);
		}
		
		return returnValue;
	}
	
	public boolean saveImmagini(ArrayList<ImmagineVO> immagini,Boolean isnew){
		boolean returnValue = true;
		ImmagineVO immagine = null;
		try {
			Iterator<ImmagineVO> it = immagini.iterator();
			while(it.hasNext()){
				immagine = it.next();
				WinkhouseUtils.getInstance().copiaFile(pathImmaginiOrigine+File.separator+((isnew)?0:immagine.getCodImmobile())+File.separator+immagine.getPathImmagine(),
						 								 pathImmaginiDestinazione+File.separator+immagine.getCodImmobile()+File.separator+immagine.getPathImmagine());
			}
		} catch (Exception e) {
			MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
									"Errore cancellazione immagine", 
									"Si � verificato un errore nella copia dell' immagine da : " + 
									pathImmaginiOrigine+immagine.getPathImmagine() + " \n a : " + 
									pathImmaginiDestinazione+immagine.getPathImmagine());
			returnValue = false;
		}
		return returnValue;
	}
	
	public boolean deleteImmagini(ArrayList<ImmagineVO> immagini){
		boolean returnValue = true;
		ImmagineVO immagine = null;
		try {
			Iterator<ImmagineVO> it = immagini.iterator();
			while(it.hasNext()){
				immagine = it.next();
				File f = new File (pathImmaginiDestinazione+File.separator+immagine.getPathImmagine());
				f.delete();
			}
		} catch (Exception e) {
			MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
									"Errore cancellazione immagine", 
									"Si � verificato un errore nella cancellazione dell' immagine da : " + 									 
									pathImmaginiDestinazione+immagine.getPathImmagine());
			returnValue = false;
		}
		return returnValue;
		
	}
	
	
	
}