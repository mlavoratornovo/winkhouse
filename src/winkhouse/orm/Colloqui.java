package winkhouse.orm;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;

import winkhouse.orm.auto._Colloqui;

public class Colloqui extends _Colloqui {

    private static final long serialVersionUID = 1L; 
    private ObjectContext editObjectContext = null;
    
    public ObjectContext getEditObjectContext() {
		return editObjectContext;
	}

	public void setEditObjectContext(ObjectContext editObjectContext) {
		this.editObjectContext = editObjectContext;
	}

	public int getCodColloquio() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }
}
