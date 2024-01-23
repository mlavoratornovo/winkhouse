package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Anagrafiche;

public class Anagrafiche extends _Anagrafiche {

    private static final long serialVersionUID = 1L;
    
    public int getCodAnagrafica() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }
    
	@Override
	public String toString() {		
		return getNome() + " " + getCognome() + " " + getRagsoc() + " - " + getCitta() + " " + getIndirizzo();
	}

}
