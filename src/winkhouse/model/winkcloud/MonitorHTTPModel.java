package winkhouse.model.winkcloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.viewers.TreeViewer;

import javolution.util.FastList;
import javolution.util.FastMap;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.ColloquiCriteriRicercaModel;
import winkhouse.model.winkcloud.helpers.HTTPHelper;
import winkhouse.model.winkcloud.jobs.HTTPJob;
import winkhouse.model.xml.RicercheXMLModel;
import winkhouse.view.winkcloud.QueryFilesView;
import winkhouse.xmlser.helper.XMLExportHelper;

public class MonitorHTTPModel implements XMLSerializable, MonitorModel {
	
	public final static long serialVersionUID = 1000001;
	public final static String XMLTAG = "monitorhttp"; 
	private CloudMonitorState stato = CloudMonitorState.PAUSA;
	private HttpServerConnector connector = null;
	private ArrayList<CloudQueryModel> cloudQueries = null;
	private long pollingInterval = 5;	
	private QueryFilesView qfv = null;
	private HTTPJob job = null;
	private ArrayList<RicercheXMLModel> ricerche = null;
	private HTTPHelper httpHelper = null;
	private String pathloadingFile = null;
	private HashMap<String, RicercheXMLModel> ricercheServite = new HashMap<String, RicercheXMLModel>(); 
	private TreeViewer tvMonitors = null;
	private Integer port = null;
	private Boolean login = null;
	
	public MonitorHTTPModel() {		
	}

	public MonitorHTTPModel(HttpServerConnector connector) {
		this.connector = connector;
		httpHelper = new HTTPHelper();
	}
	
	
	
	@Override
	public void start() {
		job = new HTTPJob("Monitor Ascolto HTTP", this, qfv);
		job.schedule();
	}

	@Override
	public void setCloudQueries(ArrayList<CloudQueryModel> cloudQueries) {
		this.cloudQueries = cloudQueries;

	}

	@Override
	public ArrayList<CloudQueryModel> getCloudQueries() {			
		if (cloudQueries == null){
			cloudQueries = new ArrayList<CloudQueryModel>();
		}
		return cloudQueries;
	}
	

	@Override
	public CloudMonitorState getStato() {
		return stato;
	}

	@Override
	public void setStato(CloudMonitorState stato) {
		this.stato = stato;
	}

	@Override
	public void stop() {
		if (this.getJob() != null){
			this.getJob().setChkvar(false);
		}

	}

	
	@Override
	public long getPollingIntervall() {
		return pollingInterval;
	}

	public void setPollingIntervall(long pollingInterval) {
		this.pollingInterval = pollingInterval;
	}

	@Override
	public ConnectorTypes getType() {
		return ConnectorTypes.WINKCLOUD;
	}

	@Override
	public boolean save(String pathFileName) {
			
		try{
			XMLExportHelper xmleh = new XMLExportHelper();
			HashMap item = new HashMap();
			
			item.put(((HttpServerConnector)getConnector()).getPort(), this);
			xmleh.exportSelection(item, pathFileName);
			
		}catch(Exception e){
			return false;
		}
		
		return true;
	}

	@Override
	public void setQueryFilesView(QueryFilesView qfv) {
		this.qfv = qfv;
	}

	public HttpServerConnector getConnector() {
		return connector;
	}

	public void setConnector(HttpServerConnector connector) {
		this.connector = connector;
	}
	
	public ArrayList<RicercheXMLModel> getRicerche() {
		if (ricerche == null){
			ricerche = new ArrayList<RicercheXMLModel>();
		}
		return ricerche;
	}
	
	@Override	
	public String toString() {
		
		if (getConnector() != null){
		
			return ((HttpServerConnector)getConnector()).getPort().toString()+"@"+String.valueOf(((HttpServerConnector)getConnector()).isLogin());
			
		}
		return super.toString();
	}

