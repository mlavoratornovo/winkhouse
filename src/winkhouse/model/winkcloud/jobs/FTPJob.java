package winkhouse.model.winkcloud.jobs;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import winkhouse.Activator;
import winkhouse.helper.WinkCloudHelper;
import winkhouse.model.winkcloud.BaseConnector;
import winkhouse.model.winkcloud.CloudQueryModel;
import winkhouse.model.winkcloud.CloudQueryOrigin;
import winkhouse.model.winkcloud.CloudQueryTypes;
import winkhouse.model.winkcloud.ConnectorTypes;
import winkhouse.model.winkcloud.FTPConnector;
import winkhouse.util.WinkhouseUtils;
import winkhouse.util.ZipUtils;
import winkhouse.view.winkcloud.QueryFilesView;

public class FTPJob extends Job{

	boolean chkvar = true;
	private BaseConnector connector = null;
	private ArrayList<CloudQueryModel> cloudQueries = null;
	private long pollingIntervall = 5;
	private FTPClient client = null;
	private QueryFilesView qfv = null;
	
	public QueryFilesView getQfv() {
		return qfv;
	}

	public void setQfv(QueryFilesView qfv) {
		this.qfv = qfv;
	}

	public long getPollingIntervall() {
		return pollingIntervall;
	}

	public void setPollingIntervall(long pollingIntervall) {
		this.pollingIntervall = pollingIntervall;
	}

	public ArrayList<CloudQueryModel> getCloudQueries() {
		return cloudQueries;
	}
	
