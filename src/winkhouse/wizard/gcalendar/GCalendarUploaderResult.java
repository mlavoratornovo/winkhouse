package winkhouse.wizard.gcalendar;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;

import winkhouse.Activator;
import winkhouse.dao.AgentiAppuntamentiDAO;
import winkhouse.dao.AppuntamentiDAO;
import winkhouse.dao.ColloquiDAO;
import winkhouse.dao.WinkGCalendarDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.engine.gcalendar.GoogleCalendarSyncEngine;
import winkhouse.engine.gcalendar.GoogleCalendarSyncEngine.ContattiResult;
import winkhouse.engine.gcalendar.GoogleCalendarSyncEngine.ItemDownload;
import winkhouse.engine.gcalendar.GoogleCalendarSyncEngine.ItemUpload;
import winkhouse.helper.GoogleCalendarV3Helper;
import winkhouse.model.AppuntamentiModel;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ContattiModel;
import winkhouse.vo.AgentiAppuntamentiVO;
import winkhouse.vo.WinkGCalendarVO;
import winkhouse.wizard.GCalendarSyncWizard;

import com.google.api.services.calendar.Calendar;
import com.google.gdata.data.calendar.CalendarEventEntry;

public class GCalendarUploaderResult extends WizardPage {

	private TableViewer tvRisultatiUPDAppuntamenti = null;
	private SimpleDateFormat formatterDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private Button startButton = null;
	private ArrayList<AppuntamentiResult> appuntamentiResult = null;
	private ArrayList<ColloquiResult> colloquiResult = null;
	private ArrayList<CalendarEventEntryResult> calendarEventEntryResults = null;
	private Image OK_IMG = Activator.getImageDescriptor("icons/adept_commit.png").createImage();
	private Image ERROR_IMG = Activator.getImageDescriptor("icons/button_cancel.png").createImage();
	private Label lstartsending = null;

	public class AppuntamentiResult extends AppuntamentiModel{
		
		private ContattiModel cm = null;
		
		public AppuntamentiResult(AppuntamentiModel m){			
			super(m);						
		}

		public ContattiModel getCm() {
			return cm;
		}

		public void setCm(ContattiModel cm) {
			this.cm = cm;
		}
		
	}
	
	public class ColloquiResult extends ColloquiModel{
		
		private ContattiModel cm = null;
		
		public ColloquiResult(ColloquiModel m){			
			super(m);						
		}

		public ContattiModel getCm() {
			return cm;
		}

		public void setCm(ContattiModel cm) {
			this.cm = cm;
		}
		
	}
	
	public class CalendarEventEntryResult extends CalendarEventEntry{
		
		private ContattiModel cm = null;
		
		public CalendarEventEntryResult(CalendarEventEntry cee){
			super();
			setTitle(cee.getTitle());
			addTime(cee.getTimes().get(0));			
			setIcalUID(cee.getIcalUID());
		}

		public ContattiModel getCm() {
			return cm;
		}

		public void setCm(ContattiModel cm) {
			this.cm = cm;
		}
		
	}
	
	public GCalendarUploaderResult(String pageName) {
		super(pageName);		
	}

