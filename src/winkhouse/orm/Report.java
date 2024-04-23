package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Report;

public class Report extends _Report {

    private static final long serialVersionUID = 1L; 

	public int getCodReport() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }
    
}
