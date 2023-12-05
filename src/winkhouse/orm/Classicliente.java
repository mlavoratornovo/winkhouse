package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Classicliente;

public class Classicliente extends _Classicliente {

    private static final long serialVersionUID = 1L;
    
    public int getId() {
        return Cayenne.intPKForObject(this);
    }
}
