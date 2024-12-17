package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Riscaldamenti;

public class Riscaldamenti extends _Riscaldamenti {

    private static final long serialVersionUID = 1L; 
    private int codRiscaldamento;
    
	public int getCodRiscaldamento() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }

	public void setCodRiscaldamento(int codRiscaldamento) {
		this.codRiscaldamento = codRiscaldamento;
	}
	
	public void initData() {
		this.descrizione = "";
	}

}
