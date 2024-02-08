package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Statoconservativo;

public class Statoconservativo extends _Statoconservativo {

    private static final long serialVersionUID = 1L; 

	public int getCodStatoConservativo() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }

}
