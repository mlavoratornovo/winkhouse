package winkhouse.model.winkcloud;

import winkhouse.util.WinkhouseUtils;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;

public class HTTPConnector extends BaseConnector implements XMLSerializable {

	private String code = null;
	private String winkCloudURL = null;
	
	public HTTPConnector() {}
	
	public HTTPConnector(String code, String winkCloudURL) {
		this.code = code;
		this.winkCloudURL = winkCloudURL;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getWinkCloudURL() {
		return winkCloudURL;
	}

	public void setWinkCloudURL(String winkCloudURL) {
		this.winkCloudURL = winkCloudURL;
	}

	protected static final XMLFormat<HTTPConnector> FTPCONNECTOR_XML = new XMLFormat<HTTPConnector>(HTTPConnector.class) {
		
        public void write(HTTPConnector monitorhttp, OutputElement xml) {
        	
        	try {
				xml.setAttribute("code", monitorhttp.getCode());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        	
        	try {        		
				xml.setAttribute("url", monitorhttp.getUrl());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			} 
        	
        }

		@Override
		public void read(InputElement arg0, HTTPConnector arg1) throws XMLStreamException {
			
			arg1.setCode(arg0.getAttribute("code", null));
			arg1.setUrl(arg0.getAttribute("url", null));
			
		}
        
	};

	
}
