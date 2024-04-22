package winkhouse.action.immagini;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.dao.ImmaginiDAO;
import winkhouse.model.ImmagineModel;
import winkhouse.orm.Immagini;
import winkhouse.util.ImageProperties;
import winkhouse.view.immobili.ImmaginiImmobiliView;


public class MarcaLocandinaAction extends Action{

	public MarcaLocandinaAction() {
	}

	public MarcaLocandinaAction(String text) {
		super(text);
	}

	public MarcaLocandinaAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public MarcaLocandinaAction(String text, int style) {
		super(text, style);
	}

	@Override
	public void run() {
		
		ImmaginiImmobiliView iiv = (ImmaginiImmobiliView)PlatformUI.getWorkbench()
																   .getActiveWorkbenchWindow()
																   .getActivePage()
																   .findView(ImmaginiImmobiliView.ID);
		/*
		Pattern p = Pattern.compile("\\|");
	    String[] immagineStr = p.split(iiv.getGallery().getSelection()[0].getText());					
		*/
		Immagini im = (Immagini)iiv.getGallery().getSelection()[0].getData();
//	    ImmaginiDAO iDAO = new ImmaginiDAO();
	    
	    if (!im.getProprieta().containsKey(ImageProperties.LOCANDINA)){
	    	im.setImgprops((((im.getImgprops() != null)&&(!im.getImgprops().equalsIgnoreCase("")))
	    					  ?""
	    					  :im.getImgprops() + ImageProperties.PROPERTY_SEPARATOR) + 
	    					   ImageProperties.LOCANDINA);
	    }else{
	    	im.setImgprops("");	    	
	    }
		
		//iDAO.saveUpdate(im, null, true);
		
		//iiv.getImmobile().setImmagini(null);
		iiv.showImages();
		
	}
	
	
	
}
