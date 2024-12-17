package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Classienergetiche;

public class Classienergetiche extends _Classienergetiche {

    private static final long serialVersionUID = 1L; 
    private int codClasseEnergetica;
    
	public void setCodClasseEnergetica(int codClasseEnergetica) {
		this.codClasseEnergetica = codClasseEnergetica;
	}

	public int getCodClasseEnergetica() {
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
