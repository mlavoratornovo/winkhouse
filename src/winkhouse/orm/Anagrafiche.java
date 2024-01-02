package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Anagrafiche;

public class Anagrafiche extends _Anagrafiche {

    private static final long serialVersionUID = 1L;
    
    public int getId() {
        return Cayenne.intPKForObject(this);
    }
    
	@Override
	public String toString() {		
		return getNome() + " " + getCognome() + " " +getRagsoc() + " - " + getCitta() + " " + getIndirizzo();
	}

}
