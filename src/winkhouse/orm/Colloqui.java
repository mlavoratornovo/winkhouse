package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Colloqui;

public class Colloqui extends _Colloqui {

    private static final long serialVersionUID = 1L; 

    public int getCodColloquio() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }
}
