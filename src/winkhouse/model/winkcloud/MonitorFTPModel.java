package winkhouse.model.winkcloud;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.apache.commons.net.ftp.FTPClient;

import winkhouse.model.winkcloud.jobs.FTPJob;
import winkhouse.view.winkcloud.QueryFilesView;
import winkhouse.xmlser.helper.XMLExportHelper;

public class MonitorFTPModel implements MonitorModel,XMLSerializable {
	
	public final static long serialVersionUID = 1000000;
	public final static String XMLTAG = "monitorftp"; 
	private CloudMonitorState stato = null;
	private FTPConnector connector = null;
	private ArrayList<CloudQueryModel> cloudQueries = null;
	private long pollingIntervall = 5;
	private boolean chkvar = false;
	private FTPJob job = null;
	private QueryFilesView qfv = null;
	private boolean isWink = false;
	
	public MonitorFTPModel() {

	}

	public MonitorFTPModel(FTPConnector connector) {
		this.connector = connector;
	}

	public long getPollingIntervall() {
		return pollingIntervall;
	}

	public void setPollingIntervall(long pollingIntervall) {
		this.pollingIntervall = pollingIntervall;
	}

	public ConnectorTypes getType() {
		return ConnectorTypes.FTP;
	}

	public BaseConnector getConnector() {
		if (connector == null){
			connector = new FTPConnector();
		}
		return connector;
	}

	public ArrayList<CloudQueryModel> getCloudQueries() {
		
		if (cloudQueries == null){
			cloudQueries = new ArrayList<CloudQueryModel>();
		}
		return cloudQueries;
	}

	public boolean testConnection(){
		if (getConnector() != null){
			FTPClient client = new FTPClient();
			try {
				client.connect(((FTPConnector)getConnector()).getUrl());
				if (client.login(((FTPConnector)getConnector()).getUsername(), ((FTPConnector)getConnector()).getPassword())){
					client.logout();
					client.disconnect();
					return true;
				}else{
					return false;
				}
			} catch (SocketException e) {
				return false;
			} catch (IOException e) {
				return false;
			}
		}
		return false;
	}

	public void setCloudQueries(ArrayList<CloudQueryModel> cloudQueries) {
		this.cloudQueries = cloudQueries;
	}

	public void start(){
		
		job = new FTPJob("Monitor Ascolto FTP");
		job.setConnector(getConnector());
		job.setPollingIntervall(getPollingIntervall());
		job.setQfv(qfv);
		job.setCloudQueries(getCloudQueries());
		job.schedule();
		
	}

	public CloudMonitorState getStato() {
		return stato;
	}

	public void setStato(CloudMonitorState stato) {
		this.stato = stato;
	}

	public boolean isChkvar() {
		return chkvar;
	}

	public void setChkvar(boolean chkvar) {
		this.chkvar = chkvar;
	}

	public void stop(){
		if (this.getJob() != null){
			this.getJob().setChkvar(false);
		}
	}
	
	@Override
	public boolean save(String pathFileName) {
		
		try{
			XMLExportHelper xmleh = new XMLExportHelper();
			HashMap item = new HashMap();
			item.put(((FTPConnector)getConnector()).getUrl(), this);
		
			xmleh.exportSelection(item, pathFileName);
			
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	
	protected static final XMLFormat<MonitorFTPModel> MONITORFTP_XML = new XMLFormat<MonitorFTPModel>(MonitorFTPModel.class) {
		
        public void write(MonitorFTPModel monitorftp, OutputElement xml) {
        	
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
        }

		@Override
		public void read(InputElement arg0, MonitorFTPModel arg1) throws XMLStreamException {
			
			arg1.setPollingIntervall(arg0.getAttribute("pollingIntervall", 5));
			arg1.setConnector((FTPConnector)arg0.getNext());
			arg1.setWink(arg0.getAttribute("isWink", false));
			
		}
        
	};
	
	@Override
	
	public String toString() {
		
		if (getConnector() != null){
		
			return ((FTPConnector)getConnector()).getUsername()+"@"+((FTPConnector)getConnector()).getUrl()+":"+String.valueOf(((FTPConnector)getConnector()).getPorta());
			
		}
		return super.toString();
	}

	public void setConnector(FTPConnector connector) {
		this.connector = connector;
	}

	public FTPJob getJob() {
		return job;
	}

	
	@Override
	public void setQueryFilesView(QueryFilesView qfv) {
		this.qfv = qfv; 
	}

	public boolean isWink() {
		return isWink;
	}

	public void setWink(boolean isWink) {
		this.isWink = isWink;
	}


}
