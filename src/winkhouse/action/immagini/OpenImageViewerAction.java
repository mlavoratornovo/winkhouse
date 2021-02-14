package winkhouse.action.immagini;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.ui.PlatformUI;

import winkhouse.dialogs.custom.ImageViewer;
import winkhouse.view.immobili.ImmaginiImmobiliView;

public class OpenImageViewerAction extends Action {
	
	public OpenImageViewerAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	@Override
	public void run() {
		ImmaginiImmobiliView immaginiView = (ImmaginiImmobiliView)PlatformUI.getWorkbench()
																			.getActiveWorkbenchWindow()
																			.getActivePage()
																			.findView(ImmaginiImmobiliView.ID);
		
		GalleryItem[] gi = immaginiView.getGallery().getSelection();			
		if ((gi[0] != null)&&(gi[0].getData() != null)){
			ImageViewer iv = new ImageViewer();
			iv.showImageViewer();
			iv.getSwtImageCanvas().setImageData(gi[0].getImage().getImageData());
			iv.getSwtImageCanvas().redraw();
			iv.show();
			
		}
	}

}
