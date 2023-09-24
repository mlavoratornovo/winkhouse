package winkhouse.model.winkcloud.jobs;

import java.util.Date;

import winkhouse.model.winkcloud.ConnectorTypes;

public class WCFileInfo {

	private String name = null;
	private String id	= null;
	private ConnectorTypes tipo = null;
	private Date dataFile = null;
	
	public WCFileInfo(){
		
	}

	public WCFileInfo(String name,String id,ConnectorTypes tipo,Date dataFile){
		this.name = name;
		this.id	  = id;		
		this.tipo = tipo;
		this.dataFile = dataFile;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ConnectorTypes getTipo() {
		return tipo;
	}

	public void setTipo(ConnectorTypes tipo) {
		this.tipo = tipo;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		
		if (getTipo() != null){
			
			if (getTipo() == ConnectorTypes.FTP){
				return getName();
			}

			if (getTipo() == ConnectorTypes.GOOGLEDRIVE){
				return getId();
			}

		}
		
		return null;
	}
	
	public Date getDataFile() {
		return dataFile;
	}

	public void setDataFile(Date dataFile) {
		this.dataFile = dataFile;
	}

}
