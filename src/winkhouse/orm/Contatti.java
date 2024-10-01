package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Contatti;

public class Contatti extends _Contatti {

    private static final long serialVersionUID = 1L;
    
	public int getCodContatto() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }

}
