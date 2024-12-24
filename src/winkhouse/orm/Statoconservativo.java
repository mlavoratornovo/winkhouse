package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Statoconservativo;

public class Statoconservativo extends _Statoconservativo {

    private static final long serialVersionUID = 1L;
    private String comune = null;

	public int getCodStatoConservativo() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }

	public void initData() {
		this.descrizione = "";
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

}
