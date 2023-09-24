package winkhouse.model.winkcloud;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.util.WinkhouseUtils;

public class FTPConnector extends BaseConnector implements XMLSerializable{

	private String url = null;
	private String percorso = null;
	private String username = null;
	private String password = null;
	private int porta = 21;
	
	public FTPConnector()  {
		
	}

	public FTPConnector(String url,int porta,String username,String password) {
		this.url = url;
		this.porta = porta;
		this.username = username;
		this.password = password;
	}

//	public String getUrl() {
//		return url;
//	}
//
//	public void setUrl(String url) {
//		this.url = url;
//	}
//
//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}

	public int getPorta() {
		return porta;
	}

	public void setPorta(int porta) {
		this.porta = porta;
	}
	
	@Override
	public String toString() {
		return this.url + ":" + this.porta + "/" + this.percorso;
	}

	
	public String getPercorso() {
		return percorso;
	}

	
	public void setPercorso(String percorso) {
		this.percorso = percorso;
	}

	protected static final XMLFormat<FTPConnector> FTPCONNECTOR_XML = new XMLFormat<FTPConnector>(FTPConnector.class) {
		
        public void write(FTPConnector monitorftp, OutputElement xml) {
        	
        	try {
				xml.setAttribute("url", monitorftp.getUrl());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        	
        	try {
				xml.setAttribute("percorso", monitorftp.getPercorso());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        	try {
				xml.setAttribute("username", monitorftp.getUsername());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        	try {
				xml.setAttribute("password", WinkhouseUtils.getInstance().EncryptStringStandard(monitorftp.getPassword()));
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        	try {
				xml.setAttribute("porta", monitorftp.getPorta());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
        	
        }

		@Override
		public void read(InputElement arg0, FTPConnector arg1) throws XMLStreamException {
			
			arg1.setUrl(arg0.getAttribute("url", ""));
			arg1.setPercorso(arg0.getAttribute("percorso", ""));
			arg1.setUsername(arg0.getAttribute("username", ""));
			arg1.setPassword(WinkhouseUtils.getInstance().DecryptStringStandard(arg0.getAttribute("password", "")));
			arg1.setPorta(arg0.getAttribute("porta", 21));
			
		}
        
	};

}
