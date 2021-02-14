package winkhouse.action.immagini;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.ui.PlatformUI;

import winkhouse.dao.ImmaginiDAO;
import winkhouse.model.ImmagineModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.view.immobili.ImmaginiImmobiliView;


public class PushDownImmagineAction extends Action {

	public PushDownImmagineAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public void run() {
		ImmaginiImmobiliView immaginiView = (ImmaginiImmobiliView)PlatformUI.getWorkbench()
													  						.getActiveWorkbenchWindow()
																		    .getActivePage()
																			.findView(ImmaginiImmobiliView.ID);
		ImmobiliModel im = immaginiView.getImmobile();
		ImmaginiDAO iDAO = new ImmaginiDAO();
		if (im != null){
			GalleryItem[] gi = immaginiView.getGallery().getSelection();			
			if ((gi[0] != null)&&(gi[0].getData() != null)){
				Iterator it = im.getImmagini().iterator();
				while (it.hasNext()){
					ImmagineModel img = (ImmagineModel)it.next();
					if (img.getOrdine() == im.getImmagini().size()){
						img.setOrdine(1);
					}else{
						img.setOrdine(img.getOrdine() + 1);
					}

					if (!iDAO.saveUpdate(img, null, true)){
						MessageDialog.openError(PlatformUI.getWorkbench()
														  .getActiveWorkbenchWindow()
														  .getShell(), 
														  "Errore salvaggio immagine", 
														  "Si è verificato un errore nell'aggiornamento dell'ordine dell'immagine");
					}
				}
				
			}else{
				MessageDialog.openWarning(PlatformUI.getWorkbench()
						  							.getActiveWorkbenchWindow()
						  							.getShell(), 
						  				  "Attenzione", 
						  				  "Selezionare una immagine");
				
			}
			im.setImmagini(null);
			immaginiView.setImmobile(im);
		}
		
	}

}
