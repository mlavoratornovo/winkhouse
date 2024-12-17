package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Tipiappuntamenti;

public class Tipiappuntamenti extends _Tipiappuntamenti {

    private static final long serialVersionUID = 1L; 

    public int getCodTipoAppuntamento() {
        return Cayenne.intPKForObject(this);
    }
    
	public void initData() {
		this.descrizione = "";
	}

}
