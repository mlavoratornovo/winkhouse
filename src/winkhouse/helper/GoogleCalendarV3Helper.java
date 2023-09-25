package winkhouse.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.swt.program.Program;

import winkhouse.Activator;
import winkhouse.dao.WinkGCalendarDAO;
import winkhouse.dialogs.custom.GAuthCodeSaver;
import winkhouse.dialogs.custom.WinkBrowserPopUp;
import winkhouse.engine.gcalendar.GoogleCalendarSyncEngine;
import winkhouse.model.AppuntamentiModel;
import winkhouse.model.ColloquiModel;
import winkhouse.model.WinkGCalendarModel;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

public class GoogleCalendarV3Helper {

	public final String APPLICATION_NAME = "winkhouse.org";

	  /** Directory to store user credentials. */
	private final File DATA_STORE_DIR = new File(Activator.getDefault().getStateLocation()
			   														   .toFile()
			   														   .toString() + 
			   												           File.separator + 
			   												           "gcredentials");

	  /**
	   * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
	   * globally shared instance across your application.
	   */
	protected FileDataStoreFactory dataStoreFactory;
	  
	  /** Global instance of the HTTP transport. */
	public static HttpTransport httpTransport;

	  /** Global instance of the JSON factory. */
	public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private com.google.api.services.calendar.Calendar client;
    
    private static final String CALLBACK_URL = "urn:ietf:wg:oauth:2.0:oob";
    
    private Integer codAgente = null;
    
    
	public GoogleCalendarV3Helper() throws GeneralSecurityException, IOException {
		
		httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		if (!DATA_STORE_DIR.exists()){
			DATA_STORE_DIR.mkdirs();
		}
		dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
	}	
	
