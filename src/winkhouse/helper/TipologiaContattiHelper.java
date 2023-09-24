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
import winkhouse.dao.ContattiDAO;
import winkhouse.dao.TipologiaContattiDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.vo.ContattiVO;
import winkhouse.vo.TipologiaContattiVO;



public class TipologiaContattiHelper {

	public TipologiaContattiHelper() {
	
	}

	
	private class DeleteUpdaterProgressDialog implements IRunnableWithProgress{
		
		private	TipologiaContattiVO tipologiaContatti = null;
		private	int contatti = 0;
		
		public DeleteUpdaterProgressDialog(Shell parent,
										   TipologiaContattiVO tipologiaContatti,
										   int contatti){
			this.tipologiaContatti = tipologiaContatti;
			this.contatti = contatti;
			
		}

		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

			Connection con = ConnectionManager.getInstance().getConnection();
			TipologiaContattiDAO tipologiaContattiDAO = new TipologiaContattiDAO();
			
			ContattiDAO contattiDAO = new ContattiDAO();
			
			int countoperations = 1;
			
			if (this.contatti != 0){
				countoperations ++;
			}
			
			monitor.beginTask("Cancellazione tipologia contatto", (countoperations * 10));
			if (contattiDAO.updateTipologiaContatti(this.tipologiaContatti.getCodTipologiaContatto(),null, con, false)){
				monitor.worked(10);
				monitor.setTaskName("Aggiornamento contatti");
				if (tipologiaContattiDAO.delete(this.tipologiaContatti.getCodTipologiaContatto(), con, false)){
					try {
						con.commit();
						monitor.worked(10);
						monitor.setTaskName("Operazione completata con successo");											
					} catch (SQLException e) {
						con = null;
					}
				}else{
					try {
						con.rollback();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
											"Errore", 
											"Errore aggiornamento contatti \n la cancellazione della tipologia contatto è annullata");
				}
				
			}else{
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
										"Errore", 
										"Errore cancellazione del tipologia contatti \n la cancellazione della tipologia contatto è annullata");
			}
			MobiliaDatiBaseCache.getInstance().setTipologieContatti(null);
		}
		
	}
	

	public boolean updateDatiBase(ArrayList tipologieContatti){
		
		boolean returnValue = true;
			
		if (tipologieContatti != null){
			
			TipologiaContattiDAO tcDAO = new TipologiaContattiDAO();			
			Iterator it  = tipologieContatti.iterator();
			
			while (it.hasNext()){
				TipologiaContattiVO tipologiaContattiVO = (TipologiaContattiVO)it.next();
				if (!tcDAO.saveUpdate(tipologiaContattiVO, null, true)){
					returnValue = false;
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore salvataggio tipologia contatto", 
											"Si è verificato un errore nel salvataggio della tipologia contatto : " + 
											tipologiaContattiVO.getDescrizione());					
				}
			}
			
		}
		MobiliaDatiBaseCache.getInstance().setTipologieContatti(null);
		return returnValue;
	}
	
	public Boolean deleteTipologiaContatti(TipologiaContattiVO tipologiaContattiVO){
		Boolean result = true;
		
		TipologiaContattiDAO tcDAO = new TipologiaContattiDAO();
				
		ContattiDAO contattiDAO = new ContattiDAO();
		ArrayList contatti = contattiDAO.listByTipologia(ContattiVO.class.getName(), tipologiaContattiVO.getCodTipologiaContatto());
				
		if ((contatti.size() != 0)){
			boolean risposta = MessageDialog.openQuestion(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
														  "Informazioni - cancellazione tipologia contatto",
														  buildDeleteMessage(tipologiaContattiVO,
																  			 contatti.size()));  
			
			if (risposta){
				try {
				       IRunnableWithProgress op = new DeleteUpdaterProgressDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
				    		   													  tipologiaContattiVO,
				    		   													  contatti.size());
				       
				       new ProgressMonitorDialog(Activator.getDefault()
				    		   							  .getWorkbench()
				    		   							  .getActiveWorkbenchWindow()
				    		   							  .getShell()).run(false, false, op);
				       
				    } catch (InvocationTargetException e) {
				       // handle exception
				    } catch (InterruptedException e) {
				       // handle cancelation
				    }
			}
		}else{
			result = tcDAO.delete(tipologiaContattiVO.getCodTipologiaContatto(), null, true);
			if (result){
				MessageDialog.openInformation(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											  "Informazioni - cancellazione tipologia contatto", 
											  "Cancellazione eseguita con successo");
			}else{
				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
						  				"Errore - cancellazione tipologia contatto", 
						  				"Errore durante la cancellazione operazione annullata");				
			}
		}
		
		MobiliaDatiBaseCache.getInstance().setTipologieContatti(null);
		
		return result;
	}
	
	private String buildDeleteMessage(TipologiaContattiVO tipologiaContattiVO, int contattiSize){
		String returnValue = "";
		
		returnValue += "Cancellazione stato tipologia contatto : " + tipologiaContattiVO.getDescrizione();
		if (contattiSize != 0){
			returnValue += " Verranno aggiornati " + contattiSize + " contatti" + "\n";
		}
		
		returnValue += "Procedere con l'operazione ?";
		
		return returnValue;
	}	
	
}
