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
import winkhouse.dao.AnagraficheDAO;
import winkhouse.dao.ClassiClientiDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ClassiClientiVO;



public class ClassiClientiHelper {

	public ClassiClientiHelper() {
	}
	
	private class DeleteUpdaterProgressDialog implements IRunnableWithProgress{
		
		private	ClassiClientiVO classeCliente = null;
		private	int anagrafiche = 0;
		
		public DeleteUpdaterProgressDialog(Shell parent,
										   ClassiClientiVO classeCliente,
										   int anagrafiche){
			this.classeCliente = classeCliente;
			this.anagrafiche = anagrafiche;
			
		}

		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

			Connection con = ConnectionManager.getInstance().getConnection();
			ClassiClientiDAO classiClientiDAO = new ClassiClientiDAO();
			
			AnagraficheDAO anagraficheDAO = new AnagraficheDAO();
			
			int countoperations = 1;
			
			if (this.anagrafiche != 0){
				countoperations ++;
			}
			
			monitor.beginTask("Aggiornamento anagrafiche", (countoperations * 10));
			if (anagraficheDAO.updateClasseCliente(this.classeCliente.getCodClasseCliente(),null, con, false)){
				monitor.worked(10);
				monitor.setTaskName("Cancellazione classe cliente");
				if (classiClientiDAO.delete(this.classeCliente.getCodClasseCliente(), con, false)){
					try{
						con.commit();
						MobiliaDatiBaseCache.getInstance().setClassiClienti(null);
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
											"Errore aggiornamento anagrafiche \n la cancellazione della classe cliente � annullata");
				}
				
			}else{
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), 
										"Errore", 
										"Errore cancellazione della classe cliente \n la cancellazione della classe cliente � annullata");
			}
			
			MobiliaDatiBaseCache.getInstance().setClassiClienti(null);

		}
		
	}
	

	public boolean updateDatiBase(ArrayList classiClienti){
		
		boolean returnValue = true;
			
		if (classiClienti != null){
			
			ClassiClientiDAO ccDAO = new ClassiClientiDAO();			
			Iterator it  = classiClienti.iterator();
			
			while (it.hasNext()){
				ClassiClientiVO classe = (ClassiClientiVO)it.next();
				if (!ccDAO.saveUpdate(classe, null, true)){
					returnValue = false;
					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											"Errore salvataggio classe cliente", 
											"Si � verificato un errore nel salvataggio della classe : " + 
											classe.getDescrizione());					
				}
			}
			
		}
		MobiliaDatiBaseCache.getInstance().setClassiClienti(null);
		return returnValue;
	}
	
	public Boolean deleteClasseCliente(ClassiClientiVO classiClientiVO){
		Boolean result = true;
		
		ClassiClientiDAO ccDAO = new ClassiClientiDAO();
				
		AnagraficheDAO anagraficheDAO = new AnagraficheDAO();
		ArrayList anagrafiche = anagraficheDAO.getAnagraficheByClasse(AnagraficheVO.class.getName(), classiClientiVO.getCodClasseCliente());
				
		if ((anagrafiche.size() != 0)){
			boolean risposta = MessageDialog.openQuestion(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
														  "Informazioni - cancellazione classe cliente",
														  buildDeleteMessage(classiClientiVO,
																  			 anagrafiche.size()));  
			if (risposta){
				try {
				       IRunnableWithProgress op = new DeleteUpdaterProgressDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
				    		   													  classiClientiVO,
				    		   													  anagrafiche.size());
				       
				       new ProgressMonitorDialog(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell()).run(false, false, op);
				       
				    } catch (InvocationTargetException e) {
				       // handle exception
				    } catch (InterruptedException e) {
				       // handle cancelation
				    }
			}
		}else{
			result = ccDAO.delete(classiClientiVO.getCodClasseCliente(), null, true);
			if (result){
				MessageDialog.openInformation(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
											  "Informazioni - cancellazione agente", 
											  "Cancellazione eseguita con successo");
			}else{
				MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
						  				"Errore - cancellazione agente", 
						  				"Errore durante la cancellazione operazione annullata");				
			}
		}
		
		MobiliaDatiBaseCache.getInstance().setClassiClienti(null);
		
		return result;
	}
	
	private String buildDeleteMessage(ClassiClientiVO classe, int anagraficheSize){
		String returnValue = "";
		
		returnValue += "Cancellazione classe cliente : " + classe.getDescrizione();
		if (anagraficheSize != 0){
			returnValue += " Verranno aggiornate " + anagraficheSize + " anagrafiche" + "\n";
		}
		
		returnValue += "Procedere con l'operazione ?";
		
		return returnValue;
	}
	
	
}
