package winkhouse.orm;

import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;

import winkhouse.orm.auto._Agenti;

public class Agenti extends _Agenti {

    private static final long serialVersionUID = 1L; 
    private ObjectContext editObjectContext = null;
    
    public ObjectContext getEditObjectContext() {
		return editObjectContext;
	}

	public void setEditObjectContext(ObjectContext editObjectContext) {
		this.editObjectContext = editObjectContext;
	}

	public int getCodAgente() {
        return Cayenne.intPKForObject(this);
    }
    
    @Override
	public String toString() {
		return getNome() + " " + getCognome() + " - " + getCitta();
	}
    
    public void initData() {
    	this.nome = "";
    	this.cognome = "";
    	this.cap = "";
    	this.citta = "";
    	this.indirizzo = "";
    	this.provincia = "";
    	this.username = "";
    	this.password = "";
    }
    
    
}
