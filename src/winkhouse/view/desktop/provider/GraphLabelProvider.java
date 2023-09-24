package winkhouse.view.desktop.provider;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IFigureProvider;

import winkhouse.Activator;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.desktop.figure.NodeFigure;
import winkhouse.view.desktop.model.MyConnection;
import winkhouse.view.desktop.model.MyNode;

public class GraphLabelProvider extends LabelProvider 
								implements IFigureProvider{
	
	private ImageData nodo = Activator.getImageDescriptor("icons/postit.png").getImageData();
	private ImageData nodosel = Activator.getImageDescriptor("icons/postitsel.png").getImageData();
	private ImageData immobile = Activator.getImageDescriptor("icons/gohome128.png").getImageData();
	private ImageData immobilesel = Activator.getImageDescriptor("icons/gohome128sel.png").getImageData();
	private ImageData anagrafica = Activator.getImageDescriptor("icons/personal.png").getImageData();
	private ImageData anagraficasel = Activator.getImageDescriptor("icons/personalsel.png").getImageData();
	private ImageData affitti = Activator.getImageDescriptor("icons/affitti128.png").getImageData();
	private ImageData affittisel = Activator.getImageDescriptor("icons/affitti128sel.png").getImageData();
	private ImageData colloqui = Activator.getImageDescriptor("icons/colloqui128.png").getImageData();
	private ImageData colloquisel = Activator.getImageDescriptor("icons/colloqui128sel.png").getImageData();
	private ImageData urllink = Activator.getImageDescriptor("icons/promemoria_url.png").getImageData();
	private ImageData urllinksel = Activator.getImageDescriptor("icons/promemoria_url_sel.png").getImageData();
	private ImageData filelink = Activator.getImageDescriptor("icons/promemoria_link.png").getImageData();
	private ImageData filelinksel = Activator.getImageDescriptor("icons/promemoria_link_sel.png").getImageData();
	private ImageData anagraficheabbinate = Activator.getImageDescriptor("icons/anagraficheabbinateDesktop.png").getImageData();
	private ImageData anagraficheabbinatesel = Activator.getImageDescriptor("icons/anagraficheabbinateDesktopSel.png").getImageData();
	
	@Override
	public IFigure getFigure(Object element) {
		if (element instanceof MyNode) {
			Image img = null;
			IFigure figure = null;
			
			if (((MyNode)element).isSelected()){
				if (((MyNode)element).getType() == WinkhouseUtils.PROMEMORIA){
					img = new Image(Display.getCurrent(), nodosel);
					figure = new NodeFigure(img,((MyNode)element).getName());
					figure.setSize(128, 128);
					
				}
				if (((MyNode)element).getType() == WinkhouseUtils.ANAGRAFICHE){
					img = new Image(Display.getCurrent(), anagraficasel);
					img.getImageData().scaledTo(128, 128);
					figure = new NodeFigure(img,((MyNode)element).getName());
					figure.setSize(128, 128);
					
				}
				if (((MyNode)element).getType() == WinkhouseUtils.IMMOBILI){
					img = new Image(Display.getCurrent(), immobilesel);
					img.getImageData().scaledTo(128, 128);
					figure = new NodeFigure(img,((MyNode)element).getName());
					figure.setSize(128, 128);
					
				}
				if (((MyNode)element).getType() == WinkhouseUtils.COLLOQUI){
					img = new Image(Display.getCurrent(), colloquisel);
					img.getImageData().scaledTo(128, 128);
					figure = new NodeFigure(img,((MyNode)element).getName());
					figure.setSize(128, 128);
					
				}				
				if (((MyNode)element).getType() == WinkhouseUtils.AFFITTI){
					img = new Image(Display.getCurrent(), affittisel);
					img.getImageData().scaledTo(128, 128);
					figure = new NodeFigure(img,((MyNode)element).getName());
					figure.setSize(128, 128);
					
				}				
				if (((MyNode)element).getType() == MyNode.FILELINK){
					img = new Image(Display.getCurrent(), filelinksel);
					img.getImageData().scaledTo(128, 128);
					figure = new NodeFigure(img,((MyNode)element).getName());
					figure.setSize(128, 128);
					
				}				
				if (((MyNode)element).getType() == MyNode.URLLINK){
					img = new Image(Display.getCurrent(), urllinksel);
					img.getImageData().scaledTo(128, 128);
					figure = new NodeFigure(img,((MyNode)element).getName());
					figure.setSize(128, 128);
					
				}				
				
			}else{
				if (((MyNode)element).getType() == WinkhouseUtils.PROMEMORIA){
					img = new Image(Display.getCurrent(), nodo);
					figure = new NodeFigure(img,((MyNode)element).getName());
					figure.setSize(128, 128);
					
				}
				if (((MyNode)element).getType() == WinkhouseUtils.ANAGRAFICHE){
					img = new Image(Display.getCurrent(), anagrafica);
					img.getImageData().scaledTo(128, 128);
					figure = new NodeFigure(img,((MyNode)element).getName());
					figure.setSize(128, 128);
					
				}
				if (((MyNode)element).getType() == WinkhouseUtils.IMMOBILI){
					img = new Image(Display.getCurrent(), immobile);
					img.getImageData().scaledTo(128, 128);
					figure = new NodeFigure(img,((MyNode)element).getName());
					figure.setSize(128, 128);
					
				}
				if (((MyNode)element).getType() == WinkhouseUtils.COLLOQUI){
					img = new Image(Display.getCurrent(), colloqui);
					img.getImageData().scaledTo(128, 128);
					figure = new NodeFigure(img,((MyNode)element).getName());
					figure.setSize(128, 128);
					
				}				
				if (((MyNode)element).getType() == WinkhouseUtils.AFFITTI){
					img = new Image(Display.getCurrent(), affitti);
					img.getImageData().scaledTo(128, 128);
					figure = new NodeFigure(img,((MyNode)element).getName());
					figure.setSize(128, 128);
					
				}				
				if (((MyNode)element).getType() == MyNode.FILELINK){
					img = new Image(Display.getCurrent(), filelink);
					img.getImageData().scaledTo(128, 128);
					figure = new NodeFigure(img,((MyNode)element).getName());
					figure.setSize(128, 128);
					
				}				
				if (((MyNode)element).getType() == MyNode.URLLINK){
					img = new Image(Display.getCurrent(), urllink);
					img.getImageData().scaledTo(128, 128);
					figure = new NodeFigure(img,((MyNode)element).getName());
					figure.setSize(128, 128);
					
				}				
				
			}

			return figure;
		}
		if (element instanceof MyConnection) {
			
		}
		return null;
	}

	@Override
	public String getText(Object element) {
	  
		if (element instanceof MyNode) {
			MyNode myNode = (MyNode) element;
			return myNode.getName();
		}

		if (element instanceof MyConnection) {
			MyConnection myConnection = (MyConnection) element;
			return myConnection.getLabel();
		}

		if (element instanceof EntityConnectionData) {
			EntityConnectionData test = (EntityConnectionData) element;
			return "";
		}
		
		throw new RuntimeException("Wrong type: " + element.getClass().toString());
		
  }

}
