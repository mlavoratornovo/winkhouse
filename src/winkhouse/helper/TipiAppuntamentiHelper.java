package winkhouse.helper;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.cayenne.DeleteDenyException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

import winkhouse.Activator;
import winkhouse.dao.AppuntamentiDAO;
import winkhouse.dao.TipiAppuntamentiDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.orm.Tipiappuntamenti;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.vo.AppuntamentiVO;
import winkhouse.vo.TipiAppuntamentiVO;



public class TipiAppuntamentiHelper {

	public TipiAppuntamentiHelper() {
	}
	
	private class DeleteUpdaterProgressDialog implements IRunnableWithProgress{
		
		private	Tipiappuntamenti tipoAppuntamento = null;
		private	int appuntamenti = 0;
		
		public DeleteUpdaterProgressDialog(Shell parent,
										   Tipiappuntamenti tipoAppuntamento,
										   int appuntamenti){
			this.tipoAppuntamento = tipoAppuntamento;
			this.appuntamenti = appuntamenti;
			
		}

		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

			Connection con = ConnectionManager.getInstance().getConnection();
			TipiAppuntamentiDAO tipiAppuntamentiDAO = new TipiAppuntamentiDAO();
			
			AppuntamentiDAO appuntametiDAO = new AppuntamentiDAO();
			
			int countoperations = 1;
			
			if (this.appuntamenti != 0){
				countoperations ++;
			}
			
			monitor.beginTask("Cancellazione tipo appuntamento", (countoperations * 10));
			if (appuntametiDAO.updateTipoAppuntamento(this.tipoAppuntamento.getCodTipoAppuntamento(),null, con, false)){
				monitor.worked(10);
				monitor.setTaskName("Aggiornamento appuntamenti");
				if (tipiAppuntamentiDAO.delete(this.tipoAppuntamento.getCodTipoAppuntamento(), con, false)){
					try{
						con.commit();
						MobiliaDatiBaseCache.getInstance().setTipiAppuntamenti(null);
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
											"Errore aggiornamento appuntamenti \n la cancellazione del tipo appuntamento � annullata");
				}
				
			}else{
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
										"Errore", 
										"Errore cancellazione del tipo appuntamento \n la cancellazione del tipo appuntamento � annullata");
			}
			
			MobiliaDatiBaseCache.getInstance().setTipiAppuntamenti(null);

		}
		
	}
	

	public boolean updateDatiBase(ArrayList tipiAppuntamenti){
		
		boolean returnValue = true;
			
		if (tipiAppuntamenti != null){
			
			TipiAppuntamentiDAO taDAO = new TipiAppuntamentiDAO();			
			Iterator it  = tipiAppuntamenti.iterator();
			
			while (it.hasNext()){
				TipiAppuntamentiVO tipo = (TipiAppuntamentiVO)it.next();
				if (!taDAO.saveUpdate(tipo, null, true)){
					returnValue = false;
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore salvataggio tipo appuntamento ", 
											"Si � verificato un errore nel salvataggio della tipo appuntamento : " + 
											tipo.getDescrizione());					
				}
			}
			
		}
		MobiliaDatiBaseCache.getInstance().setTipiAppuntamenti(null);
		return returnValue;
	}
	
	public Boolean deleteTipiAppuntamenti(Tipiappuntamenti tipiAppuntamentiVO){
		
		Boolean result = true;
		
		try {
			tipiAppuntamentiVO.getObjectContext().deleteObject(tipiAppuntamentiVO);
			tipiAppuntamentiVO.getObjectContext().commitChanges();
		} catch (DeleteDenyException e) {
			// TODO Auto-generated catch block
			result = false;
		}

//		Boolean result = true;
//		
//		TipiAppuntamentiDAO taDAO = new TipiAppuntamentiDAO();
//				
//		AppuntamentiDAO appuntamentiDAO = new AppuntamentiDAO();
//		ArrayList appuntamenti = appuntamentiDAO.listAppuntamentiByCodTipoAppuntamento(AppuntamentiVO.class.getName(), 
//																					   tipiAppuntamentiVO.getCodTipoAppuntamento());
//				
//		if ((appuntamenti.size() != 0)){
//			boolean risposta = MessageDialog.openQuestion(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
//														  "Informazioni - cancellazione tipo appuntamento",
//														  buildDeleteMessage(tipiAppuntamentiVO,
//																  			 appuntamenti.size()));  
//			if (risposta){
//				try {
//				       IRunnableWithProgress op = new DeleteUpdaterProgressDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
//				    		   													  tipiAppuntamentiVO,
//				    		   													  appuntamenti.size());
//				       
//				       new ProgressMonitorDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell()).run(false, false, op);
//				       
//				    } catch (InvocationTargetException e) {
//				       // handle exception
//				    } catch (InterruptedException e) {
//				       // handle cancelation
//				    }
//			}
//			
//		}else{
//			result = taDAO.delete(tipiAppuntamentiVO.getCodTipoAppuntamento(), null, true);
//			if (result){
//				MessageDialog.openInformation(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
//											  "Informazioni - cancellazione tipo appuntamento", 
//											  "Cancellazione eseguita con successo");
//			}else{
//				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
//						  				"Errore - tipo appuntamento", 
//						  				"Errore durante la cancellazione operazione annullata");				
//			}
//		}
//		
		MobiliaDatiBaseCache.getInstance().setTipiAppuntamenti(null);
		
		return result;
	}
	
	private String buildDeleteMessage(Tipiappuntamenti tipo, int appuntamentiSize){
		String returnValue = "";
		
		returnValue += "Cancellazione tipo appuntametno : " + tipo.getDescrizione();
		if (appuntamentiSize != 0){
			returnValue += "Verranno aggiornati " + appuntamentiSize + " appuntamenti" + "\n";
		}
		
		returnValue += "Procedere con l'operazione ?";
		
		return returnValue;
	}
	
	
}
