package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Anagrafiche;

public class Anagrafiche extends _Anagrafiche {

    private static final long serialVersionUID = 1L;
    
    public long getId() {
        return Cayenne.longPKForObject(this);
     }
}
