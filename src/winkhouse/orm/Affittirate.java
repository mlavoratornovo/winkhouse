package winkhouse.orm;

import org.apache.cayenne.ObjectContext;

import winkhouse.orm.auto._Affittirate;
import winkhouse.util.WinkhouseUtils;

public class Affittirate extends _Affittirate {

    private static final long serialVersionUID = 1L;
    private String nomeMese = null;
    private ObjectContext editObjectContext = null;
    
	public ObjectContext getEditObjectContext() {
		return editObjectContext;
	}

	public void setEditObjectContext(ObjectContext editObjectContext) {
		this.editObjectContext = editObjectContext;
	}

	public String getNomeMese() {
		nomeMese = WinkhouseUtils.getInstance()
								   .getMesi()
								   .get(getMese())
								   .getNome();
		return nomeMese;
	}
}
