package winkhouse.dialogs.custom;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.PojoProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import winkhouse.Activator;
import winkhouse.dao.GDataDAO;
import winkhouse.helper.GDataHelper;
import winkhouse.helper.GoogleCalendarV3Helper;
import winkhouse.model.ContattiModel;
import winkhouse.model.GDataModel;
import winkhouse.util.WinkhouseUtils;

public class GoogleConfDialog {

	public final static String ID = "winkhouse.googleconf";
	
	private Form f = null;
	private FormToolkit ft = null;
	private GridData gd = null;
	private GridData gd_h = null;
	private GridLayout gl_info = null;
	private GridData gd_h_r = null;
	
	private Text tindirizzo = null;	
	private Text tpassword = null;

	private TableViewer tvIndirizziCal = null;
	
	private Shell s = null;
	private ContattiModel contatto = null;
	
	GDataModel gdM = null;
	
	public GoogleConfDialog(ContattiModel contatto) {
		this.contatto = contatto;
		createDialog();	
	}
	
	public void bindData(){
		GDataDAO gdDAO = new GDataDAO();
		this.gdM = (GDataModel)gdDAO.getGoogleDataByCodContatto(GDataModel.class.getName(), this.contatto.getCodContatto());
		if (this.gdM == null){
			this.gdM = new GDataModel();
			this.gdM.setCodContatto(this.contatto.getCodContatto());
			
		} 
		DataBindingContext bindingContext = new DataBindingContext();
		bindGData(bindingContext);
		//tvIndirizziCal.setInput(gdM.getCalendarSettings());
		
	}

	public void createDialog(){
		
		GridLayout gl = new GridLayout();
		s = new Shell(PlatformUI.createDisplay(),SWT.CLOSE|SWT.MAX|SWT.MIN|SWT.TITLE|SWT.APPLICATION_MODAL|SWT.RESIZE);
		ft = new FormToolkit(Activator.getDefault()
									  .getWorkbench()
									  .getDisplay());
		s.setMinimumSize(400, 300);
		s.setSize(400, 300);
		s.setLayout(gl);
		s.setBackground(new Color(null,255,255,255));
		s.setImage(Activator.getImageDescriptor("icons/icowin/home16.png").createImage());
		s.setText("Configurazioni Google agente : " + 
				   this.contatto.getAgente().getCognome() + " " + 
				   this.contatto.getAgente().getNome() + " : " + this.contatto.getContatto());
		
		gd = new GridData();
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		
		gd_h = new GridData();
		gd_h.horizontalAlignment = SWT.FILL;
		gd_h.grabExcessHorizontalSpace = true;
		
		gd_h_r = new GridData();
		gd_h_r.horizontalAlignment = SWT.RIGHT;
		gd_h_r.verticalAlignment = SWT.BOTTOM;
		gd_h_r.grabExcessVerticalSpace = false;
		gd_h_r.grabExcessHorizontalSpace = true;
		gd_h_r.heightHint = 25;
		
		gl_info = new GridLayout(2, false);
		
		
		f = ft.createForm(s);
		f.setLayoutData(gd);
		f.getBody().setLayout(gl);
		f.getBody().setLayoutData(gd);
//		f.getBody().setBackground(new Color(null, 120,247,123));
	

		CTabFolder folder = new CTabFolder(f.getBody(), SWT.TOP|SWT.BORDER);
		folder.setMaximized(true);
		folder.setLayoutData(gd);
		folder.setLayout(gl);
		folder.setSimple(false);
		folder.setUnselectedImageVisible(false);
		folder.setUnselectedCloseVisible(false);		
						
		CTabItem imail = new CTabItem(folder,SWT.NONE);		
		imail.setText("Account");
		imail.setImage(Activator.getImageDescriptor("icons/gmail.png").createImage());
		imail.setControl(createMailConf(folder));		
	/*	
		CTabItem iCalendar = new CTabItem(folder, SWT.NONE);
		iCalendar.setText("Calendar");
		iCalendar.setImage(Activator.getImageDescriptor("icons/google_calendar.png").createImage());
		iCalendar.setControl(createCalendarConf(folder));
		*/
		
		bindData();
/*		bindAccountData();
		bindCalendarData();*/
		s.open ();

	}
	
	private void bindAccountData(){
		
//		if (this.contatto != null){			
//			
//			if (gdM.getPwsKey().equalsIgnoreCase("")){
//				tpassword.setText("");
//			}else{
//				tpassword.setText(WinkhouseUtils.getInstance()
//												.DecryptString(gdM.getPwsKey()));
//				gdM.setPwsKey(WinkhouseUtils.getInstance()
//											.DecryptString(gdM.getPwsKey()));
//				
//			}	
//			
//		}
	}
	
