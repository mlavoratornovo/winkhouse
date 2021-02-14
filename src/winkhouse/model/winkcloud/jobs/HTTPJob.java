package winkhouse.model.winkcloud.jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import winkhouse.Activator;
import winkhouse.engine.search.SearchEngineImmobili;
import winkhouse.helper.WinkCloudHelper;
import winkhouse.model.ColloquiCriteriRicercaModel;
import winkhouse.model.winkcloud.CloudQueryModel;
import winkhouse.model.winkcloud.MonitorHTTPModel;
import winkhouse.model.winkcloud.RicercaModel;
import winkhouse.model.winkcloud.helpers.HTTPHelper;
import winkhouse.model.winkcloud.restmsgs.QueryByCode;
import winkhouse.model.xml.RicercheXMLModel;
import winkhouse.util.WinkCloudDeviceType;
import winkhouse.util.WinkhouseUtils;


public class HTTPJob extends Job {

	boolean chkvar = true;	
	private ArrayList<CloudQueryModel> cloudQueries = null;
	private HTTPHelper httpHelper = null;
	private MonitorHTTPModel monitorHTTP = null; 
	
	public HTTPJob(String name, MonitorHTTPModel monitorHTTP) {
		super(name);
		httpHelper = new HTTPHelper();
		this.monitorHTTP = monitorHTTP;
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		
		while (this.isChkvar()){
					
			QueryByCode[] queryFiles = httpHelper.getQueriesByCode(this.monitorHTTP.getConnector(), this.monitorHTTP.getConnector().getCode());
			if (queryFiles != null){
				winkhouse.util.ZipUtils zu_unzip = new winkhouse.util.ZipUtils();		
				for (int i = 0; i < queryFiles.length; i++) {
					
					QueryByCode queryFileName = queryFiles[i];
					
					if (monitorHTTP.getRicercheServite().get(queryFileName.filename) == null){
									
						String pathdownload = Activator.getDefault().getStateLocation().toFile().toString() + 
											  File.separator + "cloudsearch" + File.separator + this.monitorHTTP.getConnector().getCode();
						
						File fpathdownload = new File(pathdownload);
						fpathdownload.mkdirs();
						
						File requestFile = new File(pathdownload + File.separator + queryFileName.filename);
						
						if (httpHelper.downloadQueryRequest(this.monitorHTTP.getConnector(), queryFileName.filename, this.monitorHTTP.getConnector().getCode(), requestFile)){
							try {
								
								String unzipFolder = String.valueOf(new Date().getTime());
								File funzipFolder = new File(pathdownload + File.separator + unzipFolder);
								funzipFolder.mkdirs();
								
								if (zu_unzip.unZip4jArchivio(pathdownload + File.separator + queryFileName.filename, 
														     pathdownload + File.separator + unzipFolder)){
									
									String[] files = funzipFolder.list();
									
									for (int x = 0; x < files.length; x++) {
										
										if (files[x].endsWith(".xml")){
											
											WinkCloudDeviceType tipoFile = null;
											tipoFile = this.getQueryFromType(new File(pathdownload + File.separator + 
					 					   											  unzipFolder + File.separator + 
					 					   											  files[x]));
											
											if (tipoFile != null){
												
												ArrayList al_ricerche = new ArrayList();
												WinkCloudHelper wch = new WinkCloudHelper();
												
												if (tipoFile == WinkCloudDeviceType.DEVICE_MOBILE){
													al_ricerche = wch.loadFromFile(new File(pathdownload + File.separator + 
															 					   			unzipFolder + File.separator + 
															 								files[x]), 
															 					   wch.XML_TAG_MOBILE, 
															 					   RicercaModel.class);
												}
												if (tipoFile == WinkCloudDeviceType.DEVICE_PC){
													al_ricerche = wch.loadFromFile(new File(pathdownload + File.separator + 
												 	  						                unzipFolder + File.separator + 
													 							            files[x]), 
													 							   RicercheXMLModel.class.getName(), 
													 							   RicercheXMLModel.class);
													
												}
												if (al_ricerche.size() > 0){
													
													ArrayList immobili = new ArrayList();
													ArrayList alCriteria = new ArrayList();
													
													for (Object criterio : al_ricerche) {

														if ((criterio instanceof RicercaModel) && ((RicercaModel)criterio).getSearchType().equalsIgnoreCase("immobili")){
															alCriteria.add(((RicercaModel)criterio).toCriteriRicercaModel());
														}
														
														if ((criterio instanceof RicercheXMLModel) && ((RicercheXMLModel)criterio).getTipo() == RicercheXMLModel.RICERCHE_IMMOBILI){
															RicercheXMLModel rxml = (RicercheXMLModel)criterio;
															alCriteria.add(rxml);
														}

													}
													
													SearchEngineImmobili sei = new SearchEngineImmobili(alCriteria);
													immobili = sei.find();													
													
													int maxcloud = Integer.valueOf((WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.MAXCLOUDRESULT).equalsIgnoreCase(""))
				            														? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.MAXCLOUDRESULT)
				            														: WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.MAXCLOUDRESULT));
				            
									            	try {
									            		immobili = new ArrayList(immobili.subList(0, maxcloud));																								
													} catch (Exception e1) {}
									            	
													String exportPath = pathdownload + File.separator + String.valueOf(new Date().getTime());
													File fexportPath = new File(exportPath);
													fexportPath.mkdirs();
													
													if (wch.exportImmobili(immobili, exportPath)){
														
														String fzip = pathdownload + File.separator + String.valueOf(new Date().getTime()) + ".zip";
														
														winkhouse.util.ZipUtils zu_zip = new winkhouse.util.ZipUtils();
														
														zu_zip.zip4jArchivio(exportPath, fzip);
														if (httpHelper.uploadResponse2QueryRequest(this.monitorHTTP.getConnector(), 
																								   new File(fzip), 
																								   this.monitorHTTP.getConnector().getCode(), 
																								   queryFileName.filename)){
															
															if (tipoFile == WinkCloudDeviceType.DEVICE_MOBILE){
																
																RicercheXMLModel r = null;
																for (Object criterio : al_ricerche) {
																	
																	if (r == null){
																		r = new RicercheXMLModel((RicercaModel)criterio);
																	}else{
																		r.getCriteri().add((new ColloquiCriteriRicercaModel(((RicercaModel)criterio).toCriteriRicercaModel())));
																	}
																
																}
																monitorHTTP.getRicercheServite().put(queryFileName.filename, r);

																
															}
															
															if (tipoFile == WinkCloudDeviceType.DEVICE_PC){
																RicercheXMLModel r = null;
																for (Object criterio : al_ricerche) {
																	
																	if (r == null){
																		r = new RicercheXMLModel();
																	}
																	r.getCriteri().addAll(((RicercheXMLModel)criterio).getCriteri());
																}

																int count = 0;
																for (Object criterio : al_ricerche) {
																	monitorHTTP.getRicercheServite().put(queryFileName.filename+String.valueOf(count), (RicercheXMLModel)criterio);
																	count ++;
																}
															}
															
															if (monitorHTTP.getPathloadingFile() != null){
																monitorHTTP.save(monitorHTTP.getPathloadingFile());
															}
															monitorHTTP.getTvMonitors().getControl().getDisplay().asyncExec(new Runnable() {
																
																@Override
																public void run() {
																	monitorHTTP.getTvMonitors().refresh();
																	
																}
															});
														}else{
															System.out.println("upload respose None");
														}
														
													}
																						
												}
											}									
										}
										
									}
									
								}
								
							}catch(Exception e){
								e.printStackTrace();
							}
						}else{
							// TODO scrivere nel log
						}
						
					}
					
				}
			}
			try {
				TimeUnit.SECONDS.sleep(this.monitorHTTP.getPollingInterval());
			} catch (InterruptedException e) {
				chkvar = false;
				e.printStackTrace();
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

	public ArrayList<CloudQueryModel> getCloudQueries() {
		return cloudQueries;
	}

	public void setCloudQueries(ArrayList<CloudQueryModel> cloudQueries) {
		this.cloudQueries = cloudQueries;
	}
	
	protected WinkCloudDeviceType getQueryFromType(File ricercaXMLFile) throws IOException{
		
		BufferedReader reader = new BufferedReader(new FileReader (ricercaXMLFile));
		String content = "";
		String line = null;
		
		while((line = reader.readLine()) != null) {
			content += line;	            
		}
		reader.close();
	    
		if (content.indexOf("<ricerca ") > 0){
			return WinkCloudDeviceType.DEVICE_MOBILE;
		}
		if (content.indexOf("<winkhouse.model.xml.RicercheXMLModel") > 0){
			return WinkCloudDeviceType.DEVICE_PC;
		}
		return null;
	}

}