	public Calendar getClient(Integer codAgente){
		
		this.codAgente = codAgente;
		
		if (client == null){
			try{					

				Credential credential = getCredential(String.valueOf(this.codAgente));
				
				if (credential != null){
					client = new Calendar.Builder(httpTransport, 
												  JSON_FACTORY, 
												  credential).setApplicationName(APPLICATION_NAME).build();
					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return client;
		
	}
	
	public GoogleAuthorizationCodeFlow getFlow(){

	    GoogleClientSecrets clientSecrets = null;
		try {
			clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
													 new InputStreamReader(getClass().getResourceAsStream("/winkhouse/configuration/client_id.json")));
		} catch (IOException e3) {
			return null;
		}
	    
	    if (clientSecrets!= null && (clientSecrets.getDetails().getClientId().startsWith("Enter") || clientSecrets.getDetails().getClientSecret().startsWith("Enter "))) {
	    	return null;
	    }
	    // set up authorization code flow
	    GoogleAuthorizationCodeFlow flow;
		try {
			flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, 
														   JSON_FACTORY, 
														   clientSecrets,
														   Collections.singleton(CalendarScopes.CALENDAR)).setDataStoreFactory(dataStoreFactory)
																					   					  .build();
			return flow;
		} catch (IOException e2) {
			return null;
		}

	}
	
	public Credential getCredential(String userId){
		
		Credential returnValue = null;
		
		try {
			
			returnValue = getFlow().loadCredential(userId);
			
			if (returnValue == null){
				authorize(userId);
				returnValue = getFlow().loadCredential(userId);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return returnValue;
		
	}
	
	public String getAuthorizationUrl(){
		
		String returnValue = null;
										
		returnValue = getFlow().newAuthorizationUrl().setRedirectUri(CALLBACK_URL).build();
		
		return returnValue;
		
	}
	
	public void setCodeCallBack(String code){
		authorizeToken(code, String.valueOf(codAgente));
	}
	
	public boolean authorizeToken(String authorizationCode,String userId){
		
		GoogleAuthorizationCodeFlow flow = getFlow();
		if (flow != null){
			
			GoogleAuthorizationCodeTokenRequest tokenRequest = flow.newTokenRequest(authorizationCode);
			tokenRequest.setRedirectUri(CALLBACK_URL);
			GoogleTokenResponse tokenResponse = null;;
			
			try {
				tokenResponse = tokenRequest.execute();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
	
			try {
				flow.createAndStoreCredential(tokenResponse, userId);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}else{
			return false;
		}
		
	}
	
	public void authorize(String idCode){
		Program.launch(getAuthorizationUrl());
		GAuthCodeSaver s = new GAuthCodeSaver(idCode);
		//WinkBrowserPopUp wbpp = new WinkBrowserPopUp(getAuthorizationUrl(),this,"setCodeCallBack");
		
	}

	
	public CalendarList showCalendars(com.google.api.services.calendar.Calendar client) throws IOException {
		
	    CalendarList feed = client.calendarList().list().execute();
	    return feed;
	}

	
	public Events showEvents(com.google.api.services.calendar.Calendar client, String calendar_id, Date startSyncDate,Date endSyncDate) throws IOException {
	    
	    Events feed = client.events().list(calendar_id)
	    							 .setTimeMin(new DateTime(startSyncDate))
	    							 .setTimeMax(new DateTime(endSyncDate))
	    							 .execute();
	    
	    return feed;	    
	}

	
	public Event addEvent(com.google.api.services.calendar.Calendar client,String calendar_id,Event event) throws IOException{
		return client.events().insert(calendar_id, event).execute();
	}
	
	
	public Event getEvent(com.google.api.services.calendar.Calendar client,String calendar_id,String eventid) throws IOException{
		return client.events().get(calendar_id, eventid).execute();
	}
	
	
	public void deleteEvent(com.google.api.services.calendar.Calendar client,String calendar_id,String eventid) throws IOException{
		client.events().delete(calendar_id, eventid).execute();		
	} 

	
	public Event updateEvent(com.google.api.services.calendar.Calendar client,String calendar_id,String eventid,Event event) throws IOException{
		return client.events().update(calendar_id, eventid, event).execute();		
	} 

	
	public String handleEvent(com.google.api.services.calendar.Calendar client,String calendar_id,GoogleCalendarSyncEngine.ItemUpload itemUpload) throws IOException{
		
		if (itemUpload.getWinkobj() instanceof AppuntamentiModel) {

			WinkGCalendarDAO wgcDAO = new WinkGCalendarDAO();
			
			WinkGCalendarModel wgcModel = (WinkGCalendarModel)wgcDAO.getWinkGCalendarByCodAgenteCodAppuntamentoCalendarId(WinkGCalendarModel.class.getName(),
																									  					  itemUpload.getAgente().getCodAgente(), 
																									  					  ((AppuntamentiModel)itemUpload.getWinkobj()).getCodAppuntamento(),
																									  					  calendar_id);

			Event event = null;
			
			if (wgcModel == null){
				
				event = new Event();
				
			    event.setSummary(((AppuntamentiModel)itemUpload.getWinkobj()).getLuogo());
			    event.setDescription(((AppuntamentiModel)itemUpload.getWinkobj()).getDescrizione());
			    
			    Date startDate = ((AppuntamentiModel)itemUpload.getWinkobj()).getDataAppuntamento();
			    Date endDate = ((AppuntamentiModel)itemUpload.getWinkobj()).getDataFineAppuntamento();
			    
			    DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));			    
			    event.setStart(new EventDateTime().setDateTime(start));
			    
			    DateTime end = new DateTime((endDate != null)? endDate: startDate, TimeZone.getTimeZone("UTC"));
			    event.setEnd(new EventDateTime().setDateTime(end));
			    
			    event = addEvent(client, calendar_id, event);
			    
			    if (event.getId() != null){ 
				    wgcModel = new WinkGCalendarModel(itemUpload.getAgente().getCodAgente(),
				    								  ((AppuntamentiModel)itemUpload.getWinkobj()).getCodAppuntamento(),
				    								  null, 
				    								  itemUpload.getCle().getId(),
				    								  event.getId());
				    
				    
				    wgcDAO.saveUpdate(wgcModel, null, true);
				    
					return event.getId();
					
			    }else{
			    	
			    	return null;
			    	
			    }
				
			}else{
												
				if ((wgcModel.getEventId() != null) && (wgcModel.getEventId().equalsIgnoreCase(""))) {
					
					event = getEvent(client, calendar_id, wgcModel.getEventId());
					
					event.setSummary(((AppuntamentiModel)itemUpload.getWinkobj()).getLuogo());
					event.setDescription(((AppuntamentiModel)itemUpload.getWinkobj()).getDescrizione());
					
				    Date startDate = ((AppuntamentiModel)itemUpload.getWinkobj()).getDataAppuntamento();
				    Date endDate = ((AppuntamentiModel)itemUpload.getWinkobj()).getDataFineAppuntamento();
				    
				    DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));			    
				    event.setStart(new EventDateTime().setDateTime(start));
				    
				    DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
				    event.setEnd(new EventDateTime().setDateTime(end));
				    
					event = updateEvent(client, calendar_id, event.getId(), event);
					
					if (event.getId() != null){ 
											
					    wgcModel.setEventId(event.getId());
					    
					    wgcDAO.saveUpdate(wgcModel, null, true);
					    
					    return event.getId();
					    
					}
					
					return null;
				}
				
			}						
			
		}
		if (itemUpload.getWinkobj() instanceof ColloquiModel) {
			
			WinkGCalendarDAO wgcDAO = new WinkGCalendarDAO();
			
			WinkGCalendarModel wgcModel = (WinkGCalendarModel)wgcDAO.getWinkGCalendarByCodAgenteCodColloquioCalendarId(WinkGCalendarModel.class.getName(),
																													   itemUpload.getAgente().getCodAgente(), 
																									  				   ((ColloquiModel)itemUpload.getWinkobj()).getCodColloquio(),
																									  				    calendar_id);

			Event event = null;
			
			if (wgcModel == null){
				
				event = new Event();
				
			    event.setSummary(((ColloquiModel)itemUpload.getWinkobj()).getLuogoIncontro());
			    event.setDescription(((ColloquiModel)itemUpload.getWinkobj()).getDescrizione());
			    
			    Date startDate = ((ColloquiModel)itemUpload.getWinkobj()).getDataColloquio();
			    Date endDate = ((ColloquiModel)itemUpload.getWinkobj()).getDataColloquio();
			    
			    DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));			    
			    event.setStart(new EventDateTime().setDateTime(start));
			    
			    DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
			    event.setEnd(new EventDateTime().setDateTime(end));
			    
			    event = addEvent(client, calendar_id, event);
			    
			    if (event.getId() != null){ 
				    wgcModel = new WinkGCalendarModel(itemUpload.getAgente().getCodAgente(),
				    								  null,
				    								  ((ColloquiModel)itemUpload.getWinkobj()).getCodColloquio(), 
				    								  itemUpload.getCle().getId(),
				    								  event.getId());
				    
				    
				    wgcDAO.saveUpdate(wgcModel, null, true);
				    
					return event.getId();
					
			    }else{
			    	
			    	return null;
			    	
			    }
				
			}else{
												
				if ((wgcModel.getEventId() != null) && (wgcModel.getEventId().equalsIgnoreCase(""))) {
					
					event = getEvent(client, calendar_id, wgcModel.getEventId());
					
					event.setSummary(((ColloquiModel)itemUpload.getWinkobj()).getLuogoIncontro());
					event.setDescription(((ColloquiModel)itemUpload.getWinkobj()).getDescrizione());
					
				    Date startDate = ((ColloquiModel)itemUpload.getWinkobj()).getDataColloquio();
				    Date endDate = ((ColloquiModel)itemUpload.getWinkobj()).getDataColloquio();
				    
				    DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));			    
				    event.setStart(new EventDateTime().setDateTime(start));
				    
				    DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
				    event.setEnd(new EventDateTime().setDateTime(end));
				    
					event = updateEvent(client, calendar_id, event.getId(), event);
					
					if (event.getId() != null){ 
											
					    wgcModel.setEventId(event.getId());
					    
					    wgcDAO.saveUpdate(wgcModel, null, true);
					    
					    return event.getId();
					    
					}
					
					return null;
				}
				
			}						
			
			
		}
		
		return null;
		
	}
	
}