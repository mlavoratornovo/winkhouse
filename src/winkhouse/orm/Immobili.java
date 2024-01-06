package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Immobili;

public class Immobili extends _Immobili {

    private static final long serialVersionUID = 1L; 

    public int getCodImmobile() {
        return Cayenne.intPKForObject(this);
    }

}
