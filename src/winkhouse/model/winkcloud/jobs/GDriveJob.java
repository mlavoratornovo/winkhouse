package winkhouse.model.winkcloud.jobs;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import winkhouse.model.winkcloud.BaseConnector;
import winkhouse.model.winkcloud.CloudQueryModel;
import winkhouse.model.winkcloud.ConnectorTypes;

import com.google.api.client.http.FileContent;
import com.google.api.client.util.DateTime;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GDriveJob extends FTPJob {
	
	private boolean chkvar = true;
	private long pollingIntervall = 0;
	private BaseConnector connector = null;
	private ArrayList<CloudQueryModel> cloudQueries = null;
	private Drive driveService = null;

	public boolean isChkvar() {
		return chkvar;
	}

	public void setChkvar(boolean chkvar) {
		this.chkvar = chkvar;
	}

	public long getPollingIntervall() {
		return pollingIntervall;
	}

	public void setPollingIntervall(long pollingIntervall) {
		this.pollingIntervall = pollingIntervall;
	}

	public BaseConnector getConnector() {
		return connector;
	}

	public void setConnector(BaseConnector connector) {
		this.connector = connector;
	}

	public ArrayList<CloudQueryModel> getCloudQueries() {
		return cloudQueries;
	}

	public void setCloudQueries(ArrayList<CloudQueryModel> cloudQueries) {
		this.cloudQueries = cloudQueries;
	}

	public Drive getDriveService() {
		return driveService;
	}

	public void setDriveService(Drive driveService) {
		this.driveService = driveService;
	}

	public GDriveJob(String name) {
		super(name);
	}

	@Override
	public boolean dologin(String username, String password) throws SocketException, IOException {
		return true;
	}

	@Override
	public WCFileInfo[] getFileListNames() throws IOException {
		
		java.util.List<File> result = new ArrayList<File>();
		ArrayList<WCFileInfo> returnValue = new ArrayList<WCFileInfo>();
	    Files.List request = null;

		do {
		    try {
		    	request = getDriveService().files().list();
		    	FileList files = request.setQ("'root' in parents and trashed=false")
		    							.setFields("nextPageToken, files(id,name,modifiedTime)")
		    							.setPageSize(20).execute();
		    	result.addAll(files.getFiles());          
		    	request.setPageToken(files.getNextPageToken());
		    } 
		    catch (IOException e)   {
		    	System.out.println("An error occurred: " + e);
		    	request.setPageToken(null);
		    }
       } while (request.getPageToken() != null && request.getPageToken().length() > 0);

	   for(File f: result){
		   
	       if (f.getName().startsWith("mwinkhouse_Q_") || f.getName().startsWith("mwinkhouse_R_")){
	    	   
	    		returnValue.add(new WCFileInfo(f.getName(),
	    									   f.getId(),
	    									   ConnectorTypes.GOOGLEDRIVE,
	    									   new Date(f.getModifiedTime().getValue())));
	       }
	       
	   }
	    
	   return returnValue.toArray(new WCFileInfo[returnValue.size()]);

	}

	@Override
	public boolean downloadRequestFile(String filename,BufferedOutputStream filedownload) throws IOException {
		
		try{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			getDriveService().files().get(filename).executeMediaAndDownloadTo(outputStream);
			outputStream.writeTo(filedownload);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String getPercorso() {
		return "";
	}

	@Override
	public void deleteRemoteFile(String filename) throws IOException {
		
		getDriveService().files().delete(filename).execute();
	}

	@Override
	public boolean uploadResultFile(String percorsoFileName, java.io.File in) throws IOException {
		
        try {
			File fileMetadata = new File();
			fileMetadata.setName(percorsoFileName);
			
			FileContent mediaContent = new FileContent("application/zip", in);
			File file = getDriveService().files().create(fileMetadata, mediaContent)
			        							 .setFields("id")
			        							 .execute();
			
			System.out.println("File ID: " + file.getId());
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void disconnect() throws IOException {
		
	}
	
	

}
