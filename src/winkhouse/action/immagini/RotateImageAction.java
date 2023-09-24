package winkhouse.action.immagini;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

import winkhouse.dialogs.custom.ImageViewer;

public class RotateImageAction extends Action {
	
	ImageViewer imageViewer = null;
	
	public RotateImageAction(ImageViewer imageViewer, String text, ImageDescriptor image) {
		super(text, image);
		this.imageViewer = imageViewer;
	}

	@Override
	public void run() {
		ImageData src=imageViewer.getSwtImageCanvas().getImageData();
		if(src==null) return;
		PaletteData srcPal=src.palette;
		PaletteData destPal;
		ImageData dest;
		/* construct a new ImageData */
		if(srcPal.isDirect){
			destPal=new PaletteData(srcPal.redMask,srcPal.greenMask,srcPal.blueMask);
		}else{
			destPal=new PaletteData(srcPal.getRGBs());
		}
		dest=new ImageData(src.height,src.width,src.depth,destPal);
		/* rotate by rearranging the pixels */
		for(int i=0;i<src.width;i++){
			for(int j=0;j<src.height;j++){
				int pixel=src.getPixel(i,j);
				dest.setPixel(j,src.width-1-i,pixel);
			}
		}
		imageViewer.getSwtImageCanvas().setImageData(dest);
		return;	}
	
	
}
