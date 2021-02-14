package winkhouse.wizard.gcalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.nebula.widgets.calendarcombo.CalendarCombo;
import org.eclipse.nebula.widgets.calendarcombo.ICalendarListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

import winkhouse.Activator;
import winkhouse.engine.gcalendar.GoogleCalendarSyncEngine;
import winkhouse.engine.gcalendar.GoogleCalendarSyncEngine.AgenteResult;
import winkhouse.helper.GCalendarHelper;
import winkhouse.model.AgentiModel;
import winkhouse.model.AppuntamentiModel;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ContattiModel;
import winkhouse.wizard.GCalendarSyncWizard;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;

public class GCalSyncOpeartions extends WizardPage {

	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	private Image agenteImg = Activator.getImageDescriptor("icons/wizardgcalendar/looknfeel.png").createImage();
	private Image gmailImg = Activator.getImageDescriptor("icons/wizardgcalendar/gmail.png").createImage();
	private Image appuntamentoImg = Activator.getImageDescriptor("icons/wizardgcalendar/korgac-to-gcalendar.png").createImage();
	private Image colloquiImg = Activator.getImageDescriptor("icons/colloqui-gcal.png").createImage();
	private Image ceeImg = Activator.getImageDescriptor("icons/google_calendar_download.png").createImage();
	private Image calendarImg = Activator.getImageDescriptor("icons/wizardgcalendar/calendario.png").createImage();
	private Image calendarEventImg = Activator.getImageDescriptor("icons/google_calendar_download.png").createImage();
	private TreeViewer tree = null;
	private SimpleDateFormat formatterDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private CalendarCombo data = null;
	private CalendarCombo datafine = null;
	private Button startButton = null;
	
	private static enum TYPE_MODELS {COLLOQUIMODEL,APPUNTAMENTIMODEL,MAPENTRY,AGENTERESULT};
	
	public GCalSyncOpeartions(String pageName) {
		super(pageName);
	}

