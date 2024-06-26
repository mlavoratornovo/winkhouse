package winkhouse.orm;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.auto._Colloquicriteriricerca;

public class Colloquicriteriricerca extends _Colloquicriteriricerca {

    private static final long serialVersionUID = 1L; 
    private Integer lineNumber = null;
    
	public int getCodColloquicriteriricerca() {
    	try {
        	return Cayenne.intPKForObject(this);
    	}catch(Exception ex) {
    		return 0;
    	}
    }

	public Integer getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}
    
}
