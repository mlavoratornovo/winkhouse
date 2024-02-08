package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Riscaldamenti;

public class Riscaldamenti extends _Riscaldamenti {

    private static final long serialVersionUID = 1L; 

	public int getCodRiscaldamento() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }

}
