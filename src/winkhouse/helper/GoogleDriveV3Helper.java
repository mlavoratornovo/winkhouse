package winkhouse.helper;


import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import winkhouse.Activator;
import winkhouse.dialogs.custom.WinkBrowserPopUp;
import winkhouse.model.winkcloud.CloudQueryModel;
import winkhouse.model.winkcloud.CloudQueryOrigin;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.http.GenericUrl;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GoogleDriveV3Helper extends GoogleCalendarV3Helper {
	
	private Drive driveService = null;
	private String monitorid = null;
	public static String MONITOR_ID = "cloudsearch";
			
	public GoogleDriveV3Helper() throws GeneralSecurityException, IOException {
		super();
	}
		
	public Drive getDriveService(String monitorid) throws IOException {
		
		if (driveService == null){
			try{					
				this.monitorid = monitorid;
				Credential credential = getCredential(String.valueOf(monitorid));				
				if (credential != null){
					driveService = new Drive.Builder(httpTransport, 
											  		 JSON_FACTORY, 
											   		 credential).setApplicationName(APPLICATION_NAME).build();
					
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return driveService;

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
														   Collections.singleton(DriveScopes.DRIVE)).setDataStoreFactory(dataStoreFactory)
																					   				.build();
			return flow;
		} catch (IOException e2) {
			return null;
		}

	}

	public void authorize(){
		
		WinkBrowserPopUp wbpp = new WinkBrowserPopUp(getAuthorizationUrl(),this,"setCodeCallBack");
		
	}
	
	public void setCodeCallBack(String code){
		if (this.monitorid != null){
			authorizeToken(code, String.valueOf(this.monitorid));
		}
	}

}
