package winkhouse.action.report;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import winkhouse.dao.ReportDAO;
import winkhouse.helper.OptimisticLockHelper;
import winkhouse.helper.ReportHelper;
import winkhouse.model.ReportModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.report.DettaglioReportView;
import winkhouse.view.report.ReportTreeView;


public class SalvaReport extends Action {

	public SalvaReport() {
	}

	public SalvaReport(String text) {
		super(text);
	}

	public SalvaReport(String text, ImageDescriptor image) {
		super(text, image);
	}

	public SalvaReport(String text, int style) {
		super(text, style);
	}

	@Override
	public void run() {
		
		ProgressMonitorDialog pmd = new ProgressMonitorDialog(PlatformUI.getWorkbench()
																		.getActiveWorkbenchWindow()
																		.getShell());
		
		try {
			pmd.run(false, false, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
																 InterruptedException {
					
					DettaglioReportView drv = (DettaglioReportView)PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							  												 .getActivePage()
							  												 .getActivePart();
					
					ReportTreeView rtv = (ReportTreeView)PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							  									   .getActivePage()
							  									   .findView(ReportTreeView.ID);
					
					if (drv != null){
						ReportModel rm = drv.getReport();
						
						if ((rm.getNome() != null) && (!rm.getNome().equalsIgnoreCase(""))){
							
							if (!rm.getTipo().equalsIgnoreCase("")){
								
								OptimisticLockHelper olh = new OptimisticLockHelper();
								
								rm.setDateUpdate(new Date());
								if (WinkhouseUtils.getInstance().getLoggedAgent() != null){
									rm.setCodUserUpdate(WinkhouseUtils.getInstance().getLoggedAgent().getCodAgente());
								}
								
								String decision = olh.checkOLReport(rm);
								
								if (decision.equalsIgnoreCase(OptimisticLockHelper.SOVRASCRIVI)){
								
									ReportHelper rh = new ReportHelper();
									if (!rh.saveReport(rm)){
										MessageBox mb = new MessageBox(drv.getSite().getShell(),SWT.ERROR);
										mb.setText("Errore salvataggio");
										mb.setMessage("Si � verificato un errore nel salvataggio del report");			
										mb.open();
										
									}
									rm.setMarkers(null);
									drv.setReport(rm);
									rtv.getViewer().refresh();
									
								}else if (decision.equalsIgnoreCase(OptimisticLockHelper.VISUALIZZA)){
									
									try {
										DettaglioReportView div_comp = (DettaglioReportView)PlatformUI.getWorkbench()
																									 	  .getActiveWorkbenchWindow()
																									 	  .getActivePage()
																									 	  .showView(DettaglioReportView.ID,
																								        		   	 String.valueOf(rm.getCodReport()) + "Comp",
																								        		   	 IWorkbenchPage.VIEW_CREATE);
										ReportDAO r_compDAO = new ReportDAO();
										ReportModel report_comp = (ReportModel)r_compDAO.getReportByID(rm.getCodReport());
										
										div_comp.setReport(report_comp);
										div_comp.setCompareView(false);
										
									} catch (PartInitException e) {
										e.printStackTrace();
									}
								}
								
							}else{
								MessageBox mb = new MessageBox(drv.getSite().getShell(),SWT.ERROR);
								mb.setText("Errore salvataggio");
								mb.setMessage("Selezionare la tipologia del report");			
								mb.open();								
							}
						}else{
							MessageBox mb = new MessageBox(drv.getSite().getShell(),SWT.ERROR);
							mb.setText("Errore salvataggio");
							mb.setMessage("Inserire il nome del report");			
							mb.open();
						}	
					}
					
				}
			});
		} catch (InvocationTargetException e) {
			MessageBox mb = new MessageBox(PlatformUI.getWorkbench()
													 .getActiveWorkbenchWindow()
													 .getShell(),SWT.ERROR);
			mb.setText("Errore salvataggio");
			mb.setMessage("Inserire il nome del report");			
			mb.open();
		} catch (InterruptedException e) {
			MessageBox mb = new MessageBox(PlatformUI.getWorkbench()
													 .getActiveWorkbenchWindow()
													 .getShell(),SWT.ERROR);
			mb.setText("Errore salvataggio");
			mb.setMessage("Inserire il nome del report");			
			mb.open();
		}

	}
	
}
