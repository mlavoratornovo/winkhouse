package winkhouse.view.immobili;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.nebula.widgets.gallery.ListItemRenderer;
import org.eclipse.nebula.widgets.gallery.NoGroupRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.immagini.AggiungiImmaginiAction;
import winkhouse.action.immagini.CancellaImmaginiAction;
import winkhouse.action.immagini.MarcaLocandinaAction;
import winkhouse.action.immagini.OpenFolderImmaginiAction;
import winkhouse.action.immagini.OpenImageViewerAction;
import winkhouse.action.immagini.PushDownImmagineAction;
import winkhouse.action.immagini.PushUpImmagineAction;
import winkhouse.action.immagini.RefreshImmaginiAction;
import winkhouse.model.ImmagineModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.util.ImageProperties;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.ImmagineVO;



public class ImmaginiImmobiliView extends ViewPart {

	public final static String ID = "winkhouse.immaginiimmobiliview";
		
	private Gallery gallery = null;
	private ImmobiliModel immobile = null;
	private String pathImmagini = Activator.getDefault().getStateLocation().toFile() + 
								  File.separator + "tmpimmagini";
	private String pathImmaginiServer = WinkhouseUtils.getInstance()
														.getPreferenceStore()
														.getString(WinkhouseUtils.IMAGEPATH);
	
	private final static String ERROR_IMAGE_FILENAME = "no-photo.jpg";

	private AggiungiImmaginiAction aia = null;
	private CancellaImmaginiAction cia = null;
	private PushUpImmagineAction puia = null;
	private PushDownImmagineAction pdia = null;
	private MarcaLocandinaAction mla = null;
	private RefreshImmaginiAction ria = null;
	private OpenFolderImmaginiAction ofia = null; 
	
	public ImmaginiImmobiliView() {

	}
	
	Comparator<ImmagineVO> comparatorImmagine = new Comparator<ImmagineVO>(){

		@Override
		public int compare(ImmagineVO o1, ImmagineVO o2) {
		
			if (o1.getOrdine().intValue() == o2.getOrdine().intValue()){
				return 0;
			}else if (o1.getOrdine().intValue() < o2.getOrdine().intValue()){
				return -1;
			}else{
				return 1;
			}
			
		}
		
	};

	@Override
	public void createPartControl(Composite parent) {
		FormToolkit ft = new FormToolkit(getViewSite().getShell().getDisplay());
		Form f = ft.createForm(parent);
		f.setImage(Activator.getImageDescriptor("icons/immagini.png").createImage());
		f.setText("Immagini");
		f.getBody().setLayout(new GridLayout());

		OpenImageViewerAction oiva = new OpenImageViewerAction("Visualizza dettaglio immagine",
															   Activator.getImageDescriptor("icons/kfind.png"));
		getViewSite().getActionBars().getToolBarManager().add(oiva);
		aia = new AggiungiImmaginiAction("Aggiungi immagini",
										 Activator.getImageDescriptor("icons/pathimmagini.png"));
		getViewSite().getActionBars().getToolBarManager().add(aia);
		cia = new CancellaImmaginiAction("Cancella immagini",
										 Activator.getImageDescriptor("icons/button_cancel.png"));
		getViewSite().getActionBars().getToolBarManager().add(cia);
		
		puia = new PushUpImmagineAction("Sposta su",
			    						Activator.getImageDescriptor("icons/1uparrow.png"));
		getViewSite().getActionBars().getToolBarManager().add(puia);
		
		pdia = new PushDownImmagineAction("Sposta giu",
										  Activator.getImageDescriptor("icons/1downarrow.png"));
		getViewSite().getActionBars().getToolBarManager().add(pdia);
		
		mla = new MarcaLocandinaAction("Seleziona per locandina",
									   Activator.getImageDescriptor("icons/document.png"));
		getViewSite().getActionBars().getToolBarManager().add(mla);
		
		ria = new RefreshImmaginiAction("Aggiorna immagini",
										Activator.getImageDescriptor("/icons/adept_reinstall.png"));
		getViewSite().getActionBars().getToolBarManager().add(ria);
		
		ofia = new OpenFolderImmaginiAction("Apri cartella immagini", 
											Activator.getImageDescriptor("/icons/folder.png"));
		
		getViewSite().getActionBars().getToolBarManager().add(ofia);
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;

		gallery = new Gallery(f.getBody(), SWT.V_SCROLL);
		gallery.setLayoutData(gdFillHV);
		
		NoGroupRenderer gr = new NoGroupRenderer();		
		gr.setMinMargin(2);
		gr.setItemHeight(200);
		gr.setItemWidth(300);
		gr.setAutoMargin(true);
		
		gallery.setGroupRenderer(gr);
		
		ListItemRenderer ir = new ListItemRenderer();
		ir.setShowLabels(true);
		ir.setSelectionBackgroundColor(new Color(null,100,170,100));
		gallery.setItemRenderer(ir);		

		
		//int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT;
		/*DropTarget target = new DropTarget(gallery, operations);
		
		final TextTransfer textTransfer = TextTransfer.getInstance();
		final FileTransfer fileTransfer = FileTransfer.getInstance();
		Transfer[] types = new Transfer[] {fileTransfer, textTransfer};
		target.setTransfer(types);
		
		target.addDropListener(new DropTargetListener(){

			@Override
			public void dragEnter(DropTargetEvent event) {
				 if (event.detail == DND.DROP_DEFAULT) {
					 if ((event.operations & DND.DROP_COPY) != 0) {
						 event.detail = DND.DROP_COPY;
					 } else {
						 event.detail = DND.DROP_NONE;
					 }
				 }
				
				 for (int i = 0; i < event.dataTypes.length; i++) {
					 if (fileTransfer.isSupportedType(event.dataTypes[i])){
						 event.currentDataType = event.dataTypes[i];
						 if (event.detail != DND.DROP_COPY) {
							 event.detail = DND.DROP_NONE;
						 }
					     break;
					 }
				 }
				
			}

			@Override
			public void dragLeave(DropTargetEvent event) {
		
				
			}

			@Override
			public void dragOperationChanged(DropTargetEvent event) {
			    if (event.detail == DND.DROP_DEFAULT) {
			    	if ((event.operations & DND.DROP_COPY) != 0) {
			    		event.detail = DND.DROP_COPY;
			    	} else {
			    		event.detail = DND.DROP_NONE;
			    	}
			    }
			    
			    if (fileTransfer.isSupportedType(event.currentDataType)){
			    	if (event.detail != DND.DROP_COPY) {
			    		event.detail = DND.DROP_NONE;
			    	}
			    }
			}

			@Override
			public void dragOver(DropTargetEvent event) {
				 event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
				 if (textTransfer.isSupportedType(event.currentDataType)) {
				 
					 Object o = textTransfer.nativeToJava(event.currentDataType);
					 String t = (String)o;

				 }			
			}

			@Override
			public void drop(DropTargetEvent event) {
				if (textTransfer.isSupportedType(event.currentDataType)) {
					String text = (String)event.data;
				}
				if (fileTransfer.isSupportedType(event.currentDataType)){
					String[] files = (String[])event.data;
					ImmaginiDAO iDAO = new ImmaginiDAO();
					for (int i = 0; i < files.length; i++) {
						String nomeFile = files[i].substring(files[i].lastIndexOf("\\")+1,files[i].length());
						String or = files[i];
						String dest = pathImmagini + File.separator +  
									  immobile.getCodImmobile() + File.separator + nomeFile;						

						if (WinkhouseUtils.getInstance().copiaFile(or,dest)){
							ImmagineVO iVO = new ImmagineVO();
							iVO.setCodImmobile(immobile.getCodImmobile());
							iVO.setOrdine(immobile.getImmagini().size()+1);
							iVO.setPathImmagine(nomeFile);

							immobile.getImmagini().add(iVO);
							Collections.sort(immobile.getImmagini(), comparatorImmagine);
							showImages();
				
						}else{
							MessageBox mb = new MessageBox(Activator.getDefault()
																	.getWorkbench().getActiveWorkbenchWindow().getShell(),
														   SWT.ERROR);
						    mb.setMessage("Errore nel copiare l'immagine");
						    mb.open();										

						}
					}
			}	}		

			@Override
			public void dropAccept(DropTargetEvent event) {

				
			}
			
		});*/

		gallery.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
		
				
			}

