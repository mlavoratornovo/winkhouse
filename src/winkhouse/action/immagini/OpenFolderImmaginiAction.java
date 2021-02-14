package winkhouse.action.immagini;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.util.WinkhouseUtils;
import winkhouse.view.immobili.ImmaginiImmobiliView;

public class OpenFolderImmaginiAction extends Action {

	public OpenFolderImmaginiAction() {
		// TODO Auto-generated constructor stub
	}

	public OpenFolderImmaginiAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public OpenFolderImmaginiAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public OpenFolderImmaginiAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void run() {
		
		ImmaginiImmobiliView iiv = null;
		
		iiv = (ImmaginiImmobiliView)PlatformUI.getWorkbench()
									 .getActiveWorkbenchWindow()
				  					 .getActivePage()
				  					 .getActivePart();
		
		if ((iiv.getImmobile() != null) && (iiv.getImmobile().getCodImmobile() != null)){
			
			String pathRepositoryAllegati = (WinkhouseUtils.getInstance()
					   									   .getPreferenceStore()
					   									   .getString(WinkhouseUtils.IMAGEPATH)
					   									   .equalsIgnoreCase(""))
					   		                 ? WinkhouseUtils.getInstance()
					   		                		 	     .getPreferenceStore()
					   		                		 	     .getDefaultString(WinkhouseUtils.IMAGEPATH)
					   		                 : WinkhouseUtils.getInstance()
					   		                 				 .getPreferenceStore()
					   		                 				 .getString(WinkhouseUtils.IMAGEPATH);
	       				
			File f = new File(pathRepositoryAllegati + File.separator + iiv.getImmobile().getCodImmobile().toString());
			if (!f.exists()){
				f.mkdirs();
			}
			try {
				Desktop.getDesktop().open(f);
			} catch (IOException e1) {
				MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
							            "Apertura cartella", 
										"Impossibile aprire la cartella delle immagini");
			}
		}else{
			MessageDialog.openWarning(PlatformUI.getWorkbench()
												.getActiveWorkbenchWindow()
												.getShell(),
									  "Attenzione", 
									  "Selezionare un dettaglio immobile");
		}
			
	}

	
}
