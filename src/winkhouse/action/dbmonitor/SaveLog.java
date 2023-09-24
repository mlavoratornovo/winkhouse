package winkhouse.action.dbmonitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import winkhouse.view.db.DBMonitorView;

public class SaveLog extends Action {

	public SaveLog(String text, ImageDescriptor image) {
		super(text, image);
		setToolTipText("Salva il Log su file");
	}

	@Override
	public void run() {
		
		DBMonitorView mv = (DBMonitorView)PlatformUI.getWorkbench()
				  						  			.getActiveWorkbenchWindow()
				  						  			.getActivePage()
				  						  			.findView(DBMonitorView.ID);

		FileDialog fd = new FileDialog(PlatformUI.getWorkbench()
												 .getActiveWorkbenchWindow()
												 .getShell(), SWT.SAVE);
		fd.setText("Salva le istruzioni visualizzate");
        fd.setFilterPath("C:/");
        String[] filterExt = { "*.txt","*.log"};
        fd.setFilterExtensions(filterExt);
        String selected = fd.open();
        System.out.println(selected);
        File filesave = new File(selected);
        	
    	try {
			        		
    		FileWriter fw = null;
    		
			if (filesave.createNewFile()){
				fw = new FileWriter(filesave,false);
				fw.append(mv.getConsole().getText());
				
			}else{
				fw = new FileWriter(filesave,true);
				fw.append(mv.getConsole().getText());
			}
			
			if (fw != null){
				fw.flush();
				fw.close();
			}
			
		} catch (IOException e) {
			MessageDialog.openError(PlatformUI.getWorkbench()
											  .getActiveWorkbenchWindow()
											  .getShell(),
									"Errore salvataggio file", 
									"Impossibile scrivere nel file di destinazione \n " +
									"controllare i permessi su file e cartella");
		}
        	
        
        		
	}


}
