package winkhouse.orm;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;

import winkhouse.orm.auto._Appuntamenti;

public class Appuntamenti extends _Appuntamenti {

    private static final long serialVersionUID = 1L; 
    private ObjectContext editObjectContext = null;
    
    
    public ObjectContext getEditObjectContext() {
		return editObjectContext;
	}


	public void setEditObjectContext(ObjectContext editObjectContext) {
		this.editObjectContext = editObjectContext;
	}


	public int getCodAppuntamento() {
        return Cayenne.intPKForObject(this);
    }
}
