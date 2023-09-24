package winkhouse.export.helpers;

import org.eclipse.swt.graphics.Image;

import winkhouse.dialogs.custom.ImageViewer;

public class ImmaginiHelper {
	
	/**
	 * Ritorna il visualizzatore di immagini di winkhouse con caricata l'immagine passata in input.
	 */
	public ImageViewer getImageViewer(Image immagine){
		
		ImageViewer iv = new ImageViewer();
		
		iv.showImageViewer();
		iv.getSwtImageCanvas().setImageData(immagine.getImageData());
		iv.getSwtImageCanvas().redraw();
		
		return iv;
	}
	
}
