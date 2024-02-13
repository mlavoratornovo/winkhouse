package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Agentiappuntamenti;

public class Agentiappuntamenti extends _Agentiappuntamenti {

    private static final long serialVersionUID = 1L; 
    
    public int getCodAgentiAppuntamenti() {
        return Cayenne.intPKForObject(this);
    }
}
