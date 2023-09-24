package winkhouse.model.winkcloud;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;

public class HttpServerConnector extends BaseConnector  implements XMLSerializable {

	private Integer port = null;
	private boolean login = false;
	
	public HttpServerConnector(Integer port, boolean login) {
		this.port = port;
		this.login = login;
	}

	public HttpServerConnector() {
		// TODO Auto-generated constructor stub
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}
	
	protected static final XMLFormat<HttpServerConnector> HTTPSERVERCONNECTOR_XML = new XMLFormat<HttpServerConnector>(HttpServerConnector.class) {
		
        public void write(HttpServerConnector monitorhttp, OutputElement xml) {
        	
        	try {
				xml.setAttribute("port", monitorhttp.getPort());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        	
        	try {        		
				xml.setAttribute("login", monitorhttp.isLogin());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			} 
        	
        }

		@Override
		public void read(InputElement arg0, HttpServerConnector arg1) throws XMLStreamException {
			
			arg1.setPort(Integer.decode(arg0.getAttribute("port", null)));
			arg1.setLogin(Boolean.getBoolean(arg0.getAttribute("login", null)));
			
		}
        
	};


}
