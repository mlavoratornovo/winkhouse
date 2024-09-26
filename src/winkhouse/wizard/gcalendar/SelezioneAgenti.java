package winkhouse.wizard.gcalendar;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

import winkhouse.Activator;
import winkhouse.dao.AgentiDAO;
import winkhouse.engine.gcalendar.GoogleCalendarSyncEngine;
import winkhouse.helper.GoogleCalendarV3Helper;
import winkhouse.model.AgentiModel;
import winkhouse.orm.Agenti;
import winkhouse.wizard.GCalendarSyncWizard;

import com.google.api.services.calendar.model.CalendarListEntry;

public class SelezioneAgenti extends WizardPage {
	
	private ArrayList agenti_al = null;
	private Image agenteImg = Activator.getImageDescriptor("icons/wizardgcalendar/looknfeel.png").createImage();
	private Image gmailImg = Activator.getImageDescriptor("icons/wizardgcalendar/calendario.png").createImage();
	private Composite backgr = null;
	private TableViewer tvAllegati = null;
	private GoogleCalendarV3Helper gcv3h = null; 	
	private Button bFromWinkToGCal = null;
	private Button bFromGCalToWink = null;
	
	public SelezioneAgenti(String pageName) {
		super(pageName);
	}

	public SelezioneAgenti(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	@Override
	public void createControl(Composite parent) {
		
		setDescription("selezione agenti/contatti da sincronizzare con Google Calendar");
		
		GridData gdFillHV = new GridData();		
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;
		
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new GridLayout());
		c.setLayoutData(gdFillHV);
		
		GridData gdFillH = new GridData();
		gdFillH.grabExcessHorizontalSpace = true;
		gdFillH.horizontalAlignment = SWT.FILL;
		
		Composite cb = new Composite(c, SWT.NONE);
		cb.setLayoutData(gdFillH);
		cb.setLayout(new GridLayout());

		Group g = new Group(cb,SWT.SHADOW_IN);
		//g.setBackground(new Color(null,255,255,255));
		GridLayout gl2 = new GridLayout();
		gl2.numColumns = 2;
		g.setLayout(gl2);
		g.setLayoutData(gdFillH);
		
		bFromWinkToGCal = new Button(g,SWT.RADIO);
		bFromWinkToGCal.setText("Da Winkhouse a Google Calendar");
		bFromWinkToGCal.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
												  .setOperarationType(GCalendarSyncWizard.GCalSyncVO
														  								 .UPLOAD_APPUNTAMENTI_MODEL);
				((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
				  								  .setAlagentiResult(null);
				((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
												  .setContattiAppuntamentiUPL(null);

			}
			
			@Override
			public void mouseDown(MouseEvent e) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {}
		});

		bFromGCalToWink = new Button(g,SWT.RADIO);
		bFromGCalToWink.setText("Da Google Calendar a Winkhouse");
		bFromGCalToWink.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
				  								  .setOperarationType(GCalendarSyncWizard.GCalSyncVO
				  										  								 .DOWNLOAD_GCALENDAR_EVENTS);
				((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
				                                  .setAlagentiResult(null);
				((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
				                                  .setContattiAppuntamentiUPL(null);

			}
			
			@Override
			public void mouseDown(MouseEvent e) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {}
		});
							
		TreeViewer tree = new TreeViewer(c, SWT.CHECK);
		tree.getTree().setLayoutData(gdFillHV);
		tree.getTree().setBounds(parent.getBounds());
		
		tree.getTree().addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event event) {
	            if (event.detail == SWT.CHECK) {
	                TreeItem item = (TreeItem) event.item;
	                boolean checked = item.getChecked();
	                checkItems(item, checked);
	                checkPath(item.getParentItem(), checked, false);
	            }
	        }
	    });
		tree.setContentProvider(new ITreeContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			
			}
			
			@Override
			public void dispose() {}
			
			@Override
			public boolean hasChildren(Object element) {
				
				if (element instanceof AgentiModel){
					return true;
		
				}				

				return false;
			}
			