	public GCalSyncOpeartions(String pageName, String title,
							  ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	@Override
	public void createControl(Composite parent) {
		
		setDescription("Sincronizzazione appuntamenti locali con eventi Google calendar");		
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;		
		
		GridData gdFillH = new GridData();
		gdFillH.grabExcessHorizontalSpace = true;
		gdFillH.horizontalAlignment = SWT.FILL;		
		
		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		
		Composite cb = new Composite(parent, SWT.NONE);
		cb.setLayoutData(gdFillHV);
		cb.setLayout(new GridLayout());
		
		Label ldata = new Label(cb, SWT.NONE);		
		ldata.setText("Selezione intervallo di sincronizzazione");
		
		Composite c = new Composite(cb, SWT.NONE);
		c.setLayoutData(gdFillH);
		c.setLayout(gl);
						
		data = new CalendarCombo(c, SWT.READ_ONLY|SWT.DOUBLE_BUFFERED);
		data.addCalendarListener(new ICalendarListener() {
			
			@Override
			public void popupClosed() {}
			
			@Override
			public void dateRangeChanged(Calendar arg0, Calendar arg1) {}
			
			@Override
			public void dateChanged(Calendar arg0) {
				try {					 
					((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
													  .setStartDate(formatter.parse(formatter.format(arg0.getTime())));
					
				} catch (ParseException e) {
					((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
					  								  .setStartDate(new Date());
				}
				
			}
		});
		data.setDate(((GCalendarSyncWizard)getWizard()).getGcalsyncVO().getStartDate());
		
		if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO().isUpl_from_detail()){
			data.setEnabled(false);
		}
				
		datafine = new CalendarCombo(c, SWT.READ_ONLY|SWT.DOUBLE_BUFFERED);
		datafine.addCalendarListener(new ICalendarListener() {
			
			@Override
			public void popupClosed() {}
			
			@Override
			public void dateRangeChanged(Calendar arg0, Calendar arg1) {}
			
			@Override
			public void dateChanged(Calendar arg0) {
				try {					 
					((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
													  .setEndDate(formatter.parse(formatter.format(arg0.getTime())));
					
				} catch (ParseException e) {
					((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
					  								  .setEndDate(new Date());
				}
				
			}
		});
		datafine.setDate(((GCalendarSyncWizard)getWizard()).getGcalsyncVO().getEndDate());

		if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO().isUpl_from_detail()){
			datafine.setEnabled(false);
		}

		startButton = new Button(c,SWT.FLAT);
		startButton.setImage(Activator.getImageDescriptor("icons/adept_reinstall.png").createImage());
		startButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				
				GCalendarHelper gch = new GCalendarHelper();
				
//				((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
//												  .setAlagentiResult(null);
				((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
                								  .setContattiAppuntamentiUPL(null);
                ((GCalendarSyncWizard)getWizard()).getGcalsyncVO().resetAlagentiResultValues();								  
				
				if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO().getOperarationType() == GCalendarSyncWizard.GCalSyncVO.UPLOAD_APPUNTAMENTI_MODEL){
					gch.synchAppuntamentiToGCalendar(
							((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
							 								  .getOperarationType(),
		    				((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
		    												  .getStartDate(),
		    				((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
			    											  .getEndDate(),	    												  
		    			    ((GCalendarSyncWizard)getWizard()).getShell(),	    			    
		    			    new ArrayList<AgenteResult>(((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
		    			    								  							  .getAlagentiResult().values()));
				}else{
					gch.synchGCalendarToAppuntamenti(((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
							  														   .getStartDate(),
							  						 ((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
							  						 								   .getEndDate(),	    												  
							  						 ((GCalendarSyncWizard)getWizard()).getShell(),	    			    
							  						 new ArrayList<AgenteResult>(((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
							  								 													   .getAlagentiResult().values()));
//							((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
//							 								  .getOperarationType(),
//		    				((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
//		    												  .getStartDate(),
//		    				((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
//			    											  .getEndDate(),	    												  
//		    			    ((GCalendarSyncWizard)getWizard()).getShell(),	    			    
//		    			    new ArrayList<AgenteResult>(((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
//		    			    								  .getAlagentiResult().values()));
					
				}
			    tree.setInput(((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
			    												.getAlagentiResult());
			    
			    tree.expandAll();
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});


		tree = new TreeViewer(cb, SWT.HORIZONTAL|SWT.VERTICAL|SWT.BORDER|SWT.CHECK);
		tree.getTree().setLayoutData(gdFillHV);
		
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
				
				if (element instanceof GoogleCalendarSyncEngine.AgenteResult){
						if((((GoogleCalendarSyncEngine.AgenteResult)element).getHmAppuntamenti().size() > 0) || 
						   (((GoogleCalendarSyncEngine.AgenteResult)element).getHmColloqui().size() > 0) || 
						   (((GoogleCalendarSyncEngine.AgenteResult)element).getHmEvents().size() > 0)){
							return true;
						}else{
							return false;
						}
				}else if (element instanceof Map.Entry){
					return ((ArrayList)((Map.Entry)element).getValue()).size() > 0;
				}else if (element instanceof GoogleCalendarSyncEngine.ContattiResult){
					if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO().getOperarationType() == 
						GCalendarSyncWizard.GCalSyncVO.UPLOAD_APPUNTAMENTI_MODEL){
						
						boolean returnValueAppuntamenti = false;
						boolean returnValueColloqui = false;
						
						if(((GoogleCalendarSyncEngine.ContattiResult)element).getAppuntamenti().size() > 0){
							returnValueAppuntamenti = true;
						}
						
						if(((GoogleCalendarSyncEngine.ContattiResult)element).getColloqui().size() > 0){
							returnValueColloqui = true;
						}
						
						if (!returnValueAppuntamenti && !returnValueColloqui){
							return false;
						}else{
							return true;
						}
						
						
					}else{
						if(((GoogleCalendarSyncEngine.ContattiResult)element).getCee().size() > 0){
							return true;
						}else{
							return false;
						}						
					}
					
				}				

				return false;
			}
			
			@Override
			public Object getParent(Object element) {
				return null;
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				
				if (inputElement instanceof HashMap<?,?>){
					
					return ((HashMap) inputElement).values().toArray();
					
				
//				else if (inputElement instanceof GoogleCalendarSyncEngine.AgenteResult){
//					
//						return ((GoogleCalendarSyncEngine.AgenteResult)inputElement).getHmEvents().values().toArray();
//						
//				}else if (inputElement instanceof GoogleCalendarSyncEngine.ContattiResult){
//					
//					if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO().getOperarationType() == 
//							GCalendarSyncWizard.GCalSyncVO.UPLOAD_APPUNTAMENTI_MODEL){
//						
//						ArrayList al = new ArrayList();
//						
//						al.addAll(((GoogleCalendarSyncEngine.ContattiResult)inputElement).getAppuntamenti());
//						al.addAll(((GoogleCalendarSyncEngine.ContattiResult)inputElement).getColloqui());
//						
//						return al.toArray();
//					}else{
//						return ((GoogleCalendarSyncEngine.ContattiResult)inputElement).getCee().toArray();
//					}
				}else{
					return null;
				}
			}
			
			@Override
			public Object[] getChildren(Object parentElement) {
				
				if (parentElement instanceof GoogleCalendarSyncEngine.AgenteResult)
				{
					if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO().getOperarationType() == GCalendarSyncWizard.GCalSyncVO.UPLOAD_APPUNTAMENTI_MODEL){
						ArrayList al = new ArrayList();
						al.addAll(((GoogleCalendarSyncEngine.AgenteResult)parentElement).getHmColloqui().entrySet());
						al.addAll(((GoogleCalendarSyncEngine.AgenteResult)parentElement).getHmAppuntamenti().entrySet());
						return al.toArray();
//						return ((GoogleCalendarSyncEngine.AgenteResult)parentElement).getHmAppuntamenti().entrySet().toArray();
					}
					if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO().getOperarationType() == GCalendarSyncWizard.GCalSyncVO.DOWNLOAD_GCALENDAR_EVENTS){
						return ((GoogleCalendarSyncEngine.AgenteResult)parentElement).getHmEvents().entrySet().toArray();
					}
					
				}
				if (parentElement instanceof CalendarListEntry){	
					ArrayList returnvalue = new ArrayList();
					
					Collection al = null;
					
					al = (Collection)((GoogleCalendarSyncEngine.AgenteResult)parentElement).getHmAppuntamenti().values();
					for (Object object : al) {
						returnvalue.addAll((ArrayList)object);
					}
					
					Collection alcolloqui = null;
					alcolloqui = (Collection)((GoogleCalendarSyncEngine.AgenteResult)parentElement).getHmColloqui().values();
					for (Object object : alcolloqui) {
						returnvalue.addAll((ArrayList)object);
					}
						
					return  returnvalue.toArray();
						
				}else if (parentElement instanceof Map.Entry){
					
					return ((ArrayList)((Map.Entry)parentElement).getValue()).toArray();
					
				}

				return null;
			}
		});
		
		tree.setLabelProvider(new ViewLabelProvider());
	
		setControl(cb);

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
	    
	    checkPath(item.getParentItem(), checked, grayed);
	}
	
	private String compose_key_hm_itemupload(TreeItem item,String extraPath,TYPE_MODELS type){
		
		String key_value = null;
		
		switch (type) {
		
		case APPUNTAMENTIMODEL:
			
			key_value = ((AgenteResult)item.getParentItem().getParentItem().getData()).getCodAgente().toString() + "_" +
						((CalendarListEntry)((Map.Entry)item.getParentItem().getData()).getKey()).getId() + "_" + 
						item.getData().getClass().getName() + "_" + 
						((AppuntamentiModel)item.getData()).getCodAppuntamento().toString(); 
			break;
			
		case COLLOQUIMODEL:
			
			key_value = ((AgenteResult)item.getParentItem().getParentItem().getData()).getCodAgente().toString() + "_" +
						((CalendarListEntry)((Map.Entry)item.getParentItem().getData()).getKey()).getId() + "_" + 
						item.getData().getClass().getName() + "_" + 
						((ColloquiModel)item.getData()).getCodColloquio().toString(); 
			break;
		
		case AGENTERESULT:
			
			key_value = ((AgenteResult)item.getData()).getCodAgente().toString() + "_" + extraPath;
			break;
			
		case MAPENTRY:
			
			key_value = ((AgenteResult)item.getParentItem().getParentItem().getData()).getCodAgente().toString() + "_" +
						((CalendarListEntry)((Map.Entry)item.getParentItem().getData()).getKey()).getId() + "_" + extraPath; 
			
			break;
			
		default:
			break;
		}
		return key_value;
		
	}
	
	private void checkItems(TreeItem item, boolean checked) {
	    item.setGrayed(false);
	    item.setChecked(checked);
	    
	    String extraPath = null;
	    if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO().getOperarationType() == GCalendarSyncWizard.GCalSyncVO.UPLOAD_APPUNTAMENTI_MODEL){
		    if (item.getData() instanceof AppuntamentiModel){
		    	
				if (checked){
					
					if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
						  	  							  .getHm_item_upload()
						  	  							  .get(compose_key_hm_itemupload(item,null,TYPE_MODELS.APPUNTAMENTIMODEL)) == null){
						
						GoogleCalendarSyncEngine.ItemUpload iu = new GoogleCalendarSyncEngine().new ItemUpload((AgenteResult)item.getParentItem().getParentItem().getData(),
																											   (CalendarListEntry)((Map.Entry)item.getParentItem().getData()).getKey(), 
																											   item.getData());
						
						((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
							  	  						  .getHm_item_upload()
							  	  						  .put(compose_key_hm_itemupload(item,null,TYPE_MODELS.APPUNTAMENTIMODEL),iu);
						
					}
					
				}else{
					if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
							  							  .getHm_item_upload()
							  							  .get(compose_key_hm_itemupload(item,null,TYPE_MODELS.APPUNTAMENTIMODEL)) != null){
						
						((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
						  								  .getHm_item_upload()
						  								  .remove(compose_key_hm_itemupload(item,null,TYPE_MODELS.APPUNTAMENTIMODEL));
						
					}
					
				}
				
				getWizard().getContainer().updateButtons();
		    		    	
		    }
		    
		    if (item.getData() instanceof ColloquiModel){
		    	
				if (checked){
					
					if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
						  	  							  .getHm_item_upload()
						  	  							  .get(compose_key_hm_itemupload(item,null,TYPE_MODELS.COLLOQUIMODEL)) == null){
	
						GoogleCalendarSyncEngine.ItemUpload iu = new GoogleCalendarSyncEngine().new ItemUpload((AgenteResult)item.getParentItem().getParentItem().getData(),
																											   (CalendarListEntry)((Map.Entry)item.getParentItem().getData()).getKey(), 
																											   item.getData());
						
						((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
							  	  						  .getHm_item_upload()
							  	  						  .put(compose_key_hm_itemupload(item,null,TYPE_MODELS.COLLOQUIMODEL),iu);
						
					}
					
				}else{
					if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
								  							  .getHm_item_upload()
								  							  .get(compose_key_hm_itemupload(item,null,TYPE_MODELS.COLLOQUIMODEL)) != null){
						
						((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
							  								  .getHm_item_upload()
							  								  .remove(compose_key_hm_itemupload(item,null,TYPE_MODELS.COLLOQUIMODEL));
						
					}
					
				}
				
				getWizard().getContainer().updateButtons();
		    	
		    }
		    
			if (item.getData() instanceof Map.Entry){
				
				ArrayList al = (ArrayList)((Map.Entry)item.getData()).getValue();
				Iterator it = al.iterator();
				
				while (it.hasNext()) {
					
					Object object = (Object) it.next();
					
					
					
					if (object instanceof AppuntamentiModel){
						
						extraPath = ((AppuntamentiModel)object).getCodAppuntamento().toString();
						
						if (checked){
							
							if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
								  	  							  .getHm_item_upload()
								  	  							  .get(compose_key_hm_itemupload(item,extraPath,TYPE_MODELS.MAPENTRY)) == null){
	
								GoogleCalendarSyncEngine.ItemUpload iu = new GoogleCalendarSyncEngine().new ItemUpload((AgenteResult)item.getParentItem().getData(),
																													   (CalendarListEntry)((Map.Entry)item.getData()).getKey(), 
																													   object);
								
								((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
									  	  						  .getHm_item_upload()
									  	  						  .put(compose_key_hm_itemupload(item,extraPath,TYPE_MODELS.MAPENTRY),iu);
								
							}
							
						}else{
							if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
		  							  							  .getHm_item_upload()
		  							  							  .get(compose_key_hm_itemupload(item,extraPath,TYPE_MODELS.MAPENTRY)) != null){
								
								((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
		  						  								  .getHm_item_upload()
		  						  								  .remove(compose_key_hm_itemupload(item,extraPath,TYPE_MODELS.MAPENTRY));
								
							}
							
						}
						
						getWizard().getContainer().updateButtons();
						
					}
	
					
					if (object instanceof ColloquiModel){
						
						extraPath = ((ColloquiModel)object).getCodColloquio().toString();
						
						if (checked){
							
							if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
								  	  							  .getHm_item_upload()
								  	  							  .get(compose_key_hm_itemupload(item,extraPath,TYPE_MODELS.MAPENTRY)) == null){
	
								GoogleCalendarSyncEngine.ItemUpload iu = new GoogleCalendarSyncEngine().new ItemUpload((AgenteResult)item.getParentItem().getData(),
																													   (CalendarListEntry)((Map.Entry)item.getData()).getKey(), 
																													   object);
								
								((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
									  	  						  .getHm_item_upload()
									  	  						  .put(compose_key_hm_itemupload(item,extraPath,TYPE_MODELS.MAPENTRY),iu);
								
							}
							
						}else{
							if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
		  							  							  .getHm_item_upload()
		  							  							  .get(compose_key_hm_itemupload(item,extraPath,TYPE_MODELS.MAPENTRY)) != null){
								
								((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
		  						  								  .getHm_item_upload()
		  						  								  .remove(compose_key_hm_itemupload(item,extraPath,TYPE_MODELS.MAPENTRY));
								
							}
							
						}
						
						getWizard().getContainer().updateButtons();
						
					}
					
				}
	
				if (item.getData() instanceof AgenteResult){
					
					Set elementi = (Set)((AgenteResult)item.getData()).getHmAppuntamenti().entrySet();
					elementi.addAll((Set)((AgenteResult)item.getData()).getHmColloqui().entrySet());
					
					Iterator ite = elementi.iterator();
					
					while (ite.hasNext()) {
						
						Map.Entry me = (Map.Entry) ite.next();
											
						ArrayList alv = (ArrayList)me.getValue();
						
						Iterator iter = alv.iterator();
						
						while (iter.hasNext()) {
	
							Object object = iter.next();
							
							if (object instanceof AppuntamentiModel){
								
								extraPath = ((CalendarListEntry)me.getKey()).getId() + "_" + ((AppuntamentiModel)object).getCodAppuntamento().toString();
								
								if (checked){
									
									if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
										  	  							  .getHm_item_upload()
										  	  							  .get(compose_key_hm_itemupload(item,extraPath,TYPE_MODELS.AGENTERESULT)) == null){
		
										GoogleCalendarSyncEngine.ItemUpload iu = new GoogleCalendarSyncEngine().new ItemUpload((AgenteResult)item.getData(),
																															   (CalendarListEntry)me.getKey(), 
																															   object);
										
										((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
											  	  						  .getHm_item_upload()
											  	  						  .put(compose_key_hm_itemupload(item,extraPath,TYPE_MODELS.AGENTERESULT),iu);
										
									}
									
								}else{
									
									if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
				  							  							  .getHm_item_upload()
				  							  							  .get(compose_key_hm_itemupload(item,extraPath,TYPE_MODELS.AGENTERESULT)) != null){
										
										((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
				  						  								  .getHm_item_upload()
				  						  								  .remove(compose_key_hm_itemupload(item,extraPath,TYPE_MODELS.AGENTERESULT));
										
									}
									
								}
								
								getWizard().getContainer().updateButtons();
								
							}
		
							
							if (object instanceof ColloquiModel){
								
								extraPath = ((CalendarListEntry)me.getKey()).getId() + "_" + ((ColloquiModel)object).getCodColloquio().toString();
								
								if (checked){
									
									if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
										  	  							  .getHm_item_upload()
										  	  							  .get(compose_key_hm_itemupload(item,extraPath,TYPE_MODELS.AGENTERESULT)) == null){
		
										GoogleCalendarSyncEngine.ItemUpload iu = new GoogleCalendarSyncEngine().new ItemUpload((AgenteResult)item.getData(),
												   																			   (CalendarListEntry)me.getKey(), 
												   																			   object);
										
										((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
											  	  						  .getHm_item_upload()
											  	  						  .put(compose_key_hm_itemupload(item,extraPath,TYPE_MODELS.AGENTERESULT),iu);
										
									}
									
								}else{
									if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
				  							  							  .getHm_item_upload()
				  							  							  .get(compose_key_hm_itemupload(item,extraPath,TYPE_MODELS.AGENTERESULT)) != null){
										
										((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
				  						  								  .getHm_item_upload()
				  						  								  .remove(compose_key_hm_itemupload(item,extraPath,TYPE_MODELS.AGENTERESULT));
										
									}
									
								}
								
								getWizard().getContainer().updateButtons();
								
							}
							
						}
					}
				}
			}
			
	    }else{
	    	
	    	if (item.getData() instanceof Map.Entry){
	    		System.out.println("Map");
	    	}
	    	
	    	if (item.getData() instanceof AgenteResult){
	    		System.out.println("Agente");
	    	}
	    	
	    	if (item.getData() instanceof com.google.api.services.calendar.model.Event){
	    		
				if (checked){
					
					if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
						  	  							  .getAl_item_download()
						  	  							  .get(((com.google.api.services.calendar.model.Event)item.getData()).getId()) == null){
						
						Collection c = ((HashMap)item.getParent().getData()).values();
						Iterator it = c.iterator();
						
						while (it.hasNext()){
							
							AgenteResult ar = (AgenteResult)it.next();
							Iterator<CalendarListEntry> itcle = ar.getCleSelected().iterator();
							
							while (itcle.hasNext()) {
								
								CalendarListEntry cle = itcle.next();
								GoogleCalendarSyncEngine.ItemDownload iu = new GoogleCalendarSyncEngine().new ItemDownload(ar,
										   																				   cle, 
										   																				   (com.google.api.services.calendar.model.Event)item.getData());

								((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
								  							      .getAl_item_download()
								  							      .put(((com.google.api.services.calendar.model.Event)item.getData()).getId(),iu);
								
								getWizard().getContainer().updateButtons();
							}
							
						}
						
					}
					
				}else{
					
					if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
  							  							  .getAl_item_download()
  							  							  .get(((com.google.api.services.calendar.model.Event)item.getData()).getId()) != null){
						
						((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
						  								  .getAl_item_download()
						  								  .remove(((com.google.api.services.calendar.model.Event)item.getData()).getId());
						
						getWizard().getContainer().updateButtons();
						
					}
										
				}
				
	    	}
	    	
	    }
	    
	    TreeItem[] items = item.getItems();
	    for (int i = 0; i < items.length; i++) {
	        checkItems(items[i], checked);
	    }
	}
	
	class ViewLabelProvider extends LabelProvider {
		
		public String getText(Object obj) {
			String returnValue = null;
			if (obj instanceof Map.Entry){
				CalendarListEntry cle = ((CalendarListEntry)((Map.Entry)obj).getKey());
				returnValue = ((cle.getId()!= null)?cle.getId():"") + " " +
							  ((cle.getSummary()!= null)?cle.getSummary():"") + " " +
							  ((cle.getDescription()!= null)?cle.getDescription():"") ;
			}
			
			if (obj instanceof AgentiModel){
				returnValue = ((AgentiModel)obj).getCognome() + " " + ((AgentiModel)obj).getNome();
			}
			if (obj instanceof AppuntamentiModel){
				returnValue = ((AppuntamentiModel)obj).toString();
			}
			if (obj instanceof ColloquiModel){
				returnValue = ((ColloquiModel)obj).toString();
			}						
			if (obj instanceof CalendarEventEntry){ 
				CalendarEventEntry cee = (CalendarEventEntry)obj;
				Date datainizio = new Date(cee.getTimes().get(0).getStartTime().getValue());
				Date datafine = new Date(cee.getTimes().get(0).getEndTime().getValue());
				
				returnValue = cee.getTitle().getPlainText() + " - Inizio: " + formatterDateTime.format(datainizio) + 
															  " - Fine: " + formatterDateTime.format(datafine);
			}
			if (obj instanceof com.google.api.services.calendar.model.Event){
				Date datainizio = new Date(((com.google.api.services.calendar.model.Event)obj).getStart().getDateTime().getValue());
				Date datafine = new Date(((com.google.api.services.calendar.model.Event)obj).getEnd().getDateTime().getValue());
				
				returnValue = ((com.google.api.services.calendar.model.Event)obj).getSummary() + " - " + 
							  ((com.google.api.services.calendar.model.Event)obj).getDescription() + " - " + 
							  " - Inizio: " + formatterDateTime.format(datainizio) + 
						  	  " - Fine: " + formatterDateTime.format(datafine);
				
			}
			
			
			return returnValue;
		}

		@Override
		public Image getImage(Object element) {
			
			if (element instanceof Map.Entry){
				return calendarImg;
			}
			if (element instanceof AgentiModel){
				return agenteImg;
			}
			if (element instanceof ContattiModel){
				return gmailImg;
			}			
			if (element instanceof AppuntamentiModel){
				return appuntamentoImg;
			}
			if (element instanceof ColloquiModel){
				return colloquiImg;
			}			
			if (element instanceof CalendarEventEntry){
				return ceeImg;
			}
			if (element instanceof com.google.api.services.calendar.model.Event){
				return calendarEventImg;
			}else{
				return null;
			}
			
		}
		
		
	}

	public void setSyncResult(HashMap<Integer,GoogleCalendarSyncEngine.AgenteResult> agenti){
		
		if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO().isUpl_from_detail()){
			startButton.setEnabled(false);
			data.setEnabled(false);
			datafine.setEnabled(false);
		}
		
		Iterator<GoogleCalendarSyncEngine.AgenteResult> ite = agenti.values().iterator();
		//HashMap<Integer,AgenteResult> hm = new HashMap<Integer, GoogleCalendarSyncEngine.AgenteResult>();		
		
		while(ite.hasNext()){
			
			GoogleCalendarSyncEngine.AgenteResult agente = ite.next();
			
			Iterator<CalendarListEntry> it = agente.getCleSelected().iterator();
			
			while(it.hasNext()){
				
				CalendarListEntry cle = it.next();
				if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO().getFrom_appuntamento_detail() != null){
					
					ArrayList<AppuntamentiModel> items = new ArrayList<AppuntamentiModel>();
					items.add(((GCalendarSyncWizard)getWizard()).getGcalsyncVO().getFrom_appuntamento_detail());
					
					agente.getHmAppuntamenti().put(cle, items);					
					
				}
				if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO().getFrom_colloqui_detail() != null){
	
					ArrayList<ColloquiModel> items = new ArrayList<ColloquiModel>();
					items.add(((GCalendarSyncWizard)getWizard()).getGcalsyncVO().getFrom_colloqui_detail());
	
					agente.getHmColloqui().put(cle, items);
					
				}
				
			}
//			hm.put(agente.getCodAgente(), agente);
		}
		((GCalendarSyncWizard)getWizard()).getGcalsyncVO().setAlagentiResult(agenti);
	    
		tree.setInput(((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
													    .getAlagentiResult());

		tree.expandAll();
		//getWizard().getContainer().updateButtons();
	}

	public TreeViewer getTree() {
		return tree;
	}
	
}
