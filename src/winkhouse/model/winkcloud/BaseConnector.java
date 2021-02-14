package winkhouse.model.winkcloud;

public class BaseConnector {
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPercorso() {
		return percorso;
	}
	public void setPercorso(String percorso) {
		this.percorso = percorso;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private String url = null;
	private String percorso = null;
	private String username = null;
	private String password = null;
	
}
