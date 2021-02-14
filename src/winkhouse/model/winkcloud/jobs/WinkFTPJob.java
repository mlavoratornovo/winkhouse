package winkhouse.model.winkcloud.jobs;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import winkhouse.Activator;
import winkhouse.helper.WinkCloudHelper;
import winkhouse.model.winkcloud.CloudQueryModel;
import winkhouse.model.winkcloud.CloudQueryOrigin;
import winkhouse.model.winkcloud.CloudQueryTypes;
import winkhouse.model.xml.RicercheXMLModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.xmlser.helper.XMLExportHelper;
import winkhouse.util.ZipUtils;

public class WinkFTPJob extends FTPJob {
	
	private RicercheXMLModel ricerca = null;
	
	String basePath = Activator.getDefault().getStateLocation().toFile().toString() + File.separator + "winkcloudsearch";
	
	public WinkFTPJob(String name, RicercheXMLModel ricerca) {
		super(name);
		this.ricerca = ricerca;
	}
	
	protected boolean uploadRicerca(){
		
		boolean returnValue = true;
		
		if (getConnector() != null){
			
			try {
				
				if (dologin(getConnector().getUsername(), getConnector().getPassword())){
					
					XMLExportHelper xmlExportHelper = new XMLExportHelper();
					HashMap hm = new HashMap();
					hm.put(this.ricerca.getNome(), this.ricerca);
					
					String folderName = basePath + File.separator + String.valueOf(new Date().getTime());
					String fileName = this.ricerca.getNome() + ".xml";
					
					if (xmlExportHelper.exportSelection(hm, folderName + File.separator + fileName)){
						
						String zipFile = basePath + File.separator + fileName.replace(".xml", ".zip");
						ZipUtils zu = new ZipUtils();
						zu.zip4jArchivio(folderName, zipFile);
						
						File f = new File(zipFile);
						
						if (f.exists()){
							
							if (this.uploadResultFile(this.getPercorso(), f)){
								
								WinkhouseUtils.getInstance().tmpDirectoryDeleter(folderName);
								f.delete();
								
							}
							
						}
						
					}else{
						returnValue = false;
					}
				}else{
					returnValue = false;
				}
				
			}catch(Exception e){
				returnValue = false;
			}
			
		}else{
			returnValue = false;
		}
		
		return returnValue;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
			
		if (uploadRicerca()){
				
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
													zu.zip4jArchivio(zipdir,zipdir + File.separator + exportfilename);
													File up = new File(zipdir + File.separator + exportfilename);
									                uploadResultFile(getConnector().getPercorso() + exportfilename.substring(exportfilename.lastIndexOf(File.separator)+1), up);
									                up.delete();
									                WinkhouseUtils.getInstance().tmpDirectoryDeleter(zipdir);
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
						TimeUnit.SECONDS.sleep(getPollingIntervall());
					} catch (InterruptedException e) {
						chkvar = false;
						e.printStackTrace();
					}
					
				}
			
		}else{
			
		}
		return Status.OK_STATUS;
		}
	
	

}