			@Override
			public Object getParent(Object element) {
				return null;
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof ArrayList){
					return ((ArrayList) inputElement).toArray();
//				}else if (inputElement instanceof AgentiModel){
//					return ((AgentiModel)inputElement).getGMailContatti().toArray();
				}else{
					return null;
				}
			}
			
			@Override
			public Object[] getChildren(Object parentElement) {
				
				if (parentElement instanceof AgentiModel){
					
					return ((AgentiModel)parentElement).getCleObjs();
				}
				return null;
			}
		});
		
		tree.setLabelProvider(new ViewLabelProvider());
		tree.setInput(getAgenti());
		//tree.expandAll();
		
		setControl(c);

	}
	
	private void checkPath(TreeItem item, boolean checked, boolean grayed) {
	    if (item == null) return;
	    if (grayed) {
	        checked = true;
	    } else {
	        int index = 0;
	        TreeItem[] items = item.getItems();
	        while (index < items.length) {
	            TreeItem child = items[index];
	            if (child.getGrayed() || checked != child.getChecked()) {
	                checked = grayed = true;
	                break;
	            }
	            index++;
	        }
	    }
	    item.setChecked(checked);
	    item.setGrayed(grayed);
	    
	    if(item.getData() instanceof CalendarListEntry) {
	    	if (checked){
	    		if (!((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
	    											  .getAlagentiResult()
	    											  .containsKey(((AgentiModel)item.getParentItem().getData()).getCodAgente())){
	    			
	    			GoogleCalendarSyncEngine.AgenteResult ar = new GoogleCalendarSyncEngine().new AgenteResult(((Agenti)item.getParentItem().getData()));
	    			ar.getCleSelected().add((CalendarListEntry)item.getData());
	    			((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
					  								  .getAlagentiResult()
					  								  .put(((AgentiModel)item.getParentItem().getData()).getCodAgente(), ar);
	    		}else{
	    			((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
						      						  .getAlagentiResult()
						      						  .get(((AgentiModel)item.getParentItem().getData()).getCodAgente())
						      						  .getCleSelected().add((CalendarListEntry)item.getData());
	    		}
	    		getWizard().getContainer().updateButtons();
	    	
	    									  
	    	}else{
	    		
	    		if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
					  							      .getAlagentiResult()
						  							  .containsKey(((AgentiModel)item.getParentItem().getData()).getCodAgente())){

	    			((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
						  							  .getAlagentiResult()
						  							  .get(((AgentiModel)item.getParentItem().getData()).getCodAgente())
						  							  .getCleSelected().remove((CalendarListEntry)item.getData());
	    		}
	    		getWizard().getContainer().updateButtons();
	    	}
	    }
	    checkPath(item.getParentItem(), checked, grayed);
	}
	
	private void checkItems(TreeItem item, boolean checked) {
		
	    item.setGrayed(false);
	    item.setChecked(checked);
	    
	    if (item.getData() instanceof CalendarListEntry) {
	    	
	    	if (checked){
	    		if (!((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
	    											  .getAlagentiResult()
	    											  .containsKey(((AgentiModel)item.getParentItem().getData()).getCodAgente())){
	    			
	    			GoogleCalendarSyncEngine.AgenteResult ar = new GoogleCalendarSyncEngine().new AgenteResult(((Agenti)item.getParentItem().getData()));
	    			ar.getCleSelected().add((CalendarListEntry)item.getData());
	    			
	    			((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
					  								  .getAlagentiResult()
					  								  .put(((AgentiModel)item.getParentItem().getData()).getCodAgente(), ar);
	    		}else{
	    			((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
						      						  .getAlagentiResult()
						      						  .get(((AgentiModel)item.getParentItem().getData()).getCodAgente())
						      						  .getCleSelected().add((CalendarListEntry)item.getData());
	    		}
	    		getWizard().getContainer().updateButtons();
	    	
	    									  
	    	}else{
	    		
	    		if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
					  							      .getAlagentiResult()
						  							  .containsKey(((AgentiModel)item.getParentItem().getData()).getCodAgente())){

	    			((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
						  							  .getAlagentiResult()
						  							  .get(((AgentiModel)item.getParentItem().getData()).getCodAgente())
						  							  .getCleSelected().remove((CalendarListEntry)item.getData());
	    		}
	    		getWizard().getContainer().updateButtons();
	    	}
	    	
	    }
	    
	    TreeItem[] items = item.getItems();
	    for (int i = 0; i < items.length; i++) {
	        checkItems(items[i], checked);
	    }
	}
	
	private ArrayList getAgenti(){
		
		if (agenti_al == null){
			
			if (!((GCalendarSyncWizard)getWizard()).getGcalsyncVO().isUpl_from_detail()){
				
				agenti_al = new ArrayList<AgentiModel>();
				
				AgentiDAO aDAO = new AgentiDAO();
				ArrayList al = aDAO.list(AgentiModel.class.getName());
				
				Iterator it = al.iterator();
				
				while(it.hasNext()){
					AgentiModel am = (AgentiModel)it.next();
					agenti_al.add(am);				
				}
				
			}else{
				bFromWinkToGCal.setEnabled(false);
				bFromGCalToWink.setEnabled(false);
				
				bFromWinkToGCal.setSelection(true);
				bFromGCalToWink.setSelection(false);
				
				((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
				  								  .setOperarationType(GCalendarSyncWizard.GCalSyncVO
						  						  .UPLOAD_APPUNTAMENTI_MODEL);
				
				agenti_al = ((GCalendarSyncWizard)getWizard()).getGcalsyncVO().getAlagenti();
			}
			
		}
		return agenti_al;
	}
	
	class ViewLabelProvider extends LabelProvider {
		
		public String getText(Object obj) {
			String returnValue = null;
			if (obj instanceof AgentiModel){
				returnValue = ((AgentiModel)obj).getCognome() + " " + ((AgentiModel)obj).getNome();
			}
			if (obj instanceof CalendarListEntry){
				returnValue = ((((CalendarListEntry)obj).getId() != null)
							   ? ((CalendarListEntry)obj).getId()
							   : "") + " " + 
							   ((((CalendarListEntry)obj).getSummary() != null)
							   ? ((CalendarListEntry)obj).getSummary()
							   : "") + " " +
							   ((((CalendarListEntry)obj).getDescription() != null)
							   ? ((CalendarListEntry)obj).getDescription()
							   : "");
			}			
			return returnValue;
		}

		@Override
		public Image getImage(Object element) {
			if (element instanceof AgentiModel){
				return agenteImg;
			}
			if (element instanceof CalendarListEntry){
				return gmailImg;
			}else{
				return null;
			}
			
		}
		
		
	}


}
