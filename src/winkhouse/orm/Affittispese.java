package winkhouse.orm;

import org.apache.cayenne.ObjectContext;

import winkhouse.orm.auto._Affittispese;

public class Affittispese extends _Affittispese {

    private static final long serialVersionUID = 1L; 
    private ObjectContext editObjectContext = null;
    
	public ObjectContext getEditObjectContext() {
		return editObjectContext;
	}
	public void setEditObjectContext(ObjectContext editObjectContext) {
		this.editObjectContext = editObjectContext;
	}
}
