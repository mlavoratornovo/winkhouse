package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Affitti;

public class Affitti extends _Affitti {

    private static final long serialVersionUID = 1L; 

    public int getCodAffitto() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }
}
