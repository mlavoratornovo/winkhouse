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
import winkhouse.dao.ImmobiliDAO;
import winkhouse.dao.StatoConservativoDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.orm.Statoconservativo;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.vo.ImmobiliVO;
import winkhouse.vo.StatoConservativoVO;



public class StatoConservativoHelper {

	public StatoConservativoHelper() {
		
	}
	
	private class DeleteUpdaterProgressDialog implements IRunnableWithProgress{
		
		private	Statoconservativo statoConservativo = null;
		private	int immobili = 0;
		
		public DeleteUpdaterProgressDialog(Shell parent,
										   Statoconservativo statoConservativo,
										   int immobili){
			this.statoConservativo = statoConservativo;
			this.immobili = immobili;
			
		}

		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

			Connection con = ConnectionManager.getInstance().getConnection();
			StatoConservativoDAO statoConservativoDAO = new StatoConservativoDAO();
			
			ImmobiliDAO immobiliDAO = new ImmobiliDAO();
			
			int countoperations = 1;
			
			if (this.immobili != 0){
				countoperations ++;
			}
			
			monitor.beginTask("Cancellazione stato conservativo", (countoperations * 10));
			if (immobiliDAO.updateStatoConservativo(this.statoConservativo.getCodStatoConservativo(),null, con, false)){
				monitor.worked(10);
				monitor.setTaskName("Aggiornamento immobili");
				if (statoConservativoDAO.delete(this.statoConservativo.getCodStatoConservativo(), con, false)){
					try{
						con.commit();
						MobiliaDatiBaseCache.getInstance().setStatiConservativi(null);
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
											"Errore aggiornamento immobili \n la cancellazione dello stato conservativo � annullata");
				}
				
			}else{
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
										"Errore", 
										"Errore cancellazione del riscaldamento \n la cancellazione dello stato conservativo � annullata");
			}
			MobiliaDatiBaseCache.getInstance().setStatiConservativi(null);
		}
		
	}
	

	public boolean updateDatiBase(ArrayList staticonservativi){
		
		boolean returnValue = true;
			
		if (staticonservativi != null){
			
			StatoConservativoDAO scDAO = new StatoConservativoDAO();			
			Iterator it  = staticonservativi.iterator();
			
			while (it.hasNext()){
				StatoConservativoVO statoconservativo = (StatoConservativoVO)it.next();
				if (!scDAO.saveUpdate(statoconservativo, null, true)){
					returnValue = false;
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore salvataggio stato conservativo", 
											"Si � verificato un errore nel salvataggio dello stato conservativo : " + 
											statoconservativo.getDescrizione());					
				}
			}
			
		}
		MobiliaDatiBaseCache.getInstance().setStatiConservativi(null);
		return returnValue;
	}
	
	public Boolean deleteStatoConservativo(Statoconservativo statoConservativoVO){
		
Boolean result = true;
		
		try {
			statoConservativoVO.getObjectContext().deleteObject(statoConservativoVO);
			statoConservativoVO.getObjectContext().commitChanges();
		} catch (DeleteDenyException e) {
			// TODO Auto-generated catch block
			result = false;
		}
		
//		Boolean result = true;
//		
//		StatoConservativoDAO scDAO = new StatoConservativoDAO();
//				
//		ImmobiliDAO immobiliDAO = new ImmobiliDAO();
//		ArrayList immobili = immobiliDAO.getImmobiliByStatoConservativo(ImmobiliVO.class.getName(), statoConservativoVO.getCodStatoConservativo());
//				
//		if ((immobili.size() != 0)){
//			boolean risposta = MessageDialog.openQuestion(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
//														  "Informazioni - cancellazione stato conservativo",
//														  buildDeleteMessage(statoConservativoVO,
//																  			 immobili.size()));  
//			if (risposta){
//				try {
//				       IRunnableWithProgress op = new DeleteUpdaterProgressDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
//				    		   													  statoConservativoVO,
//				    		   													  immobili.size());
//				       
//				       new ProgressMonitorDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell()).run(false, false, op);
//				       
//				    } catch (InvocationTargetException e) {
//				       // handle exception
//				    } catch (InterruptedException e) {
//				       // handle cancelation
//				    }
//			}
//		}else{
//			result = scDAO.delete(statoConservativoVO.getCodStatoConservativo(), null, true);
//			if (result){
//				MobiliaDatiBaseCache.getInstance().setStatiConservativi(null);
//				MessageDialog.openInformation(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
//											  "Informazioni - cancellazione stato conservativo", 
//											  "Cancellazione eseguita con successo");
//			}else{
//				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
//						  				"Errore - cancellazione stato conservativo", 
//						  				"Errore durante la cancellazione operazione annullata");				
//			}
//		}		
		MobiliaDatiBaseCache.getInstance().setStatiConservativi(null);
		return result;
	}
	
	private String buildDeleteMessage(Statoconservativo statoConservativoVO, int immobiliSize){
		String returnValue = "";
		
		returnValue += "Cancellazione stato conservativo : " + statoConservativoVO.getDescrizione()+"\n";
		if (immobiliSize != 0){
			returnValue += "Verranno aggiornati " + immobiliSize + " immobili" + "\n";
		}
		
		returnValue += "Procedere con l'operazione ?";
		
		return returnValue;
	}	
}