	public void addCloudQuery(CloudQueryModel cqm){
		getCloudQueries().add(cqm);
		
		if (getQfv() != null){
			if (getQfv().getSite().getWorkbenchWindow().getWorkbench().getDisplay() != null){
				getQfv().getSite().getWorkbenchWindow().getWorkbench().getDisplay().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						getQfv().setQueries(getCloudQueries());
					}
				});
			}
		}
		
	} 

	public void setCloudQueries(ArrayList<CloudQueryModel> cloudQueries) {
		this.cloudQueries = cloudQueries;
	}

	public BaseConnector getConnector() {
		return connector;
	}

	public void setConnector(BaseConnector connector) {
		this.connector = connector;
	}

	public FTPJob(String name) {
		super(name);
	}
	
	public boolean dologin(String username,String password) throws SocketException, IOException{
		
		boolean result = false;
		
		client = new FTPClient();
		client.connect(((FTPConnector)getConnector()).getUrl());
		result = client.login(username, password);
		client.enterLocalPassiveMode();
		client.setFileType(FTP.BINARY_FILE_TYPE);
		return result;
	}
	
	public WCFileInfo[] getFileListNames() throws IOException{
		
		FTPFile[] files = client.listFiles();
		client.setFileType(FTP.BINARY_FILE_TYPE);
		ArrayList<WCFileInfo> alfiles = new ArrayList<WCFileInfo>();
		
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().startsWith("mwinkhouse_Q_") || files[i].getName().startsWith("mwinkhouse_R_")){
				alfiles.add(new WCFileInfo(files[i].getName(),
										   null,
										   ConnectorTypes.FTP,
										   new Date(files[i].getTimestamp().getTimeInMillis())));
			}
		}
		
		return alfiles.toArray(new WCFileInfo[alfiles.size()]);
		
	}
	
	public boolean downloadRequestFile(String filename,BufferedOutputStream filedownload) throws IOException{
		
		return client.retrieveFile(filename, filedownload);
		
	}

	public String getPercorso(){
		return ((FTPConnector)getConnector()).getPercorso();
	}
	
	public void deleteRemoteFile(String filename) throws IOException{
		client.deleteFile(filename);
	}
	
	public boolean uploadResultFile(String percorsoFileName,File in) throws IOException{
		
		FileInputStream fis = new FileInputStream(in);
		
		return client.storeFile(percorsoFileName, fis);
		
	}
	
	public void disconnect() throws IOException{
        if (client.isConnected()) {
            client.logout();
            client.disconnect();
        }

	}
		
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		
		if (getConnector() != null){
			
			while (isChkvar()) {
									
				try {
					
					if (dologin(getConnector().getUsername(), getConnector().getPassword())){
												
						WCFileInfo[] files = getFileListNames();
						
						for (int i = 0; i < files.length; i++) {
							
							if (files[i].getName().startsWith("mwinkhouse_R_")){
								
								try {
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									Date dateWithoutTime = sdf.parse(sdf.format(files[i].getDataFile()));

									SimpleDateFormat sdftoday = new SimpleDateFormat("yyyy-MM-dd");								
									Date todaydateWithoutTime = sdftoday.parse(sdf.format(new Date()));

									if (dateWithoutTime.before(todaydateWithoutTime)){
										deleteRemoteFile(getPercorso() + files[i].toString());
									}
									
									continue;
									
								} catch (ParseException e) {
									e.printStackTrace();
								}
								
							}
							
							if (files[i].getName().startsWith("mwinkhouse_Q_")){
															
								String unzipdir = "tmp_" + new Date().getTime();
									
					            String remoteFile1 = getPercorso() + files[i].toString();
					            
					            String filePathName = Activator.getDefault().getStateLocation().toFile().toString() + File.separator + "cloudsearch";
					            File f = new File(filePathName);
					            f.mkdirs();
					            									            
					            File downloadFile1 = new File(filePathName + File.separator + files[i].getName());
					            
					            BufferedOutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
					            boolean exportresult = true;
					            
					            boolean success = downloadRequestFile(remoteFile1, outputStream1);
					            
					            outputStream1.close();
						            					            
					            if (success){
					            	
					            	deleteRemoteFile(remoteFile1);
					            	
						            WinkCloudHelper wch = new WinkCloudHelper();								            
						            ArrayList qal = wch.parseMobileRequest(filePathName + File.separator + files[i].getName(),unzipdir);
						            
						            if (qal.size() > 0){
						            							            
							            CloudQueryModel cqm = new CloudQueryModel(CloudQueryOrigin.MOBILE,
							            										  filePathName + File.separator + files[i], 
							            										  new Date());
							            
							            int maxcloud = Integer.valueOf((WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.MAXCLOUDRESULT).equalsIgnoreCase(""))
							            								? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.MAXCLOUDRESULT)
							            								: WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.MAXCLOUDRESULT));
							            
						            	try {
						            		qal = new ArrayList(qal.subList(0, maxcloud));
											cqm.setResults(qal);											
										} catch (Exception e1) {
											cqm.setResults(qal);											
					            		}
							            try {
											cqm.setCriteri(wch.getCriteri());
										} catch (Exception e) {
										}
							            addCloudQuery(cqm);
							            downloadFile1.delete();
							            
							            try {
							            	
											if (wch.getType() == CloudQueryTypes.IMMOBILI){
												exportresult = wch.exportImmobili(qal,filePathName + File.separator + unzipdir);
												cqm.setQueryType(CloudQueryTypes.IMMOBILI);
											}
											if (wch.getType() == CloudQueryTypes.ANAGRAFICHE){
												cqm.setQueryType(CloudQueryTypes.ANAGRAFICHE);
											}											
											
											String exportfilename = files[i].getName().replace("_Q_", "_R_");
											String zipdir = filePathName + File.separator + unzipdir;
											
											File zipdirfile = new File(zipdir);
											
											zipdirfile.mkdirs();
											
											String xmlfilepath = null;
											
											if (exportresult){
												
												ZipUtils zu = new ZipUtils();
												if (zu.zip4jArchivio(zipdir, zipdir + File.separator + exportfilename)){
													File up = new File(zipdir + File.separator + exportfilename);
									                uploadResultFile(getConnector().getPercorso() + exportfilename.substring(exportfilename.lastIndexOf(File.separator)+1), up);
									                up.delete();
									                WinkhouseUtils.getInstance().tmpDirectoryDeleter(zipdir);
												}
												zipdirfile.delete();
											}
											
										
										} catch (Exception e) {
											e.printStackTrace();
										}
							            
						            }else{
						            	String exportfilename = files[i].getName().replace("_Q_", "_R_");
						            	String zipdir = filePathName + File.separator + unzipdir;
						            	
						            	File emptyFile = new File(zipdir + File.separator + exportfilename);
						            	
						            	File zipdirfile = new File(zipdir);										
										zipdirfile.mkdirs();
						            	emptyFile.createNewFile();
						            	
						                boolean result = uploadResultFile(getConnector().getPercorso() + exportfilename.substring(exportfilename.lastIndexOf(File.separator)+1), emptyFile);
						                emptyFile.delete();
						                WinkhouseUtils.getInstance().tmpDirectoryDeleter(zipdir);
						                zipdirfile.delete();
						            	
						            }
						            
					            }
					            
							}
							
						}
														
					}
				} catch (SocketException e) {
					chkvar = false;
				} catch (IOException e) {
					chkvar = false;
				} finally {
		            try {
		            	disconnect();
		            } catch (IOException ex) {
		                ex.printStackTrace();
		                chkvar = false;
		            }
		        }
				
				try {
					TimeUnit.SECONDS.sleep(pollingIntervall);
				} catch (InterruptedException e) {
					chkvar = false;
					e.printStackTrace();
				}
				
			}
		}
		return Status.OK_STATUS;
	

	}

	public boolean isChkvar() {
		return chkvar;
	}

	public void setChkvar(boolean chkvar) {
		this.chkvar = chkvar;
	}
	
}