	public GCalendarUploaderResult(String pageName, String title,ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public void refreshLabels(){
		
		if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
											  .getOperarationType() == GCalendarSyncWizard.GCalSyncVO.UPLOAD_APPUNTAMENTI_MODEL){

			setDescription("Sincronizzazione appuntamenti locali con eventi Google calendar");

		}else{

			setDescription("Sincronizzazione eventi Google calendar con appuntamenti locali");

		}
		if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
				  							  .getOperarationType() == GCalendarSyncWizard.GCalSyncVO.UPLOAD_APPUNTAMENTI_MODEL){
			lstartsending.setText("Invio dati a Google Calendar");
		}else{
			lstartsending.setText("Salvataggio dati da Google Calendar");	
		}
		
	}
	
	@Override
	public void createControl(Composite parent) {
		
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;		

		Composite cb = new Composite(parent, SWT.NONE);
		cb.setLayoutData(gdFillHV);
		cb.setLayout(new GridLayout());
		
		lstartsending = new Label(cb, SWT.NONE);
		lstartsending.setText("                                                               ");
		GridData gdFillH = new GridData();
		gdFillH.grabExcessHorizontalSpace = true;
		gdFillH.horizontalAlignment = SWT.FILL;		
		
		startButton = new Button(cb,SWT.FLAT);
		startButton.setLayoutData(gdFillH);
		if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
				  							  .getOperarationType() == 
			  GCalendarSyncWizard.GCalSyncVO.UPLOAD_APPUNTAMENTI_MODEL){
		
			startButton.setImage(Activator.getImageDescriptor("icons/wizardgcalendar/uploadevents_button.png").createImage());
		}else{
			startButton.setImage(Activator.getImageDescriptor("icons/wizardgcalendar/downloadevents_button.png").createImage());
		}
		
		startButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				
				try {
					
					GoogleCalendarV3Helper gch = new GoogleCalendarV3Helper();
					
					AppuntamentiDAO appDAO = new AppuntamentiDAO();
					ColloquiDAO cDAO = new ColloquiDAO();
					WinkGCalendarDAO wgcDAO = new WinkGCalendarDAO();
					AgentiAppuntamentiDAO aaDAO = new AgentiAppuntamentiDAO();
					
					if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO().getOperarationType() == GCalendarSyncWizard.GCalSyncVO.UPLOAD_APPUNTAMENTI_MODEL){
						
						ArrayList<GoogleCalendarSyncEngine.ItemUpload> aliu = new ArrayList(((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
							    																						  	  .getHm_item_upload().values());
						
						Iterator<GoogleCalendarSyncEngine.ItemUpload> it = aliu.iterator();
						
						while (it.hasNext()) {
							
							GoogleCalendarSyncEngine.ItemUpload itemUpload = (GoogleCalendarSyncEngine.ItemUpload) it.next();
							Calendar c = gch.getClient(itemUpload.getAgente().getCodAgente());
							if (c != null){
								try{
									
									String eventid = gch.handleEvent(c, itemUpload.getCle().getId(), itemUpload);
									
									if (eventid != null){
										
										if (itemUpload.getWinkobj() instanceof AppuntamentiModel){
											
											itemUpload.setIdEvento(((AppuntamentiModel)itemUpload.getWinkobj()).getiCalUID());
											
										}else if(itemUpload.getWinkobj() instanceof ColloquiModel){
											
											itemUpload.setIdEvento(((ColloquiModel)itemUpload.getWinkobj()).getiCalUid());
											
										}
																			
										itemUpload.setOperationResult(GoogleCalendarSyncEngine.ItemUpload.UPL_OK);
										
									}else{
										
										itemUpload.setOperationResult(GoogleCalendarSyncEngine.ItemUpload.UPL_ERROR);
										
									}
									
								}catch(Exception ex){
									itemUpload.setOperationResult(GoogleCalendarSyncEngine.ItemUpload.UPL_ERROR);
								}
							}							
						}
						
						tvRisultatiUPDAppuntamenti.setInput(aliu);

					}else{
						ArrayList<GoogleCalendarSyncEngine.ItemDownload> aliu = new ArrayList(((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
							  	  																								.getAl_item_download().values());

						Iterator<GoogleCalendarSyncEngine.ItemDownload> it = aliu.iterator();

						while (it.hasNext()) {
							
							GoogleCalendarSyncEngine.ItemDownload id = it.next();
							try{
								
								ArrayList<AppuntamentiModel> al_am = (ArrayList<AppuntamentiModel>)appDAO.getAppuntamentoByICalID(AppuntamentiModel.class.getName(), id.getEvento().getId());
								
								if (al_am.size() > 0){
									
									AppuntamentiModel am = al_am.get(0);
									
									am.setDataAppuntamento(new Date(id.getEvento().getStart().getDateTime().getValue()));
									am.setDataFineAppuntamento(new Date(id.getEvento().getEnd().getDateTime().getValue()));
									am.setLuogo(id.getEvento().getSummary());
									am.setDescrizione(id.getEvento().getDescription());
									
									if (appDAO.saveUpdate(am, null, true)){
										id.setWinkobj(am);
										id.setOperationResult(GoogleCalendarSyncEngine.ItemDownload.DWN_OK);
									}else{
										id.setOperationResult(GoogleCalendarSyncEngine.ItemDownload.DWN_ERROR);
									}
									
								}else{
									
									ArrayList<ColloquiModel> al_cm = (ArrayList<ColloquiModel>)cDAO.getColloquiByICalId(ColloquiModel.class.getName(), id.getEvento().getId());
									
									if (al_cm.size() > 0){
										
										ColloquiModel cm = al_cm.get(0);
										cm.setDataColloquio(new Date(id.getEvento().getStart().getDateTime().getValue()));
										cm.setLuogoIncontro(id.getEvento().getSummary());
										cm.setDescrizione(id.getEvento().getDescription());
										
										if (cDAO.saveUpdate(cm, null, true)){
											id.setWinkobj(cm);
											id.setOperationResult(GoogleCalendarSyncEngine.ItemDownload.DWN_OK);
										}else{
											id.setOperationResult(GoogleCalendarSyncEngine.ItemDownload.DWN_ERROR);
										}
										
									}else{
										
										AppuntamentiModel am = new AppuntamentiModel();
										
										am.setDataAppuntamento(new Date(id.getEvento().getStart().getDateTime().getValue()));
										am.setDataFineAppuntamento(new Date(id.getEvento().getEnd().getDateTime().getValue()));
										am.setLuogo(id.getEvento().getSummary());
										am.setDescrizione(id.getEvento().getDescription());
										id.setWinkobj(am);
																				
										Connection con = ConnectionManager.getInstance().getConnection();
										
										if (appDAO.saveUpdate(am, con, false)){
											
											WinkGCalendarVO wgcVO = new WinkGCalendarVO(id.getAgente().getCodAgente(),
																						am.getCodAppuntamento(),
																						null,
																						id.getCle().getId(),
																						id.getEvento().getId());
											
											if (wgcDAO.saveUpdate(wgcVO, con, false)){
												
												AgentiAppuntamentiVO aaVO = new AgentiAppuntamentiVO();
												aaVO.setCodAgente(id.getAgente().getCodAgente());
												aaVO.setCodAppuntamento(am.getCodAppuntamento());
												
												if (aaDAO.saveUpdate(aaVO, con, false)){
													id.setOperationResult(GoogleCalendarSyncEngine.ItemDownload.DWN_OK);
													con.commit();
												}else{
													con.rollback();
													id.setOperationResult(GoogleCalendarSyncEngine.ItemDownload.DWN_ERROR);													
												}
																								
											}else{
												con.rollback();
												id.setOperationResult(GoogleCalendarSyncEngine.ItemDownload.DWN_ERROR);
											}
											con.close();
											
										}else{
											id.setOperationResult(GoogleCalendarSyncEngine.ItemDownload.DWN_ERROR);
										}
										
										
									}
								}
								id.setOperationResult(GoogleCalendarSyncEngine.ItemDownload.DWN_OK);
								
							}catch(Exception ex){
								id.setOperationResult(GoogleCalendarSyncEngine.ItemDownload.DWN_ERROR);
							}
							
						}
						tvRisultatiUPDAppuntamenti.setInput(aliu);
					}
					
				} catch (GeneralSecurityException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});

		tvRisultatiUPDAppuntamenti = new TableViewer(cb, SWT.FULL_SELECTION);
		tvRisultatiUPDAppuntamenti.getTable().setLayoutData(gdFillHV);
		tvRisultatiUPDAppuntamenti.getTable().setHeaderVisible(true);
		tvRisultatiUPDAppuntamenti.getTable().setLinesVisible(true);
		tvRisultatiUPDAppuntamenti.setContentProvider(new IStructuredContentProvider(){

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
					return new ArrayList().toArray();
				}
			}
			
		});
		
		tvRisultatiUPDAppuntamenti.setLabelProvider(new ITableLabelProvider(){

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

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				
				if (columnIndex == 0){
					
					if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
							  							  .getOperarationType() == GCalendarSyncWizard.GCalSyncVO.UPLOAD_APPUNTAMENTI_MODEL){
						
						if (element instanceof ItemUpload){
							
							if (((ItemUpload)element).getIdEvento() != null){
								return OK_IMG;
							}else{
								return ERROR_IMG;
							}
							
						}
					}
					
					if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
							  							  .getOperarationType() == GCalendarSyncWizard.GCalSyncVO.DOWNLOAD_GCALENDAR_EVENTS){

						if (element instanceof ItemDownload){

							if (((ItemDownload)element).getCodAppuntamento() != null){
								return OK_IMG;
							}else{
								return ERROR_IMG;
							}

						}
					}
					
				}
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
							
				if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
						  							  .getOperarationType() == GCalendarSyncWizard.GCalSyncVO.UPLOAD_APPUNTAMENTI_MODEL){
					if (element instanceof ItemUpload){
						switch (columnIndex) {
						case 1: return ((ItemUpload)element).getAgente().toString();
						case 2: return ((ItemUpload)element).getCle().getId();
						case 3: return (((ItemUpload)element).getWinkobj() != null)?((ItemUpload)element).getWinkobj().toString():"";
						case 4: return ((ItemUpload)element).getIdEvento();
						default:
							break;
						}
					}
				}
				if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
						  							  .getOperarationType() == GCalendarSyncWizard.GCalSyncVO.DOWNLOAD_GCALENDAR_EVENTS){
					if (element instanceof ItemDownload){
						switch (columnIndex) {
						case 1: return ((ItemDownload)element).getAgente().toString();
						case 2: return ((ItemDownload)element).getCle().getId();
						case 3: return (((ItemDownload)element).getWinkobj() != null)?((ItemDownload)element).getWinkobj().toString():"";
						case 4: return ((ItemDownload)element).getEvento().getId();
						default:
							break;
						}
					}
				}	
				
				return null;
			}
			
		});
		
		TableColumn tcicona = new TableColumn(tvRisultatiUPDAppuntamenti.getTable(),SWT.CENTER,0);
		tcicona.setWidth(20);
		tcicona.setText("");

		TableColumn tccontatto = new TableColumn(tvRisultatiUPDAppuntamenti.getTable(),SWT.CENTER,1);
		tccontatto.setWidth(100);
		tccontatto.setText("Agente");
		
		TableColumn tccalendario = new TableColumn(tvRisultatiUPDAppuntamenti.getTable(),SWT.CENTER,2);
		tccalendario.setWidth(100);
		tccalendario.setText("Calendario");
		
		TableColumn tcappuntamento = new TableColumn(tvRisultatiUPDAppuntamenti.getTable(),SWT.CENTER,3);
		tcappuntamento.setWidth(100);
		tcappuntamento.setText("Appuntamento");

		TableColumn tcicalid = new TableColumn(tvRisultatiUPDAppuntamenti.getTable(),SWT.CENTER,4);
		tcicalid.setWidth(100);
		tcicalid.setText("ID Evento");
		
		setControl(cb);
	}
	
	public ArrayList<GCalendarUploaderResult.AppuntamentiResult> castContattiResultToAppuntamentiResult(ArrayList<ContattiResult> contatti){
		
		if (appuntamentiResult == null){
			appuntamentiResult  = new ArrayList<GCalendarUploaderResult.AppuntamentiResult>();
			Iterator<ContattiResult> it = contatti.iterator();
			
			while(it.hasNext()){
				
				ContattiResult cr = it.next();
				
				Iterator<AppuntamentiModel> itam = cr.getAppuntamenti().iterator();				
				while(itam.hasNext()){
					
					AppuntamentiModel am = itam.next();
					AppuntamentiResult ar = new AppuntamentiResult(am);
//					ar.setCm(cr);
					
					appuntamentiResult.add(ar);
					
				}
								
			}
		}
		return appuntamentiResult;
		
	}
	
	public ArrayList<GCalendarUploaderResult.ColloquiResult> castContattiResultToColloquiResult(ArrayList<ContattiResult> contatti){
		
		if (colloquiResult == null){
			colloquiResult  = new ArrayList<GCalendarUploaderResult.ColloquiResult>();
			Iterator<ContattiResult> it = contatti.iterator();
			
			while(it.hasNext()){
				
				ContattiResult cr = it.next();
	
				Iterator<ColloquiModel> itcm = cr.getColloqui().iterator();
				while(itcm.hasNext()){
		
					ColloquiModel cm = itcm.next();
					ColloquiResult cmr = new ColloquiResult(cm);
//					cmr.setCm(cr);
					
					colloquiResult.add(cmr);
					
				}
			}
		}
		
		return colloquiResult;
		
	}

	public ArrayList<GCalendarUploaderResult.CalendarEventEntryResult> castContattiResultToCalendarEventEntryResult(ArrayList<ContattiResult> contatti){
		
		if (calendarEventEntryResults == null){
			calendarEventEntryResults  = new ArrayList<GCalendarUploaderResult.CalendarEventEntryResult>();
			Iterator<ContattiResult> it = contatti.iterator();
			
			while(it.hasNext()){
				
				ContattiResult cr = it.next();
				
				Iterator<CalendarEventEntry> itam = cr.getCee().iterator();
				
				while(itam.hasNext()){
					
					CalendarEventEntry cee = itam.next();
					GCalendarUploaderResult.CalendarEventEntryResult ceer = new GCalendarUploaderResult.CalendarEventEntryResult(cee);
//					ceer.setCm(cr);
					
					calendarEventEntryResults.add(ceer);
					
				}
				
			}
		}
		return calendarEventEntryResults;
		
	}
	
	public void refreshControls(){
	
		if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
				  							  .getOperarationType() == 
			GCalendarSyncWizard.GCalSyncVO.UPLOAD_APPUNTAMENTI_MODEL){
			setDescription("Sincronizzazione appuntamenti locali con eventi Google calendar");
		}else{
			setDescription("Sincronizzazione eventi Google calendar con appuntamenti locali");
		}
		
		if (((GCalendarSyncWizard)getWizard()).getGcalsyncVO()
				  							  .getOperarationType() == 
		    GCalendarSyncWizard.GCalSyncVO.UPLOAD_APPUNTAMENTI_MODEL){
			startButton.setImage(Activator.getImageDescriptor("icons/wizardgcalendar/uploadevents_button.png").createImage());
		}else{
			startButton.setImage(Activator.getImageDescriptor("icons/wizardgcalendar/downloadevents_button.png").createImage());
		}
		
	}

}
