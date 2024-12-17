package winkhouse.orm;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;

import winkhouse.orm.auto._Tipologiecontatti;

public class Tipologiecontatti extends _Tipologiecontatti {

    private static final long serialVersionUID = 1L; 
    private ObjectContext editObjectContext = null;
    
   	public ObjectContext getEditObjectContext() {
   		return editObjectContext;
   	}

   	public void setEditObjectContext(ObjectContext editObjectContext) {
   		this.editObjectContext = editObjectContext;
   	}

   	public int getCodTipologiaContatto() {
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