	private void bindCalendarData(){
		
		if (this.contatto != null){
			

		}
	}
	
	
	private Composite createMailConf(Composite c){
		
		Composite panel = ft.createComposite(c);
		panel.setLayoutData(gd);
		panel.setLayout(new GridLayout());
		panel.setBackground(new Color(null,255,255,255));
		
		Composite cinfo = ft.createComposite(panel);
		cinfo.setLayout(gl_info);
		ImageHyperlink i = ft.createImageHyperlink(cinfo, SWT.NONE);
		i.setImage(Activator.getImageDescriptor("icons/gmail_24.png").createImage());
		
		Label info = ft.createLabel(cinfo, "Inserire l'indirizzo mail e la password dell'account Gmail");
		
		Composite cc = new Composite(panel, SWT.NONE);
		cc.setLayout(new GridLayout());
		cc.setLayoutData(gd);
		cc.setBackground(new Color(null,255,255,255));
		Label lpassword = ft.createLabel(cc, "password gmail");
		tpassword = ft.createText(cc, null,SWT.PASSWORD);
		tpassword.setLayoutData(gd_h);
		
		Composite ctoolbar = ft.createComposite(panel);
		ctoolbar.setLayout(gl_info);
		ctoolbar.setLayoutData(gd_h_r);
		
		ImageHyperlink ihConferma = ft.createImageHyperlink(ctoolbar, SWT.WRAP);
		ihConferma.setImage(Activator.getImageDescriptor("/icons/adept_commit.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/adept_commit_hover.png").createImage());
		ihConferma.setToolTipText("Salva le modifiche");
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
							
				try {
					GoogleCalendarV3Helper gch = new GoogleCalendarV3Helper();
					gch.getClient(contatto.getCodAgente());
				} catch (Exception e1) {				
					e1.printStackTrace();
				}
				
//				GDataHelper gdH = new GDataHelper();
//				
//				if (gdH.saveUpdateGDataVO(gdM)){
//					gdM.setPwsKey(WinkhouseUtils.getInstance()
//									  			.DecryptString(gdM.getPwsKey()));
//					bindData();
//					MessageDialog.openInformation(s,
//												  "Salvataggio password Google", 
//												  "La password � stata salvata in formato criptato");
//
//				}else{
//					MessageDialog.openError(s,
//							  				"Salvataggio password Google", 
//							  				"Si � verificato un errore la password non � stata salvata");					
//				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});


		ImageHyperlink ihCancella = ft.createImageHyperlink(ctoolbar, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/button_cancel.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/button_cancel_hover.png").createImage());
		ihCancella.setToolTipText("Annulla le modifiche");
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				s.close();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		
		ft.paintBordersFor(cc);
		ft.paintBordersFor(panel);
		
		return panel;
	}
	/*
	private Composite createCalendarConf(Composite c){
		
		Composite panelcal = ft.createComposite(c);
		//panelcal.setBackground(new Color(null,new RGB(100,120,120)));
		panelcal.setLayoutData(gd);
		panelcal.setLayout(new GridLayout());
		
		Composite cinfo = ft.createComposite(panelcal);
		cinfo.setLayout(gl_info);
		ImageHyperlink i = ft.createImageHyperlink(cinfo, SWT.NONE);
		i.setImage(Activator.getImageDescriptor("icons/google_calendar_24.png").createImage());
		
		Label info = ft.createLabel(cinfo, "Inserire la password dell'account Gmail, \n " +
										   "inserendo i dati verranno abilitate le altre sezioni di configurazione");
		
		
		Composite toolbar = new Composite(panelcal,SWT.NONE);
		toolbar.setBackground(new Color(null,new RGB(255,255,255)));
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ImageHyperlink ihNew = new ImageHyperlink(toolbar, SWT.WRAP);
		ihNew.setBackground(new Color(null,new RGB(255,255,255)));
		ihNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNew.setToolTipText("Nuova categoria cliente");
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				GCalendarVO gcVO = new GCalendarVO();
				gcVO.setCodGData(gdM.getCodGData());
				gdM.getCalendarSettings().add(gcVO);
				tvIndirizziCal.setInput(gdM.getCalendarSettings());
				tvIndirizziCal.refresh();
				
				TableItem ti = tvIndirizziCal.getTable().getItem(tvIndirizziCal.getTable().getItemCount()-1);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();

				StructuredSelection ss = new StructuredSelection(sel);
				
				tvIndirizziCal.setSelection(ss, true);

				Event ev = new Event();
				ev.item = ti;
				ev.data = ti.getData();
				ev.widget = tvIndirizziCal.getTable();
				tvIndirizziCal.getTable().notifyListeners(SWT.Selection, ev);
				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihUndo = new ImageHyperlink(toolbar, SWT.WRAP);
		ihUndo.setBackground(new Color(null,new RGB(255,255,255)));
		ihUndo.setImage(Activator.getImageDescriptor("/icons/adept_reinstall.png").createImage());
		ihUndo.setHoverImage(Activator.getImageDescriptor("/icons/adept_reinstall_hover.png").createImage());
		ihUndo.setToolTipText("Ricarica dall'archivio");
		ihUndo.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {				
				GDataDAO gdDAO = new GDataDAO();
				gdM = (GDataModel)gdDAO.getGoogleDataByCodContatto(GDataModel.class.getName(), contatto.getCodContatto());
				tvIndirizziCal.setInput(gdM.getCalendarSettings());				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihCancella = new ImageHyperlink(toolbar, SWT.WRAP);
		ihCancella.setBackground(new Color(null,new RGB(255,255,255)));
		ihCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancella.setToolTipText("Cancella elemento selezionato");
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				
				if (tvIndirizziCal.getSelection() != null){
					int count = 0;
					boolean result = true;
					Iterator it = ((StructuredSelection)tvIndirizziCal.getSelection()).iterator();
					GCalendarHelper gch = new GCalendarHelper();					
					while (it.hasNext()) {
						GCalendarVO gcVO = (GCalendarVO)it.next();
						if (gch.deleteGCalendarData(gcVO, null, true)){
							gdM.getCalendarSettings().remove(gcVO);							
						}						
						
					}					
					tvIndirizziCal.setInput(gdM.getCalendarSettings());
					tvIndirizziCal.refresh();
				}
				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
				
		
		tvIndirizziCal = new TableViewer(panelcal, SWT.FULL_SELECTION);
		tvIndirizziCal.getTable().setLayoutData(gd);
		tvIndirizziCal.getTable().setLinesVisible(true);
		
		tvIndirizziCal.setContentProvider(new IStructuredContentProvider(){

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			
			}

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof ArrayList){
					return ((ArrayList)inputElement).toArray();
				}else{
					return null;
				}				
			}
			
		});
		
		tvIndirizziCal.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				if (columnIndex == 0){
					return ((GCalendarVO)element).getPrivateUrl();
				}else{
					return null;
				}					
			}
			
			@Override
			public void addListener(ILabelProviderListener listener) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
			}

			
		});
		
		TableColumn tcurl = new TableColumn(tvIndirizziCal.getTable(), SWT.LEFT, 0);
		tcurl.setWidth(400);
		tcurl.setText("Indirizzo calendario");
				
		TableViewerColumn tcvurl = new TableViewerColumn(tvIndirizziCal,tcurl);
		tcvurl.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				//System.out.println("-"+((GCalendarVO)cell.getElement()).getPrivateUrl());
				GCalendarVO gcVO = ((GCalendarVO)cell.getElement());
				cell.setText((gcVO.getPrivateUrl() == null)
							 ? ""
							 : gcVO.getPrivateUrl());
				
			}
		});
		
		tcvurl.setEditingSupport(new EditingSupport(tvIndirizziCal) {
			
			@Override
			protected void setValue(Object element, Object value) {
				((GCalendarVO)element).setPrivateUrl(String.valueOf(value));
				tvIndirizziCal.refresh();
			}
			
			@Override
			protected Object getValue(Object element) {
				//System.out.println("-"+((GCalendarVO)element).getPrivateUrl());
				return ((GCalendarVO)element).getPrivateUrl();
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvIndirizziCal.getTable());
			}
			
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		
		Composite ctoolbar = ft.createComposite(panelcal);
		//ctoolbar.setBackground(new Color(null,new RGB(180,120,180)));
		ctoolbar.setLayout(gl_info);
		ctoolbar.setLayoutData(gd_h_r);
		
		ImageHyperlink ihConferma = ft.createImageHyperlink(ctoolbar, SWT.WRAP);
		ihConferma.setImage(Activator.getImageDescriptor("/icons/adept_commit.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/adept_commit_hover.png").createImage());
		ihConferma.setToolTipText("Salva le modifiche");
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				
				GCalendarHelper gcH = new GCalendarHelper();
				gcH.saveUpdateGCalendarVO(gdM.getCalendarSettings());
				bindData();
				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});


		ImageHyperlink ihCancellaClose = ft.createImageHyperlink(ctoolbar, SWT.WRAP);		
		ihCancellaClose.setImage(Activator.getImageDescriptor("/icons/button_cancel.png").createImage());
		ihCancellaClose.setHoverImage(Activator.getImageDescriptor("/icons/button_cancel_hover.png").createImage());
		ihCancellaClose.setToolTipText("Chiudi");
		ihCancellaClose.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				s.close();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		
				
		ft.paintBordersFor(panelcal);
		
		return panelcal;
	}
	*/
	private void bindGData(DataBindingContext bindingContext){
		bindAccountData();
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tpassword),
								 PojoProperties.value("pwsKey").observe(gdM),
								 null, 
								 null);
	}

	
}
