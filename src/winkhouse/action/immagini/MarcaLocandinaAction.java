package winkhouse.action.immagini;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.dao.ImmaginiDAO;
import winkhouse.model.ImmagineModel;
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
		ImmagineModel im = (ImmagineModel)iiv.getGallery().getSelection()[0].getData();
	    ImmaginiDAO iDAO = new ImmaginiDAO();
	    
	    if (!im.getPropieta().containsKey(ImageProperties.LOCANDINA)){
	    	im.setImgPropsStr((((im.getImgPropsStr() != null)&&(!im.getImgPropsStr().equalsIgnoreCase("")))
	    					  ?""
	    					  :im.getImgPropsStr() + ImageProperties.PROPERTY_SEPARATOR) + 
	    					   ImageProperties.LOCANDINA);
	    }else{
	    	im.setImgPropsStr("");	    	
	    }
		
		iDAO.saveUpdate(im, null, true);
		
		iiv.getImmobile().setImmagini(null);
		iiv.showImages();
		
	}
	
	
	
}
