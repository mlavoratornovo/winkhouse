package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Classicliente;

public class Classicliente extends _Classicliente {

    private static final long serialVersionUID = 1L;
    
    public int getCodClasseCliente() {
        return Cayenne.intPKForObject(this);
    }
    
    public void initData() {
    	this.descrizione = "";    	
    }
}
