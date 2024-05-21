package winkhouse.orm;

import java.util.List;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;

import winkhouse.orm.auto._Ricerche;

public class Ricerche extends _Ricerche {

    private static final long serialVersionUID = 1L; 
    private ObjectContext editObjectContext = null;
    
    
	public ObjectContext getEditObjectContext() {
		return editObjectContext;
	}
	public void setEditObjectContext(ObjectContext editObjectContext) {
		this.editObjectContext = editObjectContext;
	}
	
	public int getCodRicerche() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }

    
}
