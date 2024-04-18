package winkhouse.orm;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;

import winkhouse.orm.auto._Affitti;

public class Affitti extends _Affitti {

    private static final long serialVersionUID = 1L; 
    private ObjectContext editObjectContext = null;
    
    public ObjectContext getEditObjectContext() {
		return editObjectContext;
	}

	public void setEditObjectContext(ObjectContext editObjectContext) {
		this.editObjectContext = editObjectContext;
	}

	public int getCodAffitti() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }
}
