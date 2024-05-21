package winkhouse.model.winkcloud.jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;






import org.apache.cayenne.query.QueryCacheStrategy;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;






import winkhouse.Activator;
import winkhouse.engine.search.SearchEngineImmobili;
import winkhouse.helper.WinkCloudHelper;
import winkhouse.model.ColloquiCriteriRicercaModel;
import winkhouse.model.CriteriRicercaModel;
import winkhouse.model.winkcloud.CloudQueryModel;
import winkhouse.model.winkcloud.CloudQueryOrigin;
import winkhouse.model.winkcloud.MonitorHTTPModel;
import winkhouse.model.winkcloud.RicercaModel;
import winkhouse.model.winkcloud.helpers.HTTPHelper;
import winkhouse.model.winkcloud.helpers.RESTAPIHelper;
import winkhouse.model.winkcloud.mobilecolumns.ImmobiliColumnNames;
import winkhouse.model.winkcloud.restmsgs.QueryByCode;
import winkhouse.model.xml.RicercheXMLModel;
import winkhouse.orm.Colloquicriteriricerca;
import winkhouse.util.AnagraficheMethodName;
import winkhouse.util.ColloquiMethodName;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.util.WinkCloudDeviceType;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.winkcloud.QueryFilesView;
import static spark.Spark.*;

public class HTTPJob extends Job {

	boolean chkvar = true;	
	private ArrayList<CloudQueryModel> cloudQueries = null;
	private HTTPHelper httpHelper = null;
	private RESTAPIHelper restAPIHelper = null;
	private MonitorHTTPModel monitorHTTP = null; 
	private QueryFilesView qfv = null;
	
