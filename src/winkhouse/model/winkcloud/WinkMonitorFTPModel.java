package winkhouse.model.winkcloud;

import java.util.ArrayList;
import java.util.Iterator;

import javolution.util.FastList;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.ColloquiCriteriRicercaModel;
import winkhouse.model.winkcloud.jobs.WinkFTPJob;
import winkhouse.model.xml.RicercheXMLModel;

public class WinkMonitorFTPModel extends MonitorFTPModel {	
	
	private ArrayList<WinkFTPJob> winkFTPJob = null;
	
	@Override
	public void start() {
		
		winkFTPJob = new ArrayList<WinkFTPJob>();
		
		Iterator<RicercheXMLModel> it = getRicerche().iterator();
		int count = 0;
		while (it.hasNext()) {
			RicercheXMLModel ricercheXMLModel = (RicercheXMLModel) it.next();
			WinkFTPJob wFtpJob = new WinkFTPJob(ricercheXMLModel.getNome() + "_" + String.valueOf(count++), ricercheXMLModel);
			
		}
		
	}

	private ArrayList<RicercheXMLModel> ricerche = new ArrayList<RicercheXMLModel>();
	
	
	public WinkMonitorFTPModel() {}

	public WinkMonitorFTPModel(FTPConnector connector) {
		super(connector);
	}

	public WinkMonitorFTPModel(MonitorFTPModel model) {
		setCloudQueries(model.getCloudQueries());
		setConnector((model.getConnector() != null)?(FTPConnector)model.getConnector():null);
		setPollingIntervall(model.getPollingIntervall());
		setStato(model.getStato());
		setWink(model.isWink());
	}
	
	public ArrayList<RicercheXMLModel> getRicerche() {
		if (ricerche == null){
			ricerche = new ArrayList<RicercheXMLModel>();
		}
		return ricerche;
	}

	public void setRicerche(ArrayList<RicercheXMLModel> ricerche) {
		this.ricerche = ricerche;
	}

	@Override
	public String toString() {
		
		return "WinkRicerca" + super.toString();
	}

	public void setAl_criteri(ArrayList<ColloquiCriteriRicercaModel> criteri){
		RicercheXMLModel rm = new RicercheXMLModel();
		rm.setCriteri(criteri);
		rm.setNome("ricerca");
		getRicerche().add(rm);
	}
	
	protected static final XMLFormat<WinkMonitorFTPModel> WINKMONITORFTP_XML = new XMLFormat<WinkMonitorFTPModel>(WinkMonitorFTPModel.class) {
		
        public void write(WinkMonitorFTPModel monitorftp, OutputElement xml) {
        	
        	try {
				xml.setAttribute("pollingIntervall", monitorftp.getPollingIntervall());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        	
        	try {
				xml.setAttribute("isWink", monitorftp.isWink());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        	
        	try {
        		if (monitorftp.getConnector() != null){
        			xml.add(monitorftp.getConnector());
        		}				
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}

        	try {
        		
        		if (monitorftp.getRicerche() != null){        			
        			xml.add(new FastList(monitorftp.getRicerche()));
        		}				
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}

        }

		@Override
		public void read(InputElement arg0, WinkMonitorFTPModel arg1) throws XMLStreamException {
			
			arg1.setPollingIntervall(arg0.getAttribute("pollingIntervall", 5));
			arg1.setWink(arg0.getAttribute("isWink", false));
			arg1.setConnector((FTPConnector)arg0.getNext());
			try{
				arg1.setRicerche(new ArrayList((FastList)arg0.getNext()));
			}catch(Exception e){
				arg1.setRicerche(null);
			}
		}
        
	};
}
