package winkhouse.model.xml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javolution.util.FastList;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.eclipse.jface.viewers.TreeViewer;

import winkhouse.model.ColloquiCriteriRicercaModel;
import winkhouse.model.RicercheModel;
import winkhouse.model.winkcloud.CloudMonitorState;
import winkhouse.model.winkcloud.MonitorHTTPModel;
import winkhouse.model.winkcloud.RicercaModel;
import winkhouse.model.winkcloud.jobs.HTTPRicercaJob;
//import winkhouse.model.winkcloud.jobs.HTTPRicercaJob;
import winkhouse.xmldeser.wizard.importer.vo.ImporterVO;

public class RicercheXMLModel extends RicercheModel {
	
	private ArrayList<ImporterVO> importerVO = null;
	private HTTPRicercaJob ricercaJob = null;	
	private String pathRequestZipFile = null;
	private MonitorHTTPModel monitor = null;
	private TreeViewer tvMonitors = null;
	private CloudMonitorState state = CloudMonitorState.PAUSA;
	private String filename = null;
	
	public RicercheXMLModel() {
	}
	
	public RicercheXMLModel(RicercaModel ricerca) {
		ArrayList<ColloquiCriteriRicercaModel> al = new ArrayList<ColloquiCriteriRicercaModel>();
		al.add(new ColloquiCriteriRicercaModel(ricerca.toCriteriRicercaModel()));
		this.setCriteri(al);
		this.setNome(((ricerca.getSearchType() != null)?ricerca.getSearchType():"") + "@"+
					 ((ricerca.getColumn_name() != null)?ricerca.getColumn_name():"")+"="+
					 ((ricerca.getValue_da() != null)?ricerca.getValue_da():"")+"_"+
					 ((ricerca.getValue_a()!= null)?ricerca.getValue_a():""));
	}

	public RicercheXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	public void start(){
		
		if (this.ricercaJob == null){
			this.ricercaJob = new HTTPRicercaJob(monitor.getConnector().getCode() + "_" + this.toString(), 
												 monitor.getPollingIntervall(), 
												 this);
			this.ricercaJob.schedule();
		}else{
			this.ricercaJob.schedule();
		} 
		
	}
	
	public void stop(){
		
		if (this.ricercaJob != null){
			ricercaJob.setChkvar(false);
		}
		
	}

	
	protected static final XMLFormat<RicercheXMLModel> RICERCHE_XML = new XMLFormat<RicercheXMLModel>(RicercheXMLModel.class){
		
        public void write(RicercheXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	
        	xml.setAttribute("codRicerca", a_xml.getCodRicerca());
			xml.setAttribute("nome", a_xml.getNome());
			xml.setAttribute("descrizione", a_xml.getDescrizione());
			xml.setAttribute("tipo", a_xml.getTipo());
			xml.setAttribute("pathRequestZipFile", a_xml.getPathRequestZipFile());
			
			FastList<DummyColloquiCriteriRicercaXMLModel> alcriteri = new FastList();
			for (ColloquiCriteriRicercaModel criterio : a_xml.getCriteri()) {
				alcriteri.add(new DummyColloquiCriteriRicercaXMLModel(criterio));
			}
			xml.add(alcriteri);
        }
        
        public void read(InputElement xml, RicercheXMLModel a_xml) throws XMLStreamException{
        	
        	try{
        		a_xml.setCodRicerca(xml.getAttribute("codRicerca").toInt());
        	}catch(Exception e){
        		a_xml.setCodRicerca(null);
        	}
        	a_xml.setNome(xml.getAttribute("nome", ""));
        	a_xml.setDescrizione(xml.getAttribute("descrizione", ""));
        	
        	try {
				a_xml.setTipo(xml.getAttribute("tipo").toInt());
			} catch (Exception e) {
				a_xml.setTipo(null);
			}
        	
        	a_xml.setPathRequestZipFile(xml.getAttribute("pathRequestZipFile", null));
        	
        	FastList fl = (FastList)xml.getNext();
        	ArrayList al = new ArrayList();
        	for (Iterator iterator = fl.iterator(); iterator.hasNext();) {
				DummyColloquiCriteriRicercaXMLModel object = (DummyColloquiCriteriRicercaXMLModel) iterator.next();
				al.add(object);
			}
        	a_xml.setCriteri(al);
        	
        }
        
    };
   
	@Override
	public String toString() {
		
		StringBuffer retval = new StringBuffer();
		for (Iterator<ColloquiCriteriRicercaModel> iterator = getCriteri().iterator(); iterator.hasNext();) {
			retval.append(iterator.next().toString().replace("(", "").replace(")", "").replace(" ", "_").replace(":", ""));			
		}
		return retval.toString();
	}


	public ArrayList<ImporterVO> getImporterVO() {
		if (importerVO == null){
			importerVO = new ArrayList<ImporterVO>();
		}
		return importerVO;
	}

	public void setImporterVO(ArrayList<ImporterVO> importerVO) {
		this.importerVO = importerVO;
	}	
	
	public CloudMonitorState getState(){
		return state;
		
	}

//	public HTTPRicercaJob getRicercaJob() {
//		return ricercaJob;
//	}
//		
//	
//	public void start(){
//		
//		if (this.ricercaJob == null){
//			this.ricercaJob = new HTTPRicercaJob(monitor.getConnector().getCode() + "_" + this.toString(), 
//												 monitor.getPollingIntervall(), 
//												 this);
//			this.ricercaJob.schedule();
//		}
//		
//	}
//	
//	public void stop(){
//		
//		if (this.ricercaJob != null){
//			ricercaJob.setChkvar(false);
//		}
//		
//	}

	public MonitorHTTPModel getMonitor() {
		return monitor;
	}

	public void setMonitor(MonitorHTTPModel monitor) {
		this.monitor = monitor;
	}

	public String getPathRequestZipFile() {
		return pathRequestZipFile;
	}

	public void setPathRequestZipFile(String pathRequestZipFile) {
		this.pathRequestZipFile = pathRequestZipFile;
	}

	public TreeViewer getTvMonitors() {
		return tvMonitors;
	}

	public void setTvMonitors(TreeViewer tvMonitors) {
		this.tvMonitors = tvMonitors;
	}

	public void setStato(CloudMonitorState state) {
		this.state = state;
	}

	public CloudMonitorState getStato() {
		return this.state;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
