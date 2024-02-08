package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Classienergetiche;

public class Classienergetiche extends _Classienergetiche {

    private static final long serialVersionUID = 1L; 

	public int getCodClasseEnergetica() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }

}
