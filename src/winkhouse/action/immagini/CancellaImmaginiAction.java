package winkhouse.action.immagini;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.ImmaginiDAO;
import winkhouse.model.ImmagineModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.immobili.ImmaginiImmobiliView;



public class CancellaImmaginiAction extends Action {

	public CancellaImmaginiAction() {
	}

	public CancellaImmaginiAction(String text) {
		super(text);
	}

	public CancellaImmaginiAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public CancellaImmaginiAction(String text, int style) {
		super(text, style);
	}

	@Override
	public void run() {
		
		ImmaginiImmobiliView iiv = (ImmaginiImmobiliView)PlatformUI.getWorkbench()
																   .getActiveWorkbenchWindow()
																   .getActivePage()
																   .findView(ImmaginiImmobiliView.ID);
		
		/*Pattern p = Pattern.compile("\\|");
	    String[] immagineStr = p.split(iiv.getGallery().getSelection()[0].getText());					
		*/
		ImmagineModel im = (ImmagineModel)iiv.getGallery().getSelection()[0].getData();
		/*ImmagineVO iVO = new ImmagineVO();
		iVO.setCodImmobile(new Integer(immagineStr[0]));
		iVO.setPathImmagine(immagineStr[1]);
		iVO.setOrdine(new Integer(immagineStr[2]));
		iVO.setCodImmagine(new Integer(immagineStr[3]));
		*/
		ImmaginiDAO iDAO = new ImmaginiDAO();
		
		String pathImmagini = WinkhouseUtils.getInstance()
		   									  .getPreferenceStore()
		   									  .getString(WinkhouseUtils.IMAGEPATH);
		
		if (iDAO.delete(im.getCodImmagine(),null,true)){
			
			File f  = new File(pathImmagini + File.separator + File.separator + 
							   im.getCodImmobile() + File.separator + File.separator + 
							   im.getPathImmagine());
			
			if (f.exists()){
				if (!f.delete()){
					MessageBox mb = new MessageBox(Activator.getDefault()
															.getWorkbench()
															.getActiveWorkbenchWindow().getShell(),
												   SWT.ICON_WARNING);
					mb.setMessage("Riferimenti dell'immagine in banca dati eliminati. \n Errore nella cancellazione del file immagine su disco");
					mb.open();
	
					
				}
			}else{
				MessageBox mb = new MessageBox(Activator.getDefault()
														.getWorkbench()
														.getActiveWorkbenchWindow().getShell(),
											   SWT.ICON_WARNING);
				
				mb.setMessage("Riferimenti dell'immagine in banca dati eliminati. \n Il file di immagine non è presente su disco");
				mb.open();
				
			}
			iiv.getImmobile().setImmagini(null);
			iiv.showImages();

			
		}else{
			MessageBox mb = new MessageBox(Activator.getDefault()
													.getWorkbench()
													.getActiveWorkbenchWindow().getShell(),
										   SWT.ERROR);
			mb.setMessage("Errore cancellazione immagine");
			mb.open();	
		}
		
	}
	
}
