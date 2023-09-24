package winkhouse.action.agenda;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.dao.AppuntamentiDAO;
import winkhouse.dao.ColloquiDAO;
import winkhouse.model.AgentiModel;
import winkhouse.model.AppuntamentiModel;
import winkhouse.model.ColloquiModel;
import winkhouse.view.agenda.CalendarioView;
import winkhouse.view.agenda.ListaAppuntamentiView;



public class CercaAppuntamentiAction extends Action {

	private AgentiModel agente = null;
	private Date dataDA = null;
	private Date dataA = null;
	private AppuntamentiDAO aDAO = null;
	private ColloquiDAO cDAO = null;
	private ListaAppuntamentiView lav = null;
	private CalendarioView cv = null;
	private String returnView = null;
	private Date startDate = null;
	private Date endDate = null;
	
	public CercaAppuntamentiAction(String returnView) {
		this.returnView = returnView;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getImageDescriptor("icons/kfind.png");
	}

	@Override
	public String getText() {
		return "Cerca appuntamenti";
	}

	@Override
	public void run() {
		ViewPart vp = null;
		if (this.returnView.equalsIgnoreCase(ListaAppuntamentiView.class.getName())){
			
			lav = (ListaAppuntamentiView)PlatformUI.getWorkbench()
					  							   .getActiveWorkbenchWindow()
					  							   .getActivePage()
					  							   .findView(ListaAppuntamentiView.ID);
			
			vp = lav;
			agente = lav.getAgenteSearch();
			startDate = lav.getDataDASearch();
			endDate = lav.getDataASearch();
		}
		if (this.returnView.equalsIgnoreCase(CalendarioView.class.getName())){
			cv = (CalendarioView)PlatformUI.getWorkbench()
					   					   .getActiveWorkbenchWindow()
					   					   .getActivePage()
					   					   .findView(CalendarioView.ID);
			vp = cv;
			agente = cv.getAgenteSearch();
			startDate = cv.getDataDASearch();
			endDate = cv.getDataASearch();
			
		}
			
		aDAO = new AppuntamentiDAO();
		cDAO = new ColloquiDAO();
		
		dataDA = null;
		
		if (startDate == null){
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR,1970);
			c.set(Calendar.MONTH, 0);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);			
			dataDA = c.getTime();
		}else{
			Calendar c = Calendar.getInstance();
			c.setTime(startDate);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);						
			dataDA = c.getTime();
		}
		
		dataA = null;
		if (endDate == null){
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR,2100);
			c.set(Calendar.MONTH, 11);
			c.set(Calendar.DAY_OF_MONTH, 31);			
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);
			dataA = c.getTime();
		}else{
			Calendar c = Calendar.getInstance();
			c.setTime(endDate);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			c.set(Calendar.SECOND, 59);						
			dataA = c.getTime();
		}
		
		if (agente != null){
			ProgressMonitorDialog pmd = new ProgressMonitorDialog(vp.getViewSite().getShell());
			try {
				pmd.run(false, true, new IRunnableWithProgress() {
					
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException,
																     InterruptedException {
						
						monitor.beginTask("Ricerca appuntamenti e colloqui in agenda ...", 3);
						monitor.subTask("Ricerca in appuntamenti ...");
						ArrayList resultAppuntamenti = aDAO.listAppuntamentiByAgenteDaA(AppuntamentiModel.class.getName(), 
																						agente.getCodAgente(), 
																						dataDA, 
																						dataA);
						monitor.worked(1);
						monitor.subTask("Ricerca in colloqui ...");
						resultAppuntamenti.addAll(cDAO.listColloquiByAgenteDaA(ColloquiModel.class.getName(),
												  agente.getCodAgente(),
												  dataDA, 
												  dataA));
						monitor.worked(1);
						if (returnView.equalsIgnoreCase(ListaAppuntamentiView.class.getName())){
							lav.setSearchResults(resultAppuntamenti);
						}
						if (returnView.equalsIgnoreCase(CalendarioView.class.getName())){
							cv.setSearchResults(resultAppuntamenti);
						}
						
						monitor.subTask("Restituzione risultati ...");
						monitor.worked(1);
					}
				});
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else{
			ProgressMonitorDialog pmd = new ProgressMonitorDialog(vp.getViewSite().getShell());
			try {
				pmd.run(false, true, new IRunnableWithProgress() {
					
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException,
																     InterruptedException {
						
						monitor.beginTask("Ricerca appuntamenti e colloqui in agenda ...", 3);
						monitor.subTask("Ricerca in appuntamenti ...");
						ArrayList resultAppuntamenti = aDAO.listAppuntamentiByDaA(AppuntamentiModel.class.getName(), 
																				  dataDA, 
																				  dataA);
						monitor.worked(1);
						monitor.subTask("Ricerca in colloqui ...");
						resultAppuntamenti.addAll(cDAO.listColloquiByDaA(ColloquiModel.class.getName(),
												  						 dataDA, 
												  						 dataA));
						monitor.worked(1);
						if (returnView.equalsIgnoreCase(ListaAppuntamentiView.class.getName())){
							lav.setSearchResults(resultAppuntamenti);
						}
						if (returnView.equalsIgnoreCase(CalendarioView.class.getName())){
							cv.setSearchResults(resultAppuntamenti);
						}

						monitor.subTask("Restituzione risultati ...");
						monitor.worked(1);
					}
				});
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
/*			
			MessageDialog.openWarning(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
									  "Selezionare agente", 
									  "Selezionare un agente per eseguire la ricerca ");
*/
		}
	}
	
	
}
