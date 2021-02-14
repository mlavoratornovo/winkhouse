package winkhouse.view.common;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import winkhouse.Activator;
import winkhouse.dao.WinkGCalendarDAO;
import winkhouse.helper.GoogleCalendarV3Helper;
import winkhouse.model.AppuntamentiModel;
import winkhouse.model.ColloquiModel;
import winkhouse.model.WinkGCalendarModel;

import com.google.api.services.calendar.Calendar;


public class PopUpGCalendarEvents {

	private Shell popup = null;
	
	private CheckboxTableViewer tv = null;
	private Object appuntamento_colloquio = null;
	private Image calendarImg = Activator.getImageDescriptor("icons/google_calendar.png").createImage();
	private Image agenteImg = Activator.getImageDescriptor("icons/wizardgcalendar/looknfeel.png").createImage();
	private Image colloquioImg = Activator.getImageDescriptor("icons/colloqui16.png").createImage();
	private Image appuntamentoImg = Activator.getImageDescriptor("icons/korgac16.png").createImage();
	private ImageDescriptor ok = Activator.getImageDescriptor("icons/adept_commit.png");
	private ImageDescriptor cancel = Activator.getImageDescriptor("icons/button_cancel.png");
	private HashMap<Integer,WinkGCalendarModel> hmselection = null;
	private Button chkalsodelGCalendar = null;
	
	class RemoveEventsFromGCalendar extends Action{

		public RemoveEventsFromGCalendar(){
			setImageDescriptor(ok);
		}

		@Override
		public void run() {
			
			Object[] checked = tv.getCheckedElements();
			
			if (checked.length > 0){
							
				WinkGCalendarDAO wgcdao = new WinkGCalendarDAO();
				
				GoogleCalendarV3Helper gcv3h;
				
				try {
					
					gcv3h = new GoogleCalendarV3Helper();
					
					for (Object model : checked) {
						
						if (wgcdao.deleteById(((WinkGCalendarModel)model).getCodWinkGCalendar(), null, true)){
							
							if (chkalsodelGCalendar.getSelection()){
								Calendar c = gcv3h.getClient(((WinkGCalendarModel)model).getCodAgente());
								if (c != null){
									gcv3h.deleteEvent(c, 
													  ((WinkGCalendarModel)model).getCalendarId(), 
													  ((WinkGCalendarModel)model).getEventId());									
								}
							}
							
						}
						
					}
					
					popup.close();
					
				} catch (GeneralSecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				MessageDialog.open(MessageDialog.WARNING, popup, "Selezione evento", "Selezionare almeno un evento", SWT.SHEET);
			}
			
		}

	}
	
	class CloseCancelAction extends Action{

		public CloseCancelAction(){
			setImageDescriptor(cancel);
		}
		
		@Override
		public void run() {
			popup.close();
		}				
		
	}
	
	public PopUpGCalendarEvents(Object appuntamento_colloquio) {
		this.appuntamento_colloquio = appuntamento_colloquio;
		this.hmselection = new HashMap<Integer, WinkGCalendarModel>();
		createContent();
	}
		
