package winkhouse.action.immagini;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import winkhouse.dialogs.custom.ImageViewer;

public class ZoomInImageAction extends Action {

	private ImageViewer imageViewer = null;
	
	public ZoomInImageAction(ImageViewer imageViewer, String text, ImageDescriptor image) {
		super(text, image);
		this.imageViewer = imageViewer;
	}

	@Override
	public void run() {
	
		if (this.imageViewer != null){
			
			this.imageViewer.getSwtImageCanvas().zoomIn();
			
		}

	}
	
}