	public HTTPJob(String name, MonitorHTTPModel monitorHTTP, QueryFilesView qfv) {
		super(name);
		httpHelper = new HTTPHelper();
		restAPIHelper = new RESTAPIHelper();
		this.monitorHTTP = monitorHTTP;
		this.qfv = qfv;
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		port(this.monitorHTTP.getConnector().getPort());
		//
		post("/search", (req, res) -> {
			ArrayList searchResult = restAPIHelper.searchItems(req);
			if (searchResult != null && searchResult.size() == 3) {
				ArrayList<CloudQueryModel> alq = this.monitorHTTP.getCloudQueries();
				CloudQueryOrigin type = (CloudQueryOrigin)searchResult.get(2);
				CloudQueryModel cqm = new CloudQueryModel(type, "", new Date());
				ArrayList<Colloquicriteriricerca> al = new ArrayList<Colloquicriteriricerca>();
				for (RicercaModel ricerca : (ArrayList<RicercaModel>)searchResult.get(0)) {
					al.add(ricerca.toCriteriRicercaModel());
				} 
				cqm.setCriteri(al);
				alq.add(cqm);
				qfv.setQueries(alq);
				return searchResult.get(1);				
			}
			return searchResult;
		});
		get("/core/search", (req, res) -> {
			ArrayList<CloudQueryModel> alq = this.monitorHTTP.getCloudQueries();
			
			String tipo = req.queryParams("tipo");
			CloudQueryOrigin type = null;
			switch (tipo) {
			case "ce": 
				type = CloudQueryOrigin.CORE_CLASSIENERGETICHE;
				break;
			case "ti":
				type = CloudQueryOrigin.CORE_TIPIIMMOBILI;
				break;
			case "cc":
				type = CloudQueryOrigin.CORE_CLASSICLIENTI;
				break;
			case "tc":			
				type = CloudQueryOrigin.CORE_TIPICOLLOQUI;
				break;
			case "ts":
				type = CloudQueryOrigin.CORE_TIPISTANZE;
				break;
			case "tcon":
				type = CloudQueryOrigin.CORE_TIPICONTATTI;
				break;
			case "ri":
				type = CloudQueryOrigin.CORE_RISCALDAMENTI;
				break;
			case "sc":
				type = CloudQueryOrigin.CORE_STATOCONSERVATIVO;
				break;

			default:
				break;
			}
			CloudQueryModel cqm = new CloudQueryModel(type, "", new Date());
			
			ArrayList<Colloquicriteriricerca> al = new ArrayList<Colloquicriteriricerca>();
			al.add(restAPIHelper.serchCoreCriteria(req));
			cqm.setCriteri(al);
			alq.add(cqm);
			qfv.setQueries(alq);
			return restAPIHelper.searchCore(req);
		});
		get("/colloqui/immobile/:idimmobile", (req, res) -> {
			ArrayList<CloudQueryModel> alq = this.monitorHTTP.getCloudQueries();
			CloudQueryModel cqm = new CloudQueryModel(CloudQueryOrigin.GET_COLLOQUI_IMMOBILE, "", new Date());
			ArrayList<Colloquicriteriricerca> al = new ArrayList<Colloquicriteriricerca>();
			Colloquicriteriricerca c = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Colloquicriteriricerca.class);
			c.setGettermethodname(ImmobiliMethodName.GET_CODIMMOBILE);
			c.setFromvalue(req.params(":idimmobile"));
			al.add(c);			
			cqm.setCriteri(al);
			alq.add(cqm);
			qfv.setQueries(alq);
			return restAPIHelper.getColloquiByImmobile(Integer.parseInt(req.params(":idimmobile")));
		});
		get("/colloqui/anagrafica/:idanagrafica", (req, res) -> {
			ArrayList<CloudQueryModel> alq = this.monitorHTTP.getCloudQueries();
			CloudQueryModel cqm = new CloudQueryModel(CloudQueryOrigin.GET_COLLOQUI_ANAGRAFICA, "", new Date());
			ArrayList<Colloquicriteriricerca> al = new ArrayList<Colloquicriteriricerca>();
			Colloquicriteriricerca c = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Colloquicriteriricerca.class);;
			c.setGettermethodname(AnagraficheMethodName.GET_CODANAGRAFICA);
			c.setFromvalue(req.params(":idanagrafica"));
			al.add(c);			
			cqm.setCriteri(al);			
			cqm.setCriteri(al);
			alq.add(cqm);
			qfv.setQueries(alq);
			return restAPIHelper.getColloquiByAnagrafica(Integer.parseInt(req.params(":idanagrafica")));
		});
		get("/anagrafica/:idanagrafica/abbinati", (req, res) -> {
			ArrayList<CloudQueryModel> alq = this.monitorHTTP.getCloudQueries();
			CloudQueryModel cqm = new CloudQueryModel(CloudQueryOrigin.GET_ABBINAMENTI_IMMOBILI_ANAGRAFICA, "", new Date());
			ArrayList<Colloquicriteriricerca> al = new ArrayList<Colloquicriteriricerca>();
			Colloquicriteriricerca c = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Colloquicriteriricerca.class);;
			c.setGettermethodname(AnagraficheMethodName.GET_CODANAGRAFICA);
			c.setFromvalue(req.params(":idanagrafica"));
			al.add(c);			
			cqm.setCriteri(al);
			alq.add(cqm);
			qfv.setQueries(alq);
			return restAPIHelper.getImmobiliAbbinatiByAnagrafica(Integer.parseInt(req.params(":idanagrafica")));
		});
		get("/immobile/:idimmobile/abbinati", (req, res) -> {
			ArrayList<CloudQueryModel> alq = this.monitorHTTP.getCloudQueries();
			CloudQueryModel cqm = new CloudQueryModel(CloudQueryOrigin.GET_ABBINAMENTI_ANAGRAFICHE_IMMOBILE, "", new Date());			
			ArrayList<Colloquicriteriricerca> al = new ArrayList<Colloquicriteriricerca>();
			Colloquicriteriricerca c = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Colloquicriteriricerca.class);;
			c.setGettermethodname(ImmobiliMethodName.GET_CODIMMOBILE);
			c.setFromvalue(req.params(":idimmobile"));
			al.add(c);
			cqm.setCriteri(al);
			alq.add(cqm);
			qfv.setQueries(alq);
			return restAPIHelper.getAnagraficheAbbinateByImmobile(Integer.parseInt(req.params(":idimmobile")));
		});
		post("/immobile", (req, res) -> {
			ArrayList<CloudQueryModel> alq = this.monitorHTTP.getCloudQueries();
			CloudQueryModel cqm = new CloudQueryModel(CloudQueryOrigin.POST_PUT_IMMOBILE, "", new Date());
			ArrayList<Colloquicriteriricerca> al = new ArrayList<Colloquicriteriricerca>();
			Colloquicriteriricerca c = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Colloquicriteriricerca.class);;
			c.setGettermethodname("invio immobili");			
			al.add(c);
			cqm.setCriteri(al);
			alq.add(cqm);
			qfv.setQueries(alq);
			return restAPIHelper.postImmobile(req, cqm);
//			qfv.setQueries(alq);
		});

		
		return Status.OK_STATUS;
	}
	
	public void stop(){
		spark.Spark.stop();
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
