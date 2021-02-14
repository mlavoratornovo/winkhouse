package winkhouse.action.report;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.engine.report.ReportEngine;
import winkhouse.model.ReportModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.report.DettaglioReportView;
import winkhouse.vo.ReportMarkersVO;


public class ReloadReportMarkersAction extends Action {

	public ReloadReportMarkersAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	@Override
	public void run() {
		
		ProgressMonitorDialog pmd = new ProgressMonitorDialog(PlatformUI.getWorkbench()
																		.getActiveWorkbenchWindow()
																		.getShell());
		
		DettaglioReportView drv = (DettaglioReportView)PlatformUI.getWorkbench()
		 														 .getActiveWorkbenchWindow()
		 														 .getActivePage()
		 														 .getActivePart();
		ReportModel rm = drv.getReport();
		MarkerReloader mr = new MarkerReloader(rm);
		
		try {
			pmd.run(true, false,mr); 
			drv.setReport(rm);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	private class MarkerReloader implements IRunnableWithProgress {
		
		private ReportModel rm = null;
		
		public MarkerReloader(ReportModel rm){
			this.rm = rm;
		}
		
		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException,
														 InterruptedException {
			

			ArrayList<ReportMarkersVO> objMarkers = null;
			
			if (rm != null){
				objMarkers = rm.getMarkers();
				monitor.beginTask("lettura segnaposti", objMarkers.size()+1);
				ReportEngine re = new ReportEngine();
				String reportPath = (WinkhouseUtils.getInstance()
												     .getPreferenceStore()
												     .getString(WinkhouseUtils.REPORTTEMPLATEPATH)
												     .equalsIgnoreCase(""))
									? WinkhouseUtils.getInstance()
										     		  .getPreferenceStore()
										     		  .getDefaultString(WinkhouseUtils.REPORTTEMPLATEPATH)
									: WinkhouseUtils.getInstance()
								     				  .getPreferenceStore()
								     				  .getString(WinkhouseUtils.REPORTTEMPLATEPATH);
										     

				ArrayList<ReportMarkersVO> al = re.getextractReportMarkers(reportPath + File.separator +
																		   rm.getCodReport() + File.separator + 
																		   rm.getTemplate());

				monitor.worked(1);
				Iterator<ReportMarkersVO> ital = al.iterator();
				ArrayList toAdd = new ArrayList<ReportMarkersVO>();

				while(ital.hasNext()){

					ReportMarkersVO rmVO = ital.next();
					Iterator<ReportMarkersVO> it = objMarkers.iterator();
					boolean find = false;
					while (it.hasNext()){
						ReportMarkersVO rmalVO = it.next();
						if ((rmalVO.getTipo().equalsIgnoreCase(rmVO.getTipo())) &&
								(rmalVO.getNome().equalsIgnoreCase(rmVO.getNome()))){
							find = true;
							break;
						}					
					}
					monitor.worked(1);
					if (!find){
						toAdd.add(rmVO);
					}

				}

				rm.getMarkers().addAll(toAdd);				
			
			}
		}				

	}

}