			@Override
			public void keyReleased(KeyEvent e) {			
				if (e.keyCode == 127){
					CancellaImmaginiAction cia = new CancellaImmaginiAction();
					cia.run();
				}
			}
		});
	}
	
	@Override
	public void setFocus() {
	}

	public void showImages(){
		
		for (int i = 0; i < gallery.getItems().length; i++) {
			gallery.remove(gallery.getItems()[i]);
		} 
		
		
		GalleryItem group = new GalleryItem(gallery, SWT.NONE);
		if (this.immobile != null){
			if (this.immobile.getImmagini() != null){
				
				Iterator<ImmagineModel> it = this.immobile.getImmagini().iterator();
				while (it.hasNext()){
					
					ImmagineModel iModel = it.next();
					GalleryItem item = new GalleryItem(group, SWT.NONE);
					item.setExpanded(true);
					Image i = null;
					
					try{
						
						File f = new File(((iModel.getCodImmagine() == 0)?pathImmagini:pathImmaginiServer) + File.separator + File.separator + 
										  iModel.getCodImmobile() + File.separator + File.separator + iModel.getPathImmagine());

						if (!f.exists()){
							i = Activator.getImageDescriptor("icons/no-photo.jpg").createImage();
						}else{
							i = new Image(Activator.getDefault().getWorkbench().getDisplay(),f.getPath());							
						}
						
	
						Image itemImage = new Image(Activator.getDefault().getWorkbench().getDisplay(), 
													i.getImageData());
						
						
						if (itemImage != null) {
							item.setImage(itemImage);
							if (iModel.getPropieta().containsKey(ImageProperties.LOCANDINA)){
								item.setText("Locandina");						
							}
						}
					
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						item.setData(iModel);
					}
					
				}
			}
		}
		

	}
	
	public ImmobiliModel getImmobile() {
		return immobile;
	}

	public void setImmobile(ImmobiliModel immobile) {
		this.immobile = immobile;
		if ((immobile != null) && (this.immobile.getImmagini() != null)){
			Collections.sort(this.immobile.getImmagini(), comparatorImmagine);
		}
		showImages();
	}

	public Gallery getGallery() {
		return gallery;
	}

	public void setCompareView(boolean enabled){
		
		aia.setEnabled(!enabled);
		cia.setEnabled(!enabled);
		puia.setEnabled(!enabled);
		pdia.setEnabled(!enabled);
		mla.setEnabled(!enabled);
		ria.setEnabled(!enabled);

	}
}
