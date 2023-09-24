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
import winkhouse.dao.RiscaldamentiDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.vo.ImmobiliVO;
import winkhouse.vo.RiscaldamentiVO;



public class RiscaldamentiHelper {

	public RiscaldamentiHelper() {

	}
	
	private class DeleteUpdaterProgressDialog implements IRunnableWithProgress{
		
		private	RiscaldamentiVO riscaldamento = null;
		private	int immobili = 0;
		
		public DeleteUpdaterProgressDialog(Shell parent,
										   RiscaldamentiVO riscaldamento,
										   int immobili){
			this.riscaldamento = riscaldamento;
			this.immobili = immobili;
			
		}

		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

			Connection con = ConnectionManager.getInstance().getConnection();
			RiscaldamentiDAO riscaldamentiDAO = new RiscaldamentiDAO();
			
			ImmobiliDAO immobiliDAO = new ImmobiliDAO();
			
			int countoperations = 1;
			
			if (this.immobili != 0){
				countoperations ++;
			}
			
			monitor.beginTask("Cancellazione classe cliente", (countoperations * 10));
			if (immobiliDAO.updateRiscaldamento(this.riscaldamento.getCodRiscaldamento(), null, con, false)){
				monitor.worked(10);
				monitor.setTaskName("Aggiornamento immobili");
				if (riscaldamentiDAO.delete(this.riscaldamento.getCodRiscaldamento(), con, false)){
					try{
						con.commit();
						MobiliaDatiBaseCache.getInstance().setRiscaldamenti(null);
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
											"Errore aggiornamento immobili \n la cancellazione del riscaldamento è annullata");
				}
				
			}else{
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
										"Errore", 
										"Errore cancellazione del riscaldamento \n la cancellazione del riscaldamento è annullata");
			}
			
			MobiliaDatiBaseCache.getInstance().setRiscaldamenti(null);

		}
		
	}
	

	public boolean updateDatiBase(ArrayList riscaldamenti){
		
		boolean returnValue = true;
			
		if (riscaldamenti != null){
			
			RiscaldamentiDAO rDAO = new RiscaldamentiDAO();			
			Iterator it  = riscaldamenti.iterator();
			
			while (it.hasNext()){
				RiscaldamentiVO riscaldamento = (RiscaldamentiVO)it.next();
				if (!rDAO.saveUpdate(riscaldamento, null, true)){
					returnValue = false;
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore salvataggio riscaldamento", 
											"Si è verificato un errore nel salvataggio del riscaldamento : " + 
											riscaldamento.getDescrizione());					
				}
			}
			
		}
		MobiliaDatiBaseCache.getInstance().setRiscaldamenti(null);
		return returnValue;
	}
	
	public Boolean deleteRiscaldamento(RiscaldamentiVO riscaldamentiVO){
		Boolean result = true;
		
		RiscaldamentiDAO rDAO = new RiscaldamentiDAO();
				
		ImmobiliDAO immobiliDAO = new ImmobiliDAO();
		ArrayList immobili = immobiliDAO.getImmobiliByRiscaldamento(ImmobiliVO.class.getName(), riscaldamentiVO.getCodRiscaldamento());
				
		if ((immobili.size() != 0)){
			boolean risposta = MessageDialog.openQuestion(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
														  "Informazioni - cancellazione riscaldamento",
														  buildDeleteMessage(riscaldamentiVO,
																  			 immobili.size()));  
			if (risposta){
				try {
				       IRunnableWithProgress op = new DeleteUpdaterProgressDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
				    		   													  riscaldamentiVO,
				    		   													  immobili.size());
				       
				       new ProgressMonitorDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell()).run(false, false, op);
				       
				    } catch (InvocationTargetException e) {
				       // handle exception
				    } catch (InterruptedException e) {
				       // handle cancelation
				    }
			}
		}else{
			result = rDAO.delete(riscaldamentiVO.getCodRiscaldamento(), null, true);
			if (result){
				MobiliaDatiBaseCache.getInstance().setRiscaldamenti(null);
				MessageDialog.openInformation(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											  "Informazioni - cancellazione riscaldamento", 
											  "Cancellazione eseguita con successo");
			}else{
				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
						  				"Errore - cancellazione riscaldamento", 
						  				"Errore durante la cancellazione operazione annullata");				
			}
		}
		
		MobiliaDatiBaseCache.getInstance().setRiscaldamenti(null);
		
		return result;
	}
	
	private String buildDeleteMessage(RiscaldamentiVO riscaldamento, int immobiliSize){
		String returnValue = "";
		
		returnValue += "Cancellazione riscaldamento : " + riscaldamento.getDescrizione() + "\n";
		if (immobiliSize != 0){
			returnValue += "Verranno aggiornati " + immobiliSize + " immobili" + "\n";
		}
		
		returnValue += "Procedere con l'operazione ?";
		
		return returnValue;
	}
	

}
