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
import winkhouse.dao.StanzeDAO;
import winkhouse.dao.TipologiaStanzeDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.orm.Tipologiastanze;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.vo.StanzeImmobiliVO;
import winkhouse.vo.TipologiaStanzeVO;



public class TipologiaStanzeHelper {

	public TipologiaStanzeHelper() {

	}

	
	private class DeleteUpdaterProgressDialog implements IRunnableWithProgress{
		
		private	Tipologiastanze tipologiaStanze = null;
		private	int stanze = 0;
		
		public DeleteUpdaterProgressDialog(Shell parent,
										   Tipologiastanze tipologiaStanze,
										   int stanze){
			this.tipologiaStanze = tipologiaStanze;
			this.stanze = stanze;
			
		}

		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

			Connection con = ConnectionManager.getInstance().getConnection();
			TipologiaStanzeDAO tsDAO = new TipologiaStanzeDAO();
			
			StanzeDAO stanzeDAO = new StanzeDAO();
			
			int countoperations = 1;
			
			if (this.stanze != 0){
				countoperations ++;
			}
			
			monitor.beginTask("Cancellazione tipologia stanza", (countoperations * 10));
			if (stanzeDAO.updateTipologia(this.tipologiaStanze.getCodTipologiaStanza(),null, con, false)){
				monitor.worked(10);
				monitor.setTaskName("Aggiornamento stanze");
				if (tsDAO.delete(this.tipologiaStanze.getCodTipologiaStanza(), con, false)){
					try{
						con.commit();
						MobiliaDatiBaseCache.getInstance().setTipologieStanze(null);
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
											"Errore aggiornamento tipologia stanze \n la cancellazione della tipologia stanze � annullata");
				}
				
			}else{
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
										"Errore", 
										"Errore cancellazione della tipolgia stanze \n la cancellazione della tipolgia stanze � annullata");
			}
			MobiliaDatiBaseCache.getInstance().setTipologieStanze(null);
		}
		
	}
	

	public boolean updateDatiBase(ArrayList tipologieStanze){
		
		boolean returnValue = true;
			
		if (tipologieStanze != null){
			
			TipologiaStanzeDAO tsDAO = new TipologiaStanzeDAO();			
			Iterator it  = tipologieStanze.iterator();
			
			while (it.hasNext()){
				TipologiaStanzeVO tipologiaStanzeVO = (TipologiaStanzeVO)it.next();
				if (!tsDAO.saveUpdate(tipologiaStanzeVO, null, true)){
					returnValue = false;
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore salvataggio tipologia stanze", 
											"Si � verificato un errore nel salvataggio della tipologia stanze : " + 
											tipologiaStanzeVO.getDescrizione());					
				}
			}
			
		}
		MobiliaDatiBaseCache.getInstance().setTipologieStanze(null);
		return returnValue;
	}
	
	public Boolean deleteTipologiaStanze(Tipologiastanze tipologiaStanzeVO){
		Boolean result = true;
		
		TipologiaStanzeDAO tsDAO = new TipologiaStanzeDAO();
				
		StanzeDAO stanzeDAO = new StanzeDAO();
		ArrayList stanze = stanzeDAO.listByTipologia(StanzeImmobiliVO.class.getName(), tipologiaStanzeVO.getCodTipologiaStanza());
				
		if ((stanze.size() != 0)){
			boolean risposta = MessageDialog.openQuestion(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
														  "Informazioni - cancellazione tipologia stanze",
														  buildDeleteMessage(tipologiaStanzeVO,
																  			 stanze.size()));  
			if (risposta){
				try {
				       IRunnableWithProgress op = new DeleteUpdaterProgressDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
				    		   													  tipologiaStanzeVO,
				    		   													  stanze.size());
				       
				       new ProgressMonitorDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell()).run(false, false, op);
				       
				    } catch (InvocationTargetException e) {
				       // handle exception
				    } catch (InterruptedException e) {
				       // handle cancelation
				    }
			}
			
		}else{
			result = tsDAO.delete(tipologiaStanzeVO.getCodTipologiaStanza(), null, true);			
			if (result){
				MessageDialog.openInformation(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											  "Informazioni - cancellazione tipologia stanze", 
											  "Cancellazione eseguita con successo");
			}else{
				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
						  				"Errore - cancellazione tipologia stanze", 
						  				"Errore durante la cancellazione operazione annullata");				
			}
		}
		
		MobiliaDatiBaseCache.getInstance().setTipologieStanze(null);
		
		return result;
	}
	
	private String buildDeleteMessage(Tipologiastanze tipologiaStanzeVO, int stanzeSize){
		String returnValue = "";
		
		returnValue += "Cancellazione stato tipologia stanze : " + tipologiaStanzeVO.getDescrizione() + "\n";
		if (stanzeSize != 0){
			returnValue += "Verranno aggiornati " + stanzeSize + " stanze" + "\n";
		}
		
		returnValue += "Procedere con l'operazione ?";
		
		return returnValue;
	}		

}
