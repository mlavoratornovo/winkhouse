package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Tipologieimmobili;

public class Tipologieimmobili extends _Tipologieimmobili {

    private static final long serialVersionUID = 1L; 
    private String comune = null;
    
	public int getCodTipologiaImmobile() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }
	
	public void initData() {
		this.descrizione = "";
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

}
