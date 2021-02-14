package winkhouse.engine.gcalendar;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import winkhouse.dao.AppuntamentiDAO;
import winkhouse.dao.ColloquiDAO;
import winkhouse.helper.GCalendarHelper;
import winkhouse.helper.GoogleCalendarV3Helper;
import winkhouse.model.AgentiModel;
import winkhouse.model.AppuntamentiModel;
import winkhouse.model.ColloquiModel;
import winkhouse.vo.AgentiVO;
import winkhouse.wizard.GCalendarSyncWizard;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.google.gdata.data.calendar.CalendarEventEntry;

public class GoogleCalendarSyncEngine implements IRunnableWithProgress {

	public final static int NEW_FROM_GCAL = 1;
	public final static int UPD_FROM_GCAL = 2;
	public final static int DEL_FROM_GCAL = 3;
	public final static int NEW_FROM_WINKAPP = 4;
	public final static int UPD_FROM_WINKAPP = 5;
	
	private Date startSync = null;
	private Date endSync = null;
	private ArrayList<AgenteResult> returnValue = null; 
	private int typeSync = GCalendarSyncWizard.GCalSyncVO.UPLOAD_APPUNTAMENTI_MODEL;
	
	private Comparator IcalUIDComparator = new Comparator(){

		@Override
		public int compare(Object arg0, Object arg1) {
			
			Date datainizio = null;
			Date datafine = null;
			Date datainizio1 = null;
			Date datafine1 = null;
			Calendar c = Calendar.getInstance();
			
			if (arg0 instanceof AppuntamentiModel){
				datainizio = ((AppuntamentiModel)arg0).getDataAppuntamento();
				c.setTime(datainizio);
				c.set(Calendar.MILLISECOND, 0);
				datainizio = c.getTime();
				
				datafine = ((AppuntamentiModel)arg0).getDataFineAppuntamento();
				c.setTime(datafine);
				c.set(Calendar.MILLISECOND, 0);
				datafine = c.getTime();
				
			}
			if (arg0 instanceof ColloquiModel){
				datainizio = ((ColloquiModel)arg0).getDataColloquio();
				c.setTime(datainizio);
				c.set(Calendar.MILLISECOND, 0);
				datainizio = c.getTime();
				
				datafine = ((ColloquiModel)arg0).getDataColloquio();
				c.setTime(datafine);
				c.set(Calendar.MILLISECOND, 0);
				datafine = c.getTime();
				
			}
			if (arg1 instanceof AppuntamentiModel){
				datainizio1 = ((AppuntamentiModel)arg1).getDataAppuntamento();
				c.setTime(datainizio1);
				c.set(Calendar.MILLISECOND, 0);
				datainizio1 = c.getTime();
				
				datafine1 = ((AppuntamentiModel)arg1).getDataFineAppuntamento();
				c.setTime(datafine1);
				c.set(Calendar.MILLISECOND, 0);
				datafine1 = c.getTime();
				
			}
			if (arg1 instanceof ColloquiModel){
				datainizio1 = ((ColloquiModel)arg1).getDataColloquio();
				c.setTime(datainizio1);
				c.set(Calendar.MILLISECOND, 0);
				datainizio1 = c.getTime();
				
				datafine1 = ((ColloquiModel)arg1).getDataColloquio();
				c.setTime(datafine1);
				c.set(Calendar.MILLISECOND, 0);
				datafine1 = c.getTime();
				
			}
			
			if ((arg0 instanceof AppuntamentiModel) && (arg1 instanceof AppuntamentiModel)){ 
				if (((AppuntamentiModel)arg0).getiCalUID().equalsIgnoreCase(((AppuntamentiModel)arg1).getiCalUID())){
										
					if ((datainizio.compareTo(datainizio1)==0) && (datafine.compareTo(datafine1) == 0)){
						return 0;
					}else if (datainizio.compareTo(datainizio1)< 0){
						return -1;
					}else{
						return 1;
					}
									
				}else {
					return (((AppuntamentiModel)arg0).getiCalUID().compareTo(((AppuntamentiModel)arg1).getiCalUID()));	
				}
			}else
			if ((arg0 instanceof ColloquiModel) && (arg1 instanceof ColloquiModel)){ 
				if (((ColloquiModel)arg0).getiCalUid().equalsIgnoreCase(((ColloquiModel)arg1).getiCalUid())){
										
					if ((datainizio.compareTo(datainizio1)==0) && (datafine.compareTo(datafine1) == 0)){
						return 0;
					}else if (datainizio.compareTo(datainizio1)< 0){
						return -1;
					}else{
						return 1;
					}
									
				}else {
					return (((ColloquiModel)arg0).getiCalUid().compareTo(((ColloquiModel)arg1).getiCalUid()));	
				}
			}else

			if ((arg0 instanceof ColloquiModel) && (arg1 instanceof AppuntamentiModel)){ 
				if (((ColloquiModel)arg0).getiCalUid().equalsIgnoreCase(((AppuntamentiModel)arg1).getiCalUID())){
										
					if ((datainizio.compareTo(datainizio1)==0) && (datafine.compareTo(datafine1) == 0)){
						return 0;
					}else if (datainizio.compareTo(datainizio1)< 0){
						return -1;
					}else{
						return 1;
					}
									
				}else {
					return (((ColloquiModel)arg0).getiCalUid().compareTo(((AppuntamentiModel)arg1).getiCalUID()));	
				}
			}else{ 
				if (((AppuntamentiModel)arg0).getiCalUID().equalsIgnoreCase(((ColloquiModel)arg1).getiCalUid())){
										
					if ((datainizio.compareTo(datainizio1)==0) && (datafine.compareTo(datafine1) == 0)){
						return 0;
					}else if (datainizio.compareTo(datainizio1)< 0){
						return -1;
					}else{
						return 1;
					}
									
				}else {
					return (((AppuntamentiModel)arg0).getiCalUID().compareTo(((ColloquiModel)arg1).getiCalUid()));	
				}
			}
			
		}
		
	};
	