	private void createContent(){
		popup = new Shell(Activator.getDefault().getWorkbench()
												.getActiveWorkbenchWindow()
												.getShell()
												.getDisplay(),
												SWT.APPLICATION_MODAL|SWT.SHELL_TRIM);
		
		popup.setText("Lista pubblicazioni su Google Calendar");
		popup.setImage(calendarImg);
		popup.setBackground(new Color(null,255,255,255));
		popup.setSize(400, 400);
				
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.FILL;
		
		GridData gdH = new GridData();
		gdH.grabExcessHorizontalSpace = true;
		gdH.horizontalAlignment = SWT.FILL;
		
		GridData gdH2 = new GridData();
		gdH2.grabExcessHorizontalSpace = true;
		gdH2.horizontalAlignment = SWT.FILL;
		gdH2.horizontalSpan = 2;

		GridData gdH3 = new GridData();
		gdH3.grabExcessHorizontalSpace = true;
		gdH3.horizontalAlignment = SWT.FILL;
		gdH3.verticalAlignment = SWT.FILL;
		gdH3.heightHint = 55;

		popup.setLayout(new GridLayout());
			
		FormToolkit ft = new FormToolkit(popup.getDisplay());
		Form f = ft.createForm(popup);
		f.getBody().setLayout(new GridLayout());
		f.setLayoutData(gd);		
		
		RemoveEventsFromGCalendar refgc = new RemoveEventsFromGCalendar();
		f.getToolBarManager().add(refgc);
		CloseCancelAction cca = new CloseCancelAction();
		f.getToolBarManager().add(cca);
		f.updateToolBar();
		
//		});
		chkalsodelGCalendar = ft.createButton(f.getBody(), "Cancella eventi selezionati anche da Google Calendar", SWT.CHECK);
		
		
		Table t = new Table(f.getBody(),SWT.HORIZONTAL|SWT.VERTICAL|SWT.CHECK);
		tv = new CheckboxTableViewer(t);
		tv.getTable().setLayoutData(gd);
		tv.setContentProvider(new IStructuredContentProvider(){

			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Object[] getElements(Object arg0) {
				if (arg0 instanceof ArrayList){
					return ((ArrayList) arg0).toArray();
				}
				return null;
			}
			
		});
		tv.setLabelProvider(new ITableLabelProvider() {
			
			@Override
			public void removeListener(ILabelProviderListener arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isLabelProperty(Object arg0, String arg1) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addListener(ILabelProviderListener arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public String getColumnText(Object arg0, int arg1) {
				switch(arg1){
					case 1: return ((WinkGCalendarModel)arg0).getAgente().toString();
					case 2: return ((WinkGCalendarModel)arg0).getCalendarId();
					case 3: return (((((WinkGCalendarModel)arg0).getCodAppuntamento() == null) || (((WinkGCalendarModel)arg0).getCodAppuntamento() == 0))
									?((WinkGCalendarModel)arg0).getColloquio().toString()
									:((WinkGCalendarModel)arg0).getAppuntamento().toString());
					case 4: return ((WinkGCalendarModel)arg0).getEventId();
					default: return "";
											
				}				
			}
			
			@Override
			public Image getColumnImage(Object arg0, int arg1) {
				switch(arg1){
				case 1: return agenteImg;
				case 2: return calendarImg;
				case 3: return ((((WinkGCalendarModel)arg0).getCodAppuntamento() == null)
								?colloquioImg
								:appuntamentoImg);
				
				default: return null;
										
			}							}
		});
//		tv.addCheckStateListener(new ICheckStateListener(){
//
//			@Override
//			public void checkStateChanged(CheckStateChangedEvent event) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		});
		
		tv.getTable().setHeaderVisible(true);
		tv.getTable().setLinesVisible(true);

		TableColumn tcchk = new TableColumn(tv.getTable(),SWT.LEFT,0);
		tcchk.setWidth(20);

		TableColumn tcAgente = new TableColumn(tv.getTable(),SWT.LEFT,1);
		tcAgente.setText("Agente");
		tcAgente.setWidth(100);

		TableColumn tcComune = new TableColumn(tv.getTable(),SWT.LEFT,2);
		tcComune.setText("Calendario");
		tcComune.setWidth(100);

		TableColumn tcProvincia = new TableColumn(tv.getTable(),SWT.LEFT,3);
		tcProvincia.setText("Evento");
		tcProvincia.setWidth(100);

		TableColumn tcRegione = new TableColumn(tv.getTable(),SWT.LEFT,4);
		tcRegione.setText("ID evento");
		tcRegione.setWidth(100);		

		ft.paintBordersFor(f.getBody());
		
		if (this.appuntamento_colloquio instanceof AppuntamentiModel){
			tv.setInput(((AppuntamentiModel)this.appuntamento_colloquio).getWinkGCalendarModels());
		}
		if (this.appuntamento_colloquio instanceof ColloquiModel){
			tv.setInput(((ColloquiModel)this.appuntamento_colloquio).getWinkGCalendarModels());
		}
		
		popup.open();
		while (!popup.isDisposed()){
			if (!Activator.getDefault().getWorkbench()
									   .getActiveWorkbenchWindow()
									   .getShell()
									   .getDisplay().readAndDispatch()){
				Activator.getDefault().getWorkbench()
				.getActiveWorkbenchWindow()
				.getShell()
				.getDisplay().sleep();
			}
		}
		
	}
	
}
