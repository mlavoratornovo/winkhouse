package winkhouse.helper;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

import winkhouse.Activator;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.dao.TipologieImmobiliDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.vo.ImmobiliVO;
import winkhouse.vo.TipologieImmobiliVO;



public class TipologieImmobiliHelper {

	public TipologieImmobiliHelper() {
		
	}

	
	private class DeleteUpdaterProgressDialog implements IRunnableWithProgress{
		
		private	TipologieImmobiliVO tipologiaImmobili = null;
		private	int immobili = 0;
		
		public DeleteUpdaterProgressDialog(Shell parent,
										   TipologieImmobiliVO tipologiaImmobili,
										   int immobili){
			this.tipologiaImmobili = tipologiaImmobili;
			this.immobili = immobili;
			
		}

		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

			Connection con = ConnectionManager.getInstance().getConnection();
			TipologieImmobiliDAO tipologieImmobiliDAO = new TipologieImmobiliDAO();
			
			ImmobiliDAO immobiliDAO = new ImmobiliDAO();
			
			int countoperations = 1;
			
			if (this.immobili != 0){
				countoperations ++;
			}
			
			monitor.beginTask("Cancellazione tipologia immobile", (countoperations * 10));
			if (immobiliDAO.updateTipologiaImmobili(this.tipologiaImmobili.getCodTipologiaImmobile(),null, con, false)){
				monitor.worked(10);
				monitor.setTaskName("Aggiornamento contatti");
				if (tipologieImmobiliDAO.delete(this.tipologiaImmobili.getCodTipologiaImmobile(), con, false)){
					try{
						con.commit();
						MobiliaDatiBaseCache.getInstance().setTipologieImmobili(null);
					}catch (SQLException e) {
						con = null;
					}					
					monitor.worked(10);
					monitor.setTaskName("Operazione completata con successo");					
				}else{
					try {
						con.rollback();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
											"Errore", 
											"Errore aggiornamento tipologia immobili \n la cancellazione della tipologia immobile è annullata");
				}
				
			}else{
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
										"Errore", 
										"Errore cancellazione della tipolgia immobile\n la cancellazione della tipolgia immobile è annullata");
			}
			MobiliaDatiBaseCache.getInstance().setTipologieImmobili(null);
		}
		
	}
	

	public boolean updateDatiBase(ArrayList tipologieImmobili){
		
		boolean returnValue = true;
			
		if (tipologieImmobili != null){
			
			TipologieImmobiliDAO tiDAO = new TipologieImmobiliDAO();			
			Iterator it  = tipologieImmobili.iterator();
			
			while (it.hasNext()){
				TipologieImmobiliVO tipologiaImmobiliVO = (TipologieImmobiliVO)it.next();
				if (!tiDAO.saveUpdate(tipologiaImmobiliVO, null, true)){
					returnValue = false;
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore salvataggio tipologia immobile", 
											"Si è verificato un errore nel salvataggio della tipologia immobile : " + 
											tipologiaImmobiliVO.getDescrizione());					
				}
			}
			
		}
		MobiliaDatiBaseCache.getInstance().setTipologieImmobili(null);
		return returnValue;
	}
	
	public Boolean deleteTipologiaImmobili(TipologieImmobiliVO tipologieImmobiliVO){
		Boolean result = true;
		
		TipologieImmobiliDAO tiDAO = new TipologieImmobiliDAO();
				
		ImmobiliDAO immobiliDAO = new ImmobiliDAO();
		ArrayList immobili = immobiliDAO.getImmobiliByTipologia(ImmobiliVO.class.getName(), tipologieImmobiliVO.getCodTipologiaImmobile());
				
		if ((immobili.size() != 0)){
			boolean risposta = MessageDialog.openQuestion(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
														  "Informazioni - cancellazione tipologia immobile",
														  buildDeleteMessage(tipologieImmobiliVO,
																  			 immobili.size()));  
			if (risposta){
				try {
				       IRunnableWithProgress op = new DeleteUpdaterProgressDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
				    		   													  tipologieImmobiliVO,
				    		   													  immobili.size());
				       
				       new ProgressMonitorDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell()).run(false, false, op);
				       
				    } catch (InvocationTargetException e) {
				       // handle exception
				    } catch (InterruptedException e) {
				       // handle cancelation
				    }
			}
		}else{
			result = tiDAO.delete(tipologieImmobiliVO.getCodTipologiaImmobile(), null, true);
			if (result){
				MobiliaDatiBaseCache.getInstance().setTipologieImmobili(null);
				MessageDialog.openInformation(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											  "Informazioni - cancellazione tipologia immobili", 
											  "Cancellazione eseguita con successo");
			}else{
				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
						  				"Errore - cancellazione tipologia immobili", 
						  				"Errore durante la cancellazione operazione annullata");				
			}
		}
		
		MobiliaDatiBaseCache.getInstance().setTipologieImmobili(null);
		
		return result;
	}
	
	private String buildDeleteMessage(TipologieImmobiliVO tipologieImmobiliVO, int immobiliSize){
		String returnValue = "";
		
		returnValue += "Cancellazione stato tipologia immobili : " + tipologieImmobiliVO.getDescrizione()  + "\n";
		if (immobiliSize != 0){
			returnValue += "Verranno aggiornati " + immobiliSize + " immobili" + "\n";
		}
		
		returnValue += "Procedere con l'operazione ?";
		
		return returnValue;
	}		
}
