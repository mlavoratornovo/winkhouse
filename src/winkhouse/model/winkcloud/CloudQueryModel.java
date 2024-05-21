package winkhouse.model.winkcloud;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.ArrayListMultimap;

import winkhouse.engine.search.SearchEngineImmobili;
import winkhouse.model.CriteriRicercaModel;
import winkhouse.orm.Colloquicriteriricerca;



public class CloudQueryModel {	
	
	private CloudQueryOrigin type = null;
	private CloudQueryTypes queryType = null;
	private ArrayList results = null;
	private String filePathName = null;
	private Date dataRicezioneFile = null;
	private String descrizioneQuery = null;
	private ArrayList<Colloquicriteriricerca> criteri = null;
	
	public CloudQueryModel() {

	}
	
	
	
	public CloudQueryModel(CloudQueryOrigin type,String filePathName,Date dataRicezioneFile) {
		this.type = type;
		this.filePathName = filePathName;
		this.dataRicezioneFile = dataRicezioneFile; 
	}

	public CloudQueryOrigin getType() {
		return type;
	}

	public void setType(CloudQueryOrigin type) {
		this.type = type;
	}
	
	public String getTypeDescription(){		
		return this.type.toString();
	}
	
	public ArrayList getResults() {
		
		return results;
		
	}

	public void setResults(ArrayList results) {
		this.results = results;
	}

	public String getFilePathName() {
		return filePathName;
	}

	public void setFilePathName(String filePathName) {
		this.filePathName = filePathName;
	}

	
	public Date getDataRicezioneFile() {
		return dataRicezioneFile;
	}

	
	public void setDataRicezioneFile(Date dataRicezioneFile) {
		this.dataRicezioneFile = dataRicezioneFile;
	}

	
	public String getDescrizioneQuery() {
		if (descrizioneQuery == null){
			if (getCriteri() != null && getCriteri().size() > 0){
				descrizioneQuery = "";
				for (Colloquicriteriricerca crm : getCriteri()) {
					descrizioneQuery += crm.toString() + " ";
				}
			}
		}
		return descrizioneQuery;
	}

	
	public void setDescrizioneQuery(String descrizioneQuery) {
		this.descrizioneQuery = descrizioneQuery;
	}

	
	public ArrayList<Colloquicriteriricerca> getCriteri() {
		return criteri;
	}

	
	public void setCriteri(ArrayList<Colloquicriteriricerca> criteri) {
		this.criteri = criteri;
	}

	public CloudQueryTypes getQueryType() {
		return queryType;
	}

	public void setQueryType(CloudQueryTypes queryType) {
		this.queryType = queryType;
	}
	
	public String getQueryTypeDescription(){
		if (queryType != null){
			if (queryType == CloudQueryTypes.IMMOBILI){
				return "Immobili";
			}
			if (queryType == CloudQueryTypes.ANAGRAFICHE){
				return "Anagrafiche";
			}			
		}
		return "";
	}

}