	public GoogleCalendarSyncEngine(){}
	
	public GoogleCalendarSyncEngine(int typeSync,Date startSync,Date endSync,ArrayList<AgenteResult> returnValue){
		
		this.endSync = endSync;
		this.startSync = startSync;
		this.returnValue = (returnValue == null)
				           ? new ArrayList<GoogleCalendarSyncEngine.AgenteResult>()
				           : returnValue;
		this.typeSync = typeSync;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
													 InterruptedException {
		
// 		monitor.beginTask("Sincronizzazione appuntamenti con Google Calendar", this.contatti.size());
		
		GCalendarHelper gch = new GCalendarHelper();
		GoogleCalendarV3Helper gcvh = null;
		
		try {
			gcvh = new GoogleCalendarV3Helper();
		} catch (GeneralSecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		int totalWork = 0;
		Iterator<AgenteResult> it = this.returnValue.iterator();
		AppuntamentiDAO appDAO = new AppuntamentiDAO();
		ColloquiDAO colloquiDAO = new ColloquiDAO(); 
		
		HashMap<Integer, Events> gCalEvents = new HashMap<Integer, Events>();
		HashMap<AgenteResult, ArrayList> winkEvents = new HashMap<AgenteResult, ArrayList>();
		
		if (gcvh != null){
			while (it.hasNext()) {
				
				totalWork ++;
				AgenteResult agentiModel = it.next();
				monitor.subTask(agentiModel.toString());
				
				try {				
					
					monitor.subTask("Inizio lettura dati");
					
					Iterator<CalendarListEntry> ite = agentiModel.getCleSelected().iterator();
					while (ite.hasNext()){
						
						CalendarListEntry c = ite.next();
						com.google.api.services.calendar.Calendar client = gcvh.getClient(agentiModel.getCodAgente());
						if (client != null){
							Events gcalevents = gcvh.showEvents(client, c.getId(), this.startSync,this.endSync);
							
							gCalEvents.put(agentiModel.getCodAgente(),gcalevents);
							
							if (this.typeSync == GCalendarSyncWizard.GCalSyncVO.UPLOAD_APPUNTAMENTI_MODEL){
								
								ArrayList uploadItems = new ArrayList();
								
								uploadItems.addAll(appDAO.listAppuntamentiByAgenteDaAICALL_Null(
								   												AppuntamentiModel.class.getName(), 
								   												agentiModel.getCodAgente(), 
								   												this.startSync,
								   												this.endSync));
			//					
								uploadItems.addAll(colloquiDAO.listColloquiByAgenteDaA_ICALL_Null(
																		   		ColloquiModel.class.getName(), 
																		   		agentiModel.getCodAgente(), 
																		   		this.startSync,
																		   		this.endSync));
								
								winkEvents.put(agentiModel,uploadItems);
								
								
							}else{
								
								ArrayList localItems = new ArrayList();
								
								localItems.addAll(appDAO.listAppuntamentiByAgenteDaA_Not_ICALL_Null(
							   		    										AppuntamentiModel.class.getName(), 
							   		    										agentiModel.getCodAgente(), 
																	   		    this.startSync,
																	   		    this.endSync));
								
								localItems.addAll(colloquiDAO.listColloquiByAgenteDaA_Not_ICALL_Null(
				   	   			   												ColloquiModel.class.getName(), 
				   	   			   												agentiModel.getCodAgente(), 
														   	   			   		this.startSync,
														   	   			  this.endSync));
								
								winkEvents.put(agentiModel,localItems);
								
								
							}
							
						}
						
						monitor.subTask("Inizio sincronizzazione");
										
	//					ArrayList<AgenteResult> arList = doSyncPreview(gCalEvents, winkEvents);				
	//					
	//					this.returnValue.addAll(arList);
						this.returnValue = doSyncPreview(gCalEvents, winkEvents);
					}
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
		
	public class ContattiResult {
		
		private ArrayList<AppuntamentiModel> appuntamenti = null;
		private ArrayList<ColloquiModel> colloqui = null;
		private ArrayList<CalendarEventEntry> cee = null;
		
		public ContattiResult() {}

		public ArrayList<AppuntamentiModel> getAppuntamenti() {
			if (appuntamenti == null){
				appuntamenti = new ArrayList<AppuntamentiModel>();
			}
			return appuntamenti;
		}

		public void setAppuntamenti(ArrayList<AppuntamentiModel> appuntamenti) {
			this.appuntamenti = appuntamenti;
		}

		public ArrayList<CalendarEventEntry> getCee() {
			if (cee == null){
				cee = new ArrayList<CalendarEventEntry>();
			}
			return cee;
		}

		public void setCee(ArrayList<CalendarEventEntry> cee) {
			this.cee = cee;
		}

		public ArrayList<ColloquiModel> getColloqui() {
			if (colloqui == null){
				colloqui = new ArrayList<ColloquiModel>();
			}
			return colloqui;
		}

		public void setColloqui(ArrayList<ColloquiModel> colloqui) {
			this.colloqui = colloqui;
		}
		
	}
	
	public class ItemUpload{
		
		public static final int UPL_OK 		= 0;
		public static final int UPL_ERROR 	= 1;
		
		private AgenteResult agente			= null;
		private CalendarListEntry cle 		= null;
		private Object winkobj 				= null;
		private int operationResult 		= UPL_OK;
		private String idEvento				= null;
		
		public ItemUpload(AgenteResult agente,CalendarListEntry cle, Object winkobj){
			this.agente = agente; 
			this.cle = cle;
			this.winkobj = winkobj;
		}

		public CalendarListEntry getCle() {
			return cle;
		}

		public void setCle(CalendarListEntry cle) {
			this.cle = cle;
		}

		public Object getWinkobj() {
			return winkobj;
		}

		public void setWinkobj(Object winkobj) {
			this.winkobj = winkobj;
		}

		public int getOperationResult() {
			return operationResult;
		}

		public void setOperationResult(int operationResult) {
			this.operationResult = operationResult;
		}
		
		public AgenteResult getAgente() {
			return agente;
		}
		
		public void setAgente(AgenteResult agente) {
			this.agente = agente;
		}

		public String getIdEvento() {
			return idEvento;
		}

		public void setIdEvento(String idEvento) {
			this.idEvento = idEvento;
		}
		
		
	}
	
	public class ItemDownload{

		public static final int DWN_OK 		= 0;
		public static final int DWN_ERROR 	= 1;
		
		private AgenteResult agente			= null;
		private CalendarListEntry cle 		= null;
		private Object winkobj 				= null;
		private int operationResult 		= DWN_OK;
		private Event evento				= null;
		private int codAppuntamento			= 0;
		
		public ItemDownload(AgenteResult agente,CalendarListEntry cle, Event eventobj){
			this.agente = agente;
			this.cle = cle;
			this.evento = eventobj;
		}

		public AgenteResult getAgente() {
			return agente;
		}

		public void setAgente(AgenteResult agente) {
			this.agente = agente;
		}

		public CalendarListEntry getCle() {
			return cle;
		}

		public void setCle(CalendarListEntry cle) {
			this.cle = cle;
		}

		public Object getWinkobj() {
			return winkobj;
		}

		public void setWinkobj(Object winkobj) {
			this.winkobj = winkobj;
		}

		public int getOperationResult() {
			return operationResult;
		}

		public void setOperationResult(int operationResult) {
			this.operationResult = operationResult;
		}

		public Event getEvento() {
			return evento;
		}

		public void setEvento(Event evento) {
			this.evento = evento;
		}

		public Integer getCodAppuntamento() {
			return codAppuntamento;
		}

		public void setCodAppuntamento(int codAppuntamento) {
			this.codAppuntamento = codAppuntamento;
		}
		
		

	}
	
	public class AgenteResult extends AgentiModel{

		private ArrayList<CalendarListEntry> cleSelected = null;
		private HashMap<CalendarListEntry,ArrayList<AppuntamentiModel>> hmAppuntamenti = null;
		private HashMap<CalendarListEntry,ArrayList<ColloquiModel>> hmColloqui = null;
		private HashMap<CalendarListEntry,ArrayList<Event>> hmEvents = null;
		
		public AgenteResult(AgentiVO agentiVO) {
			super(agentiVO);			
		}

		public ArrayList<CalendarListEntry> getCleSelected() {
			if (cleSelected == null){
				cleSelected = new ArrayList<CalendarListEntry>();
			}
			return cleSelected;
		}

		public void setContattiResult(ArrayList<CalendarListEntry> cleSelected) {
			this.cleSelected = cleSelected;
		}

		
		public HashMap<CalendarListEntry, ArrayList<AppuntamentiModel>> getHmAppuntamenti() {
			if (hmAppuntamenti == null){
				hmAppuntamenti = new HashMap<CalendarListEntry, ArrayList<AppuntamentiModel>>();
			}
			return hmAppuntamenti;
		}

		
		public HashMap<CalendarListEntry, ArrayList<ColloquiModel>> getHmColloqui() {
			if (hmColloqui == null){
				hmColloqui = new HashMap<CalendarListEntry, ArrayList<ColloquiModel>>();
			}
			return hmColloqui;
		}

		public HashMap<CalendarListEntry, ArrayList<Event>> getHmEvents() {
			if (hmEvents == null){
				hmEvents = new HashMap<CalendarListEntry, ArrayList<Event>>();
			}			
			return hmEvents;
		}

		public void setHmEvents(HashMap<CalendarListEntry, ArrayList<Event>> hmEvents) {
			this.hmEvents = hmEvents;
		}
				
	}
	
	private class WinkAppuntamentiModelSearcher{
		
		private ArrayList<AppuntamentiModel> winkappuntamenti = null;
		

		
		public WinkAppuntamentiModelSearcher(ArrayList<AppuntamentiModel> winkappuntamenti){
			
			this.winkappuntamenti = (ArrayList<AppuntamentiModel>) winkappuntamenti.clone();
			Collections.sort(this.winkappuntamenti, IcalUIDComparator);
			
		}
		
		public int search(Event cee){
			
			AppuntamentiModel am = new AppuntamentiModel();
			am.setiCalUID(cee.getICalUID());
			
			am.setDataAppuntamento(new Date(cee.getStart().getDateTime().getValue()));
			am.setDataFineAppuntamento(new Date(cee.getEnd().getDateTime().getValue()));
			
			return Collections.binarySearch(this.winkappuntamenti, am, IcalUIDComparator);
		}
		
	}
	
	private class WinkColloquiModelSearcher{
		
		private ArrayList<ColloquiModel> winkcolloqui = null;
				
		public WinkColloquiModelSearcher(ArrayList<ColloquiModel> winkcolloqui){
			
			this.winkcolloqui = (ArrayList<ColloquiModel>) winkcolloqui.clone();
			Collections.sort(this.winkcolloqui, IcalUIDComparator);
			
		}
		
		public int search(Event cee){
			ColloquiModel cm = new ColloquiModel();
			cm.setiCalUid(cee.getICalUID());
			
			cm.setDataColloquio(new Date(cee.getStart().getDateTime().getValue()));
			
			return Collections.binarySearch(this.winkcolloqui, cm, IcalUIDComparator);
		}
		
	}
	
	private class GCalendarEventEntrySearcher{
		
		private ArrayList<Event> calendarEventEntries = null;
		
		private Comparator<Event> IcalUIDComparator = new Comparator<Event>(){

			@Override
			public int compare(Event arg0, Event arg1) {
				
				Date datainizio = new Date(arg0.getStart().getDateTime().getValue());
				Date datafine = new Date(arg0.getEnd().getDateTime().getValue());
				Date datainizio1 = new Date(arg1.getStart().getDateTime().getValue());
				Date datafine1 = new Date(arg1.getEnd().getDateTime().getValue());
				
				if (arg0.getICalUID().equalsIgnoreCase(arg1.getICalUID())){
										
					if ((datainizio.compareTo(datainizio1)==0) && (datafine.compareTo(datafine1) == 0)){
						return 0;
					}else if (datainizio.compareTo(datainizio1)< 0){
						return -1;
					}else{
						return 1;
					}
					
				}else {
					if ((arg0.getICalUID() == null) && (arg1.getICalUID() != null)){
						return -1;
					}else if ((arg0.getICalUID() != null) && (arg1.getICalUID() == null)){
						return 1;
					}else{
						return arg0.getICalUID().compareTo(arg1.getICalUID());
					}
				}
				
			}
			
		};
		
		public GCalendarEventEntrySearcher(Events calendarEventEntries){
			
			this.calendarEventEntries = (ArrayList<Event>) calendarEventEntries.getItems();
			Collections.sort(this.calendarEventEntries, this.IcalUIDComparator);
			
		}
		
		public int search(AppuntamentiModel am){
			
			Event cee = new Event();
			cee.setICalUID(am.getiCalUID());			
		    Date startDate = new Date();
		    Date endDate = new Date(startDate.getTime() + 3600000);
		    DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
		    cee.setStart(new EventDateTime().setDateTime(start));
		    DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
		    cee.setEnd(new EventDateTime().setDateTime(end));
			
			return Collections.binarySearch(this.calendarEventEntries, cee, this.IcalUIDComparator);
		}

		public int search(ColloquiModel cm){
			
			Event cee = new Event();
			cee.setICalUID(cm.getiCalUid());			
		    Date startDate = new Date(cm.getDataColloquio().getTime() + 3600000);
		    Date endDate = new Date(cm.getDataColloquio().getTime() + 3600000);
		    DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
		    cee.setStart(new EventDateTime().setDateTime(start));
		    DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
		    cee.setEnd(new EventDateTime().setDateTime(end));			
			
			return Collections.binarySearch(this.calendarEventEntries, cee, this.IcalUIDComparator);
		}
		
	}
	
	private ArrayList<AgenteResult> doSyncPreview(HashMap<Integer, Events> gcalevents,
							  					  HashMap<AgenteResult, ArrayList> winkapps){
		
		ArrayList <AgenteResult> returnValue = new ArrayList<AgenteResult>();
		
		Iterator<Entry<AgenteResult, ArrayList>> itwinkapp = winkapps.entrySet().iterator();
		
		while (itwinkapp.hasNext()){
			
			Map.Entry<AgenteResult, ArrayList> me = itwinkapp.next();			
			AgenteResult aM = me.getKey();
			
//			AgenteResult amResult = new AgenteResult(aM);
			
			ArrayList winkappuntamenti = me.getValue();			  
						
			Iterator<CalendarListEntry> itcalendar = aM.getCleSelected().iterator();
			
			while(itcalendar.hasNext()){
				
				CalendarListEntry cm = itcalendar.next();
//				amResult.setContattiResult(new ArrayList<GoogleCalendarSyncEngine.ContattiResult>());
				
//				if (cm.isGMailAccount()){
					
					Events gcaleventList = gcalevents.get(aM.getCodAgente());
					if (gcaleventList != null){
						
						if (this.typeSync == GCalendarSyncWizard.GCalSyncVO.UPLOAD_APPUNTAMENTI_MODEL){
							
							GCalendarEventEntrySearcher gees = new GCalendarEventEntrySearcher(gcaleventList);					
//							ContattiResult cr = new ContattiResult(cm);
							
							Iterator itwinkappuntamentimodel = winkappuntamenti.iterator();
							
							while (itwinkappuntamentimodel.hasNext()){
								
								Object o = itwinkappuntamentimodel.next();
								if (o instanceof AppuntamentiModel){
									
									AppuntamentiModel amAgente = (AppuntamentiModel)o;
									if (gees.search(amAgente) < 0){
										if (aM.getHmAppuntamenti().get(cm) != null){
											aM.getHmAppuntamenti().get(cm).add(amAgente);
										}else{
											ArrayList<AppuntamentiModel> al = new ArrayList<AppuntamentiModel>();
											al.add(amAgente);
											aM.getHmAppuntamenti().put(cm, al);
										}
									}
									
								}
								if (o instanceof ColloquiModel){
									
									ColloquiModel cmAgente = (ColloquiModel)o;
									if (gees.search(cmAgente) < 0){
										if (aM.getHmColloqui().get(cm) != null){
											aM.getHmColloqui().get(cm).add(cmAgente);
										}else{
											ArrayList<ColloquiModel> al = new ArrayList<ColloquiModel>();
											al.add(cmAgente);
											aM.getHmColloqui().put(cm, al);
										}
									}
									
								}
								
							}
//							amResult.getContattiResult().add(cr);
						}else{
							
							ArrayList<AppuntamentiModel> appuntamenti = new ArrayList<AppuntamentiModel>();
							ArrayList<ColloquiModel> colloqui = new ArrayList<ColloquiModel>();
							
							Iterator it = winkappuntamenti.iterator();
							while(it.hasNext()){
								Object o = it.next();
								if (o instanceof AppuntamentiModel){
									appuntamenti.add((AppuntamentiModel)o);
								}
								if (o instanceof ColloquiModel){
									colloqui.add((ColloquiModel)o);
								}								
							}
							
							WinkAppuntamentiModelSearcher wams = new WinkAppuntamentiModelSearcher(appuntamenti);
							
							WinkColloquiModelSearcher wcms = new WinkColloquiModelSearcher(colloqui);
							
							Iterator<Event> itgcalevents = gcaleventList.getItems().iterator();
//							ContattiResult cr = new ContattiResult(cm);
							
							while (itgcalevents.hasNext()){
								
								Event cee = itgcalevents.next();
								
						//		List<String> recurrence = cee.getRecurrence();
								
								
								if ((wams.search(cee) < 0) && (wcms.search(cee) < 0)){
									if (aM.getHmEvents().get(cm) != null){
										aM.getHmEvents().get(cm).add(cee);
									}else{
										ArrayList<Event> al = new ArrayList<Event>();
										al.add(cee);
										aM.getHmEvents().put(cm, al);
									}
																	
								}
								
							}
//							
							
/*							WinkColloquiModelSearcher wcms = new WinkColloquiModelSearcher(winkappuntamenti);
							itgcalevents = gcaleventList.iterator();
							
							
							while (itgcalevents.hasNext()){
								CalendarEventEntry cee = itgcalevents.next();
								if (wcms.search(cee) < 0){
									cr.getCee().add(cee);																	
								}
							}*/
//							amResult.getContattiResult().add(cr);
							
							
						}
						
//					}
					
				}
				
			}
			
			returnValue.add(aM);
						
		}
		
		return returnValue;
				
	}

}
