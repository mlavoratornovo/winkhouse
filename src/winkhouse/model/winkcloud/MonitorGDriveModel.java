package winkhouse.model.winkcloud;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import winkhouse.Activator;
import winkhouse.helper.GoogleDriveV3Helper;
import winkhouse.helper.WinkCloudHelper;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.model.winkcloud.jobs.FTPJob;
import winkhouse.model.winkcloud.jobs.GDriveJob;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.winkcloud.QueryFilesView;
import winkhouse.xmlser.helper.XMLExportHelper;
import winkhouse.xmlser.models.xml.AnagraficheXMLModel;
import winkhouse.xmlser.models.xml.ImmobiliXMLModel;
import winkhouse.xmlser.utils.ZipUtils;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class MonitorGDriveModel implements MonitorModel {
	
	private boolean chkvar = true;
	private long pollingIntervall = 0;
	private GDriveConnector connector = null;
	private ArrayList<CloudQueryModel> cloudQueries = null;
	private Drive driveService = null;
	private CloudMonitorState stato = null;
	private String accountName = null;
	private GDriveJob job = null;
	private QueryFilesView qfv = null;
	
	public MonitorGDriveModel() {
		// TODO Auto-generated constructor stub
	}
	
	public Drive getDriveService(String monitorid) throws IOException {
		
		if (driveService == null){
			
			GoogleDriveV3Helper gdh;
			try {
				gdh = new GoogleDriveV3Helper();
				driveService = gdh.getDriveService("");				
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
			}
			
			
		}
		return driveService;

    }
	
	private void downloadFile(String gdriveFileid, String pathtodownload) throws IOException {

		FileOutputStream out = new FileOutputStream(new java.io.File(pathtodownload));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		getDriveService(GoogleDriveV3Helper.MONITOR_ID).files()
													   .get(gdriveFileid)										   
													   .executeMediaAndDownloadTo(outputStream);
		outputStream.writeTo(out);

	}
	
	public ArrayList<CloudQueryModel> getCloudQueries() {
		
		if (cloudQueries == null){
			cloudQueries = new ArrayList<CloudQueryModel>();
		}
		return cloudQueries;
	}
		
	public GDriveConnector getConnector() {
		return connector;
	}

	public void setConnector(GDriveConnector connector) {
		this.connector = connector;
	}

	@Override
	public void start() {
		
		job = new GDriveJob("Monitor Ascolto GDrive");
		job.setConnector(getConnector());
		job.setDriveService(getDriveService());
		job.setPollingIntervall(getPollingIntervall());
		job.setQfv(qfv);
		job.setCloudQueries(getCloudQueries());
		job.schedule();
		
	}

	@Override
	public void setCloudQueries(ArrayList<CloudQueryModel> cloudQueries) {
		this.cloudQueries = cloudQueries;
	}

	@Override
	public CloudMonitorState getStato() {
		return this.stato;
	}

	@Override
	public void setStato(CloudMonitorState stato) {
		this.stato = stato;
	}

	@Override
	public boolean isChkvar() {
		return chkvar;
	}
	
	public void setChkvar(boolean chkvar) {
		this.chkvar = chkvar;

	}

	@Override
	public long getPollingIntervall() {
		return pollingIntervall;
	}

	@Override
	public void setPollingIntervall(long pollingIntervall) {
		this.pollingIntervall = pollingIntervall;

	}

	@Override
	public ConnectorTypes getType() {
		return ConnectorTypes.GOOGLEDRIVE;
	}
	
	public Drive getDriveService() {
		return driveService;
	}
	
	public void setDriveService(Drive driveService) {
		this.driveService = driveService;
	}
	
	@Override
	public boolean save(String pathFileName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void stop() {
		if (this.getJob() != null){
			this.getJob().setChkvar(false);
		}
	}

@Override
	
	public String toString() {
		
		return accountName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public GDriveJob getJob() {
		return job;
	}

	
	@Override
	public void setQueryFilesView(QueryFilesView qfv) {
		this.qfv = qfv;		
	}
	
	
}