	protected static final XMLFormat<MonitorHTTPModel> HTTPMONITOR_XML = new XMLFormat<MonitorHTTPModel>(MonitorHTTPModel.class) {
		
        public void write(MonitorHTTPModel monitorhttp, OutputElement xml) {
        	
        	try {
				xml.setAttribute("pollingIntervall", monitorhttp.getPollingIntervall());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        	
        	try {
				xml.setAttribute("pathloadingFile", monitorhttp.getPathloadingFile());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        	        	
        	try {
        		if (monitorhttp.getConnector() != null){
        			xml.add(monitorhttp.getConnector());
        		}				
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}

        	try {
        		
        		if (monitorhttp.getRicerche() != null){        			
        			xml.add(new FastList(monitorhttp.getRicerche()));
        		}				
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        	
        	try {
        		
        		if (monitorhttp.getRicercheServite() != null){        			
        			xml.add(new FastMap<String, RicercheXMLModel>((monitorhttp.getRicercheServite())));
        		}				
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}

        }

		@Override
		public void read(InputElement arg0, MonitorHTTPModel arg1) throws XMLStreamException {
			
			arg1.setPollingIntervall(arg0.getAttribute("pollingIntervall", 5));	
			arg1.setPathloadingFile(arg0.getAttribute("pathloadingFile", null));
			arg1.setConnector((HttpServerConnector)arg0.getNext());			
			try{
				arg1.setRicerche(new ArrayList((FastList)arg0.getNext()));
			}catch(Exception e){
				arg1.setRicerche(null);
			}
			try{
				arg1.setRicercheServite(new HashMap<String, RicercheXMLModel>((FastMap)arg0.getNext()));
			}catch(Exception e){
				arg1.setRicercheServite(null);
			}
			
			for (Iterator iterator = arg1.getRicerche().iterator(); iterator.hasNext();) {
				RicercheXMLModel type = (RicercheXMLModel) iterator.next();
				type.setMonitor(arg1);
			}
			
		}
        
	};

	public QueryFilesView getQfv() {
		return qfv;
	}

	public void setQfv(QueryFilesView qfv) {
		this.qfv = qfv;
	}

	public void setRicerche(ArrayList<RicercheXMLModel> ricerche) {
		this.ricerche = ricerche;
	}

	public HTTPJob getJob() {
		return job;
	}

	public void setJob(HTTPJob job) {
		this.job = job;
	}

	@Override
	public boolean isChkvar() {
		return false;
	}

	public void setAl_criteri(ArrayList<ColloquiCriteriRicercaModel> criteri){
		
		RicercheXMLModel rm = new RicercheXMLModel();
		rm.setTipo(RicercheXMLModel.RICERCHE_IMMOBILI);
		rm.setTvMonitors(this.getTvMonitors());
		rm.setCriteri(criteri);
		rm.setNome("ricerca");
		rm.setMonitor(this);
		
		getRicerche().add(rm);
	}

	public boolean checkStatus(){
		return httpHelper.checkStatus((HttpServerConnector)getConnector());
	}
	
	public String getCode(){
		return httpHelper.getCode((HttpServerConnector)getConnector());
	}

	public String getPathloadingFile() {
		return pathloadingFile;
	}

	public void setPathloadingFile(String pathloadingFile) {
		this.pathloadingFile = pathloadingFile;
	}

	public long getPollingInterval() {
		return pollingInterval;
	}

	public void setPollingInterval(long pollingInterval) {
		this.pollingInterval = pollingInterval;
	}

	public HashMap<String, RicercheXMLModel> getRicercheServite() {
		return ricercheServite;
	}

	public void setRicercheServite(HashMap<String, RicercheXMLModel> ricercheServite) {
		this.ricercheServite = ricercheServite;
	}

	public TreeViewer getTvMonitors() {
		return tvMonitors;
	}

	public void setTvMonitors(TreeViewer tvMonitors) {
		this.tvMonitors = tvMonitors;
	}
		
}
