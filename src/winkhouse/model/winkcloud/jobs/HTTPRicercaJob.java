package winkhouse.model.winkcloud.jobs;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.google.api.client.googleapis.media.MediaHttpDownloader.DownloadState;

import winkhouse.Activator;
import winkhouse.model.winkcloud.helpers.HTTPHelper;
import winkhouse.model.winkcloud.restmsgs.ResponseByCodeQueryName;
import winkhouse.model.xml.RicercheXMLModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.util.ZipUtils;
import winkhouse.xmldeser.helpers.ImporterHelper;
import winkhouse.xmldeser.wizard.importer.vo.ImporterVO;
import winkhouse.xmlser.helper.XMLExportHelper;

public class HTTPRicercaJob extends Job {
	
	private boolean chkvar = true;
	private File requestZipFile = null;
	private HTTPHelper httpHelper = null;	
	private String filename = null;
	private String pathWorkingFolder = null;
	private RicercheXMLModel ricerca = null;
	private long pollingIntervall = 5;	
	private HashMap<String, File> fileFinds = new HashMap<String, File>();
	
	public HTTPRicercaJob(String name, long polling, RicercheXMLModel ricerca) {
		super(name);		
		this.pollingIntervall = polling;
		
		pathWorkingFolder = Activator.getDefault().getStateLocation().toFile().toString() + 
  							File.separator + "cloudsearch" + File.separator + name;
		
		File fpathWorkingFolder = new File(pathWorkingFolder);
		if (! fpathWorkingFolder.exists()){
			fpathWorkingFolder.mkdirs();
		}
		
		String pathrequestzipfile = null;
		
		httpHelper = new HTTPHelper();
		if ((ricerca.getPathRequestZipFile() == null) || (!new File(ricerca.getPathRequestZipFile()).exists())){
			if (ricerca.getFilename() == null){
				filename = String.valueOf(new Date().getTime()) + ".zip";
				ricerca.setFilename(filename);
			}else{
				filename = ricerca.getFilename();
			}
			pathrequestzipfile = pathWorkingFolder + File.separator + filename;
			ricerca.setPathRequestZipFile(pathrequestzipfile);
		}else{
			pathrequestzipfile = ricerca.getPathRequestZipFile();
			filename = pathrequestzipfile.substring(pathrequestzipfile.lastIndexOf(File.separator)+1);
			ricerca.setFilename(filename);
		}		
		
		this.ricerca = ricerca;
		
		try{
			if ((ricerca.getPathRequestZipFile() == null) || (!new File(ricerca.getPathRequestZipFile()).exists())){
				XMLExportHelper xmleh = new XMLExportHelper();
				HashMap item = new HashMap();
				item.put(filename, ricerca);
				
				String exportFolder = pathWorkingFolder + File.separator +  String.valueOf(new Date().getTime());
				File fexportFolder = new File(exportFolder);
				fexportFolder.mkdirs();
				String ricercaFilePathName = exportFolder + File.separator + "RicercheXMLModel.xml";
				
				xmleh.exportSelection(item, ricercaFilePathName);
				ZipUtils zu = new ZipUtils();
				zu.zip4jArchivio(exportFolder, pathrequestzipfile);
			}
			
			requestZipFile = new File(pathrequestzipfile);
			
			if (ricerca.getMonitor().getPathloadingFile() != null) {
				ricerca.getMonitor().save(ricerca.getMonitor().getPathloadingFile());	
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}

		
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
			    		    
    	return Status.OK_STATUS;
	    	    	    
	}

	public synchronized boolean isChkvar() {
		return chkvar;
	}

	public synchronized void setChkvar(boolean chkvar) {
		this.chkvar = chkvar;
	}

}
