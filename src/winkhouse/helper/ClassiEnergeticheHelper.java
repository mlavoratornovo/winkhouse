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
import winkhouse.dao.ClassiEnergeticheDAO;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Classienergetiche;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.vo.ClasseEnergeticaVO;

public class ClassiEnergeticheHelper {

	public ClassiEnergeticheHelper() {

	}

	private class DeleteUpdaterProgressDialog implements IRunnableWithProgress{
		
		private	Classienergetiche classeenergetica = null;
		private	int classienergetiche = 0;
		
		public DeleteUpdaterProgressDialog(Shell parent,
										   Classienergetiche classeenergetica,
										   int classienergetiche){
			this.classeenergetica = classeenergetica;
			this.classienergetiche = classienergetiche;
			
		}

		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

			Connection con = ConnectionManager.getInstance().getConnection();
			ClassiEnergeticheDAO classiEnergeticheDAO = new ClassiEnergeticheDAO();
			
			ImmobiliDAO immobiliDAO = new ImmobiliDAO();
			
			int countoperations = 1;
			
			if (this.classienergetiche != 0){
				countoperations ++;
			}
			
			monitor.beginTask("Cancellazione classe energetica", (countoperations * 10));
			if (immobiliDAO.updateClasseEnergetica(this.classeenergetica.getCodClasseEnergetica(),null, con, false)){
				monitor.worked(10);
				monitor.setTaskName("Aggiornamento immobili");
				if (classiEnergeticheDAO.delete(this.classeenergetica.getCodClasseEnergetica(), con, false)){
					try{
						con.commit();
						MobiliaDatiBaseCache.getInstance().setClassiEnergetiche(null);
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
											"Errore aggiornamento immobili \n la cancellazione della classe energetica � annullata");
				}
				
			}else{
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
										"Errore", 
										"Errore cancellazione della classe energetica \n la cancellazione della classe energetica � annullata");
			}
			
			MobiliaDatiBaseCache.getInstance().setClassiEnergetiche(null);

		}
		
	}
	

	public boolean updateDatiBase(ArrayList classiEnergetiche){
		
		boolean returnValue = true;
			
		if (classiEnergetiche != null){
			
			ClassiEnergeticheDAO ceDAO = new ClassiEnergeticheDAO();			
			Iterator it  = classiEnergetiche.iterator();
			
			while (it.hasNext()){
				ClasseEnergeticaVO classe = (ClasseEnergeticaVO)it.next();
				if (!ceDAO.saveUpdate(classe, null, true)){
					returnValue = false;
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore salvataggio classe energetica ", 
											"Si � verificato un errore nel salvataggio della classe energetica : " + 
											classe.getNome());					
				}
			}
			
		}
		MobiliaDatiBaseCache.getInstance().setClassiEnergetiche(null);
		return returnValue;
	}
	
	public Boolean deleteClasseEnergetica(Classienergetiche classeEnergeticaVO){
		Boolean result = true;
		
		ClassiEnergeticheDAO ceDAO = new ClassiEnergeticheDAO();
				
		ImmobiliDAO immobiliDAO = new ImmobiliDAO();
		ArrayList immobili = immobiliDAO.listImmobiliByCodClasseEnergetica(ImmobiliModel.class.getName(), classeEnergeticaVO.getCodClasseEnergetica());
				
		if ((immobili.size() != 0)){
			boolean risposta = MessageDialog.openQuestion(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
														  "Informazioni - cancellazione classe energetica",
														  buildDeleteMessage(classeEnergeticaVO,
																  			 immobili.size()));  
			if (risposta){
				try {
				       IRunnableWithProgress op = new DeleteUpdaterProgressDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
				    		   													  classeEnergeticaVO,
				    		   													  immobili.size());
				       
				       new ProgressMonitorDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell()).run(false, false, op);
				       
				    } catch (InvocationTargetException e) {
				       // handle exception
				    } catch (InterruptedException e) {
				       // handle cancelation
				    }
			}
			
		}else{
			result = ceDAO.delete(classeEnergeticaVO.getCodClasseEnergetica(), null, true);
			if (result){
				MessageDialog.openInformation(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											  "Informazioni - cancellazione classe energetica", 
											  "Cancellazione eseguita con successo");
			}else{
				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
						  				"Errore - classe energetica", 
						  				"Errore durante la cancellazione operazione annullata");				
			}
		}
		
		MobiliaDatiBaseCache.getInstance().setClassiEnergetiche(null);
		
		return result;
	}
	
	private String buildDeleteMessage(Classienergetiche classe, int immobiliSize){
		String returnValue = "";
		
		returnValue += "Cancellazione classe energetica : " + classe.getNome();
		if (immobiliSize != 0){
			returnValue += "Verranno aggiornati " + immobiliSize + " immobili" + "\n";
		}
		
		returnValue += "Procedere con l'operazione ?";
		
		return returnValue;
	}

}
