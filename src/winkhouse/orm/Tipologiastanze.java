package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Tipologiastanze;

public class Tipologiastanze extends _Tipologiastanze {

    private static final long serialVersionUID = 1L; 

	public int getCodTipologiaStanza() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }
	
	public void initData() {
		this.descrizione = "";
	}

}
