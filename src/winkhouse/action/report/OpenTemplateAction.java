package winkhouse.action.report;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import winkhouse.util.WinkhouseUtils;
import winkhouse.view.report.DettaglioReportView;


public class OpenTemplateAction extends Action {

	public OpenTemplateAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	@Override
	public void run() {
		DettaglioReportView drv = (DettaglioReportView)PlatformUI.getWorkbench()
																 .getActiveWorkbenchWindow()
																 .getActivePage()
																 .getActivePart();

		if (WinkhouseUtils.getInstance()
							.getOpenOfficeWriterPath() != null){
			
			String templateReport = (WinkhouseUtils.getInstance()
													 .getPreferenceStore()
													 .getString(WinkhouseUtils.REPORTTEMPLATEPATH)
													 .equalsIgnoreCase(""))
					                ? WinkhouseUtils.getInstance()
					                				  .getPreferenceStore()
					                				  .getDefaultString(WinkhouseUtils.REPORTTEMPLATEPATH)
					                : WinkhouseUtils.getInstance()
					                				  .getPreferenceStore()
					                				  .getString(WinkhouseUtils.REPORTTEMPLATEPATH);
			
		    if (drv.getReport().getCodReport().intValue() != 0){
				if ((drv.getReport().getTemplate()!= null) && 
					(!drv.getReport().getTemplate().equalsIgnoreCase(""))){
					
					String program = WinkhouseUtils.getInstance()
													 .getOpenOfficeWriterPath() +  " -o \"" +
								     
									 templateReport + File.separator + 
									 drv.getReport().getCodReport() + File.separator +
									 drv.getReport().getTemplate() + "\"";
					
					try {
						Process p = Runtime.getRuntime().exec(program);
					} catch (IOException e) {
						e.printStackTrace();
					}

					
				}else{
					MessageBox mb = new MessageBox(drv.getSite().getShell(),SWT.ERROR);
					mb.setText("Errore");
					mb.setMessage("Impossibile trovare il file del template");			
					mb.open();			
				}
		    }else{
				MessageBox mb = new MessageBox(drv.getSite().getShell(),SWT.ERROR);
				mb.setText("Errore");
				mb.setMessage("E' necessario salvare il report prima di poterlo aprire con OpenOffice");			
				mb.open();			
		    }
			
		}else{
			MessageBox mb = new MessageBox(drv.getSite().getShell(),SWT.ERROR);
			mb.setText("Errore");
			mb.setMessage("OpenOffice Writer non è il programma di default per i file ODT");			
			mb.open();			
		}
	}
	
}
