package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Allegatiimmobili;

public class Allegatiimmobili extends _Allegatiimmobili {

    private static final long serialVersionUID = 1L; 
    private String fromPath = null;
    
	public String getFromPath() {
		return fromPath;
	}
	
	public void setFromPath(String fromPath) {
		this.fromPath = fromPath;
	}
    
	public int getCodImmobile() {
		return this.getImmobili().getCodImmobile();
	}
}
