package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Agenti;

public class Agenti extends _Agenti {

    private static final long serialVersionUID = 1L; 

    public int getCodAgente() {
        return Cayenne.intPKForObject(this);
    }
}
