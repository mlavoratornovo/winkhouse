package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Tipologiecolloqui;

public class Tipologiecolloqui extends _Tipologiecolloqui {

    @Override
	public String toString() {		
		return getDescrizione();
	}

	private static final long serialVersionUID = 1L; 
    
    public int getCodTipologieColloquio() {
       	try {
           	return Cayenne.intPKForObject(this);
       	}catch(Exception ex) {
       		return 0;
       	}
   	}
}
