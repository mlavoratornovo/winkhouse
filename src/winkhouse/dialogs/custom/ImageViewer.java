package winkhouse.dialogs.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import winkhouse.Activator;
import winkhouse.action.immagini.RotateImageAction;
import winkhouse.action.immagini.ZoomInImageAction;
import winkhouse.action.immagini.ZoomOutImageAction;

public class ImageViewer {

	public final static String ID = "winkhouse.imageviewer";
	
	private SWTImageCanvas swtImageCanvas = null;
	private Shell s = null;
	
	public ImageViewer(){
		
	}
	
	public void showImageViewer(){
		
		GridLayout gl = new GridLayout();
		s = new Shell(PlatformUI.createDisplay(),SWT.CLOSE|SWT.MAX|SWT.MIN|SWT.TITLE|SWT.RESIZE|SWT.APPLICATION_MODAL);
		s.setImage(Activator.getImageDescriptor("icons/immagini.png").createImage());
		FormToolkit ft = new FormToolkit(Activator.getDefault()
												  .getWorkbench()
												  .getDisplay());
		
		s.setLayout(gl);
		s.setBackground(new Color(null,255,255,255));
		
		Form f = ft.createForm(s);
		f.getToolBarManager().add(new ZoomInImageAction(this, 
														"Ingrandimento", 
														Activator.getImageDescriptor("icons/zoomin.png")));
		f.getToolBarManager().add(new ZoomOutImageAction(this, 
								  					    "Riduzione", 
								  					    Activator.getImageDescriptor("icons/zoomout.png")));
		f.getToolBarManager().add(new RotateImageAction(this, 
				    									"Rotazione", 
				    									Activator.getImageDescriptor("icons/adept_reinstall.png")));

		f.getToolBarManager().update(false);
		f.getBody().setLayout(gl);
		f.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		swtImageCanvas = new SWTImageCanvas(f.getBody(),SWT.BORDER);
		swtImageCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		
	}
	
	public void show(){
		if (s != null){
			s.open();
		} 
	}

	public SWTImageCanvas getSwtImageCanvas() {
		return swtImageCanvas;
	}

	public void setSwtImageCanvas(SWTImageCanvas swtImageCanvas) {
		this.swtImageCanvas = swtImageCanvas;
	}
	

}
