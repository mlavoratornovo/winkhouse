package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Appuntamenti;

public class Appuntamenti extends _Appuntamenti {

    private static final long serialVersionUID = 1L; 

    public int getCodAppuntamento() {
        return Cayenne.intPKForObject(this);
    }
}
